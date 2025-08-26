package com.granja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação do Sistema de Gerenciamento da Granja de Patos.
 * 
 * <p>Este é o ponto de entrada da aplicação Spring Boot,
 * responsável por inicializar todo o sistema.</p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class DuckFarmApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DuckFarmApplication.class, args);
    }
}
