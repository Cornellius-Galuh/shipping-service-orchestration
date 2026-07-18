package com.shipping.tracking_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tracking")
public class Tracking {

    @Id
    private String id;

    private Integer shipmentId;
    private String trackingNumber;
    
    private List<TrackingHistory> history = new ArrayList<>();

    public Tracking() {
    }

    public Tracking(Integer shipmentId, String trackingNumber) {
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

    public List<TrackingHistory> getHistory() {
        return history;
    }

    public void setHistory(List<TrackingHistory> history) {
        this.history = history;
    }

    public void addHistory(TrackingHistory trackingHistory) {
        this.history.add(trackingHistory);
    }
}
