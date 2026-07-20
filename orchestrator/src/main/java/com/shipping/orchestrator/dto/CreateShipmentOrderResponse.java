package com.shipping.orchestrator.dto;

public class CreateShipmentOrderResponse {

    private Long orderId;
    private String status;
    private CustomerResponse customer;
    private ShipmentResponse shipment;
    private PaymentResponse payment;
    private TrackingResponse tracking;

    public CreateShipmentOrderResponse() {
    }

    public CreateShipmentOrderResponse(Long orderId, String status, CustomerResponse customer,
                                        ShipmentResponse shipment, PaymentResponse payment, TrackingResponse tracking) {
        this.orderId = orderId;
        this.status = status;
        this.customer = customer;
        this.shipment = shipment;
        this.payment = payment;
        this.tracking = tracking;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerResponse getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerResponse customer) {
        this.customer = customer;
    }

    public ShipmentResponse getShipment() {
        return shipment;
    }

    public void setShipment(ShipmentResponse shipment) {
        this.shipment = shipment;
    }

    public PaymentResponse getPayment() {
        return payment;
    }

    public void setPayment(PaymentResponse payment) {
        this.payment = payment;
    }

    public TrackingResponse getTracking() {
        return tracking;
    }

    public void setTracking(TrackingResponse tracking) {
        this.tracking = tracking;
    }
}
