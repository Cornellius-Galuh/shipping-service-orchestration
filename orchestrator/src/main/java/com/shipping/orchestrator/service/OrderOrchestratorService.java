package com.shipping.orchestrator.service;

import com.shipping.orchestrator.client.CustomerServiceClient;
import com.shipping.orchestrator.client.PaymentServiceClient;
import com.shipping.orchestrator.client.ShipmentServiceClient;
import com.shipping.orchestrator.client.TrackingServiceClient;
import com.shipping.orchestrator.dto.*;
import com.shipping.orchestrator.model.OrderResult;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class OrderOrchestratorService {

    private static final Logger log = LoggerFactory.getLogger(OrderOrchestratorService.class);

    // In-memory saga log only — orchestrator has no database of its own.
    // Resets on restart; used purely so GET /api/orders can show recent runs.
    private final Map<Long, OrderResult> orderStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    private final CustomerServiceClient customerServiceClient;
    private final ShipmentServiceClient shipmentServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final TrackingServiceClient trackingServiceClient;

    /**
     * Orchestrates the "create shipping order" business process:
     * validate customer -> create shipment (price auto-calculated by
     * shipment-service) -> process payment -> (rollback shipment if payment
     * fails) -> confirm shipment -> create tracking record (best-effort).
     */
    public OrderResult createOrder(CreateOrderRequest request) {

        OrderResult order = new OrderResult();
        order.setOrderId(idGenerator.getAndIncrement());
        order.setCustomerId(request.getCustomerId());
        order.setOriginCity(request.getOriginCity());
        order.setDestinationCity(request.getDestinationCity());
        order.setWeight(request.getWeight());
        order.setServiceType(request.getServiceType());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus("PENDING");
        orderStore.put(order.getOrderId(), order);

        // ---------- STEP 1: Validate customer ----------
        try {
            customerServiceClient.getCustomerById(Long.valueOf(request.getCustomerId()));
            order.setStatus("CUSTOMER_VALIDATED");
        } catch (FeignException.NotFound ex) {
            return fail(order, "VALIDATE_CUSTOMER", "Customer with id " + request.getCustomerId() + " not found");
        } catch (Exception ex) {
            return fail(order, "VALIDATE_CUSTOMER", "Customer service unavailable: " + ex.getMessage());
        }

        // ---------- STEP 2: Create shipment (shipment-service calculates the price) ----------
        ShipmentDTO shipment;
        String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        try {
            ShipmentDTO newShipment = new ShipmentDTO();
            newShipment.setTrackingNumber(trackingNumber);
            newShipment.setCustomerId(request.getCustomerId());
            newShipment.setOriginCity(request.getOriginCity());
            newShipment.setDestinationCity(request.getDestinationCity());
            newShipment.setWeight(request.getWeight());
            newShipment.setServiceType(request.getServiceType());
            newShipment.setStatus("PENDING");

            shipment = shipmentServiceClient.createShipment(newShipment);

            order.setShipmentId(shipment.getId());
            order.setTrackingNumber(trackingNumber);
            order.setPrice(shipment.getPrice());
            order.setStatus("SHIPMENT_CREATED");
        } catch (FeignException.BadRequest ex) {
            // shipment-service returns 400 with a plain-text body when no pricing rule matches
            return fail(order, "CREATE_SHIPMENT", "No pricing rule found for this route/service type ("
                    + ex.contentUTF8() + ")");
        } catch (Exception ex) {
            return fail(order, "CREATE_SHIPMENT", "Shipment service unavailable: " + ex.getMessage());
        }

        // ---------- STEP 3: Process payment ----------
        try {
            PaymentDTO paymentRequest = new PaymentDTO();
            paymentRequest.setShipmentId(shipment.getId());
            paymentRequest.setAmount(shipment.getPrice());
            paymentRequest.setPaymentMethod(request.getPaymentMethod());
            paymentRequest.setPaymentStatus("SUCCESS");
            paymentRequest.setPaymentDate(LocalDateTime.now());

            PaymentDTO payment = paymentServiceClient.createPayment(paymentRequest);

            if (!"SUCCESS".equalsIgnoreCase(payment.getPaymentStatus())) {
                compensateShipment(shipment.getId());
                return fail(order, "PROCESS_PAYMENT", "Payment was declined by payment service");
            }

            order.setPaymentId(payment.getId());
            order.setStatus("PAYMENT_SUCCESS");
        } catch (Exception ex) {
            // Payment call itself failed -> compensate by rolling back the shipment
            compensateShipment(shipment.getId());
            return fail(order, "PROCESS_PAYMENT", "Payment service unavailable: " + ex.getMessage());
        }

        // ---------- STEP 4: Confirm shipment ----------
        try {
            shipment.setStatus("CONFIRMED");
            shipmentServiceClient.updateShipment(shipment.getId(), shipment);
        } catch (Exception ex) {
            // Non-fatal: payment already succeeded, so we don't roll back further.
            log.warn("Failed to confirm shipment {} after successful payment: {}", shipment.getId(), ex.getMessage());
        }

        // ---------- STEP 5: Create tracking record (best-effort) ----------
        try {
            TrackingDTO trackingDTO = new TrackingDTO(shipment.getId(), trackingNumber);
            trackingDTO.getHistory().add(new TrackingHistoryDTO(
                    request.getOriginCity(), "ORDER_CREATED", "Shipment order created and payment confirmed"));
            trackingServiceClient.createTracking(trackingDTO);
        } catch (Exception ex) {
            log.warn("Failed to create tracking record for shipment {}: {}", shipment.getId(), ex.getMessage());
        }

        order.setStatus("COMPLETED");
        return order;
    }

    /**
     * Compensating action: rolls back a previously created shipment
     * when a later step in the saga fails.
     */
    private void compensateShipment(Integer shipmentId) {
        try {
            shipmentServiceClient.deleteShipment(shipmentId);
            log.info("Rolled back (deleted) shipment {} due to downstream failure", shipmentId);
        } catch (Exception ex) {
            log.error("Compensation failed: could not delete shipment {}: {}", shipmentId, ex.getMessage());
        }
    }

    private OrderResult fail(OrderResult order, String step, String reason) {
        order.setStatus("FAILED");
        order.setFailureReason("[" + step + "] " + reason);
        log.warn("Order {} failed at step {}: {}", order.getOrderId(), step, reason);
        return order;
    }

    public Collection<OrderResult> getAllOrders() {
        return orderStore.values();
    }

    public OrderResult getOrderById(Long id) {
        return orderStore.get(id);
    }
}
