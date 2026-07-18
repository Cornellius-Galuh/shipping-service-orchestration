package com.shipping.pricing_service.repository;

import com.shipping.pricing_service.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {
    Optional<Pricing> findByOriginCityAndDestinationCityAndServiceType(String originCity, String destinationCity, String serviceType);
}
