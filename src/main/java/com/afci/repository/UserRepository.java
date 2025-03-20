package com.afci.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afci.data.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
    @Modifying
    @Query(value = "DELETE FROM books WHERE author_id = :authorId", nativeQuery = true)
    void deleteAuthorBooksById(@Param("authorId") Long authorId);
    
    @Modifying
    @Query(value = "DELETE FROM authors WHERE user_id = :authorId", nativeQuery = true)
    void deleteAuthorById(@Param("authorId") Long authorId);
}