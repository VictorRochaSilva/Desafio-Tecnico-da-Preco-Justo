package com.granja.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

/**
 * Data Transfer Object for Sale entity operations.
 * 
 * <p>This DTO encapsulates the data required for creating and updating
 * sale records in the system.</p>
 * 
 * <p>Validation rules:
 * <ul>
 *   <li>Duck IDs list must not be empty</li>
 *   <li>Customer ID must not be null</li>
 *   <li>Seller ID must not be null</li>
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
public class SaleDTO {
    
    /**
     * Unique identifier for the sale (null for new records)
     */
    private Long id;
    
    /**
     * List of duck IDs to be sold in this transaction
     */
    @NotEmpty(message = "At least one duck must be selected for sale")
    private List<Long> duckIds;
    
    /**
     * ID of the customer making the purchase
     */
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    /**
     * ID of the seller facilitating the transaction
     */
    @NotNull(message = "Seller ID is required")
    private Long sellerId;
    
    /**
     * Original price before any discounts (calculated automatically)
     */
    private Double originalPrice;
    
    /**
     * Discount amount applied (calculated automatically)
     */
    private Double discountAmount;
    
    /**
     * Final price after applying discounts (calculated automatically)
     */
    private Double finalPrice;
}
