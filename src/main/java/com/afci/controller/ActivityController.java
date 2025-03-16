package com.afci.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentActivities(@RequestParam(defaultValue = "10") int limit) {
        // Données statiques pour le moment
        List<Map<String, Object>> activities = new ArrayList<>();
        
        for (int i = 0; i < limit; i++) {
            Map<String, Object> activity = new HashMap<>();
            activity.put("id", i + 1);
            activity.put("type", i % 3 == 0 ? "login" : (i % 3 == 1 ? "order" : "profile_update"));
            activity.put("user", "Utilisateur " + (i + 1));
            activity.put("timestamp", new Date());
            activity.put("details", "Activité " + (i + 1));
            activities.add(activity);
        }
        
        return ResponseEntity.ok(activities);
    }
}