package com.granja.service.impl;

import com.granja.entity.Sale;
import com.granja.entity.Seller;
import com.granja.repository.SaleRepository;
import com.granja.repository.SellerRepository;
import com.granja.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementação do ReportService para geração de relatórios de negócio.
 * 
 * <p>Este serviço fornece implementações concretas para gerar
 * vários relatórios de negócio em formato Excel.</p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    
    private final SaleRepository saleRepository;
    private final SellerRepository sellerRepository;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Gera um relatório de vendas em formato Excel para o período especificado.
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return array de bytes contendo o arquivo Excel
     */
    @Override
    public byte[] generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Gerando relatório de vendas de {} até {}", startDate, endDate);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Relatório de Vendas");
            
            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle statusStyle = createStatusStyle(workbook);
            
            // Cabeçalho do relatório
            createSalesReportHeader(sheet, startDate, endDate, headerStyle);
            
            // Cabeçalhos das colunas
            String[] headers = {"Nome", "Status", "Cliente", "Tipo Cliente", "Valor", "Data/Hora", "Vendedor"};
            createColumnHeaders(sheet, headers, 4, headerStyle);
            
            // Buscar dados das vendas
            List<Sale> sales = saleRepository.findBySaleDateBetween(startDate, endDate);
            
            // Preencher dados
            int rowNum = 5;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalDiscount = BigDecimal.ZERO;
            
            for (Sale sale : sales) {
                Row row = sheet.createRow(rowNum++);
                
                // Nome do Pato
                row.createCell(0).setCellValue(sale.getDuck().getName());
                row.getCell(0).setCellStyle(dataStyle);
                
                // Status do Pato
                row.createCell(1).setCellValue(sale.getDuck().getStatus().toString());
                row.getCell(1).setCellStyle(statusStyle);
                
                // Nome do Cliente
                row.createCell(2).setCellValue(sale.getCustomer().getName());
                row.getCell(2).setCellStyle(dataStyle);
                
                // Tipo do Cliente (com/sem desconto)
                String customerType = sale.getCustomer().getDiscountEligible() ? "Com Desconto" : "Sem Desconto";
                row.createCell(3).setCellValue(customerType);
                row.getCell(3).setCellStyle(dataStyle);
                
                // Valor Final da Venda
                row.createCell(4).setCellValue(sale.getFinalPrice().doubleValue());
                row.getCell(4).setCellStyle(currencyStyle);
                
                // Data e Hora da Venda
                row.createCell(5).setCellValue(sale.getSaleDate().format(DATE_FORMATTER));
                row.getCell(5).setCellStyle(dateStyle);
                
                // Nome do Vendedor
                row.createCell(6).setCellValue(sale.getSeller().getName());
                row.getCell(6).setCellStyle(dataStyle);
                
                totalRevenue = totalRevenue.add(sale.getFinalPrice());
                totalDiscount = totalDiscount.add(sale.getDiscountAmount());
            }
            
            // Resumo
            createSalesSummaryRow(sheet, rowNum, totalRevenue, totalDiscount, sales.size(), headerStyle, currencyStyle);
            
            // Auto-dimensionar colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            return writeWorkbookToBytes(workbook);
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de vendas", e);
            throw new RuntimeException("Falha ao gerar relatório de vendas", e);
        }
    }
    
    /**
     * Gera um relatório de ranking de vendedores em formato Excel para o período especificado.
     * 
     * @param startDate a data de início para o período do relatório
     * @param endDate a data de fim para o período do relatório
     * @return array de bytes contendo o arquivo Excel
     */
    @Override
    public byte[] generateSellerRankingReport(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Gerando relatório de ranking de vendedores de {} até {}", startDate, endDate);
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Ranking de Vendedores");
            
            // Estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle numberStyle = createNumberStyle(workbook);
            
            // Cabeçalho do relatório
            createSellerReportHeader(sheet, startDate, endDate, headerStyle);
            
            // Cabeçalhos das colunas
            String[] headers = {"Posição", "Vendedor", "Total de Vendas", "Receita Total", "Ticket Médio", "CPF", "Matrícula"};
            createColumnHeaders(sheet, headers, 3, headerStyle);
            
            // Buscar dados dos vendedores
            List<Seller> sellers = sellerRepository.findAll();
            
            // Calcular métricas para cada vendedor
            List<SellerMetrics> sellerMetricsList = sellers.stream()
                    .map(seller -> calculateSellerMetrics(seller, startDate, endDate))
                    .filter(metrics -> metrics.totalSales > 0) // Apenas vendedores com vendas
                    .sorted((a, b) -> b.totalRevenue.compareTo(a.totalRevenue)) // Ordenar por receita
                    .collect(Collectors.toList());
            
            // Preencher dados
            int rowNum = 4;
            for (int i = 0; i < sellerMetricsList.size(); i++) {
                SellerMetrics metrics = sellerMetricsList.get(i);
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(i + 1);
                row.getCell(0).setCellStyle(numberStyle);
                
                row.createCell(1).setCellValue(metrics.sellerName);
                row.getCell(1).setCellStyle(dataStyle);
                
                row.createCell(2).setCellValue(metrics.totalSales);
                row.getCell(2).setCellStyle(numberStyle);
                
                row.createCell(3).setCellValue(metrics.totalRevenue.doubleValue());
                row.getCell(3).setCellStyle(currencyStyle);
                
                row.createCell(4).setCellValue(metrics.averageTicket.doubleValue());
                row.getCell(4).setCellStyle(currencyStyle);
                
                row.createCell(5).setCellValue(metrics.cpf);
                row.getCell(5).setCellStyle(dataStyle);
                
                row.createCell(6).setCellValue(metrics.employeeId);
                row.getCell(6).setCellStyle(dataStyle);
            }
            
            // Resumo
            createSellerSummaryRow(sheet, rowNum, sellerMetricsList, headerStyle, currencyStyle, numberStyle);
            
            // Auto-dimensionar colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            return writeWorkbookToBytes(workbook);
            
        } catch (Exception e) {
            log.error("Erro ao gerar relatório de ranking de vendedores", e);
            throw new RuntimeException("Falha ao gerar relatório de ranking de vendedores", e);
        }
    }
    
    // Métodos auxiliares para criação de estilos e formatação
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
    
    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setDataFormat(workbook.createDataFormat().getFormat("R$ #,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }
    
    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setDataFormat(workbook.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    private CellStyle createNumberStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createStatusStyle(Workbook workbook) {
        CellStyle style = createDataStyle(workbook);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    private void createSalesReportHeader(Sheet sheet, LocalDateTime startDate, LocalDateTime endDate, CellStyle style) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Relatório de Vendas");
        titleCell.setCellStyle(style);
        
        Row periodRow = sheet.createRow(1);
        Cell periodCell = periodRow.createCell(0);
        periodCell.setCellValue("Período: " + startDate.format(DATE_ONLY_FORMATTER) + " a " + endDate.format(DATE_ONLY_FORMATTER));
        periodCell.setCellStyle(style);
        
        Row dateRow = sheet.createRow(2);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Gerado em: " + LocalDateTime.now().format(DATE_FORMATTER));
        dateCell.setCellStyle(style);
        
        // Mesclar células do cabeçalho
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
    }
    
    private void createSellerReportHeader(Sheet sheet, LocalDateTime startDate, LocalDateTime endDate, CellStyle style) {
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Ranking de Vendedores");
        titleCell.setCellStyle(style);
        
        Row periodRow = sheet.createRow(1);
        Cell periodCell = periodRow.createCell(0);
        periodCell.setCellValue("Período: " + startDate.format(DATE_ONLY_FORMATTER) + " a " + endDate.format(DATE_ONLY_FORMATTER));
        periodCell.setCellStyle(style);
        
        Row dateRow = sheet.createRow(2);
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Gerado em: " + LocalDateTime.now().format(DATE_FORMATTER));
        dateCell.setCellStyle(style);
        
        // Mesclar células do cabeçalho (ajustado para 7 colunas)
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
    }
    
    private void createColumnHeaders(Sheet sheet, String[] headers, int rowNum, CellStyle style) {
        Row headerRow = sheet.createRow(rowNum);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }
    
    private void createSummaryRow(Sheet sheet, int rowNum, BigDecimal totalRevenue, BigDecimal totalDiscount, int totalSales, 
                                 CellStyle headerStyle, CellStyle currencyStyle) {
        Row summaryRow = sheet.createRow(rowNum + 1);
        
        summaryRow.createCell(0).setCellValue("RESUMO:");
        summaryRow.getCell(0).setCellStyle(headerStyle);
        
        summaryRow.createCell(4).setCellValue("Total de Vendas:");
        summaryRow.getCell(4).setCellStyle(headerStyle);
        
        summaryRow.createCell(5).setCellValue(totalSales);
        summaryRow.getCell(5).setCellStyle(headerStyle);
        
        summaryRow.createCell(6).setCellValue("Receita Total:");
        summaryRow.getCell(6).setCellStyle(headerStyle);
        
        summaryRow.createCell(7).setCellValue(totalRevenue.doubleValue());
        summaryRow.getCell(7).setCellStyle(currencyStyle);
        
        // Mesclar células
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 6, 7));
    }

    private void createSalesSummaryRow(Sheet sheet, int rowNum, BigDecimal totalRevenue, BigDecimal totalDiscount, int totalSales, 
                                 CellStyle headerStyle, CellStyle currencyStyle) {
        Row summaryRow = sheet.createRow(rowNum + 1);
        
        summaryRow.createCell(0).setCellValue("RESUMO:");
        summaryRow.getCell(0).setCellStyle(headerStyle);
        
        summaryRow.createCell(4).setCellValue("Total de Vendas:");
        summaryRow.getCell(4).setCellStyle(headerStyle);
        
        summaryRow.createCell(5).setCellValue(totalSales);
        summaryRow.getCell(5).setCellStyle(headerStyle);
        
        summaryRow.createCell(6).setCellValue("Receita Total:");
        summaryRow.getCell(6).setCellStyle(headerStyle);
        
        // Mesclar células (ajustado para 7 colunas)
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 4, 5));
        // Não mesclar célula única (coluna 6)
    }
    
    private void createSellerSummaryRow(Sheet sheet, int rowNum, List<SellerMetrics> metricsList, 
                                       CellStyle headerStyle, CellStyle currencyStyle, CellStyle numberStyle) {
        Row summaryRow = sheet.createRow(rowNum + 1);
        
        BigDecimal totalRevenue = metricsList.stream()
                .map(m -> m.totalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalSales = metricsList.stream()
                .mapToInt(m -> m.totalSales)
                .sum();
        
        summaryRow.createCell(0).setCellValue("RESUMO:");
        summaryRow.getCell(0).setCellStyle(headerStyle);
        
        summaryRow.createCell(2).setCellValue("Total de Vendedores:");
        summaryRow.getCell(2).setCellStyle(headerStyle);
        
        summaryRow.createCell(3).setCellValue(metricsList.size());
        summaryRow.getCell(3).setCellStyle(numberStyle);
        
        summaryRow.createCell(4).setCellValue("Total de Vendas:");
        summaryRow.getCell(4).setCellStyle(headerStyle);
        
        summaryRow.createCell(5).setCellValue(totalSales);
        summaryRow.getCell(5).setCellStyle(numberStyle);
        
        summaryRow.createCell(6).setCellValue("Receita Total:");
        summaryRow.getCell(6).setCellStyle(headerStyle);
        
        summaryRow.createCell(7).setCellValue(totalRevenue.doubleValue());
        summaryRow.getCell(7).setCellStyle(currencyStyle);
        
        // Mesclar células
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 0, 1));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 2, 3));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 4, 5));
        sheet.addMergedRegion(new CellRangeAddress(rowNum + 1, rowNum + 1, 6, 7));
    }
    
    private SellerMetrics calculateSellerMetrics(Seller seller, LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> sellerSales = saleRepository.findBySellerIdAndSaleDateBetween(seller.getId(), startDate, endDate);
        
        BigDecimal totalRevenue = sellerSales.stream()
                .map(Sale::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal averageTicket = sellerSales.isEmpty() ? BigDecimal.ZERO : 
                totalRevenue.divide(BigDecimal.valueOf(sellerSales.size()), 2, BigDecimal.ROUND_HALF_UP);
        
        return new SellerMetrics(
                seller.getName(),
                seller.getCpf(),
                seller.getEmployeeId(),
                sellerSales.size(),
                totalRevenue,
                averageTicket
        );
    }
    
    private byte[] writeWorkbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    // Classe auxiliar para métricas do vendedor
    private static class SellerMetrics {
        final String sellerName;
        final String cpf;
        final String employeeId;
        final int totalSales;
        final BigDecimal totalRevenue;
        final BigDecimal averageTicket;
        
        SellerMetrics(String sellerName, String cpf, String employeeId, int totalSales, 
                     BigDecimal totalRevenue, BigDecimal averageTicket) {
            this.sellerName = sellerName;
            this.cpf = cpf;
            this.employeeId = employeeId;
            this.totalSales = totalSales;
            this.totalRevenue = totalRevenue;
            this.averageTicket = averageTicket;
        }
    }
}
