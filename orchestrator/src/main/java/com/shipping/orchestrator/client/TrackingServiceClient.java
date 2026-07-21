package com.shipping.orchestrator.client;

import com.shipping.orchestrator.dto.TrackingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tracking-service")
public interface TrackingServiceClient {

    @PostMapping("/api/tracking")
    TrackingDTO createTracking(@RequestBody TrackingDTO tracking);
}
