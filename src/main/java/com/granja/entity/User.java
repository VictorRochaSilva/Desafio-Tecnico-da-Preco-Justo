package com.granja.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário do sistema para autenticação e autorização.
 * 
 * <p>Esta entidade gerencia credenciais de usuário, funções e controle de acesso
 * dentro do sistema de gerenciamento da granja.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Gerenciamento seguro de credenciais com senhas criptografadas</li>
 *   <li>Controle de acesso baseado em funções (RBAC)</li>
 *   <li>Gerenciamento de status da conta</li>
 *   <li>Rastro de auditoria com timestamps de registro</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    /**
     * Identificador único do usuário
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nome de usuário único para autenticação
     */
    @Column(nullable = false, unique = true)
    private String username;
    
    /**
     * Senha criptografada para autenticação segura
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * Nome completo do usuário
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * Função atribuída ao usuário para controle de acesso
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    /**
     * Flag indicando se a conta do usuário está ativa
     */
    @Column(nullable = false)
    private Boolean active;
    
    /**
     * Data e hora quando o usuário foi registrado pela primeira vez
     */
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    /**
     * Callback de ciclo de vida para definir valores padrão antes da persistência
     */
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        if (active == null) {
            active = true;
        }
    }
    
    /**
     * Enumeração representando as possíveis funções que um usuário pode ter
     */
    public enum UserRole {
        /** Acesso completo ao sistema e capacidades de gerenciamento */
        ADMIN,
        /** Capacidades de vendas e gerenciamento de clientes */
        SELLER,
        /** Capacidades de gerenciamento e relatórios */
        MANAGER
    }
}
