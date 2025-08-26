package com.granja.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um cliente no sistema da granja.
 * 
 * <p>Esta entidade gerencia informações do cliente incluindo detalhes de contato,
 * elegibilidade para desconto e histórico de compras.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Gerenciamento completo de perfil do cliente</li>
 *   <li>Rastreamento de elegibilidade para desconto</li>
 *   <li>Histórico de compras através de relacionamentos de vendas</li>
 *   <li>Identificação única através do CPF</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    
    /**
     * Identificador único do cliente
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nome completo do cliente
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * CPF brasileiro (Cadastro de Pessoa Física)
     * Deve ser único entre todos os clientes
     */
    @Column(nullable = false, unique = true)
    private String cpf;
    
    /**
     * Número de telefone para contato
     */
    @Column(nullable = false)
    private String phone;
    
    /**
     * Informações completas do endereço
     */
    @Column(nullable = false)
    private String address;
    
    /**
     * Flag indicando se o cliente é elegível para descontos
     */
    @Column(name = "discount_eligible", nullable = false)
    private Boolean discountEligible;
    
    /**
     * Data e hora quando o cliente foi registrado pela primeira vez
     */
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    /**
     * Coleção de transações de venda realizadas por este cliente
     */
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Sale> sales;
    
    /**
     * Callback de ciclo de vida para definir valores padrão antes da persistência
     */
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        if (discountEligible == null) {
            discountEligible = false;
        }
    }
}
