package com.afci.service;

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

import com.afci.data.Customer;
import com.afci.data.Order;
import com.afci.data.Payment;
import com.afci.data.PaymentRepository;
import com.afci.data.PaymentStatus;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    private Payment payment;
    private Order order;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        order = new Order();
        order.setId(1L);

        payment = new Payment();
        payment.setId(1L);
        payment.setOrder(order);
        payment.setStatus(PaymentStatus.PENDING);
    }

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        when(paymentRepository.findAll()).thenReturn(Arrays.asList(payment));

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPaymentsByCustomer_ShouldReturnCustomerPayments() {
        when(paymentRepository.findByCustomer(customer)).thenReturn(Arrays.asList(payment));

        List<Payment> result = paymentService.getPaymentsByCustomer(customer);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPaymentById_ShouldReturnPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Optional<Payment> result = paymentService.getPaymentById(1L);

        assertTrue(result.isPresent());
        assertEquals(payment.getId(), result.get().getId());
    }

    @Test
    void processPayment_ShouldCreatePayment() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.processPayment(1L, payment);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(order, result.getOrder());
    }

    @Test
    void updatePaymentStatus_ShouldUpdateStatus() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.updatePaymentStatus(1L, PaymentStatus.COMPLETED);

        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
    }

    @Test
    void getOrderPayments_ShouldReturnPayments() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Arrays.asList(payment));

        List<Payment> result = paymentService.getOrderPayments(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
} 