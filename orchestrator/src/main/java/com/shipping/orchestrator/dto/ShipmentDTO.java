package com.shipping.orchestrator.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentDTO {

    private Integer id;
    private String trackingNumber;
    private Integer customerId;
    private String originCity;
    private String destinationCity;
    private Double weight;
    private String serviceType;
    // Not set by the orchestrator on create — shipment-service calculates this
    // itself from its own Pricing table (originCity + destinationCity + serviceType + weight).
    private BigDecimal price;
    private String status;
}
