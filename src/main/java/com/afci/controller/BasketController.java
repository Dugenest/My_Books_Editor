package com.afci.controller;

import com.afci.data.Basket;
import com.afci.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Operation(summary = "Create a new basket")
    @PostMapping("/create")
    public ResponseEntity<Basket> createBasket(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Basket details")
        @RequestBody Basket basket  // Maintenant utilise l'annotation Spring
    ) {
        Basket createdBasket = basketService.createBasket(basket);
        return ResponseEntity.ok(createdBasket);
    }

    @Operation(summary = "Get basket by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return basketService.getBasketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add book to basket")
    @PostMapping("/add/{basketId}")
    public ResponseEntity<Basket> addBookToBasket(
        @Parameter(description = "Basket ID") @PathVariable Long basketId,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Basket with books")
        @RequestBody Basket basket  // Maintenant utilise l'annotation Spring
    ) {
        Basket updatedBasket = basketService.addToBasket(basketId, basket);
        return ResponseEntity.ok(updatedBasket);
    }

    @Operation(summary = "Delete basket")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        basketService.deleteBasket(id);
        return ResponseEntity.ok().build();
    }
}