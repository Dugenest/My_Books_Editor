package com.afci.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Bean
        public ObjectMapper objectMapper() {
                Hibernate5JakartaModule hibernateModule = new Hibernate5JakartaModule();
                hibernateModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, false);
                hibernateModule.configure(
                                Hibernate5JakartaModule.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
                hibernateModule.configure(Hibernate5JakartaModule.Feature.USE_TRANSIENT_ANNOTATION, true);

                return Jackson2ObjectMapperBuilder.json()
                                .modules(hibernateModule, new JavaTimeModule())
                                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                                .build();
        }

        @Bean
        public MultipartResolver multipartResolver() {
                return new StandardServletMultipartResolver();
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Configure resource handler for book covers - AJOUT DU CHEMIN /api/ POUR
                // CORRESPONDRE AUX REQUÊTES FRONTEND
                registry.addResourceHandler("/api/uploads/book-covers/**")
                                .addResourceLocations("file:uploads/book-covers/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Conserver également l'ancien chemin pour la compatibilité
                registry.addResourceHandler("/uploads/book-covers/**")
                                .addResourceLocations("file:uploads/book-covers/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Configure resource handler for avatars - AJOUT DU CHEMIN /api/ POUR
                // CORRESPONDRE AUX REQUÊTES FRONTEND
                registry.addResourceHandler("/api/uploads/avatars/**")
                                .addResourceLocations("file:uploads/avatars/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Conserver également l'ancien chemin pour la compatibilité
                registry.addResourceHandler("/uploads/avatars/**")
                                .addResourceLocations("file:uploads/avatars/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Configure resource handler for default images
                registry.addResourceHandler("/img/**")
                                .addResourceLocations("classpath:/static/img/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Configure resource handler for default assets
                registry.addResourceHandler("/assets/**")
                                .addResourceLocations("classpath:/static/assets/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Configure resource handler for static js/css files
                registry.addResourceHandler("/js/**")
                                .addResourceLocations("classpath:/static/js/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                registry.addResourceHandler("/css/**")
                                .addResourceLocations("classpath:/static/css/")
                                .setCachePeriod(3600)
                                .resourceChain(true);

                // Configure resource handler for the index.html
                registry.addResourceHandler("/index.html")
                                .addResourceLocations("classpath:/static/index.html")
                                .setCachePeriod(0) // Ne pas mettre en cache l'index.html
                                .resourceChain(true);
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                                .allowedOriginPatterns("*")
                                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                .allowedHeaders("*")
                                .exposedHeaders("Authorization", "Content-Type", "Content-Disposition")
                                .allowCredentials(true)
                                .maxAge(3600);
        }
}