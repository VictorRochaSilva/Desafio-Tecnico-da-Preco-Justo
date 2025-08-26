package com.granja.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Data Transfer Object for Seller entity operations.
 * 
 * <p>This DTO encapsulates the data required for creating and updating
 * seller records in the system.</p>
 * 
 * <p>Validation rules:
 * <ul>
 *   <li>Name must not be blank</li>
 *   <li>CPF must not be blank</li>
 *   <li>Employee ID must not be blank</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerDTO {
    
    /**
     * Unique identifier for the seller (null for new records)
     */
    private Long id;
    
    /**
     * Full name of the seller
     */
    @NotBlank(message = "Seller name is required")
    private String name;
    
    /**
     * Brazilian CPF (Individual Taxpayer Registration)
     * Must be unique across all sellers
     */
    @NotBlank(message = "CPF is required")
    private String cpf;
    
    /**
     * Employee identification number
     * Must be unique across all sellers
     */
    @NotBlank(message = "Employee ID is required")
    private String employeeId;
}
