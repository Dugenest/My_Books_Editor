package com.afci.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.afci.data.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    long countByUserId(Long userId);
}
