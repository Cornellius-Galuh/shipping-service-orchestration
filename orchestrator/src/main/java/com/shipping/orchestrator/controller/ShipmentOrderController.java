package com.shipping.orchestrator.controller;

import com.shipping.orchestrator.dto.CreateShipmentOrderRequest;
import com.shipping.orchestrator.dto.CreateShipmentOrderResponse;
import com.shipping.orchestrator.model.ShipmentOrder;
import com.shipping.orchestrator.service.ShipmentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orchestrator/shipments")
public class ShipmentOrderController {

    @Autowired
    private ShipmentOrderService shipmentOrderService;

    // CREATE (jalankan alur: validasi customer -> shipment -> payment -> tracking)
    @PostMapping
    public ResponseEntity<CreateShipmentOrderResponse> createShipmentOrder(@RequestBody CreateShipmentOrderRequest request) {
        CreateShipmentOrderResponse response = shipmentOrderService.createShipmentOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<ShipmentOrder>> getAllShipmentOrders() {
        return new ResponseEntity<>(shipmentOrderService.getAll(), HttpStatus.OK);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentOrder> getShipmentOrder(@PathVariable Long id) {
        return new ResponseEntity<>(shipmentOrderService.getById(id), HttpStatus.OK);
    }
}
