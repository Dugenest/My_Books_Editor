package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(String Lastname, String Firstname);
}