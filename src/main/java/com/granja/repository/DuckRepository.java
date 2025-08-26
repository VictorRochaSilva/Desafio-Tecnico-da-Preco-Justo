package com.granja.repository;

import com.granja.entity.Duck;
import com.granja.entity.Duck.DuckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para operações da entidade Duck.
 * 
 * <p>Este repositório fornece métodos de acesso a dados para entidades Duck,
 * incluindo consultas personalizadas para operações específicas do negócio.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Operações CRUD padrão herdadas de JpaRepository</li>
 *   <li>Consultas personalizadas para requisitos de lógica de negócio</li>
 *   <li>Capacidades de filtragem baseadas em status</li>
 *   <li>Consultas de rastreamento de linhagem</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface DuckRepository extends JpaRepository<Duck, Long> {
    
    /**
     * Encontra patos por seu status atual.
     * 
     * @param status o status para filtrar
     * @return lista de patos com o status especificado
     */
    List<Duck> findByStatus(DuckStatus status);
    
    /**
     * Encontra patos pelo ID da mãe para rastreamento de linhagem.
     * 
     * @param motherId o ID da mãe do pato
     * @return lista de filhotes do pato
     */
    List<Duck> findByMotherId(Long motherId);
    
    /**
     * Encontra patos que estão disponíveis para venda (não foram vendidos).
     * 
     * @param status o status para filtrar (tipicamente AVAILABLE)
     * @return lista de patos disponíveis para compra
     */
    @Query("SELECT d FROM Duck d WHERE d.status = :status AND d.id NOT IN " +
           "(SELECT s.duck.id FROM Sale s)")
    List<Duck> findAvailableForSale(@Param("status") DuckStatus status);
    
    /**
     * Encontra patos vendidos para um cliente específico.
     * 
     * @param customerId o ID do cliente
     * @return lista de patos vendidos para o cliente
     */
    @Query("SELECT d FROM Duck d WHERE d.id IN " +
           "(SELECT s.duck.id FROM Sale s WHERE s.customer.id = :customerId)")
    List<Duck> findByCustomerId(@Param("customerId") Long customerId);
    
    /**
     * Encontra um pato pelo nome para fins de identificação.
     * 
     * @param name o nome do pato
     * @return opcional contendo o pato se encontrado
     */
    Optional<Duck> findByName(String name);
}
