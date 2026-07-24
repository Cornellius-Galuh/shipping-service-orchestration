package com.shipping.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrackingHistoryDTO {

    private String location;
    private String status;
    private String description;
}
