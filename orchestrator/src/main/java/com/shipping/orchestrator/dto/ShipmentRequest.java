package com.shipping.orchestrator.dto;

import java.math.BigDecimal;

public class ShipmentRequest {

    private String trackingNumber;
    private Integer customerId;
    private String originCity;
    private String destinationCity;
    private Double weight;
    private String serviceType;
    private BigDecimal price;
    private String status;

    public ShipmentRequest() {
    }

    public ShipmentRequest(String trackingNumber, Integer customerId, String originCity, String destinationCity,
                            Double weight, String serviceType, BigDecimal price, String status) {
        this.trackingNumber = trackingNumber;
        this.customerId = customerId;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.weight = weight;
        this.serviceType = serviceType;
        this.price = price;
        this.status = status;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getOriginCity() {
        return originCity;
    }

    public void setOriginCity(String originCity) {
        this.originCity = originCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
