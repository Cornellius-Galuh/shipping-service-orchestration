package com.shipping.orchestrator.client;

import com.shipping.orchestrator.dto.ShipmentRequest;
import com.shipping.orchestrator.dto.ShipmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shipment-service")
public interface ShipmentClient {

    @PostMapping("/api/shipments")
    ShipmentResponse createShipment(@RequestBody ShipmentRequest request);
}
