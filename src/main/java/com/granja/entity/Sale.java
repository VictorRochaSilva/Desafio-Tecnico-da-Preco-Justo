package com.granja.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidade que representa uma transação de venda no sistema da granja.
 * 
 * <p>Esta entidade gerencia o ciclo de vida completo das vendas incluindo preços,
 * descontos e gerenciamento de relacionamentos entre patos, clientes e vendedores.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Rastreamento completo de transações com detalhes de preços</li>
 *   <li>Cálculo automático de desconto para clientes elegíveis</li>
 *   <li>Rastro de auditoria com timestamps</li>
 *   <li>Gerenciamento de relacionamentos com outras entidades</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    
    /**
     * Identificador único da transação de venda
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Referência ao pato sendo vendido
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "duck_id", nullable = false)
    private Duck duck;
    
    /**
     * Referência ao cliente realizando a compra
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    /**
     * Referência ao vendedor que facilitou a transação
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;
    
    /**
     * Preço original do pato antes de qualquer desconto
     */
    @Column(name = "original_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    /**
     * Valor do desconto aplicado à venda
     */
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;
    
    /**
     * Preço final após aplicar descontos
     */
    @Column(name = "final_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalPrice;
    
    /**
     * Data e hora quando a venda foi concluída
     */
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;
    
    /**
     * Callback de ciclo de vida para definir valores padrão antes da persistência
     */
    @PrePersist
    protected void onCreate() {
        saleDate = LocalDateTime.now();
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        if (finalPrice == null) {
            finalPrice = originalPrice.subtract(discountAmount);
        }
    }
}
