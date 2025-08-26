package com.granja.service;

import com.granja.dto.SaleDTO;

import java.util.List;

/**
 * Service interface for Sale entity operations.
 * 
 * <p>This service defines the contract for sale management operations
 * following the Single Responsibility Principle and Interface Segregation Principle.</p>
 * 
 * <p>Key operations:
 * <ul>
 *   <li>CRUD operations for sale entities</li>
 *   <li>Business logic for sales processing</li>
 *   <li>Automatic discount calculation</li>
 *   <li>Sales validation and business rules</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
public interface SaleService {
    
    /**
     * Creates a new sale transaction in the system.
     * 
     * @param saleDTO the sale data to create
     * @return the created sale DTO with generated ID
     * @throws IllegalArgumentException if required data is invalid
     * @throws RuntimeException if business rules are violated
     */
    SaleDTO createSale(SaleDTO saleDTO);
    
    /**
     * Updates an existing sale record.
     * 
     * @param id the unique identifier of the sale to update
     * @param saleDTO the updated sale data
     * @return the updated sale DTO
     * @throws IllegalArgumentException if the ID is invalid
     * @throws RuntimeException if the sale is not found or business rules are violated
     */
    SaleDTO updateSale(Long id, SaleDTO saleDTO);
    
    /**
     * Retrieves a sale by its unique identifier.
     * 
     * @param id the unique identifier of the sale
     * @return the sale DTO if found
     * @throws RuntimeException if the sale is not found
     */
    SaleDTO getSaleById(Long id);
    
    /**
     * Retrieves all sales in the system.
     * 
     * @return a list of all sale DTOs
     */
    List<SaleDTO> getAllSales();
    
    /**
     * Retrieves sales filtered by customer ID.
     * 
     * @param customerId the customer ID to filter by
     * @return a list of sales for the specified customer
     */
    List<SaleDTO> getSalesByCustomerId(Long customerId);
    
    /**
     * Retrieves sales filtered by seller ID.
     * 
     * @param sellerId the seller ID to filter by
     * @return a list of sales for the specified seller
     */
    List<SaleDTO> getSalesBySellerId(Long sellerId);
    
    /**
     * Deletes a sale record from the system.
     * 
     * @param id the unique identifier of the sale to delete
     * @throws RuntimeException if the sale cannot be deleted
     */
    void deleteSale(Long id);
}
