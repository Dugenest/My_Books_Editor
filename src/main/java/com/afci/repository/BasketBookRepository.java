package com.afci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.afci.data.BasketBook;

@Repository
public interface BasketBookRepository extends JpaRepository<BasketBook, Long> {
    @Query("SELECT bb FROM BasketBook bb WHERE bb.basket.basketId = :basketId")
    List<BasketBook> findByBasketId(@Param("basketId") Long basketId);
    
    // Recherche par ID du livre
    List<BasketBook> findByBook_Id(Long bookId);
    
    // Recherche une entrée spécifique dans le panier
    @Query("SELECT bb FROM BasketBook bb WHERE bb.basket.basketId = :basketId AND bb.book.id = :bookId")
    Optional<BasketBook> findByBasketIdAndBookId(@Param("basketId") Long basketId, @Param("bookId") Long bookId);
    
    // Supprimer tous les éléments d'un panier
    @Modifying
    @Transactional
    @Query("DELETE FROM BasketBook bb WHERE bb.basket.basketId = :basketId")
    void deleteByBasketId(@Param("basketId") Long basketId);
    
    // Vérifier si un livre existe dans un panier
    @Query("SELECT COUNT(bb) > 0 FROM BasketBook bb WHERE bb.basket.basketId = :basketId AND bb.book.id = :bookId")
    boolean existsByBasketIdAndBookId(@Param("basketId") Long basketId, @Param("bookId") Long bookId);
    
    // Compter le nombre d'articles dans un panier
    @Query("SELECT COUNT(bb) FROM BasketBook bb WHERE bb.basket.basketId = :basketId")
    long countByBasketId(@Param("basketId") Long basketId);
    
    // Trouver les paniers contenant un livre spécifique avec une quantité supérieure à
    @Query("SELECT bb FROM BasketBook bb WHERE bb.book.id = :bookId AND bb.quantity > :quantity")
    List<BasketBook> findByBookIdAndQuantityGreaterThan(@Param("bookId") Long bookId, @Param("quantity") int quantity);
    
    // Supprimer une entrée spécifique du panier
    @Modifying
    @Transactional
    @Query("DELETE FROM BasketBook bb WHERE bb.basket.basketId = :basketId AND bb.book.id = :bookId")
    void deleteByBasketIdAndBookId(@Param("basketId") Long basketId, @Param("bookId") Long bookId);
}