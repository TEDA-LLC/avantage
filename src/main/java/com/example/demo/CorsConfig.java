//package com.example.demo;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer{
//
////    @Override
////    public void addCorsMappings(CorsRegistry registry) {
////        registry.addMapping("/**")
////                .allowedOrigins("*")
////                .allowedMethods("POST", "GET", "PUT", "DELETE", "OPTIONS")
////                .allowedHeaders("Accept", "Content-Type", "Content-Length", "Accept-Encoding", "X-CSRF-Token", "Authorization")
////                .exposedHeaders("X-Auth-Token")
////                .allowCredentials(true)
////                .maxAge(3600);
////    }
//
//    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("*");
//        config.addAllowedMethod("*");
//        config.addAllowedHeader("*");
//        config.setAllowCredentials(true);
//        return config;
//    }
//}
