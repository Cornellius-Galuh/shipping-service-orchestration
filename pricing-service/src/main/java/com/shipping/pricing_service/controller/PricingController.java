package com.shipping.pricing_service.controller;

import com.shipping.pricing_service.model.Pricing;
import com.shipping.pricing_service.repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @Autowired
    private PricingRepository pricingRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Pricing> createPricing(@RequestBody Pricing pricing) {
        Pricing savedPricing = pricingRepository.save(pricing);
        return new ResponseEntity<>(savedPricing, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Pricing>> getAllPricing() {
        List<Pricing> pricings = pricingRepository.findAll();
        return new ResponseEntity<>(pricings, HttpStatus.OK);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Pricing> getPricingById(@PathVariable Long id) {
        Optional<Pricing> pricingData = pricingRepository.findById(id);

        if (pricingData.isPresent()) {
            return new ResponseEntity<>(pricingData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
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

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePricing(@PathVariable Long id) {
        try {
            pricingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // CALCULATE ENDPOINT
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculatePrice(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String type,
            @RequestParam Double weight) {

        Optional<Pricing> pricingData = pricingRepository.findByOriginCityAndDestinationCityAndServiceType(
                origin, destination, type);

        if (pricingData.isPresent()) {
            Pricing basePricing = pricingData.get();
            BigDecimal finalPrice = basePricing.getPrice().multiply(BigDecimal.valueOf(weight));

            Map<String, Object> response = new HashMap<>();
            response.put("origin", origin);
            response.put("destination", destination);
            response.put("serviceType", type);
            response.put("weight", weight);
            response.put("basePrice", basePricing.getPrice());
            response.put("finalPrice", finalPrice);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Pricing data not found for the given parameters.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
