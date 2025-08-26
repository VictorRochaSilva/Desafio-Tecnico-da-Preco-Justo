package com.granja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Data Transfer Object for Customer entity operations.
 * 
 * <p>This DTO encapsulates the data required for creating and updating
 * customer records in the system.</p>
 * 
 * <p>Validation rules:
 * <ul>
 *   <li>Name must not be blank</li>
 *   <li>CPF must not be blank</li>
 *   <li>Phone must not be blank</li>
 *   <li>Address must not be blank</li>
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
public class CustomerDTO {
    
    /**
     * Unique identifier for the customer (null for new records)
     */
    private Long id;
    
    /**
     * Full name of the customer
     */
    @NotBlank(message = "Customer name is required")
    private String name;
    
    /**
     * Brazilian CPF (Individual Taxpayer Registration)
     * Must be unique across all customers
     */
    @NotBlank(message = "CPF is required")
    private String cpf;
    
    /**
     * Contact phone number
     */
    @NotBlank(message = "Phone number is required")
    private String phone;
    
    /**
     * Complete address information
     */
    @NotBlank(message = "Address is required")
    private String address;
    
    /**
     * Flag indicating if customer is eligible for discounts
     */
    @NotNull(message = "Discount eligibility status is required")
    private Boolean discountEligible;
}
