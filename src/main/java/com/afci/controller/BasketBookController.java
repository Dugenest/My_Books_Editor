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

import com.afci.data.BasketBook;
import com.afci.service.BasketBookService;

@RestController
@RequestMapping("/api/basket-books")
public class BasketBookController {
    @Autowired
    private BasketBookService basketBookService;

    @PostMapping("/baskets/{basketId}/books/{bookId}")
    public ResponseEntity<BasketBook> addBookToBasket(
            @PathVariable Long basketId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(basketBookService.addBookToBasket(basketId, bookId, quantity));
    }

    @DeleteMapping("/baskets/{basketId}/books/{bookId}")
    public ResponseEntity<Void> removeBookFromBasket(
            @PathVariable Long basketId,
            @PathVariable Long bookId) {
        basketBookService.removeBookFromBasket(basketId, bookId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/baskets/{basketId}/books/{bookId}")
    public ResponseEntity<BasketBook> updateQuantity(
            @PathVariable Long basketId,
            @PathVariable Long bookId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(basketBookService.updateQuantity(basketId, bookId, quantity));
    }

    @GetMapping("/baskets/{basketId}")
    public ResponseEntity<List<BasketBook>> getBasketBooks(@PathVariable Long basketId) {
        return ResponseEntity.ok(basketBookService.getBasketBooks(basketId));
    }

    @GetMapping("/baskets/{basketId}/total")
    public ResponseEntity<Double> getBasketTotal(@PathVariable Long basketId) {
        return ResponseEntity.ok(basketBookService.calculateBasketTotal(basketId));
    }

    @DeleteMapping("/baskets/{basketId}/clear")
    public ResponseEntity<Void> clearBasket(@PathVariable Long basketId) {
        basketBookService.clearBasket(basketId);
        return ResponseEntity.noContent().build();
    }
}