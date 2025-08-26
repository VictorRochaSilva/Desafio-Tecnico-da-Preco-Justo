package com.granja.service.impl;

import com.granja.dto.SaleDTO;
import com.granja.entity.Customer;
import com.granja.entity.Duck;
import com.granja.entity.Sale;
import com.granja.entity.Seller;
import com.granja.exception.BusinessException;
import com.granja.repository.CustomerRepository;
import com.granja.repository.DuckRepository;
import com.granja.repository.SaleRepository;
import com.granja.repository.SellerRepository;
import com.granja.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of SaleService for managing sales operations.
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {
    
    private final SaleRepository saleRepository;
    private final DuckRepository duckRepository;
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    
    @Override
    @Transactional
    public SaleDTO createSale(SaleDTO saleDTO) {
        log.info("Creating sale: {}", saleDTO);
        
        // Validate entities exist
        Customer customer = customerRepository.findById(saleDTO.getCustomerId())
                .orElseThrow(() -> new BusinessException("Customer not found"));
        
        Seller seller = sellerRepository.findById(saleDTO.getSellerId())
                .orElseThrow(() -> new BusinessException("Seller not found"));
        
        List<Duck> ducks = duckRepository.findAllById(saleDTO.getDuckIds());
        if (ducks.size() != saleDTO.getDuckIds().size()) {
            throw new BusinessException("Some ducks not found");
        }
        
        // Check if ducks are available
        for (Duck duck : ducks) {
            if (duck.getStatus() != Duck.DuckStatus.AVAILABLE) {
                throw new BusinessException("Duck " + duck.getName() + " is not available");
            }
        }
        
        // Calculate total price
        BigDecimal totalPrice = ducks.stream()
                .map(Duck::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Apply discount if customer is eligible
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (customer.getDiscountEligible()) {
            discountAmount = totalPrice.multiply(new BigDecimal("0.20")); // 20% discount
        }
        
        BigDecimal finalPrice = totalPrice.subtract(discountAmount);
        
        // Create sale - note: this is a simplified version for now
        // The actual Sale entity has a single duck relationship, not multiple
        Sale sale = Sale.builder()
                .duck(ducks.get(0)) // For now, just use the first duck
                .customer(customer)
                .seller(seller)
                .originalPrice(totalPrice)
                .discountAmount(discountAmount)
                .finalPrice(finalPrice)
                .saleDate(LocalDateTime.now())
                .build();
        
        Sale savedSale = saleRepository.save(sale);
        
        // Update duck status to SOLD
        for (Duck duck : ducks) {
            duck.setStatus(Duck.DuckStatus.SOLD);
            duckRepository.save(duck);
        }
        
        log.info("Sale created successfully: {}", savedSale.getId());
        
        return SaleDTO.builder()
                .id(savedSale.getId())
                .duckIds(saleDTO.getDuckIds())
                .customerId(savedSale.getCustomer().getId())
                .sellerId(savedSale.getSeller().getId())
                .originalPrice(savedSale.getOriginalPrice().doubleValue())
                .discountAmount(savedSale.getDiscountAmount().doubleValue())
                .finalPrice(savedSale.getFinalPrice().doubleValue())
                .build();
    }
    
    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        log.info("Updating sale: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sale not found"));
        
        // Update logic would go here
        // For now, just return the existing sale
        return mapToDTO(sale);
    }
    
    @Override
    public SaleDTO getSaleById(Long id) {
        log.info("Fetching sale by id: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sale not found"));
        return mapToDTO(sale);
    }
    
    @Override
    public List<SaleDTO> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public List<SaleDTO> getSalesByCustomerId(Long customerId) {
        log.info("Fetching sales by customer id: {}", customerId);
        return saleRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public List<SaleDTO> getSalesBySellerId(Long sellerId) {
        log.info("Fetching sales by seller id: {}", sellerId);
        return saleRepository.findBySellerId(sellerId).stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public void deleteSale(Long id) {
        log.info("Deleting sale: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Sale not found"));
        saleRepository.delete(sale);
    }
    
    private SaleDTO mapToDTO(Sale sale) {
        return SaleDTO.builder()
                .id(sale.getId())
                .duckIds(List.of(sale.getDuck().getId())) // Single duck for now
                .customerId(sale.getCustomer().getId())
                .sellerId(sale.getSeller().getId())
                .originalPrice(sale.getOriginalPrice().doubleValue())
                .discountAmount(sale.getDiscountAmount().doubleValue())
                .finalPrice(sale.getFinalPrice().doubleValue())
                .build();
    }
}
