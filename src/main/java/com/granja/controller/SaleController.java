package com.granja.controller;

import com.granja.dto.SaleDTO;
import com.granja.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Sale operations.
 * 
 * <p>This controller provides HTTP endpoints for managing
 * sales transactions in the system.</p>
 * 
 * <p>Key features:
 * <ul>
 *   <li>Sale creation and management</li>
 *   <li>Sales listing and filtering</li>
 *   <li>Business rule enforcement</li>
 *   <li>Automatic discount calculation</li>
 * </ul></p>
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Sale Management", description = "Operations for managing sales transactions")
public class SaleController {
    
    private final SaleService saleService;
    
    /**
     * Creates a new sale transaction.
     * 
     * @param saleDTO the sale data to create
     * @return the created sale with generated ID
     */
    @Operation(summary = "Create a new sale", description = "Creates a new sale transaction in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sale created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<SaleDTO> createSale(
            @Parameter(description = "Sale data to create", required = true)
            @Valid @RequestBody SaleDTO saleDTO) {
        
        log.info("Received request to create sale for customer ID: {}", saleDTO.getCustomerId());
        SaleDTO createdSale = saleService.createSale(saleDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }
    
    /**
     * Retrieves all sales in the system.
     * 
     * @return list of all sales
     */
    @Operation(summary = "Get all sales", description = "Retrieves all sales in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sales retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        log.debug("Retrieving all sales");
        List<SaleDTO> sales = saleService.getAllSales();
        
        return ResponseEntity.ok(sales);
    }
    
    /**
     * Retrieves a sale by its unique identifier.
     * 
     * @param id the unique identifier of the sale
     * @return the sale if found
     */
    @Operation(summary = "Get sale by ID", description = "Retrieves a specific sale by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sale retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Sale not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<SaleDTO> getSaleById(
            @Parameter(description = "Sale ID to retrieve", required = true)
            @PathVariable Long id) {
        
        log.debug("Retrieving sale with ID: {}", id);
        SaleDTO sale = saleService.getSaleById(id);
        
        return ResponseEntity.ok(sale);
    }
}
