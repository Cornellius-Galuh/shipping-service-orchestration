package com.shipping.orchestrator.client;

import com.shipping.orchestrator.dto.TrackingRequest;
import com.shipping.orchestrator.dto.TrackingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tracking-service")
public interface TrackingClient {

    @PostMapping("/api/tracking")
    TrackingResponse createTracking(@RequestBody TrackingRequest request);
}
