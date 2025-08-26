package com.granja.service;

import com.granja.dto.CustomerDTO;

import java.util.List;

/**
 * Service interface for Customer entity operations.
 * 
 * <p>This service defines the contract for customer management operations
 * following the Single Responsibility Principle and Interface Segregation Principle.</p>
 * 
 * <p>Key operations:
 * <ul>
 *   <li>CRUD operations for customer entities</li>
 *   <li>Customer profile management</li>
 *   <li>Discount eligibility management</li>
 *   <li>Customer validation and business rules</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
public interface CustomerService {
    
    /**
     * Creates a new customer record in the system.
     * 
     * @param customerDTO the customer data to create
     * @return the created customer DTO with generated ID
     * @throws IllegalArgumentException if required data is invalid
     * @throws RuntimeException if business rules are violated
     */
    CustomerDTO createCustomer(CustomerDTO customerDTO);
    
    /**
     * Updates an existing customer record.
     * 
     * @param id the unique identifier of the customer to update
     * @param customerDTO the updated customer data
     * @return the updated customer DTO
     * @throws IllegalArgumentException if the ID is invalid
     * @throws RuntimeException if the customer is not found or business rules are violated
     */
    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);
    
    /**
     * Retrieves a customer by its unique identifier.
     * 
     * @param id the unique identifier of the customer
     * @return the customer DTO if found
     * @throws RuntimeException if the customer is not found
     */
    CustomerDTO getCustomerById(Long id);
    
    /**
     * Retrieves all customers in the system.
     * 
     * @return a list of all customer DTOs
     */
    List<CustomerDTO> getAllCustomers();
    
    /**
     * Retrieves customers filtered by discount eligibility.
     * 
     * @param discountEligible the discount eligibility status to filter by
     * @return a list of customers matching the specified eligibility
     */
    List<CustomerDTO> getCustomersByDiscountEligibility(Boolean discountEligible);
    
    /**
     * Deletes a customer record from the system.
     * 
     * @param id the unique identifier of the customer to delete
     * @throws RuntimeException if the customer cannot be deleted
     */
    void deleteCustomer(Long id);
}
