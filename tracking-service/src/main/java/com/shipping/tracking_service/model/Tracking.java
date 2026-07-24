package com.shipping.tracking_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public void addHistory(TrackingHistory trackingHistory) {
        this.history.add(trackingHistory);
    }
}
