package com.granja.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um pato no sistema da granja.
 * 
 * <p>Esta entidade gerencia registros individuais de patos incluindo sua linhagem,
 * preços e status atual no sistema.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Rastreamento individual de patos com identificação única</li>
 *   <li>Gerenciamento de linhagem através de relacionamentos mãe-filho</li>
 *   <li>Gerenciamento de ciclo de vida baseado em status</li>
 *   <li>Rastreamento de preços e disponibilidade</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "ducks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Duck {
    
    /**
     * Identificador único do pato
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nome do pato para fins de identificação
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * ID da mãe do pato para rastreamento de linhagem.
     * Pode ser null para patos fundadores (primeiros patos criados).
     */
    @Column(name = "mother_id")
    private Long motherId;
    
    /**
     * Preço atual de mercado do pato
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /**
     * Status atual do pato no sistema
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DuckStatus status;
    
    /**
     * Data e hora quando o pato foi registrado pela primeira vez no sistema
     */
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;
    
    /**
     * Coleção de transações de venda envolvendo este pato
     */
    @OneToMany(mappedBy = "duck", cascade = CascadeType.ALL)
    private List<Sale> sales;
    
    /**
     * Callback de ciclo de vida para definir valores padrão antes da persistência
     */
    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        if (status == null) {
            status = DuckStatus.AVAILABLE;
        }
    }
    
    /**
     * Enumeração representando os possíveis estados de um pato no sistema
     */
    public enum DuckStatus {
        /** Pato está disponível para compra */
        AVAILABLE,
        /** Pato foi vendido */
        SOLD,
        /** Pato está reservado para um cliente específico */
        RESERVED
    }
}
