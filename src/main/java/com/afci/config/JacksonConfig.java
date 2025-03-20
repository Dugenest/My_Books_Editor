package com.afci.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    /**
     * Configuration améliorée pour ObjectMapper qui combine les fonctionnalités
     * de WebConfig et JacksonConfig pour éviter les erreurs de sérialisation.
     * Utilise @Primary pour indiquer que ce bean doit être préféré en cas de conflit.
     */
    @Bean
    @Primary
    public ObjectMapper enhancedObjectMapper() {
        Hibernate5JakartaModule hibernate5Module = new Hibernate5JakartaModule();
        // Configure le module Hibernate pour gérer les collections lazy
        hibernate5Module.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, false);
        
        return Jackson2ObjectMapperBuilder.json()
                .modules(hibernate5Module, new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .build();
    }
} 