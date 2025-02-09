package com.afci.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
   
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByCategory_Name(String categoryName);
    List<Book> findByAuthor_LastNameContainingIgnoreCaseOrAuthor_FirstNameContainingIgnoreCase(String lastName, String firstName);
}
