package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Serial;

import java.util.List;

@Repository
public interface SerialRepository extends JpaRepository<Serial, Long> {
    List<Serial> findByTitleContainingIgnoreCase(String title);
    List<Serial> findByAuthor_Id(Long authorId);
}