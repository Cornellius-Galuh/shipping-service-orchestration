package com.shipping.orchestrator.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrackingDTO {

    private String id;
    private Integer shipmentId;
    private String trackingNumber;
    private List<TrackingHistoryDTO> history = new ArrayList<>();

    public TrackingDTO(Integer shipmentId, String trackingNumber) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
    }
}
