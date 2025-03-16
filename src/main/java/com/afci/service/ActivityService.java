package com.afci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afci.repository.BookRepository;
import com.afci.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Récupérer les statistiques
        stats.put("totalBooks", bookRepository.count());
        stats.put("totalUsers", userRepository.count());
        
        return stats;
    }
}