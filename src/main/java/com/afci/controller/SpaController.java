package com.afci.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    /**
     * Redirige toutes les routes qui ne sont pas des API vers le point d'entrée de l'application Vue.js
     * pour permettre au router Vue.js de gérer ces routes côté client.
     * 
     * @return La page index.html qui contient l'application Vue.js
     */
    @GetMapping(value = {
        "/", 
        "/login", 
        "/register", 
        "/books", 
        "/authors", 
        "/categories", 
        "/basket", 
        "/orders", 
        "/profile", 
        "/dashboard/**", 
        "/unauthorized"
    })
    public String forward() {
        return "forward:/index.html";
    }
} 