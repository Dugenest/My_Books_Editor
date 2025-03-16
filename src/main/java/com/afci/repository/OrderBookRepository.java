package com.afci.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afci.data.OrderBook;

@Repository
public interface OrderBookRepository extends JpaRepository<OrderBook, Long> {
    // Recherche par ID de la commande
    List<OrderBook> findByOrderId(Long orderId);
    
    // Recherche par ID du livre
    List<OrderBook> findByBookId(Long bookId);
    
    // Recherche une entrée spécifique dans la commande
    Optional<OrderBook> findByOrderIdAndBookId(Long orderId, Long bookId);
    
    // Supprimer tous les éléments d'une commande
    void deleteByOrderId(Long orderId);
    
    // Vérifier si un livre existe dans une commande
    boolean existsByOrderIdAndBookId(Long orderId, Long bookId);
    
    // Compter le nombre d'articles dans une commande
    long countByOrderId(Long orderId);
    
    // Trouver les commandes contenant un livre spécifique
    List<OrderBook> findByBookIdOrderByOrderDesc(Long bookId);
    
    // Trouver les commandes avec une quantité supérieure à
    List<OrderBook> findByQuantityGreaterThan(int quantity);
    
    // Calculer la somme des quantités pour un livre spécifique
    @Query("SELECT SUM(ob.quantity) FROM OrderBook ob WHERE ob.book.id = :bookId")
    Integer getTotalQuantityForBook(@Param("bookId") Long bookId);
    
    // Trouver les livres les plus commandés
    @Query("SELECT ob.book.id, SUM(ob.quantity) as total FROM OrderBook ob GROUP BY ob.book.id ORDER BY total DESC")
    List<Object[]> findMostOrderedBooks();
}