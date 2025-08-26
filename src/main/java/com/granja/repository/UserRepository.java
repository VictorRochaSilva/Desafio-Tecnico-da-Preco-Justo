package com.granja.repository;

import com.granja.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * 
 * <p>This repository provides data access methods for User entities,
 * including authentication and authorization operations.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Standard CRUD operations inherited from JpaRepository</li>
 *   <li>Username-based queries for authentication</li>
 *   <li>Active user filtering</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a user by username for authentication purposes.
     * 
     * @param username the username to search for
     * @return optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Checks if a user with the specified username exists.
     * 
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Finds an active user by username for authentication.
     * 
     * @param username the username to search for
     * @return optional containing the active user if found
     */
    Optional<User> findByUsernameAndActiveTrue(String username);
}
