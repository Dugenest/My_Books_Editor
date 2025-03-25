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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.afci.data.Order;
import com.afci.data.OrderStatus;
import com.afci.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setCustomerId(1L);
        order.setStatus(OrderStatus.PENDING);
    }

    @Test
    void getAllOrders_ShouldReturnOrders() {
        List<Order> orders = Arrays.asList(order);
        when(orderService.getAllOrders()).thenReturn(orders);

        ResponseEntity<List<Order>> response = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        ResponseEntity<Order> response = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Order responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(order.getId(), responseBody.getId());
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<Order> response = orderController.createOrder(order);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateOrder_ShouldReturnUpdatedOrder() {
        when(orderService.updateOrder(any(Order.class))).thenReturn(order);

        ResponseEntity<Order> response = orderController.updateOrder(1L, order);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() {
        doNothing().when(orderService).deleteOrder(1L);

        ResponseEntity<Void> response = orderController.deleteOrder(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getCustomerOrders_ShouldReturnOrders() {
        Page<Order> orderPage = new PageImpl<>(Arrays.asList(order));
        when(orderService.getCustomerOrders(1L)).thenReturn(orderPage);

        ResponseEntity<Page<Order>> response = orderController.getCustomerOrders(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void createOrderFromBasket_ShouldReturnCreatedOrder() {
        when(orderService.createOrderFromBasket(1L, 1L)).thenReturn(order);

        ResponseEntity<Order> response = orderController.createOrderFromBasket(1L, 1L);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() {
        when(orderService.updateOrderStatus(1L, OrderStatus.COMPLETED)).thenReturn(order);

        ResponseEntity<Order> response = orderController.updateOrderStatus(1L, OrderStatus.COMPLETED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void cancelOrder_ShouldReturnCancelledOrder() {
        when(orderService.cancelOrder(1L)).thenReturn(order);

        ResponseEntity<Order> response = orderController.cancelOrder(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
} 