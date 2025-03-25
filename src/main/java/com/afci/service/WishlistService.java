package com.afci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afci.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    public long countByUserId(Long userId) {
        return wishlistRepository.countByUserId(userId);
    }
}
