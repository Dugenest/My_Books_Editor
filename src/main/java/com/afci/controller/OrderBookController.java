package com.afci.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.afci.data.OrderBook;
import com.afci.service.OrderBookService;

@RestController
@RequestMapping("/api/order-books")
public class OrderBookController {
    @Autowired
    private OrderBookService orderBookService;

    @PostMapping("/orders/{orderId}/books/{bookId}")
    public ResponseEntity<OrderBook> addBookToOrder(
            @PathVariable Long orderId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(orderBookService.addBookToOrder(orderId, bookId, quantity));
    }

    @DeleteMapping("/orders/{orderId}/books/{bookId}")
    public ResponseEntity<Void> removeBookFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long bookId) {
        orderBookService.removeBookFromOrder(orderId, bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/orders/{orderId}/books/{bookId}")
    public ResponseEntity<OrderBook> updateQuantity(
            @PathVariable Long orderId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(orderBookService.updateQuantity(orderId, bookId, quantity));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderBook>> getOrderBooks(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderBookService.getOrderBooks(orderId));
    }
}