package com.granja.service.impl;

import com.granja.dto.CustomerDTO;
import com.granja.entity.Customer;
import com.granja.exception.BusinessException;
import com.granja.repository.CustomerRepository;
import com.granja.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of CustomerService for managing customer operations.
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating customer: {}", customerDTO);
        
        // Check if CPF already exists
        if (customerRepository.existsByCpf(customerDTO.getCpf())) {
            throw new BusinessException("CPF already registered");
        }
        
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .cpf(customerDTO.getCpf())
                .phone(customerDTO.getPhone())
                .address(customerDTO.getAddress())
                .discountEligible(customerDTO.getDiscountEligible())
                .registrationDate(LocalDateTime.now())
                .build();
        
        Customer savedCustomer = customerRepository.save(customer);
        
        return CustomerDTO.builder()
                .id(savedCustomer.getId())
                .name(savedCustomer.getName())
                .cpf(savedCustomer.getCpf())
                .phone(savedCustomer.getPhone())
                .address(savedCustomer.getAddress())
                .discountEligible(savedCustomer.getDiscountEligible())
                .build();
    }
    
    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        log.info("Updating customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found"));
        
        // Update fields
        customer.setName(customerDTO.getName());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setDiscountEligible(customerDTO.getDiscountEligible());
        
        Customer savedCustomer = customerRepository.save(customer);
        
        return CustomerDTO.builder()
                .id(savedCustomer.getId())
                .name(savedCustomer.getName())
                .cpf(savedCustomer.getCpf())
                .phone(savedCustomer.getPhone())
                .address(savedCustomer.getAddress())
                .discountEligible(savedCustomer.getDiscountEligible())
                .build();
    }
    
    @Override
    public CustomerDTO getCustomerById(Long id) {
        log.info("Fetching customer by id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found"));
        
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .discountEligible(customer.getDiscountEligible())
                .build();
    }
    
    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public List<CustomerDTO> getCustomersByDiscountEligibility(Boolean discountEligible) {
        log.info("Fetching customers by discount eligibility: {}", discountEligible);
        return customerRepository.findByDiscountEligible(discountEligible).stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public void deleteCustomer(Long id) {
        log.info("Deleting customer: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found"));
        customerRepository.delete(customer);
    }
    
    private CustomerDTO mapToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .cpf(customer.getCpf())
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .discountEligible(customer.getDiscountEligible())
                .build();
    }
}
