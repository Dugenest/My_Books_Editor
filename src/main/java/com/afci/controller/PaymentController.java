package com.afci.controller;

import com.afci.data.Payment;
import com.afci.data.PaymentStatus;
import com.afci.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Management", description = "Payment operations")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Get payment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(
        @Parameter(description = "Payment ID") @PathVariable Long id
    ) {
        Payment payment = paymentService.getPaymentById(id)
            .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec l'ID : " + id));
        return ResponseEntity.ok(payment);
    }

    @Operation(summary = "Get all payments for an order")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getOrderPayments(
        @Parameter(description = "Order ID") @PathVariable Long orderId
    ) {
        List<Payment> payments = paymentService.getOrderPayments(orderId);
        return ResponseEntity.ok(payments);
    }

    @Operation(summary = "Process a payment")
    @PostMapping("/order/{orderId}")
    public ResponseEntity<Payment> processPayment(
        @Parameter(description = "Order ID") @PathVariable Long orderId,
        @Parameter(description = "Payment details") @Valid @RequestBody Payment payment
    ) {
        Payment processedPayment = paymentService.processPayment(orderId, payment);
        return new ResponseEntity<>(processedPayment, HttpStatus.CREATED);
    }

    @Operation(summary = "Update payment status")
    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(
        @Parameter(description = "Payment ID") @PathVariable Long id,
        @Parameter(description = "Payment status") @RequestBody PaymentStatus status
    ) {
        Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(updatedPayment);
    }
}
