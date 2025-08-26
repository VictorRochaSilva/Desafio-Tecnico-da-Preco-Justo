package com.granja.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import com.granja.entity.User.UserRole;

/**
 * Data Transfer Object for User entity operations.
 * 
 * <p>This DTO encapsulates the data required for creating and updating
 * user records in the system.</p>
 * 
 * <p>Validation rules:
 * <ul>
 *   <li>Username must not be blank</li>
 *   <li>Name must not be blank</li>
 *   <li>Role must not be null</li>
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
public class UserDTO {
    
    /**
     * Unique identifier for the user (null for new records)
     */
    private Long id;
    
    /**
     * Unique username for authentication
     */
    @NotBlank(message = "Username is required")
    private String username;
    
    /**
     * Password for authentication (required for new users)
     */
    @NotBlank(message = "Password is required")
    private String password;
    
    /**
     * Full name of the user
     */
    @NotBlank(message = "User name is required")
    private String name;
    
    /**
     * Role assigned to the user for access control
     */
    @NotNull(message = "User role is required")
    private UserRole role;
    
    /**
     * Flag indicating if the user account is active
     */
    private Boolean active;
}
