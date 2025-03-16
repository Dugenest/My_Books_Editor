package com.afci.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.Basket;
import com.afci.repository.BasketRepository;

@Service
@Transactional
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;  // Injection du repository pour accéder aux paniers en base de données

    // Méthode pour créer un panier
    public Basket createBasket(Basket basket) {
        return basketRepository.save(basket);  // Enregistrer le panier dans la base de données
    }

    // Méthode pour récupérer un panier par ID
    public Optional<Basket> getBasketById(Long id) {
        return basketRepository.findById(id);  // Rechercher le panier par ID
    }

    // Méthode pour ajouter un article au panier
    public Basket addToBasket(Long basketId, Basket item) {
        Optional<Basket> existingBasket = basketRepository.findById(basketId);
        
        if (existingBasket.isPresent()) {
            Basket basket = existingBasket.get();
            
            // Vérifier si le Set de livres n'est pas vide
            if (item.getBooks() != null && !item.getBooks().isEmpty()) {
                // Ajouter le premier livre du Set au panier
                basket.addBook(item.getBooks().iterator().next());
                return basketRepository.save(basket);  // Sauvegarder le panier mis à jour
            } else {
                throw new RuntimeException("Aucun livre à ajouter au panier.");
            }
        }
        
        throw new RuntimeException("Panier non trouvé pour l'ID " + basketId);
    }



    // Méthode pour supprimer un panier
    public void deleteBasket(Long id) {
        basketRepository.deleteById(id);  // Supprimer le panier par ID
    }
}
