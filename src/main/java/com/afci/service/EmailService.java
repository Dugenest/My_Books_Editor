package com.afci.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.frontend-url:http://localhost:8081}")
    private String frontendUrl;
    
    @Value("${spring.mail.from:no-reply@mybooks.com}")
    private String fromEmail;
    
    // Mode simulé (pas d'envoi réel d'emails)
    private static final boolean SIMULATION_MODE = true;
    
    /**
     * Envoie un email de confirmation d'inscription
     * L'annotation @Async permet d'envoyer l'email en arrière-plan
     */
    @Async
    public void sendConfirmationEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Confirmation de votre compte MyBooks");
        
        String confirmationUrl = frontendUrl + "/confirm?token=" + token;
        
        message.setText("Bonjour,\n\n" +
                "Merci pour votre inscription sur MyBooks. Pour activer votre compte, " +
                "veuillez cliquer sur le lien suivant :\n\n" +
                confirmationUrl + "\n\n" +
                "Ce lien est valable pendant 24 heures.\n\n" +
                "Si vous n'avez pas demandé cette inscription, vous pouvez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "L'équipe MyBooks");
        
        if (SIMULATION_MODE) {
            logger.info("MODE SIMULATION: Email de confirmation qui serait envoyé à {}", to);
            logger.info("URL de confirmation: {}", confirmationUrl);
        } else {
            try {
                mailSender.send(message);
            } catch (Exception e) {
                logger.error("Échec d'envoi d'email. URL de confirmation: {}", confirmationUrl, e);
            }
        }
    }
    
    /**
     * Envoie un email de réinitialisation de mot de passe
     */
    @Async
    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Réinitialisation de votre mot de passe MyBooks");
        
        String resetUrl = frontendUrl + "/reset-password?token=" + token;
        
        message.setText("Bonjour,\n\n" +
                "Vous avez demandé la réinitialisation de votre mot de passe. " +
                "Pour créer un nouveau mot de passe, veuillez cliquer sur le lien suivant :\n\n" +
                resetUrl + "\n\n" +
                "Ce lien est valable pendant 1 heure.\n\n" +
                "Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet email.\n\n" +
                "Cordialement,\n" +
                "L'équipe MyBooks");
        
        if (SIMULATION_MODE) {
            logger.info("MODE SIMULATION: Email de réinitialisation de mot de passe qui serait envoyé à {}", to);
            logger.info("URL de réinitialisation: {}", resetUrl);
        } else {
            try {
                mailSender.send(message);
            } catch (Exception e) {
                logger.error("Échec d'envoi d'email. URL de réinitialisation: {}", resetUrl, e);
            }
        }
    }
}