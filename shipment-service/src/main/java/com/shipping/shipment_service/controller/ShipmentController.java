package com.shipping.shipment_service.controller;

import com.shipping.shipment_service.model.Shipment;
import com.shipping.shipment_service.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shipping.shipment_service.model.Pricing;
import com.shipping.shipment_service.repository.PricingRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/shipments")
public class ShipmentController {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private PricingRepository pricingRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<?> createShipment(@RequestBody Shipment shipment) {
        if (shipment.getStatus() == null) {
            shipment.setStatus("PENDING");
        }

        // Otomatis menghitung ongkir
        Optional<Pricing> pricingData = pricingRepository.findByOriginCityAndDestinationCityAndServiceType(
                shipment.getOriginCity(), shipment.getDestinationCity(), shipment.getServiceType());

        if (pricingData.isPresent()) {
            BigDecimal basePrice = pricingData.get().getPrice();
            BigDecimal finalPrice = basePrice.multiply(BigDecimal.valueOf(shipment.getWeight()));
            shipment.setPrice(finalPrice);
        } else {
            return new ResponseEntity<>("Error: Data tarif (Pricing) tidak ditemukan untuk rute dan layanan ini.", HttpStatus.BAD_REQUEST);
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

    // --- PRICING MASTER DATA ENDPOINTS ---

    // CREATE PRICING
    @PostMapping("/pricing")
    public ResponseEntity<Pricing> createPricing(@RequestBody Pricing pricing) {
        Pricing savedPricing = pricingRepository.save(pricing);
        return new ResponseEntity<>(savedPricing, HttpStatus.CREATED);
    }

    // READ ALL PRICING
    @GetMapping("/pricing")
    public ResponseEntity<List<Pricing>> getAllPricing() {
        List<Pricing> pricings = pricingRepository.findAll();
        return new ResponseEntity<>(pricings, HttpStatus.OK);
    }

    // READ ONE PRICING BY ID
    @GetMapping("/pricing/{id}")
    public ResponseEntity<Pricing> getPricingById(@PathVariable Long id) {
        Optional<Pricing> pricingData = pricingRepository.findById(id);

        if (pricingData.isPresent()) {
            return new ResponseEntity<>(pricingData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE PRICING
    @PutMapping("/pricing/{id}")
    public ResponseEntity<Pricing> updatePricing(@PathVariable Long id, @RequestBody Pricing pricing) {
        Optional<Pricing> pricingData = pricingRepository.findById(id);

        if (pricingData.isPresent()) {
            Pricing _pricing = pricingData.get();
            _pricing.setOriginCity(pricing.getOriginCity());
            _pricing.setDestinationCity(pricing.getDestinationCity());
            _pricing.setServiceType(pricing.getServiceType());
            _pricing.setPrice(pricing.getPrice());
            _pricing.setWeight(pricing.getWeight());
            return new ResponseEntity<>(pricingRepository.save(_pricing), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE PRICING
    @DeleteMapping("/pricing/{id}")
    public ResponseEntity<HttpStatus> deletePricing(@PathVariable Long id) {
        try {
            pricingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
