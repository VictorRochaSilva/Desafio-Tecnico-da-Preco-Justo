package com.granja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Objeto de Transferência de Dados para operações da entidade Duck.
 * 
 * <p>Este DTO encapsula os dados necessários para criar e atualizar
 * registros de patos no sistema.</p>
 * 
 * <p>Regras de validação:
 * <ul>
 *   <li>Nome não pode estar em branco</li>
 *   <li>Preço deve ser maior que zero</li>
 *   <li>Nome da mãe é opcional para rastreamento de linhagem</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuckDTO {
    
    /**
     * Identificador único do pato (nulo para novos registros)
     */
    private Long id;
    
    /**
     * Nome do pato para identificação
     */
    @NotBlank(message = "Nome do pato é obrigatório")
    private String name;
    
    /**
     * ID da mãe do pato para rastreamento de linhagem.
     * Pode ser null para patos fundadores (primeiros patos criados).
     */
    private Long motherId;
    
    /**
     * Preço atual de mercado do pato
     */
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal price;
    
    /**
     * Status atual do pato no sistema
     */
    private String status;
}
