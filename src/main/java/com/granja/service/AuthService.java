package com.granja.service;

import com.granja.dto.LoginDTO;
import com.granja.dto.UserDTO;
import com.granja.entity.User;
import com.granja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for user authentication and registration operations.
 * 
 * <p>This service handles user login, JWT token generation,
 * and user creation with proper password encoding.</p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    
    /**
     * Authenticates a user and generates JWT token.
     * 
     * @param loginDTO the login credentials
     * @return map containing JWT token and user information
     */
    public Map<String, Object> login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", convertToDTO(user.orElse(null)));
        response.put("message", "Login successful");
        
        return response;
    }
    
    /**
     * Creates a new user in the system.
     * 
     * @param userDTO the user data to create (including password)
     * @return the created user DTO
     */
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = User.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .role(userDTO.getRole())
                .active(true)
                .build();
        
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }
    
    /**
     * Converts User entity to UserDTO.
     * 
     * @param user the source entity
     * @return the converted DTO
     */
    private UserDTO convertToDTO(User user) {
        if (user == null) return null;
        
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .role(user.getRole())
                .active(user.getActive())
                .build();
    }
}
