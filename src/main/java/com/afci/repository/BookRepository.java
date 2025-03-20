package com.afci.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Méthodes existantes conservées

    // Recherche par prix
    List<Book> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Recherche par année de publication
    @Query("SELECT b FROM Book b WHERE EXTRACT(YEAR FROM b.publishDate) = :year")
    List<Book> findByPublishDateYear(@Param("year") int year);

    // Recherche par auteur et catégorie
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE b.author.id = :authorId AND c.id = :categoryId")
    List<Book> findByAuthor_IdAndCategory_Id(Long authorId, Long categoryId);

    // Recherche des livres disponibles en stock
    List<Book> findByStockGreaterThan(int minStock);
    
    List<Book> findByPublishDateAfterOrderByPublishDateDesc(LocalDate date);
    
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.name = :categoryName")
    List<Book> findByCategory_Name(@Param("categoryName") String categoryName);

    List<Book> findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(String lastName,
            String firstName);        

    // Recherche complexe avec plusieurs critères
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:authorName IS NULL OR LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :authorName, '%'))) AND " +
           "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<Book> searchBooks(
    	    @Param("title") String title,
    	    @Param("authorName") String authorName,
    	    @Param("minPrice") BigDecimal minPrice,
    	    @Param("maxPrice") BigDecimal maxPrice,
    	    org.springframework.data.domain.Pageable pageable
    	);

    // Compter le nombre de livres par catégorie
    @Query("SELECT COUNT(b) FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    long countBooksByCategory(@Param("categoryId") Long categoryId);

    // Vérifier l'existence d'un livre par ISBN
    boolean existsByISBN(String isbn);

    // Recherche par mots-clés multiples
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.detail) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    
    List<Book> searchByKeyword(@Param("keyword") String keyword);
  
}