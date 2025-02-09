package com.afci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.Order;
import com.afci.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "Order Management", description = "Order operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Get all orders")
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
        @Parameter(description = "Order ID") @PathVariable Long id
    ) {
        return orderService.getOrderById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get customer orders")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(
        @Parameter(description = "Customer ID") @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    @Operation(summary = "Create order from basket")
    @PostMapping("/customer/{customerId}/basket/{basketId}")
    public ResponseEntity<Order> createOrderFromBasket(
        @Parameter(description = "Customer ID") @PathVariable Long customerId,
        @Parameter(description = "Basket ID") @PathVariable Long basketId
    ) {
        return new ResponseEntity<>(
            orderService.createOrderFromBasket(customerId, basketId), 
            HttpStatus.CREATED
        );
    };
}