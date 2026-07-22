package com.shipping.orchestrator.client;

import com.shipping.orchestrator.dto.PaymentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentServiceClient {

    @PostMapping("/api/payments")
    PaymentDTO createPayment(@RequestBody PaymentDTO payment);

    @PutMapping("/api/payments/{id}")
    PaymentDTO updatePayment(@PathVariable("id") String id, @RequestBody PaymentDTO payment);
}
