package com.shipping.shipment_service.controller;

import com.shipping.shipment_service.model.Shipment;
import com.shipping.shipment_service.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Shipment> createShipment(@RequestBody Shipment shipment) {
        if (shipment.getStatus() == null) {
            shipment.setStatus("PENDING");
        }
        Shipment savedShipment = shipmentRepository.save(shipment);
        return new ResponseEntity<>(savedShipment, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Shipment>> getAllShipments() {
        List<Shipment> shipments = shipmentRepository.findAll();
        return new ResponseEntity<>(shipments, HttpStatus.OK);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Integer id) {
        Optional<Shipment> shipmentData = shipmentRepository.findById(id);

        if (shipmentData.isPresent()) {
            return new ResponseEntity<>(shipmentData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Shipment> updateShipment(@PathVariable Integer id, @RequestBody Shipment shipment) {
        Optional<Shipment> shipmentData = shipmentRepository.findById(id);

        if (shipmentData.isPresent()) {
            Shipment _shipment = shipmentData.get();
            _shipment.setTrackingNumber(shipment.getTrackingNumber());
            _shipment.setCustomerId(shipment.getCustomerId());
            _shipment.setOriginCity(shipment.getOriginCity());
            _shipment.setDestinationCity(shipment.getDestinationCity());
            _shipment.setWeight(shipment.getWeight());
            _shipment.setServiceType(shipment.getServiceType());
            _shipment.setPrice(shipment.getPrice());
            _shipment.setStatus(shipment.getStatus());
            
            return new ResponseEntity<>(shipmentRepository.save(_shipment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteShipment(@PathVariable Integer id) {
        try {
            shipmentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TRACK BY RESI
    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<Shipment> trackShipment(@PathVariable String trackingNumber) {
        Optional<Shipment> shipmentData = shipmentRepository.findByTrackingNumber(trackingNumber);

        if (shipmentData.isPresent()) {
            return new ResponseEntity<>(shipmentData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
