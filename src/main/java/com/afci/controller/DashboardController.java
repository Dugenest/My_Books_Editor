package com.afci.controller;

import com.afci.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private ActivityService activityService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        logger.info("Requête reçue pour les statistiques du tableau de bord");
        try {
            Map<String, Object> stats = activityService.getStats();
            logger.info("Statistiques récupérées avec succès: {}", stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<Map<String, Object>>> getRecentActivity() {
        logger.info("Requête reçue pour l'activité récente");
        try {
            List<Map<String, Object>> activities = activityService.getRecentActivity();
            logger.info("Activités récentes récupérées avec succès: {}", activities);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des activités récentes: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}