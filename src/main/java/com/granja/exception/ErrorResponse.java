package com.granja.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standardized error response structure for the API.
 * 
 * <p>This class provides a consistent format for all error responses,
 * making it easier for clients to handle and display error information.</p>
 * 
 * <p>Key fields:
 * <ul>
 *   <li>timestamp: When the error occurred</li>
 *   <li>status: HTTP status code</li>
 *   <li>error: Error type/category</li>
 *   <li>message: Human-readable error description</li>
 *   <li>errorCode: Application-specific error code</li>
 *   <li>path: Request path that caused the error</li>
 *   <li>details: Additional error details (optional)</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    
    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;
    
    /**
     * HTTP status code
     */
    private Integer status;
    
    /**
     * Error type or category
     */
    private String error;
    
    /**
     * Human-readable error description
     */
    private String message;
    
    /**
     * Application-specific error code
     */
    private String errorCode;
    
    /**
     * Request path that caused the error
     */
    private String path;
    
    /**
     * Additional error details (optional)
     */
    private Map<String, Object> details;
}
