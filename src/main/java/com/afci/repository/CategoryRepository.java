package com.afci.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.afci.data.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameIgnoreCase(String name);

    List<Category> findByNameContainingIgnoreCase(String name);

    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.books WHERE c.id = :categoryId")
    Category initializeBooks(@Param("categoryId") Long categoryId);
}