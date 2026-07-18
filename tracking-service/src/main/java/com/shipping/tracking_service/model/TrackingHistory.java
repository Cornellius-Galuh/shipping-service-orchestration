package com.shipping.tracking_service.model;

import java.time.LocalDateTime;

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
