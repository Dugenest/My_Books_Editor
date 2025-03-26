package com.afci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Endpoint pour recevoir des messages des clients
     * et les diffuser à tous les abonnés à /topic/notifications
     */
    @MessageMapping("/send-notification")
    @SendTo("/topic/notifications")
    public Map<String, Object> sendNotification(Map<String, Object> message) {
        return message;
    }

    /**
     * Méthode utilitaire pour envoyer une notification aux clients depuis d'autres services
     */
    public void broadcastNotification(String type, String message) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", type);
        notification.put("message", message);
        notification.put("timestamp", System.currentTimeMillis());
        
        messagingTemplate.convertAndSend("/topic/notifications", notification);
    }
    
    /**
     * Méthode utilitaire pour envoyer une notification à un utilisateur spécifique
     */
    public void sendNotificationToUser(String userId, String type, String message) {
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", type);
        notification.put("message", message);
        notification.put("timestamp", System.currentTimeMillis());
        
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }
} 