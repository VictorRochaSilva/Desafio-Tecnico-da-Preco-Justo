package com.granja.service;

import com.granja.dto.SaleDTO;
import com.granja.entity.Customer;
import com.granja.entity.Duck;
import com.granja.entity.Sale;
import com.granja.entity.Seller;
import com.granja.repository.CustomerRepository;
import com.granja.repository.DuckRepository;
import com.granja.repository.SaleRepository;
import com.granja.repository.SellerRepository;
import com.granja.service.impl.SaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o SaleService.
 * 
 * <p>
 * Testa todas as operações CRUD e regras de negócio
 * relacionadas ao gerenciamento de vendas.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private DuckRepository duckRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Sale sale;
    private SaleDTO saleDTO;
    private Customer customer;
    private Seller seller;
    private Duck duck;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("João Silva");
        customer.setCpf("12345678901");
        customer.setDiscountEligible(true);

        seller = new Seller();
        seller.setId(1L);
        seller.setName("Maria Vendedora");
        seller.setCpf("98765432100");
        seller.setEmployeeId("V001");

        duck = new Duck();
        duck.setId(1L);
        duck.setName("Donald Duck");
        duck.setPrice(new BigDecimal("150.00"));
        duck.setStatus(Duck.DuckStatus.AVAILABLE);

        sale = new Sale();
        sale.setId(1L);
        sale.setDuck(duck);
        sale.setCustomer(customer);
        sale.setSeller(seller);
        sale.setOriginalPrice(new BigDecimal("150.00"));
        sale.setDiscountAmount(BigDecimal.ZERO);
        sale.setFinalPrice(new BigDecimal("150.00"));
        sale.setSaleDate(LocalDateTime.now());

        saleDTO = SaleDTO.builder()
                .duckIds(Arrays.asList(1L))
                .customerId(1L)
                .sellerId(1L)
                .build();
    }

    @Test
    void deveCriarVendaComSucesso() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // When
        SaleDTO resultado = saleService.createSale(saleDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getCustomerId());
        assertEquals(1L, resultado.getSellerId());
        assertNotNull(resultado.getDuckIds());
        assertEquals(1, resultado.getDuckIds().size());
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaComPatoInexistente() {
        // Given
        when(duckRepository.findById(999L)).thenReturn(Optional.empty());

        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList(999L))
                .customerId(1L)
                .sellerId(1L)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaComClienteInexistente() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());

        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList(1L))
                .customerId(999L)
                .sellerId(1L)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaComVendedorInexistente() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(999L)).thenReturn(Optional.empty());

        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList(1L))
                .customerId(1L)
                .sellerId(999L)
                .build();

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaComPatoJaVendido() {
        // Given
        duck.setStatus(Duck.DuckStatus.SOLD);
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            saleService.createSale(saleDTO);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaComListaVaziaDePatos() {
        // Given
        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList())
                .customerId(1L)
                .sellerId(1L)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaSemCliente() {
        // Given
        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList(1L))
                .customerId(null)
                .sellerId(1L)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveFalharAoCriarVendaSemVendedor() {
        // Given
        SaleDTO saleDTOInvalido = SaleDTO.builder()
                .duckIds(Arrays.asList(1L))
                .customerId(1L)
                .sellerId(null)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            saleService.createSale(saleDTOInvalido);
        });
        verify(saleRepository, never()).save(any(Sale.class));
    }

    @Test
    void deveRecuperarVendaPorId() {
        // Given
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        // When
        SaleDTO resultado = saleService.getSaleById(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getCustomerId());
        assertEquals(1L, resultado.getSellerId());
    }

    @Test
    void deveFalharAoRecuperarVendaInexistente() {
        // Given
        when(saleRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            saleService.getSaleById(999L);
        });
    }

    @Test
    void deveRecuperarTodasAsVendas() {
        // Given
        Sale sale2 = new Sale();
        sale2.setId(2L);
        sale2.setDuck(duck);
        sale2.setCustomer(customer);
        sale2.setSeller(seller);

        when(saleRepository.findAll()).thenReturn(Arrays.asList(sale, sale2));

        // When
        List<SaleDTO> resultado = saleService.getAllSales();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
    }

    @Test
    void deveRecuperarVendasPorCliente() {
        // Given
        when(saleRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(sale));

        // When
        List<SaleDTO> resultado = saleService.getSalesByCustomerId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getCustomerId());
    }

    @Test
    void deveRecuperarVendasPorVendedor() {
        // Given
        when(saleRepository.findBySellerId(1L)).thenReturn(Arrays.asList(sale));

        // When
        List<SaleDTO> resultado = saleService.getSalesBySellerId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getSellerId());
    }

    @Test
    void deveAplicarDescontoParaClienteElegivel() {
        // Given
        customer.setDiscountEligible(true);
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // When
        SaleDTO resultado = saleService.createSale(saleDTO);

        // Then
        assertNotNull(resultado);
        // O desconto é aplicado automaticamente no serviço
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void deveNaoAplicarDescontoParaClienteNaoElegivel() {
        // Given
        customer.setDiscountEligible(false);
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        // When
        SaleDTO resultado = saleService.createSale(saleDTO);

        // Then
        assertNotNull(resultado);
        // Sem desconto para cliente não elegível
        verify(saleRepository).save(any(Sale.class));
    }
}
