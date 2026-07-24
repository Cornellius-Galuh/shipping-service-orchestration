package com.shipping.tracking_service.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackingHistory {

    private LocalDateTime timestamp;
    private String location;
    private String status;
    private String description;

    public TrackingHistory() {
        this.timestamp = LocalDateTime.now();
    }

    public TrackingHistory(String location, String status, String description) {
        this.timestamp = LocalDateTime.now();
        this.location = location;
        this.status = status;
        this.description = description;
    }
}
