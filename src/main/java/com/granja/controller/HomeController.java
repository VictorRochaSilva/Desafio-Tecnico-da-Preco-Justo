package com.granja.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple home controller for the root endpoint.
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
public class HomeController {
    
    /**
     * Root endpoint that returns basic API information.
     * 
     * @return map containing API information
     */
    @GetMapping("/")
    public Map<String, Object> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Duck Farm Management API");
        response.put("version", "1.0.0");
        response.put("status", "running");
        response.put("documentation", "/swagger-ui/index.html");
        response.put("health", "/actuator/health");
        return response;
    }
}
