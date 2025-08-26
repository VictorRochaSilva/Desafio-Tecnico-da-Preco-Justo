package com.granja.controller;

import com.granja.dto.LoginDTO;
import com.granja.dto.UserDTO;
import com.granja.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * REST Controller for authentication operations.
 * 
 * <p>This controller provides HTTP endpoints for user authentication
 * and user creation in the system.</p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user and returns JWT token.
     * 
     * @param loginDTO the login credentials
     * @return JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new user in the system.
     * 
     * @param userDTO the user data to create
     * @param password the password for the new user
     * @return the created user information
     */
    @PostMapping("/users/create")
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody UserDTO userDTO,
            @RequestParam String password) {
        UserDTO createdUser = authService.createUser(userDTO, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
