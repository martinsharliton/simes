package com.api.api_simes.domain.controller;

import com.api.api_simes.domain.dto.AceitacaoDTO;
import com.api.api_simes.domain.dto.AceitacaoResponseDTO;
import com.api.api_simes.domain.service.AceitacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@RequestMapping("/api/aceitacao")
@CrossOrigin(origins = "*")
public class AceitacaoController {

    @Autowired
    private AceitacaoService aceitacaoService;

    @PostMapping
    public ResponseEntity<AceitacaoResponseDTO> criarAceitacao(@Valid @RequestBody AceitacaoDTO aceitacaoDTO) {
        AceitacaoResponseDTO aceitacaoCriada = aceitacaoService.criarAceitacao(aceitacaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(aceitacaoCriada);
    }

    @GetMapping
    public ResponseEntity<List<AceitacaoResponseDTO>> listarTodasAceitacoes() {
        List<AceitacaoResponseDTO> aceitacoes = aceitacaoService.listarTodasAceitacoes();
        return ResponseEntity.ok(aceitacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AceitacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        Optional<AceitacaoResponseDTO> aceitacao = aceitacaoService.buscarPorId(id);
        return aceitacao.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/teste-sistema/{idTesteSistema}")
    public ResponseEntity<AceitacaoResponseDTO> buscarPorIdTesteSistema(@PathVariable Integer idTesteSistema) {
        Optional<AceitacaoResponseDTO> aceitacao = aceitacaoService.buscarPorIdTesteSistema(idTesteSistema);
        return aceitacao.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente-aprovou/{clienteAprovou}")
    public ResponseEntity<List<AceitacaoResponseDTO>> buscarPorClienteAprovou(@PathVariable Boolean clienteAprovou) {
        List<AceitacaoResponseDTO> aceitacoes = aceitacaoService.buscarPorClienteAprovou(clienteAprovou);
        return ResponseEntity.ok(aceitacoes);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<AceitacaoResponseDTO>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        List<AceitacaoResponseDTO> aceitacoes = aceitacaoService.buscarPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(aceitacoes);
    }

    @GetMapping("/responsavel/{responsavel}")
    public ResponseEntity<List<AceitacaoResponseDTO>> buscarPorResponsavel(@PathVariable String responsavel) {
        List<AceitacaoResponseDTO> aceitacoes = aceitacaoService.buscarPorResponsavel(responsavel);
        return ResponseEntity.ok(aceitacoes);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AceitacaoResponseDTO>> buscarPorStatus(@PathVariable Integer status) {
        List<AceitacaoResponseDTO> aceitacoes = aceitacaoService.buscarPorStatus(status);
        return ResponseEntity.ok(aceitacoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AceitacaoResponseDTO> atualizarAceitacao(
            @PathVariable Long id, 
            @Valid @RequestBody AceitacaoDTO aceitacaoDTO) {
        Optional<AceitacaoResponseDTO> aceitacaoAtualizada = aceitacaoService.atualizarAceitacao(id, aceitacaoDTO);
        return aceitacaoAtualizada.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAceitacao(@PathVariable Long id) {
        boolean deletado = aceitacaoService.deletarAceitacao(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Object> obterEstatisticas() {
        List<AceitacaoResponseDTO> todasAceitacoes = aceitacaoService.listarTodasAceitacoes();
        
        long totalAceitacoes = todasAceitacoes.size();
        long aprovadas = todasAceitacoes.stream()
                .filter(a -> Boolean.TRUE.equals(a.getClienteAprovou()))
                .count();
        long reprovadas = todasAceitacoes.stream()
                .filter(a -> Boolean.FALSE.equals(a.getClienteAprovou()))
                .count();
        long pendentes = totalAceitacoes - aprovadas - reprovadas;
        
        return ResponseEntity.ok(Map.of(
            "totalAceitacoes", totalAceitacoes,
            "aprovadas", aprovadas,
            "reprovadas", reprovadas,
            "pendentes", pendentes,
            "taxaAprovacao", totalAceitacoes > 0 ? (double) aprovadas / totalAceitacoes * 100 : 0
        ));
    }
} 