package com.granja.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um vendedor no sistema da granja.
 * 
 * <p>Esta entidade gerencia informações do vendedor incluindo identificação,
 * credenciais e rastreamento de performance de vendas.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Identificação única através do CPF e ID do funcionário</li>
 *   <li>Rastreamento de performance de vendas</li>
 *   <li>Proteção contra exclusão para vendedores com histórico de vendas</li>
 *   <li>Rastro de auditoria com timestamps de registro</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seller {
    
    /**
     * Identificador único do vendedor
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nome completo do vendedor
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * CPF brasileiro (Cadastro de Pessoa Física)
     * Deve ser único entre todos os vendedores
     */
    @Column(nullable = false, unique = true)
    private String cpf;
    
    /**
     * Número de identificação do funcionário
     * Deve ser único entre todos os vendedores
     */
    @Column(nullable = false, unique = true)
    private String employeeId;
    
    /**
     * Data e hora quando o vendedor foi registrado pela primeira vez
     */
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    /**
     * Coleção de transações de venda facilitadas por este vendedor
     */
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Sale> sales;
    
    /**
     * Callback de ciclo de vida para definir valores padrão antes da persistência
     */
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
    }
}
