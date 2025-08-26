package com.granja.service;

import com.granja.dto.DuckDTO;
import com.granja.entity.Duck.DuckStatus;

import java.util.List;

/**
 * Interface de serviço para operações da entidade Duck.
 * 
 * <p>Esta interface define o contrato para operações de gerenciamento de patos
 * seguindo o Princípio da Responsabilidade Única e o Princípio da Segregação de Interface.</p>
 * 
 * <p>Principais operações:
 * <ul>
 *   <li>Operações CRUD para entidades de patos</li>
 *   <li>Filtragem e consultas baseadas em status</li>
 *   <li>Gerenciamento de linhagem</li>
 *   <li>Validação de regras de negócio</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
public interface DuckService {
    
    /**
     * Cria um novo registro de pato no sistema.
     * 
     * @param duckDTO os dados do pato para criar
     * @return o DTO do pato criado com ID gerado
     * @throws IllegalArgumentException se os dados obrigatórios forem inválidos
     * @throws RuntimeException se as regras de negócio forem violadas
     */
    DuckDTO createDuck(DuckDTO duckDTO);
    
    /**
     * Atualiza um registro de pato existente.
     * 
     * @param id o identificador único do pato para atualizar
     * @param duckDTO os dados atualizados do pato
     * @return o DTO do pato atualizado
     * @throws IllegalArgumentException se o ID for inválido
     * @throws RuntimeException se o pato não for encontrado ou as regras de negócio forem violadas
     */
    DuckDTO updateDuck(Long id, DuckDTO duckDTO);
    
    /**
     * Recupera um pato por seu identificador único.
     * 
     * @param id o identificador único do pato
     * @return o DTO do pato se encontrado
     * @throws RuntimeException se o pato não for encontrado
     */
    DuckDTO getDuckById(Long id);
    
    /**
     * Recupera todos os patos no sistema.
     * 
     * @return uma lista de todos os DTOs de patos
     */
    List<DuckDTO> getAllDucks();
    
    /**
     * Recupera patos filtrados por seu status atual.
     * 
     * @param status o status para filtrar
     * @return uma lista de patos correspondentes ao status especificado
     */
    List<DuckDTO> getDucksByStatus(DuckStatus status);
    
    /**
     * Recupera patos que estão disponíveis para venda.
     * 
     * @return uma lista de patos disponíveis para compra
     */
    List<DuckDTO> getAvailableDucksForSale();
    
    /**
     * Remove um registro de pato do sistema.
     * 
     * @param id o identificador único do pato para remover
     * @throws RuntimeException se o pato não puder ser removido (ex: já vendido)
     */
    void deleteDuck(Long id);
}
