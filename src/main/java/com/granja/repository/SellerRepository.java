package com.granja.repository;

import com.granja.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Seller entity operations.
 * 
 * <p>This repository provides data access methods for Seller entities,
 * including business-specific queries for seller management.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>CPF and employee ID-based queries for unique identification</li>
 *   <li>Performance tracking queries</li>
 *   <li>Sales history validation</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    
    /**
     * Finds a seller by CPF for unique identification.
     * 
     * @param cpf the CPF to search for
     * @return optional containing the seller if found
     */
    Optional<Seller> findByCpf(String cpf);
    
    /**
     * Finds a seller by employee ID for unique identification.
     * 
     * @param employeeId the employee ID to search for
     * @return optional containing the seller if found
     */
    Optional<Seller> findByEmployeeId(String employeeId);
    
    /**
     * Checks if a seller with the specified CPF exists.
     * 
     * @param cpf the CPF to check
     * @return true if a seller with the CPF exists, false otherwise
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Checks if a seller with the specified employee ID exists.
     * 
     * @param employeeId the employee ID to check
     * @return true if a seller with the employee ID exists, false otherwise
     */
    boolean existsByEmployeeId(String employeeId);
    
    /**
     * Finds sellers with minimum sales count for performance tracking.
     * 
     * @param minSalesCount the minimum number of sales required
     * @return list of sellers meeting the performance criteria
     */
    // TODO: Implement proper performance query when sales relationship is stable
    // List<Seller> findByMinSalesCount(@Param("minSalesCount") Integer minSalesCount);
}
