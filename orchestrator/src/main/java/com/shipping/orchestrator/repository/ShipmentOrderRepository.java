package com.shipping.orchestrator.repository;

import com.shipping.orchestrator.model.ShipmentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentOrderRepository extends JpaRepository<ShipmentOrder, Long> {
}
