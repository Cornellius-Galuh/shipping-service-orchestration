package com.shipping.payment_service.controller;

import com.shipping.payment_service.model.Payment;
import com.shipping.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus("PENDING");
        }
        if ("SUCCESS".equalsIgnoreCase(payment.getPaymentStatus()) && payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }
        
        Payment savedPayment = paymentRepository.save(payment);
        return new ResponseEntity<>(savedPayment, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    // READ ONE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String id) {
        Optional<Payment> paymentData = paymentRepository.findById(id);

        if (paymentData.isPresent()) {
            return new ResponseEntity<>(paymentData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // READ BY SHIPMENT ID
    @GetMapping("/shipment/{shipmentId}")
    public ResponseEntity<Payment> getPaymentByShipmentId(@PathVariable Integer shipmentId) {
        Optional<Payment> paymentData = paymentRepository.findByShipmentId(shipmentId);

        if (paymentData.isPresent()) {
            return new ResponseEntity<>(paymentData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable String id, @RequestBody Payment payment) {
        Optional<Payment> paymentData = paymentRepository.findById(id);

        if (paymentData.isPresent()) {
            Payment _payment = paymentData.get();
            _payment.setShipmentId(payment.getShipmentId());
            _payment.setAmount(payment.getAmount());
            _payment.setPaymentMethod(payment.getPaymentMethod());
            _payment.setPaymentStatus(payment.getPaymentStatus());
            
            if ("SUCCESS".equalsIgnoreCase(payment.getPaymentStatus()) && _payment.getPaymentDate() == null) {
                _payment.setPaymentDate(LocalDateTime.now());
            } else if (!"SUCCESS".equalsIgnoreCase(payment.getPaymentStatus())) {
                _payment.setPaymentDate(null);
            }

            _payment.setUpdatedAt(LocalDateTime.now());

            return new ResponseEntity<>(paymentRepository.save(_payment), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePayment(@PathVariable String id) {
        try {
            paymentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
