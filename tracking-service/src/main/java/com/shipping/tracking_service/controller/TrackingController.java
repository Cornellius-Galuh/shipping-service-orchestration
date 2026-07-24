package com.shipping.tracking_service.controller;

import com.shipping.tracking_service.model.Tracking;
import com.shipping.tracking_service.model.TrackingHistory;
import com.shipping.tracking_service.repository.TrackingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingRepository trackingRepository;

    // CREATE (Inisiasi awal saat paket baru dibuat)
    @PostMapping
    public ResponseEntity<Tracking> createTracking(@RequestBody Tracking tracking) {
        Tracking savedTracking = trackingRepository.save(tracking);
        return new ResponseEntity<>(savedTracking, HttpStatus.CREATED);
    }

    // READ (Melacak paket berdasarkan resi)
    @GetMapping("/{trackingNumber}")
    public ResponseEntity<Tracking> getTracking(@PathVariable String trackingNumber) {
        Optional<Tracking> trackingData = trackingRepository.findByTrackingNumber(trackingNumber);

        if (trackingData.isPresent()) {
            return new ResponseEntity<>(trackingData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE STATUS (Mendorong lokasi baru ke array history)
    @PutMapping("/{trackingNumber}/update-status")
    public ResponseEntity<Tracking> updateStatus(
            @PathVariable String trackingNumber,
            @RequestBody TrackingHistory newHistory) {
        
        Optional<Tracking> trackingData = trackingRepository.findByTrackingNumber(trackingNumber);

        if (trackingData.isPresent()) {
            Tracking tracking = trackingData.get();
            tracking.addHistory(newHistory);
            
            Tracking updatedTracking = trackingRepository.save(tracking);
            return new ResponseEntity<>(updatedTracking, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE (Menghapus data resi)
    @DeleteMapping("/{trackingNumber}")
    public ResponseEntity<HttpStatus> deleteTracking(@PathVariable String trackingNumber) {
        Optional<Tracking> trackingData = trackingRepository.findByTrackingNumber(trackingNumber);
        
        if (trackingData.isPresent()) {
            trackingRepository.delete(trackingData.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
