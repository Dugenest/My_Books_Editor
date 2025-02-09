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
@CrossOrigin(origins = "*")
public class BasketController {

    @Autowired
    private BasketService basketService;  // Injection du service BasketService

    // Méthode pour créer un panier
    @Operation(summary = "Create a new basket")
    @PostMapping("/create")
    public ResponseEntity<Basket> createBasket(@RequestBody Basket basket) {
        Basket createdBasket = basketService.createBasket(basket); // Appel au service pour créer un panier
        return ResponseEntity.ok(createdBasket);
    }

    // Méthode pour récupérer un panier par ID
    @Operation(summary = "Get basket by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable Long id) {
        return basketService.getBasketById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Méthode pour ajouter un article au panier
    @Operation(summary = "Add book to basket")
    @PostMapping("/add/{basketId}")
    public ResponseEntity<Basket> addBookToBasket(
        @Parameter(description = "Basket ID") @PathVariable Long basketId,
        @RequestBody Basket basket
    ) {
        Basket updatedBasket = basketService.addToBasket(basketId, basket);
        return ResponseEntity.ok(updatedBasket);
    }

    // Méthode pour supprimer un panier
    @Operation(summary = "Delete basket")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        basketService.deleteBasket(id);  // Appel du service pour supprimer le panier
        return ResponseEntity.ok().build();
    }
}
