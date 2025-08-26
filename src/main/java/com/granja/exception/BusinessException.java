package com.granja.exception;

/**
 * Custom exception for business rule violations.
 * 
 * <p>This exception is thrown when business logic rules are violated,
 * providing clear error messages for debugging and user feedback.</p>
 * 
 * <p>Usage examples:
 * <ul>
 *   <li>Attempting to sell an already sold duck</li>
 *   <li>Deleting a seller with sales history</li>
 *   <li>Invalid discount calculations</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * Constructs a new business exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
    }
    
    /**
     * Constructs a new business exception with the specified detail message and error code.
     * 
     * @param message the detail message
     * @param errorCode the specific error code for this exception
     */
    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructs a new business exception with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
    }
    
    /**
     * Constructs a new business exception with the specified detail message, error code, and cause.
     * 
     * @param message the detail message
     * @param errorCode the specific error code for this exception
     * @param cause the cause of the exception
     */
    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * Gets the error code associated with this exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
