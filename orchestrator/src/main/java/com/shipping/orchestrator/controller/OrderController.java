package com.shipping.orchestrator.controller;

import com.shipping.orchestrator.dto.CreateOrderRequest;
import com.shipping.orchestrator.model.OrderResult;
import com.shipping.orchestrator.service.OrderOrchestratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderOrchestratorService orderOrchestratorService;

    // CREATE ORDER -> triggers the full orchestration saga
    @PostMapping
    public ResponseEntity<OrderResult> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResult order = orderOrchestratorService.createOrder(request);

        HttpStatusCode status = "COMPLETED".equals(order.getStatus())
                ? HttpStatus.CREATED
                : HttpStatusCode.valueOf(422);

        return new ResponseEntity<>(order, status);
    }

    // READ ALL (in-memory only, resets on restart — for demo/debugging the saga states)
    @GetMapping
    public ResponseEntity<Collection<OrderResult>> getAllOrders() {
        return new ResponseEntity<>(orderOrchestratorService.getAllOrders(), HttpStatus.OK);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderResult> getOrderById(@PathVariable Long id) {
        OrderResult order = orderOrchestratorService.getOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
