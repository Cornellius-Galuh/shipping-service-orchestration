package com.shipping.orchestrator.dto;

public class TrackingHistoryDTO {

    private String location;
    private String status;
    private String description;

    public TrackingHistoryDTO() {
    }

    public TrackingHistoryDTO(String location, String status, String description) {
        this.location = location;
        this.status = status;
        this.description = description;
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
