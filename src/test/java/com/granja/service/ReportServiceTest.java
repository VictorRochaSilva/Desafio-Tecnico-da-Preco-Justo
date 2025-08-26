package com.granja.service;

import com.granja.entity.Customer;
import com.granja.entity.Duck;
import com.granja.entity.Duck.DuckStatus;
import com.granja.entity.Sale;
import com.granja.entity.Seller;
import com.granja.repository.SaleRepository;
import com.granja.repository.SellerRepository;
import com.granja.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para o ReportService
 * Cobre a geração de relatórios Excel e validações
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReportService - Testes Unitários")
class ReportServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Customer customer;
    private Duck duck;
    private Seller seller;
    private Sale sale;

    @BeforeEach
    void setUp() {
        // Configurar dados de teste
        customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .discountEligible(true)
                .build();

        duck = Duck.builder()
                .id(1L)
                .name("Donald Duck")
                .motherId(null)
                .price(new BigDecimal("150.00"))
                .status(DuckStatus.SOLD)
                .build();

        seller = Seller.builder()
                .id(1L)
                .name("Pedro Vendedor")
                .cpf("111.222.333-44")
                .employeeId("EMP001")
                .build();

        sale = Sale.builder()
                .id(1L)
                .duck(duck)
                .customer(customer)
                .seller(seller)
                .originalPrice(new BigDecimal("150.00"))
                .discountAmount(new BigDecimal("30.00"))
                .finalPrice(new BigDecimal("120.00"))
                .saleDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Deve gerar relatório de vendas com sucesso")
    void deveGerarRelatorioDeVendasComSucesso() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();
        List<Sale> vendas = Arrays.asList(sale);

        when(saleRepository.findBySaleDateBetween(dataInicial, dataFinal)).thenReturn(vendas);

        // Act
        byte[] resultado = reportService.generateSalesReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
        verify(saleRepository).findBySaleDateBetween(dataInicial, dataFinal);
    }

    @Test
    @DisplayName("Deve gerar relatório de vendas vazio quando não há vendas")
    void deveGerarRelatorioDeVendasVazioQuandoNaoHaVendas() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();
        List<Sale> vendas = Arrays.asList();

        when(saleRepository.findBySaleDateBetween(dataInicial, dataFinal)).thenReturn(vendas);

        // Act
        byte[] resultado = reportService.generateSalesReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0); // Deve gerar relatório mesmo vazio
        verify(saleRepository).findBySaleDateBetween(dataInicial, dataFinal);
    }

    @Test
    @DisplayName("Deve gerar relatório de ranking de vendedores com sucesso")
    void deveGerarRelatorioDeRankingDeVendedoresComSucesso() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();
        List<Sale> vendas = Arrays.asList(sale);

        when(saleRepository.findBySellerIdAndSaleDateBetween(1L, dataInicial, dataFinal)).thenReturn(vendas);
        when(sellerRepository.findAll()).thenReturn(Arrays.asList(seller));

        // Act
        byte[] resultado = reportService.generateSellerRankingReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
        verify(sellerRepository).findAll();
    }

    @Test
    @DisplayName("Deve gerar relatório de ranking de vendedores vazio quando não há vendas")
    void deveGerarRelatorioDeRankingDeVendedoresVazioQuandoNaoHaVendas() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();
        List<Sale> vendas = Arrays.asList();

        when(saleRepository.findBySellerIdAndSaleDateBetween(any(), eq(dataInicial), eq(dataFinal))).thenReturn(vendas);
        when(sellerRepository.findAll()).thenReturn(Arrays.asList(seller));

        // Act
        byte[] resultado = reportService.generateSellerRankingReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0); // Deve gerar relatório mesmo vazio
        verify(sellerRepository).findAll();
    }

    @Test
    @DisplayName("Deve validar datas de relatório")
    void deveValidarDatasDeRelatorio() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now();
        LocalDateTime dataFinal = LocalDateTime.now().minusDays(1); // Data final antes da inicial

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> reportService.generateSalesReport(dataInicial, dataFinal));
        
        verify(saleRepository, never()).findBySaleDateBetween(any(), any());
    }

    @Test
    @DisplayName("Deve gerar relatório com múltiplas vendas")
    void deveGerarRelatorioComMultiplasVendas() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();

        Sale sale2 = Sale.builder()
                .id(2L)
                .duck(Duck.builder().id(2L).name("Daisy Duck").status(DuckStatus.SOLD).build())
                .customer(Customer.builder().id(2L).name("Maria Santos").discountEligible(false).build())
                .seller(Seller.builder().id(2L).name("Ana Vendedora").build())
                .originalPrice(new BigDecimal("200.00"))
                .discountAmount(BigDecimal.ZERO)
                .finalPrice(new BigDecimal("200.00"))
                .saleDate(LocalDateTime.now())
                .build();

        List<Sale> vendas = Arrays.asList(sale, sale2);
        when(saleRepository.findBySaleDateBetween(dataInicial, dataFinal)).thenReturn(vendas);

        // Act
        byte[] resultado = reportService.generateSalesReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
        verify(saleRepository).findBySaleDateBetween(dataInicial, dataFinal);
    }

    @Test
    @DisplayName("Deve gerar relatório com vendas com desconto")
    void deveGerarRelatorioComVendasComDesconto() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();
        List<Sale> vendas = Arrays.asList(sale); // Venda com desconto

        when(saleRepository.findBySaleDateBetween(dataInicial, dataFinal)).thenReturn(vendas);

        // Act
        byte[] resultado = reportService.generateSalesReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
        // Verificar se o relatório foi gerado corretamente com dados de desconto
        verify(saleRepository).findBySaleDateBetween(dataInicial, dataFinal);
    }

    @Test
    @DisplayName("Deve gerar relatório com vendas sem desconto")
    void deveGerarRelatorioComVendasSemDesconto() {
        // Arrange
        LocalDateTime dataInicial = LocalDateTime.now().minusDays(30);
        LocalDateTime dataFinal = LocalDateTime.now();

        Sale saleSemDesconto = Sale.builder()
                .id(3L)
                .duck(Duck.builder().id(3L).name("Pato Teste").status(DuckStatus.SOLD).build())
                .customer(Customer.builder().id(3L).name("Cliente Teste").discountEligible(false).build())
                .seller(Seller.builder().id(3L).name("Vendedor Teste").build())
                .originalPrice(new BigDecimal("100.00"))
                .discountAmount(BigDecimal.ZERO)
                .finalPrice(new BigDecimal("100.00"))
                .saleDate(LocalDateTime.now())
                .build();

        List<Sale> vendas = Arrays.asList(saleSemDesconto);
        when(saleRepository.findBySaleDateBetween(dataInicial, dataFinal)).thenReturn(vendas);

        // Act
        byte[] resultado = reportService.generateSalesReport(dataInicial, dataFinal);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.length > 0);
        verify(saleRepository).findBySaleDateBetween(dataInicial, dataFinal);
    }
}
