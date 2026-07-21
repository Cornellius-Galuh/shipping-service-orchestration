package com.shipping.orchestrator.client;

import com.shipping.orchestrator.dto.ShipmentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "shipment-service")
public interface ShipmentServiceClient {

    // shipment-service calculates the price itself from its internal Pricing
    // table (originCity + destinationCity + serviceType + weight) and returns
    // HTTP 400 with a plain-text body if no matching pricing rule is found.
    @PostMapping("/api/shipments")
    ShipmentDTO createShipment(@RequestBody ShipmentDTO shipment);

    @PutMapping("/api/shipments/{id}")
    ShipmentDTO updateShipment(@PathVariable("id") Integer id, @RequestBody ShipmentDTO shipment);

    // Compensating action if a later saga step fails
    @DeleteMapping("/api/shipments/{id}")
    void deleteShipment(@PathVariable("id") Integer id);
}
