package com.afci.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketBookRepository extends JpaRepository<BasketBook, Long> {
    // Recherche par ID du panier
    List<BasketBook> findByBasketId(Long basketId);
    
    // Recherche par ID du livre
    List<BasketBook> findByBookId(Long bookId);
    
    // Recherche une entrée spécifique dans le panier
    Optional<BasketBook> findByBasketIdAndBookId(Long basketId, Long bookId);
    
    // Supprimer tous les éléments d'un panier
    void deleteByBasketId(Long basketId);
    
    // Vérifier si un livre existe dans un panier
    boolean existsByBasketIdAndBookId(Long basketId, Long bookId);
    
    // Compter le nombre d'articles dans un panier
    long countByBasketId(Long basketId);
    
    // Trouver les paniers contenant un livre spécifique avec une quantité supérieure à
    List<BasketBook> findByBookIdAndQuantityGreaterThan(Long bookId, int quantity);
    
    // Supprimer une entrée spécifique du panier
    void deleteByBasketIdAndBookId(Long basketId, Long bookId);
}