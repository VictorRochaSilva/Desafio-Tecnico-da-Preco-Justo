package com.granja.controller;

import com.granja.dto.CustomerDTO;
import com.granja.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller para gerenciamento de clientes da granja.
 * 
 * @author Victor Rocha Silva
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    /**
     * Lista todos os clientes.
     * 
     * @return lista de clientes
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        log.info("Listando todos os clientes");
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    
    /**
     * Obtém um cliente por ID.
     * 
     * @param id ID do cliente
     * @return cliente encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        log.info("Buscando cliente com ID: {}", id);
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }
    
    /**
     * Cria um novo cliente.
     * 
     * @param customerDTO dados do cliente
     * @return cliente criado
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Criando novo cliente: {}", customerDTO.getName());
        CustomerDTO customer = customerService.createCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
    
    /**
     * Atualiza um cliente existente.
     * 
     * @param id ID do cliente
     * @param customerDTO dados atualizados
     * @return cliente atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerDTO customerDTO) {
        log.info("Atualizando cliente com ID: {}", id);
        CustomerDTO customer = customerService.updateCustomer(id, customerDTO);
        return ResponseEntity.ok(customer);
    }
    
    /**
     * Remove um cliente.
     * 
     * @param id ID do cliente
     * @return resposta de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.info("Removendo cliente com ID: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
