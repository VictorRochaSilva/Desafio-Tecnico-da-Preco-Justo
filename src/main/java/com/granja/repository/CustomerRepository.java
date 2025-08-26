package com.granja.repository;

import com.granja.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entity operations.
 * 
 * <p>This repository provides data access methods for Customer entities,
 * including business-specific queries for customer management.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>CPF-based queries for unique identification</li>
 *   <li>Discount eligibility filtering</li>
 *   <li>Customer search capabilities</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    /**
     * Finds a customer by CPF for unique identification.
     * 
     * @param cpf the CPF to search for
     * @return optional containing the customer if found
     */
    Optional<Customer> findByCpf(String cpf);
    
    /**
     * Checks if a customer with the specified CPF exists.
     * 
     * @param cpf the CPF to check
     * @return true if a customer with the CPF exists, false otherwise
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Finds customers filtered by discount eligibility.
     * 
     * @param discountEligible the discount eligibility status to filter by
     * @return list of customers matching the specified eligibility
     */
    List<Customer> findByDiscountEligible(Boolean discountEligible);
    
    /**
     * Finds customers by name for search purposes.
     * 
     * @param name the name to search for (partial match)
     * @return list of customers with matching names
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customer> findByNameContainingIgnoreCase(@Param("name") String name);
}
