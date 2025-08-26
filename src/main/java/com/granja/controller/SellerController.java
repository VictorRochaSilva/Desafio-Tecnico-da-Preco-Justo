package com.granja.controller;

import com.granja.dto.SellerDTO;
import com.granja.service.SellerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller para gerenciamento de vendedores da granja.
 * 
 * @author Victor Rocha Silva
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {
    
    private final SellerService sellerService;
    
    /**
     * Lista todos os vendedores.
     * 
     * @return lista de vendedores
     */
    @GetMapping
    public ResponseEntity<List<SellerDTO>> getAllSellers() {
        log.info("Listando todos os vendedores");
        List<SellerDTO> sellers = sellerService.getAllSellers();
        return ResponseEntity.ok(sellers);
    }
    
    /**
     * Obtém um vendedor por ID.
     * 
     * @param id ID do vendedor
     * @return vendedor encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<SellerDTO> getSellerById(@PathVariable Long id) {
        log.info("Buscando vendedor com ID: {}", id);
        SellerDTO seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }
    
    /**
     * Cria um novo vendedor.
     * 
     * @param sellerDTO dados do vendedor
     * @return vendedor criado
     */
    @PostMapping
    public ResponseEntity<SellerDTO> createSeller(@Valid @RequestBody SellerDTO sellerDTO) {
        log.info("Criando novo vendedor: {}", sellerDTO.getName());
        SellerDTO seller = sellerService.createSeller(sellerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(seller);
    }
    
    /**
     * Atualiza um vendedor existente.
     * 
     * @param id ID do vendedor
     * @param sellerDTO dados atualizados
     * @return vendedor atualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<SellerDTO> updateSeller(@PathVariable Long id, @Valid @RequestBody SellerDTO sellerDTO) {
        log.info("Atualizando vendedor com ID: {}", id);
        SellerDTO seller = sellerService.updateSeller(id, sellerDTO);
        return ResponseEntity.ok(seller);
    }
    
    /**
     * Remove um vendedor.
     * 
     * @param id ID do vendedor
     * @return resposta de sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        log.info("Removendo vendedor com ID: {}", id);
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Obtém ranking de vendedores por performance.
     * 
     * @return lista de vendedores ordenados por receita
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<SellerDTO>> getSellerRanking() {
        log.info("Gerando ranking de vendedores");
        List<SellerDTO> ranking = sellerService.getSellerRanking();
        return ResponseEntity.ok(ranking);
    }
}
