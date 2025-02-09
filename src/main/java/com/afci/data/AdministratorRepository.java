package com.afci.data;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    List<Administrator> findByRole(String role);  // This should work if the 'role' property exists in the Administrator class

	Optional<Administrator> findByEmail(String email);
}
