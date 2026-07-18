package com.shipping.tracking_service.repository;

import com.shipping.tracking_service.model.Tracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrackingRepository extends MongoRepository<Tracking, String> {
    Optional<Tracking> findByTrackingNumber(String trackingNumber);
}
