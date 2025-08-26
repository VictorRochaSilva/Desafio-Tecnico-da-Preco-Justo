package com.granja.repository;

import com.granja.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de repositório para operações da entidade Sale.
 * 
 * <p>Este repositório fornece métodos de acesso a dados para entidades Sale,
 * incluindo consultas específicas do negócio para gerenciamento de vendas.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Operações CRUD padrão herdadas de JpaRepository</li>
 *   <li>Consultas de vendas baseadas em período</li>
 *   <li>Filtragem por cliente e vendedor</li>
 *   <li>Análise e relatórios de vendas</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    /**
     * Encontra vendas dentro de um intervalo de datas específico.
     * 
     * @param startDate a data de início do período
     * @param endDate a data de fim do período
     * @return lista de vendas dentro do período especificado
     */
    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Encontra vendas por ID do cliente.
     * 
     * @param customerId o ID do cliente para filtrar
     * @return lista de vendas para o cliente especificado
     */
    List<Sale> findByCustomerId(Long customerId);
    
    /**
     * Encontra vendas por ID do vendedor.
     * 
     * @param sellerId o ID do vendedor para filtrar
     * @return lista de vendas para o vendedor especificado
     */
    List<Sale> findBySellerId(Long sellerId);
    
    /**
     * Encontra vendas por ID do pato.
     * 
     * @param duckId o ID do pato para filtrar
     * @return lista de vendas para o pato especificado
     */
    List<Sale> findByDuckId(Long duckId);
    
    /**
     * Calcula o valor total de vendas para um vendedor dentro de um período.
     * 
     * @param sellerId o ID do vendedor
     * @param startDate a data de início do período
     * @param endDate a data de fim do período
     * @return valor total de vendas para o vendedor no período
     */
    @Query("SELECT SUM(s.finalPrice) FROM Sale s WHERE s.seller.id = :sellerId AND s.saleDate BETWEEN :startDate AND :endDate")
    Double calculateTotalSalesBySellerInPeriod(
            @Param("sellerId") Long sellerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    /**
     * Encontra vendas por ID do vendedor dentro de um intervalo de datas específico.
     * 
     * @param sellerId o ID do vendedor para filtrar
     * @param startDate a data de início do período
     * @param endDate a data de fim do período
     * @return lista de vendas para o vendedor especificado no período
     */
    List<Sale> findBySellerIdAndSaleDateBetween(
            @Param("sellerId") Long sellerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
