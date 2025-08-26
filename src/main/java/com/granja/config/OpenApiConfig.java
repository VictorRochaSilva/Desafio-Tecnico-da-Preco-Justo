package com.granja.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI configuration for Swagger documentation.
 * 
 * <p>This configuration provides comprehensive API documentation
 * including metadata, contact information, and server configurations.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Complete API metadata and description</li>
 *   <li>Contact information for developers</li>
 *   <li>License information</li>
 *   <li>Server environment configurations</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class OpenApiConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Duck Farm Management API")
                        .description("""
                                Comprehensive REST API for managing a duck farm business.
                                
                                ## Features
                                - **Duck Management**: Complete lifecycle management of individual ducks
                                - **Customer Management**: Customer profiles with discount eligibility
                                - **Sales Operations**: Transaction processing with automatic discount calculation
                                - **Seller Management**: Employee tracking and performance metrics
                                - **Reporting**: Excel report generation and analytics
                                - **Security**: JWT-based authentication with role-based access control
                                
                                ## Business Rules
                                - Ducks can only be sold once
                                - Eligible customers receive 20% discount automatically
                                - Sellers with sales history cannot be deleted
                                - All transactions are timestamped and audited
                                
                                ## Authentication
                                All endpoints (except `/api/auth/**`) require valid JWT token.
                                Include token in Authorization header: `Bearer <token>`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Victor da Rocha Silva")
                                .email("victowrs.rocha@gmail.com")
                                .url("https://github.com/VictorRochaSilva"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Local Development Environment"),
                        new Server()
                                .url("https://api.granjasystem.com")
                                .description("Production Environment")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token obtido através do endpoint de login")));
    }
}
