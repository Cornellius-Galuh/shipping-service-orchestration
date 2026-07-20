package com.shipping.orchestrator.dto;

import java.math.BigDecimal;

public class PaymentRequest {

    private Integer shipmentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String paymentStatus;

    public PaymentRequest() {
    }

    public PaymentRequest(Integer shipmentId, BigDecimal amount, String paymentMethod, String paymentStatus) {
        this.shipmentId = shipmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
