package com.granja.integration;

import com.granja.dto.DuckDTO;
import com.granja.entity.Duck;
import com.granja.entity.Duck.DuckStatus;
import com.granja.repository.DuckRepository;
import com.granja.service.DuckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de integração para o Duck
 * Testa a integração entre todas as camadas da aplicação
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Duck - Teste de Integração")
class DuckIntegrationTest {

    @Autowired
    private DuckService duckService;

    @Autowired
    private DuckRepository duckRepository;

    private DuckDTO duckDTO;

    @BeforeEach
    void setUp() {
        // Limpar dados de teste
        duckRepository.deleteAll();

        // Configurar dados de teste
        duckDTO = DuckDTO.builder()
                .name("Pato Teste Integração")
                .motherId(null)
                .price(new BigDecimal("100.00"))
                .status("AVAILABLE")
                .build();
    }

    @Test
    @DisplayName("Deve criar e recuperar pato através de todas as camadas")
    void deveCriarERecuperarPatoAtravesDeTodasAsCamadas() {
        // Act - Criar pato
        DuckDTO patoCriado = duckService.createDuck(duckDTO);

        // Assert - Verificar se foi criado
        assertNotNull(patoCriado);
        assertNotNull(patoCriado.getId());
        assertEquals("Pato Teste Integração", patoCriado.getName());
        assertEquals(100.00, patoCriado.getPrice().doubleValue());
        assertEquals("AVAILABLE", patoCriado.getStatus());

        // Act - Recuperar pato
        DuckDTO patoRecuperado = duckService.getDuckById(patoCriado.getId());

        // Assert - Verificar se foi recuperado corretamente
        assertNotNull(patoRecuperado);
        assertEquals(patoCriado.getId(), patoRecuperado.getId());
        assertEquals("Pato Teste Integração", patoRecuperado.getName());
        assertEquals(100.00, patoRecuperado.getPrice().doubleValue());
    }

    @Test
    @DisplayName("Deve listar todos os patos criados")
    void deveListarTodosOsPatosCriados() {
        // Arrange - Criar múltiplos patos
        DuckDTO pato1 = duckService.createDuck(duckDTO);
        
        DuckDTO duckDTO2 = DuckDTO.builder()
                .name("Pato Teste 2")
                .motherId(null)
                .price(new BigDecimal("150.00"))
                .status("AVAILABLE")
                .build();
        DuckDTO pato2 = duckService.createDuck(duckDTO2);

        // Act - Listar todos os patos
        List<DuckDTO> patos = duckService.getAllDucks();

        // Assert - Verificar se todos foram listados
        assertNotNull(patos);
        assertEquals(2, patos.size());
        assertTrue(patos.stream().anyMatch(p -> "Pato Teste Integração".equals(p.getName())));
        assertTrue(patos.stream().anyMatch(p -> "Pato Teste 2".equals(p.getName())));
    }

    @Test
    @DisplayName("Deve atualizar pato existente")
    void deveAtualizarPatoExistente() {
        // Arrange - Criar pato
        DuckDTO patoCriado = duckService.createDuck(duckDTO);

        // Act - Atualizar pato
        DuckDTO patoAtualizado = DuckDTO.builder()
                .name("Pato Atualizado")
                .motherId(null)
                .price(new BigDecimal("200.00"))
                .status("AVAILABLE")
                .build();

        DuckDTO resultado = duckService.updateDuck(patoCriado.getId(), patoAtualizado);

        // Assert - Verificar se foi atualizado
        assertNotNull(resultado);
        assertEquals("Pato Atualizado", resultado.getName());
        assertEquals(200.00, resultado.getPrice().doubleValue());
        assertEquals(patoCriado.getId(), resultado.getId());
    }

    @Test
    @DisplayName("Deve deletar pato existente")
    void deveDeletarPatoExistente() {
        // Arrange - Criar pato
        DuckDTO patoCriado = duckService.createDuck(duckDTO);

        // Act - Deletar pato
        duckService.deleteDuck(patoCriado.getId());

        // Assert - Verificar se foi deletado
        List<DuckDTO> patos = duckService.getAllDucks();
        assertEquals(0, patos.size());
    }

    @Test
    @DisplayName("Deve filtrar patos por status")
    void deveFiltrarPatosPorStatus() {
        // Arrange - Criar patos com diferentes status
        DuckDTO pato1 = duckService.createDuck(duckDTO);
        
        DuckDTO duckDTO2 = DuckDTO.builder()
                .name("Pato Reservado")
                .motherId(null)
                .price(new BigDecimal("150.00"))
                .status("RESERVED")
                .build();
        DuckDTO pato2 = duckService.createDuck(duckDTO2);

        // Act - Filtrar por status AVAILABLE
        List<DuckDTO> patosDisponiveis = duckService.getDucksByStatus(DuckStatus.AVAILABLE);

        // Assert - Verificar se apenas patos disponíveis foram retornados
        assertNotNull(patosDisponiveis);
        assertEquals(1, patosDisponiveis.size());
        assertEquals("AVAILABLE", patosDisponiveis.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve criar pato com linhagem")
    void deveCriarPatoComLinhagem() {
        // Arrange - Criar pato mãe
        DuckDTO patoMae = duckService.createDuck(duckDTO);

        // Act - Criar pato filho
        DuckDTO duckDTOFilho = DuckDTO.builder()
                .name("Pato Filho")
                .motherId(patoMae.getId())
                .price(new BigDecimal("80.00"))
                .status("AVAILABLE")
                .build();
        DuckDTO patoFilho = duckService.createDuck(duckDTOFilho);

        // Assert - Verificar se foi criado com linhagem
        assertNotNull(patoFilho);
        assertEquals(patoMae.getId(), patoFilho.getMotherId());
        assertEquals("Pato Filho", patoFilho.getName());
    }

    @Test
    @DisplayName("Deve validar regras de negócio na criação")
    void deveValidarRegrasDeNegocioNaCriacao() {
        // Arrange - DTO com preço inválido
        DuckDTO duckDTOInvalido = DuckDTO.builder()
                .name("Pato Inválido")
                .motherId(null)
                .price(new BigDecimal("-50.00"))
                .status("AVAILABLE")
                .build();

        // Act & Assert - Deve lançar exceção
        assertThrows(IllegalArgumentException.class, 
            () -> duckService.createDuck(duckDTOInvalido));

        // Arrange - DTO com nome vazio
        DuckDTO duckDTONomeVazio = DuckDTO.builder()
                .name("")
                .motherId(null)
                .price(new BigDecimal("100.00"))
                .status("AVAILABLE")
                .build();

        // Act & Assert - Deve lançar exceção
        assertThrows(IllegalArgumentException.class, 
            () -> duckService.createDuck(duckDTONomeVazio));
    }
}
