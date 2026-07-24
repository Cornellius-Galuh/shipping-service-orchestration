package com.shipping.shipment_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "shipments")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trackingNumber;
    private Integer customerId;
    private String originCity;
    private String destinationCity;
    private Double weight;
    private String serviceType;
    private BigDecimal price;
    private String status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Shipment() {
    }

    public Shipment(String trackingNumber, Integer customerId, String originCity, String destinationCity, Double weight, String serviceType, BigDecimal price, String status) {
        this.trackingNumber = trackingNumber;
        this.customerId = customerId;
        this.originCity = originCity;
        this.destinationCity = destinationCity;
        this.weight = weight;
        this.serviceType = serviceType;
        this.price = price;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
