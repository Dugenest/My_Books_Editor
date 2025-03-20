package com.afci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afci.repository.BookRepository;
import com.afci.repository.UserRepository;
import com.afci.repository.AuthorRepository;
import com.afci.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired(required = false)
    private AuthorRepository authorRepository;

    @Autowired(required = false)
    private OrderRepository orderRepository;

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // Statistiques des livres
        long totalBooks = bookRepository.count();
        long outOfStock = 0;
        try {
            // Si vous avez cette méthode ou utilisez une requête personnalisée
            // outOfStock = bookRepository.countByStockLessThanEqual(0);
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage des livres en rupture de stock: " + e.getMessage());
        }

        // Statistiques des utilisateurs
        long totalUsers = userRepository.count();

        // Statistiques des auteurs
        long totalAuthors = 0;
        try {
            if (authorRepository != null) {
                totalAuthors = authorRepository.count();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage des auteurs: " + e.getMessage());
        }

        // Statistiques des commandes
        long totalOrders = 0;
        double totalRevenue = 0.0;
        try {
            if (orderRepository != null) {
                totalOrders = orderRepository.count();
                // Si vous avez cette méthode
                // totalRevenue = orderRepository.sumTotalAmount();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du comptage des commandes: " + e.getMessage());
        }

        // Variations simulées (à remplacer par des calculs réels si disponibles)
        double usersChange = 2.5;
        double ordersChange = 3.8;
        double revenueChange = 1.2;

        // Ajouter toutes les statistiques au Map
        stats.put("totalBooks", totalBooks);
        stats.put("outOfStock", outOfStock);
        stats.put("totalUsers", totalUsers);
        stats.put("usersChange", usersChange);
        stats.put("totalOrders", totalOrders);
        stats.put("ordersChange", ordersChange);
        stats.put("totalRevenue", totalRevenue);
        stats.put("revenueChange", revenueChange);
        stats.put("totalAuthors", totalAuthors);

        return stats;
    }

    public List<Map<String, Object>> getRecentActivity() {
        // Créer une liste pour stocker les activités récentes
        List<Map<String, Object>> activities = new ArrayList<>();

        try {
            // Exemple d'activité de commande
            Map<String, Object> orderActivity = new HashMap<>();
            orderActivity.put("type", "order");
            orderActivity.put("description", "Nouvelle commande #123 par Client A");
            orderActivity.put("date", new Date());
            orderActivity.put("amount", 42.50);
            orderActivity.put("status", "PENDING");
            activities.add(orderActivity);

            // Exemple d'activité d'utilisateur
            Map<String, Object> userActivity = new HashMap<>();
            userActivity.put("type", "user");
            userActivity.put("description", "Nouvel utilisateur Jean Dupont");
            userActivity.put("date", new Date(System.currentTimeMillis() - 3600000)); // 1 heure avant
            userActivity.put("email", "jean.dupont@example.com");
            activities.add(userActivity);

            // Exemple d'activité de livre
            Map<String, Object> bookActivity = new HashMap<>();
            bookActivity.put("type", "book");
            bookActivity.put("description", "Nouveau livre ajouté: Le Petit Prince");
            bookActivity.put("date", new Date(System.currentTimeMillis() - 7200000)); // 2 heures avant
            bookActivity.put("author", "Antoine de Saint-Exupéry");
            activities.add(bookActivity);

            // Exemple d'activité d'auteur
            Map<String, Object> authorActivity = new HashMap<>();
            authorActivity.put("type", "author");
            authorActivity.put("description", "Nouvel auteur ajouté: Victor Hugo");
            authorActivity.put("date", new Date(System.currentTimeMillis() - 10800000)); // 3 heures avant
            authorActivity.put("books", 5);
            activities.add(authorActivity);

            // Exemple d'activité de catégorie
            Map<String, Object> categoryActivity = new HashMap<>();
            categoryActivity.put("type", "category");
            categoryActivity.put("description", "Nouvelle catégorie: Science-Fiction");
            categoryActivity.put("date", new Date(System.currentTimeMillis() - 14400000)); // 4 heures avant
            categoryActivity.put("books", 12);
            activities.add(categoryActivity);

        } catch (Exception e) {
            System.err.println("Erreur lors de la création des activités récentes: " + e.getMessage());
            e.printStackTrace();

            // En cas d'erreur, au moins retourner une activité par défaut
            Map<String, Object> defaultActivity = new HashMap<>();
            defaultActivity.put("type", "system");
            defaultActivity.put("description", "Activité du système");
            defaultActivity.put("date", new Date());
            activities.add(defaultActivity);
        }

        return activities;
    }
}