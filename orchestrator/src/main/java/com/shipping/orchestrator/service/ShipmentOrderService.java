package com.shipping.orchestrator.service;

import com.shipping.orchestrator.client.CustomerClient;
import com.shipping.orchestrator.client.PaymentClient;
import com.shipping.orchestrator.client.ShipmentClient;
import com.shipping.orchestrator.client.TrackingClient;
import com.shipping.orchestrator.dto.*;
import com.shipping.orchestrator.model.ShipmentOrder;
import com.shipping.orchestrator.repository.ShipmentOrderRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ShipmentOrderService {

    @Autowired
    private CustomerClient customerClient;

    @Autowired
    private ShipmentClient shipmentClient;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private TrackingClient trackingClient;

    @Autowired
    private ShipmentOrderRepository shipmentOrderRepository;

    public CreateShipmentOrderResponse createShipmentOrder(CreateShipmentOrderRequest request) {
        ShipmentOrder order = new ShipmentOrder();
        order.setCustomerId(request.getCustomerId());
        order.setStatus("PENDING");
        order = shipmentOrderRepository.save(order);

        CustomerResponse customer = validateCustomer(order, request.getCustomerId());
        ShipmentResponse shipment = createShipment(order, request);
        PaymentResponse payment = createPayment(order, shipment, request.getPaymentMethod());
        TrackingResponse tracking = createTracking(order, shipment);

        order.setStatus("COMPLETED");
        order = shipmentOrderRepository.save(order);

        return new CreateShipmentOrderResponse(order.getId(), order.getStatus(), customer, shipment, payment, tracking);
    }

    public ShipmentOrder getById(Long id) {
        return shipmentOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shipment order not found"));
    }

    public List<ShipmentOrder> getAll() {
        return shipmentOrderRepository.findAll();
    }

    private CustomerResponse validateCustomer(ShipmentOrder order, Long customerId) {
        try {
            return customerClient.getCustomerById(customerId);
        } catch (FeignException.NotFound e) {
            fail(order, "FAILED_AT_CUSTOMER", "Customer " + customerId + " not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        } catch (Exception e) {
            fail(order, "FAILED_AT_CUSTOMER", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "customer-service unavailable");
        }
    }

    private ShipmentResponse createShipment(ShipmentOrder order, CreateShipmentOrderRequest request) {
        try {
            String trackingNumber = "TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            ShipmentRequest shipmentRequest = new ShipmentRequest(
                    trackingNumber,
                    request.getCustomerId().intValue(),
                    request.getOriginCity(),
                    request.getDestinationCity(),
                    request.getWeight(),
                    request.getServiceType(),
                    request.getPrice(),
                    "PENDING"
            );
            ShipmentResponse shipment = shipmentClient.createShipment(shipmentRequest);
            order.setShipmentId(shipment.getId());
            order.setTrackingNumber(shipment.getTrackingNumber());
            shipmentOrderRepository.save(order);
            return shipment;
        } catch (Exception e) {
            fail(order, "FAILED_AT_SHIPMENT", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "shipment-service unavailable");
        }
    }

    private PaymentResponse createPayment(ShipmentOrder order, ShipmentResponse shipment, String paymentMethod) {
        try {
            PaymentRequest paymentRequest = new PaymentRequest(shipment.getId(), shipment.getPrice(), paymentMethod, "PENDING");
            PaymentResponse payment = paymentClient.createPayment(paymentRequest);
            order.setPaymentId(payment.getId());
            shipmentOrderRepository.save(order);
            return payment;
        } catch (Exception e) {
            fail(order, "FAILED_AT_PAYMENT", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "payment-service unavailable");
        }
    }

    private TrackingResponse createTracking(ShipmentOrder order, ShipmentResponse shipment) {
        try {
            TrackingRequest trackingRequest = new TrackingRequest(shipment.getId(), shipment.getTrackingNumber());
            TrackingResponse tracking = trackingClient.createTracking(trackingRequest);
            order.setTrackingId(tracking.getId());
            shipmentOrderRepository.save(order);
            return tracking;
        } catch (Exception e) {
            fail(order, "FAILED_AT_TRACKING", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "tracking-service unavailable");
        }
    }

    private void fail(ShipmentOrder order, String status, String reason) {
        order.setStatus(status);
        order.setFailureReason(reason);
        shipmentOrderRepository.save(order);
    }
}
