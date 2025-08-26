package com.granja.controller;

import com.granja.dto.DuckDTO;
import com.granja.entity.Duck.DuckStatus;
import com.granja.service.DuckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para operações de gerenciamento de Patos.
 * 
 * <p>Este controlador fornece endpoints HTTP para operações CRUD de patos,
 * seguindo princípios REST e implementando controles de segurança adequados.</p>
 * 
 * <p>Principais características:
 * <ul>
 *   <li>Design de endpoint RESTful</li>
 *   <li>Controle de acesso baseado em funções</li>
 *   <li>Validação abrangente de entrada</li>
 *   <li>Códigos de resposta HTTP padronizados</li>
 *   <li>Documentação da API Swagger</li>
 * </ul></p>
 * 
 * @author Sistema Granja
 * @version 1.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@RequestMapping("/api/ducks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Gerenciamento de Patos", description = "Operações para gerenciar entidades de patos")
public class DuckController {
    
    private final DuckService duckService;
    
    /**
     * Cria um novo pato no sistema.
     * 
     * @param duckDTO os dados do pato para criar
     * @return o pato criado com ID gerado
     */
    @Operation(summary = "Criar um novo pato", description = "Cria um novo registro de pato no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<DuckDTO> createDuck(
            @Parameter(description = "Dados do pato para criar", required = true)
            @Valid @RequestBody DuckDTO duckDTO) {
        
        log.info("Recebida solicitação para criar pato: {}", duckDTO.getName());
        DuckDTO createdDuck = duckService.createDuck(duckDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDuck);
    }
    
    /**
     * Atualiza um registro de pato existente.
     * 
     * @param id o identificador único do pato para atualizar
     * @param duckDTO os dados atualizados do pato
     * @return o pato atualizado
     */
    @Operation(summary = "Atualizar um pato existente", description = "Atualiza um registro de pato existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pato atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Pato não encontrado"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER')")
    public ResponseEntity<DuckDTO> updateDuck(
            @Parameter(description = "ID do pato para atualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados do pato", required = true)
            @Valid @RequestBody DuckDTO duckDTO) {
        
        log.info("Recebida solicitação para atualizar pato com ID: {}", id);
        DuckDTO updatedDuck = duckService.updateDuck(id, duckDTO);
        
        return ResponseEntity.ok(updatedDuck);
    }
    
    /**
     * Recupera um pato por seu identificador único.
     * 
     * @param id o identificador único do pato
     * @return o pato se encontrado
     */
    @Operation(summary = "Obter pato por ID", description = "Recupera um pato específico por seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pato recuperado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pato não encontrado"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<DuckDTO> getDuckById(
            @Parameter(description = "ID do pato para recuperar", required = true)
            @PathVariable Long id) {
        
        log.debug("Recuperando pato com ID: {}", id);
        DuckDTO duck = duckService.getDuckById(id);
        
        return ResponseEntity.ok(duck);
    }
    
    /**
     * Recupera todos os patos no sistema.
     * 
     * @return lista de todos os patos
     */
    @Operation(summary = "Obter todos os patos", description = "Recupera todos os patos no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patos recuperados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<List<DuckDTO>> getAllDucks() {
        log.debug("Recuperando todos os patos");
        List<DuckDTO> ducks = duckService.getAllDucks();
        
        return ResponseEntity.ok(ducks);
    }
    
    /**
     * Recupera patos filtrados por seu status atual.
     * 
     * @param status o status para filtrar
     * @return lista de patos correspondentes ao status especificado
     */
    @Operation(summary = "Obter patos por status", description = "Recupera patos filtrados por seu status atual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patos recuperados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Parâmetro de status inválido"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<List<DuckDTO>> getDucksByStatus(
            @Parameter(description = "Status para filtrar", required = true)
            @PathVariable DuckStatus status) {
        
        log.debug("Recuperando patos com status: {}", status);
        List<DuckDTO> ducks = duckService.getDucksByStatus(status);
        
        return ResponseEntity.ok(ducks);
    }
    
    /**
     * Recupera patos que estão disponíveis para venda.
     * 
     * @return lista de patos disponíveis para compra
     */
    @Operation(summary = "Obter patos disponíveis", description = "Recupera patos que estão disponíveis para venda")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patos disponíveis recuperados com sucesso"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado")
    })
    @GetMapping("/available")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SELLER') or hasRole('MANAGER')")
    public ResponseEntity<List<DuckDTO>> getAvailableDucksForSale() {
        log.debug("Recuperando patos disponíveis para venda");
        List<DuckDTO> ducks = duckService.getAvailableDucksForSale();
        
        return ResponseEntity.ok(ducks);
    }
    
    /**
     * Remove um registro de pato do sistema.
     * 
     * @param id o identificador único do pato para remover
     * @return resposta sem conteúdo em caso de remoção bem-sucedida
     */
    @Operation(summary = "Remover um pato", description = "Remove um registro de pato do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pato removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pato não encontrado"),
        @ApiResponse(responseCode = "400", description = "Pato não pode ser removido"),
        @ApiResponse(responseCode = "401", description = "Acesso não autorizado"),
        @ApiResponse(responseCode = "403", description = "Permissões insuficientes")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDuck(
            @Parameter(description = "ID do pato para remover", required = true)
            @PathVariable Long id) {
        
        log.info("Recebida solicitação para remover pato com ID: {}", id);
        duckService.deleteDuck(id);
        
        return ResponseEntity.noContent().build();
    }
}
