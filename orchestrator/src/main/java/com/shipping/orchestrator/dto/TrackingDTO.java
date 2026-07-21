package com.shipping.orchestrator.dto;

import java.util.ArrayList;
import java.util.List;

public class TrackingDTO {

    private String id;
    private Integer shipmentId;
    private String trackingNumber;
    private List<TrackingHistoryDTO> history = new ArrayList<>();

    public TrackingDTO() {
    }

    public TrackingDTO(Integer shipmentId, String trackingNumber) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Integer shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public List<TrackingHistoryDTO> getHistory() {
        return history;
    }

    public void setHistory(List<TrackingHistoryDTO> history) {
        this.history = history;
    }
}
