package com.afci.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Order;
import com.afci.data.Payment;
import com.afci.data.PaymentStatus;
import com.afci.service.PaymentService;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment payment;
    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);

        payment = new Payment();
        payment.setId(1L);
        payment.setOrder(order);
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    void getPaymentById_ShouldReturnPayment() {
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));

        ResponseEntity<Payment> response = paymentController.getPaymentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(payment.getId(), response.getBody().getId());
    }

    @Test
    void getOrderPayments_ShouldReturnPayments() {
        List<Payment> payments = Arrays.asList(payment);
        when(paymentService.getOrderPayments(1L)).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentController.getOrderPayments(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void processPayment_ShouldReturnProcessedPayment() {
        when(paymentService.processPayment(eq(1L), any(Payment.class))).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.processPayment(1L, payment);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updatePaymentStatus_ShouldReturnUpdatedPayment() {
        when(paymentService.updatePaymentStatus(1L, PaymentStatus.COMPLETED)).thenReturn(payment);

        ResponseEntity<Payment> response = 
            paymentController.updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
} 