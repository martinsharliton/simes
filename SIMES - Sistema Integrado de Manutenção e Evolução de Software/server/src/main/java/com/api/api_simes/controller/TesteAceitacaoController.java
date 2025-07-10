package com.api.api_simes.controller;

import com.api.api_simes.domain.entity.TesteAceitacao;
import com.api.api_simes.domain.service.TesteAceitacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller REST para TesteAceitacao
 * Expõe endpoints para operações CRUD
 */
@RestController
@RequestMapping("/api/teste-aceitacao")
@CrossOrigin(origins = "*")
public class TesteAceitacaoController {
    
    private final TesteAceitacaoService testeAceitacaoService;
    
    @Autowired
    public TesteAceitacaoController(TesteAceitacaoService testeAceitacaoService) {
        this.testeAceitacaoService = testeAceitacaoService;
    }
    
    /**
     * POST - Criar novo teste de aceitação
     */
    @PostMapping
    public ResponseEntity<TesteAceitacao> criar(@Valid @RequestBody TesteAceitacao testeAceitacao) {
        TesteAceitacao salvo = testeAceitacaoService.salvar(testeAceitacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
    
    /**
     * GET - Buscar teste de aceitação por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TesteAceitacao> buscarPorId(@PathVariable Integer id) {
        Optional<TesteAceitacao> teste = testeAceitacaoService.buscarPorId(id);
        return teste.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET - Listar todos os testes de aceitação
     */
    @GetMapping
    public ResponseEntity<List<TesteAceitacao>> listarTodos() {
        List<TesteAceitacao> testes = testeAceitacaoService.listarTodos();
        return ResponseEntity.ok(testes);
    }
    
    /**
     * PUT - Atualizar teste de aceitação
     */
    @PutMapping("/{id}")
    public ResponseEntity<TesteAceitacao> atualizar(@PathVariable Integer id, 
                                                   @Valid @RequestBody TesteAceitacao testeAceitacao) {
        Optional<TesteAceitacao> atualizado = testeAceitacaoService.atualizar(id, testeAceitacao);
        return atualizado.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE - Excluir teste de aceitação
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        Optional<TesteAceitacao> teste = testeAceitacaoService.buscarPorId(id);
        if (teste.isPresent()) {
            testeAceitacaoService.excluir(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * GET - Buscar testes por ID do teste do sistema
     */
    @GetMapping("/teste-sistema/{idTesteSistema}")
    public ResponseEntity<List<TesteAceitacao>> buscarPorTesteSistema(@PathVariable Integer idTesteSistema) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorIdTesteSistema(idTesteSistema);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar testes por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TesteAceitacao>> buscarPorStatus(@PathVariable String status) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorStatus(status);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar testes por responsável
     */
    @GetMapping("/responsavel/{responsavel}")
    public ResponseEntity<List<TesteAceitacao>> buscarPorResponsavel(@PathVariable String responsavel) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorResponsavel(responsavel);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar testes por data de aceite
     */
    @GetMapping("/data/{dataAceite}")
    public ResponseEntity<List<TesteAceitacao>> buscarPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataAceite) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorData(dataAceite);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar testes por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<TesteAceitacao>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar testes por aprovação do cliente
     */
    @GetMapping("/aprovacao/{aprovado}")
    public ResponseEntity<List<TesteAceitacao>> buscarPorAprovacaoCliente(@PathVariable Boolean aprovado) {
        List<TesteAceitacao> testes = testeAceitacaoService.buscarPorAprovacaoCliente(aprovado);
        return ResponseEntity.ok(testes);
    }
    
    /**
     * GET - Buscar teste mais recente por teste do sistema
     */
    @GetMapping("/teste-sistema/{idTesteSistema}/mais-recente")
    public ResponseEntity<TesteAceitacao> buscarMaisRecentePorTesteSistema(@PathVariable Integer idTesteSistema) {
        Optional<TesteAceitacao> teste = testeAceitacaoService.buscarMaisRecentePorTesteSistema(idTesteSistema);
        return teste.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * GET - Contar testes por status
     */
    @GetMapping("/contar/status/{status}")
    public ResponseEntity<Long> contarPorStatus(@PathVariable String status) {
        long quantidade = testeAceitacaoService.contarPorStatus(status);
        return ResponseEntity.ok(quantidade);
    }
    
    /**
     * GET - Verificar se existe teste para um teste do sistema
     */
    @GetMapping("/existe/teste-sistema/{idTesteSistema}")
    public ResponseEntity<Boolean> existePorTesteSistema(@PathVariable Integer idTesteSistema) {
        boolean existe = testeAceitacaoService.existePorTesteSistema(idTesteSistema);
        return ResponseEntity.ok(existe);
    }
    
    /**
     * Exception handler para validação
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
} 