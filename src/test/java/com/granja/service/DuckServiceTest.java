package com.granja.service;

import com.granja.dto.DuckDTO;
import com.granja.entity.Duck;
import com.granja.entity.Duck.DuckStatus;
import com.granja.repository.DuckRepository;
import com.granja.service.impl.DuckServiceImpl;
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
 * Testes unitários para o DuckService.
 * 
 * <p>Testa todas as operações CRUD e regras de negócio
 * relacionadas ao gerenciamento de patos.</p>
 */
@ExtendWith(MockitoExtension.class)
class DuckServiceTest {

    @Mock
    private DuckRepository duckRepository;

    @InjectMocks
    private DuckServiceImpl duckService;

    private Duck duck;
    private DuckDTO duckDTO;

    @BeforeEach
    void setUp() {
        duck = new Duck();
        duck.setId(1L);
        duck.setName("Donald Duck");
        duck.setPrice(new BigDecimal("150.00"));
        duck.setMotherId(null);
        duck.setStatus(DuckStatus.AVAILABLE);
        duck.setRegistrationDate(LocalDateTime.now());

        duckDTO = DuckDTO.builder()
                .name("Donald Duck")
                .motherId(null)
                .price(new BigDecimal("150.00"))
                .status("AVAILABLE")
                .build();
    }

    @Test
    void deveCriarPatoComSucesso() {
        // Given
        when(duckRepository.save(any(Duck.class))).thenReturn(duck);

        // When
        DuckDTO resultado = duckService.createDuck(duckDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Donald Duck", resultado.getName());
        assertEquals(new BigDecimal("150.00"), resultado.getPrice());
        assertEquals("AVAILABLE", resultado.getStatus());
        verify(duckRepository).save(any(Duck.class));
    }

    @Test
    void deveFalharAoCriarPatoComNomeVazio() {
        // Given
        DuckDTO duckDTOInvalido = DuckDTO.builder()
                .name("")
                .price(new BigDecimal("150.00"))
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            duckService.createDuck(duckDTOInvalido);
        });
        verify(duckRepository, never()).save(any(Duck.class));
    }

    @Test
    void deveFalharAoCriarPatoComPrecoZero() {
        // Given
        DuckDTO duckDTOInvalido = DuckDTO.builder()
                .name("Donald Duck")
                .price(BigDecimal.ZERO)
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            duckService.createDuck(duckDTOInvalido);
        });
        verify(duckRepository, never()).save(any(Duck.class));
    }

    @Test
    void deveFalharAoCriarPatoComPrecoNegativo() {
        // Given
        DuckDTO duckDTOInvalido = DuckDTO.builder()
                .name("Donald Duck")
                .price(new BigDecimal("-10.00"))
                .build();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            duckService.createDuck(duckDTOInvalido);
        });
        verify(duckRepository, never()).save(any(Duck.class));
    }

    @Test
    void deveAtualizarPatoComSucesso() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        when(duckRepository.save(any(Duck.class))).thenReturn(duck);

        DuckDTO duckDTOAtualizado = DuckDTO.builder()
                .name("Donald Duck Atualizado")
                .price(new BigDecimal("180.00"))
                .build();

        // When
        DuckDTO resultado = duckService.updateDuck(1L, duckDTOAtualizado);

        // Then
        assertNotNull(resultado);
        assertEquals("Donald Duck Atualizado", resultado.getName());
        assertEquals(new BigDecimal("180.00"), resultado.getPrice());
        verify(duckRepository).save(any(Duck.class));
    }

    @Test
    void deveFalharAoAtualizarPatoInexistente() {
        // Given
        when(duckRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            duckService.updateDuck(999L, duckDTO);
        });
        verify(duckRepository, never()).save(any(Duck.class));
    }

    @Test
    void deveRecuperarPatoPorId() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));

        // When
        DuckDTO resultado = duckService.getDuckById(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Donald Duck", resultado.getName());
        assertEquals(1L, resultado.getId());
    }

    @Test
    void deveFalharAoRecuperarPatoInexistente() {
        // Given
        when(duckRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            duckService.getDuckById(999L);
        });
    }

    @Test
    void deveRecuperarTodosOsPatos() {
        // Given
        Duck duck2 = new Duck();
        duck2.setId(2L);
        duck2.setName("Daisy Duck");
        duck2.setPrice(new BigDecimal("180.00"));
        duck2.setStatus(DuckStatus.AVAILABLE);

        when(duckRepository.findAll()).thenReturn(Arrays.asList(duck, duck2));

        // When
        List<DuckDTO> resultado = duckService.getAllDucks();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Donald Duck", resultado.get(0).getName());
        assertEquals("Daisy Duck", resultado.get(1).getName());
    }

    @Test
    void deveRecuperarPatosPorStatus() {
        // Given
        when(duckRepository.findByStatus(DuckStatus.AVAILABLE)).thenReturn(Arrays.asList(duck));

        // When
        List<DuckDTO> resultado = duckService.getDucksByStatus(DuckStatus.AVAILABLE);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("AVAILABLE", resultado.get(0).getStatus());
    }

    @Test
    void deveRecuperarPatosDisponiveisParaVenda() {
        // Given
        when(duckRepository.findAvailableForSale(DuckStatus.AVAILABLE)).thenReturn(Arrays.asList(duck));

        // When
        List<DuckDTO> resultado = duckService.getAvailableDucksForSale();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("AVAILABLE", resultado.get(0).getStatus());
    }

    @Test
    void deveRemoverPatoComSucesso() {
        // Given
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));
        doNothing().when(duckRepository).deleteById(1L);

        // When
        duckService.deleteDuck(1L);

        // Then
        verify(duckRepository).deleteById(1L);
    }

    @Test
    void deveFalharAoRemoverPatoVendido() {
        // Given
        duck.setStatus(DuckStatus.SOLD);
        when(duckRepository.findById(1L)).thenReturn(Optional.of(duck));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            duckService.deleteDuck(1L);
        });
        verify(duckRepository, never()).deleteById(any());
    }

    @Test
    void deveFalharAoRemoverPatoInexistente() {
        // Given
        when(duckRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            duckService.deleteDuck(999L);
        });
        verify(duckRepository, never()).deleteById(any());
    }

    @Test
    void deveCriarPatoComMae() {
        // Given
        DuckDTO duckDTOComMae = DuckDTO.builder()
                .name("Duckling 1")
                .motherId(1L)
                .price(new BigDecimal("120.00"))
                .build();

        Duck duckling = new Duck();
        duckling.setId(2L);
        duckling.setName("Duckling 1");
        duckling.setMotherId(1L);
        duckling.setPrice(new BigDecimal("120.00"));
        duckling.setStatus(DuckStatus.AVAILABLE);

        when(duckRepository.save(any(Duck.class))).thenReturn(duckling);

        // When
        DuckDTO resultado = duckService.createDuck(duckDTOComMae);

        // Then
        assertNotNull(resultado);
        assertEquals("Duckling 1", resultado.getName());
        assertEquals(1L, resultado.getMotherId());
        assertEquals(new BigDecimal("120.00"), resultado.getPrice());
    }
}
