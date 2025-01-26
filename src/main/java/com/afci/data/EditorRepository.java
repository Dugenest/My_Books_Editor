package com.afci.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EditorRepository extends JpaRepository<Editor, Long> {

}