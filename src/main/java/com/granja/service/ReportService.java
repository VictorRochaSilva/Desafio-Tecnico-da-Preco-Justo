package com.granja.service;

import java.time.LocalDateTime;

/**
 * Interface de serviço para operações de geração de relatórios.
 * 
 * <p>Este serviço define o contrato para gerar vários
 * relatórios de negócio em formato Excel.</p>
 * 
 * <p>Principais operações:
 * <ul>
 *   <li>Geração de relatórios de vendas com filtragem por período</li>
 *   <li>Relatórios de ranking de vendedores com métricas de performance</li>
 *   <li>Geração de arquivos Excel usando Apache POI</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
public interface ReportService {
    
    /**
     * Gera um relatório de vendas em formato Excel para o período atual.
     * 
     * @return array de bytes contendo o arquivo Excel
     */
    byte[] generateSalesReport();
    
    /**
     * Gera um relatório de vendas em formato Excel para o período especificado.
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return array de bytes contendo o arquivo Excel
     */
    byte[] generateSalesReport(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Gera um relatório de ranking de vendedores em formato Excel para o período atual.
     * 
     * @return array de bytes contendo o arquivo Excel
     */
    byte[] generateSellerRankingReport();
    
    /**
     * Gera um relatório de ranking de vendedores em formato Excel para o período especificado.
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return array de bytes contendo o arquivo Excel
     */
    byte[] generateSellerRankingReport(LocalDateTime startDate, LocalDateTime endDate);
}
