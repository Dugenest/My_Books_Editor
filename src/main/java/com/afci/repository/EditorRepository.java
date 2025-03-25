package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.afci.data.Editor;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {

//     // Utilisez cette méthode si vous avez un champ companyId
//     List<Editor> findByCompanyNameContainingIgnoreCase(String companyName);

//    // Et éventuellement ajouter une méthode pour rechercher par nom d'utilisateur
//    List<Editor> findByUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(String firstName, String lastName);
}