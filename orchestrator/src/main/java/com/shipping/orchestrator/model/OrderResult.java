package com.shipping.orchestrator.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the outcome of one "create shipping order" saga run.
 * <p>
 * NOTE: orchestrator has no database of its own (no JPA/datasource configured),
 * so this is kept only in an in-memory store (see OrderOrchestratorService).
 * It resets whenever the orchestrator restarts — it's for demo/inspection
 * purposes only, not a durable audit log.
 */
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderResult {

    private Long orderId;
    private Integer customerId;
    private String originCity;
    private String destinationCity;
    private Double weight;
    private String serviceType;
    private String paymentMethod;

    private Integer shipmentId;
    private String trackingNumber;
    private BigDecimal price;
    private String paymentId;

    // PENDING, CUSTOMER_VALIDATED, SHIPMENT_CREATED, PAYMENT_SUCCESS, COMPLETED, FAILED
    private String status;
    private String failureReason;

    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
