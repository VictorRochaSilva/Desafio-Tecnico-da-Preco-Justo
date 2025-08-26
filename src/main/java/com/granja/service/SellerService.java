package com.granja.service;

import com.granja.dto.SellerDTO;

import java.util.List;

/**
 * Service interface for Seller entity operations.
 * 
 * <p>This service defines the contract for seller management operations
 * following the Single Responsibility Principle and Interface Segregation Principle.</p>
 * 
 * <p>Key operations:
 * <ul>
 *   <li>CRUD operations for seller entities</li>
 *   <li>Seller performance tracking</li>
 *   <li>Deletion protection for sellers with sales history</li>
 *   <li>Seller validation and business rules</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
public interface SellerService {
    
    /**
     * Creates a new seller record in the system.
     * 
     * @param sellerDTO the seller data to create
     * @return the created seller DTO with generated ID
     * @throws IllegalArgumentException if required data is invalid
     * @throws RuntimeException if business rules are violated
     */
    SellerDTO createSeller(SellerDTO sellerDTO);
    
    /**
     * Updates an existing seller record.
     * 
     * @param id the unique identifier of the seller to update
     * @param sellerDTO the updated seller data
     * @return the updated seller DTO
     * @throws IllegalArgumentException if the ID is invalid
     * @throws RuntimeException if the seller is not found or business rules are violated
     */
    SellerDTO updateSeller(Long id, SellerDTO sellerDTO);
    
    /**
     * Retrieves a seller by its unique identifier.
     * 
     * @param id the unique identifier of the seller
     * @return the seller DTO if found
     * @throws RuntimeException if the seller is not found
     */
    SellerDTO getSellerById(Long id);
    
    /**
     * Retrieves all sellers in the system.
     * 
     * @return a list of all seller DTOs
     */
    List<SellerDTO> getAllSellers();
    
    /**
     * Retrieves sellers filtered by performance metrics.
     * 
     * @param minSalesCount the minimum number of sales to filter by
     * @return a list of sellers meeting the performance criteria
     */
    List<SellerDTO> getSellersByPerformance(Integer minSalesCount);
    
    /**
     * Deletes a seller record from the system.
     * 
     * @param id the unique identifier of the seller to delete
     * @throws RuntimeException if the seller cannot be deleted (e.g., has sales history)
     */
    void deleteSeller(Long id);
}
