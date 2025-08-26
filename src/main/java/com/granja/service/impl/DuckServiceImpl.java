package com.granja.service.impl;

import com.granja.dto.DuckDTO;
import com.granja.entity.Duck;
import com.granja.entity.Duck.DuckStatus;
import com.granja.repository.DuckRepository;
import com.granja.service.DuckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementação da interface DuckService.
 * 
 * <p>
 * Este serviço implementa operações de gerenciamento de patos seguindo
 * o Princípio da Responsabilidade Única e o Princípio Aberto/Fechado.
 * </p>
 * 
 * <p>
 * Principais responsabilidades:
 * <ul>
 * <li>Validação de lógica de negócio para operações de patos</li>
 * <li>Conversão e mapeamento de Entidade-DTO</li>
 * <li>Gerenciamento de transações</li>
 * <li>Tratamento de erros e logging</li>
 * </ul>
 * </p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DuckServiceImpl implements DuckService {

    private final DuckRepository duckRepository;

    @Override
    public DuckDTO createDuck(DuckDTO duckDTO) {
        log.info("Criando novo pato com nome: {}", duckDTO.getName());

        validateDuckData(duckDTO);

        Duck duck = buildDuckFromDTO(duckDTO);
        duck.setStatus(DuckStatus.AVAILABLE);

        Duck savedDuck = duckRepository.save(duck);
        log.info("Pato criado com sucesso com ID: {}", savedDuck.getId());

        return convertToDTO(savedDuck);
    }

    @Override
    public DuckDTO updateDuck(Long id, DuckDTO duckDTO) {
        log.info("Atualizando pato com ID: {}", id);

        Duck existingDuck = findDuckById(id);
        validateDuckData(duckDTO);

        updateDuckFields(existingDuck, duckDTO);
        Duck updatedDuck = duckRepository.save(existingDuck);

        log.info("Pato atualizado com sucesso com ID: {}", id);
        return convertToDTO(updatedDuck);
    }

    @Override
    public DuckDTO getDuckById(Long id) {
        log.debug("Recuperando pato com ID: {}", id);

        Duck duck = findDuckById(id);
        return convertToDTO(duck);
    }

    @Override
    public List<DuckDTO> getAllDucks() {
        log.debug("Recuperando todos os patos");

        List<Duck> ducks = duckRepository.findAll();
        return ducks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<DuckDTO> getDucksByStatus(DuckStatus status) {
        log.debug("Recuperando patos com status: {}", status);

        List<Duck> ducks = duckRepository.findByStatus(status);
        return ducks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<DuckDTO> getAvailableDucksForSale() {
        log.debug("Recuperando patos disponíveis para venda");

        List<Duck> ducks = duckRepository.findAvailableForSale(DuckStatus.AVAILABLE);
        return ducks.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public void deleteDuck(Long id) {
        log.info("Removendo pato com ID: {}", id);

        Duck duck = findDuckById(id);
        validateDeletion(duck);

        duckRepository.deleteById(id);
        log.info("Pato removido com sucesso com ID: {}", id);
    }

    /**
     * Valida os dados do pato antes da persistência.
     * 
     * @param duckDTO os dados do pato para validar
     * @throws IllegalArgumentException se a validação falhar
     */
    private void validateDuckData(DuckDTO duckDTO) {
        if (duckDTO.getName() == null || duckDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do pato não pode ser nulo ou vazio");
        }

        if (duckDTO.getPrice() == null || duckDTO.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço do pato deve ser maior que zero");
        }
    }

    /**
     * Valida se um pato pode ser removido.
     * 
     * @param duck o pato para validar para remoção
     * @throws RuntimeException se o pato não puder ser removido
     */
    private void validateDeletion(Duck duck) {
        if (duck.getStatus() == DuckStatus.SOLD) {
            throw new RuntimeException("Não é possível remover um pato que foi vendido");
        }
    }

    /**
     * Encontra um pato por ID ou lança uma exceção se não encontrado.
     * 
     * @param id o ID do pato para encontrar
     * @return o pato encontrado
     * @throws RuntimeException se o pato não for encontrado
     */
    private Duck findDuckById(Long id) {
        Optional<Duck> duck = duckRepository.findById(id);
        if (duck.isEmpty()) {
            log.error("Pato não encontrado com ID: {}", id);
            throw new RuntimeException("Pato não encontrado com ID: " + id);
        }
        return duck.get();
    }

    /**
     * Constrói uma entidade Duck a partir dos dados do DTO.
     * 
     * @param duckDTO o DTO de origem
     * @return a entidade Duck construída
     */
    private Duck buildDuckFromDTO(DuckDTO duckDTO) {
        Duck duck = new Duck();
        duck.setName(duckDTO.getName());
        duck.setPrice(duckDTO.getPrice());
        duck.setMotherId(duckDTO.getMotherId());

        return duck;
    }

    /**
     * Atualiza campos existentes do pato com novos dados.
     * 
     * @param existingDuck o pato para atualizar
     * @param duckDTO      os novos dados
     */
    private void updateDuckFields(Duck existingDuck, DuckDTO duckDTO) {
        existingDuck.setName(duckDTO.getName());
        existingDuck.setPrice(duckDTO.getPrice());
        existingDuck.setMotherId(duckDTO.getMotherId());
    }

    /**
     * Converte uma entidade Duck para DTO.
     * 
     * @param duck a entidade de origem
     * @return o DTO convertido
     */
    private DuckDTO convertToDTO(Duck duck) {
        return DuckDTO.builder()
                .id(duck.getId())
                .name(duck.getName())
                .motherId(duck.getMotherId())
                .price(duck.getPrice())
                .status(duck.getStatus().name())
                .build();
    }
}
