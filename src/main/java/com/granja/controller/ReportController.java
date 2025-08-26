package com.granja.controller;

import com.granja.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador REST para operações de geração de relatórios.
 * 
 * <p>Este controlador fornece endpoints HTTP para gerar
 * vários relatórios de negócio em formato Excel.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Geração de relatórios de vendas</li>
 *   <li>Relatórios de ranking de vendedores</li>
 *   <li>Download de arquivos Excel</li>
 *   <li>Filtragem baseada em período</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Geração de Relatórios", description = "Operações para gerar relatórios de negócio")
public class ReportController {
    
    private final ReportService reportService;
    
    /**
     * Gera e faz download de um relatório de vendas em formato Excel (período atual).
     * 
     * @return arquivo Excel como array de bytes
     */
    @Operation(summary = "Gerar relatório de vendas (período atual)", description = "Gera e faz download de um relatório de vendas em formato Excel para o período atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @GetMapping("/sales")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<byte[]> generateSalesReport() {
        
        log.info("Gerando relatório de vendas para período atual");
        
        byte[] reportBytes = reportService.generateSalesReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sales_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
    
    /**
     * Gera e faz download de um relatório de vendas em formato Excel (período específico).
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return arquivo Excel como array de bytes
     */
    @Operation(summary = "Gerar relatório de vendas (período específico)", description = "Gera e faz download de um relatório de vendas em formato Excel para período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de data inválidos"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @GetMapping("/sales/period")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<byte[]> generateSalesReportForPeriod(
            @Parameter(description = "Data de início para o período do relatório", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim para o período do relatório", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Gerando relatório de vendas de {} até {}", startDate, endDate);
        
        byte[] reportBytes = reportService.generateSalesReport(startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sales_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
    
    /**
     * Gera e faz download de um relatório de ranking de vendedores em formato Excel (período atual).
     * 
     * @return arquivo Excel como array de bytes
     */
    @Operation(summary = "Gerar relatório de ranking de vendedores (período atual)", description = "Gera e faz download de um relatório de ranking de vendedores em formato Excel para o período atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @GetMapping("/seller-ranking")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<byte[]> generateSellerRankingReport() {
        
        log.info("Gerando relatório de ranking de vendedores para período atual");
        
        byte[] reportBytes = reportService.generateSellerRankingReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "seller_ranking_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
    
    /**
     * Gera e faz download de um relatório de ranking de vendedores em formato Excel (período específico).
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return arquivo Excel como array de bytes
     */
    @Operation(summary = "Gerar relatório de ranking de vendedores (período específico)", description = "Gera e faz download de um relatório de ranking de vendedores em formato Excel para período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetros de data inválidos"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @GetMapping("/seller-ranking/period")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<byte[]> generateSellerRankingReportForPeriod(
            @Parameter(description = "Data de início para o período do relatório", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim para o período do relatório", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.info("Gerando relatório de ranking de vendedores de {} até {}", startDate, endDate);
        
        byte[] reportBytes = reportService.generateSellerRankingReport(startDate, endDate);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "seller_ranking_report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportBytes);
    }
}
