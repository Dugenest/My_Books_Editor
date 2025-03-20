package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afci.data.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String Lastname, String Firstname);
    
    @Modifying
    @Query(value = "UPDATE books SET author_id = NULL WHERE author_id = :authorId", nativeQuery = true)
    void detachBooksFromAuthor(@Param("authorId") Long authorId);
    
    @Modifying
    @Query(value = "DELETE FROM books WHERE author_id = :authorId", nativeQuery = true)
    void deleteBooksByAuthorId(@Param("authorId") Long authorId);
    
    @Modifying
    @Query(value = "DELETE FROM basket_books WHERE book_id IN (SELECT id FROM books WHERE author_id = :authorId)", nativeQuery = true)
    void deleteBasketBooksByAuthorId(@Param("authorId") Long authorId);
    
    @Modifying
    @Query(value = "DELETE FROM authors WHERE user_id = :authorId", nativeQuery = true)
    void deleteAuthorDirectly(@Param("authorId") Long authorId);
    
    @Modifying
    @Query(value = "DELETE FROM users WHERE id = :userId", nativeQuery = true)
    void deleteUserDirectly(@Param("userId") Long userId);
}