package com.granja.service.impl;

import com.granja.dto.SellerDTO;
import com.granja.entity.Seller;
import com.granja.exception.BusinessException;
import com.granja.repository.SellerRepository;
import com.granja.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of SellerService for managing seller operations.
 * 
 * @author Granja System
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    
    private final SellerRepository sellerRepository;
    
    @Override
    public SellerDTO createSeller(SellerDTO sellerDTO) {
        log.info("Creating seller: {}", sellerDTO);
        
        // Check if CPF already exists
        if (sellerRepository.existsByCpf(sellerDTO.getCpf())) {
            throw new BusinessException("CPF already registered");
        }
        
        // Check if employee ID already exists
        if (sellerRepository.existsByEmployeeId(sellerDTO.getEmployeeId())) {
            throw new BusinessException("Employee ID already registered");
        }
        
        Seller seller = Seller.builder()
                .name(sellerDTO.getName())
                .cpf(sellerDTO.getCpf())
                .employeeId(sellerDTO.getEmployeeId())
                .registrationDate(LocalDateTime.now())
                .build();
        
        Seller savedSeller = sellerRepository.save(seller);
        
        return SellerDTO.builder()
                .id(savedSeller.getId())
                .name(savedSeller.getName())
                .cpf(savedSeller.getCpf())
                .employeeId(savedSeller.getEmployeeId())
                .build();
    }
    
    @Override
    public SellerDTO updateSeller(Long id, SellerDTO sellerDTO) {
        log.info("Updating seller: {}", id);
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Seller not found"));
        
        // Update fields
        seller.setName(sellerDTO.getName());
        
        Seller savedSeller = sellerRepository.save(seller);
        
        return SellerDTO.builder()
                .id(savedSeller.getId())
                .name(savedSeller.getName())
                .cpf(savedSeller.getCpf())
                .employeeId(savedSeller.getEmployeeId())
                .build();
    }
    
    @Override
    public SellerDTO getSellerById(Long id) {
        log.info("Fetching seller by id: {}", id);
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Seller not found"));
        
        return SellerDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .cpf(seller.getCpf())
                .employeeId(seller.getEmployeeId())
                .build();
    }
    
    @Override
    public List<SellerDTO> getAllSellers() {
        log.info("Fetching all sellers");
        return sellerRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public List<SellerDTO> getSellersByPerformance(Integer minSalesCount) {
        log.info("Fetching sellers by performance, min sales: {}", minSalesCount);
        // TODO: Implement actual performance filtering logic when sales relationship is stable
        // For now, return all sellers
        return sellerRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    @Override
    public void deleteSeller(Long id) {
        log.info("Deleting seller: {}", id);
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Seller not found"));
        sellerRepository.delete(seller);
    }
    
    @Override
    public List<SellerDTO> getSellerRanking() {
        log.info("Generating seller ranking by performance");
        // TODO: Implement actual ranking logic when sales relationship is stable
        // For now, return all sellers ordered by registration date (newest first)
        return sellerRepository.findAllByOrderByRegistrationDateDesc().stream()
                .map(this::mapToDTO)
                .toList();
    }
    
    private SellerDTO mapToDTO(Seller seller) {
        return SellerDTO.builder()
                .id(seller.getId())
                .name(seller.getName())
                .cpf(seller.getCpf())
                .employeeId(seller.getEmployeeId())
                .build();
    }
}
