package com.api.api_simes.domain.service;

import com.api.api_simes.domain.dto.AceitacaoDTO;
import com.api.api_simes.domain.dto.AceitacaoResponseDTO;
import com.api.api_simes.domain.entity.Aceitacao;
import com.api.api_simes.domain.repository.AceitacaoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AceitacaoService {

    @Autowired
    private AceitacaoRepository aceitacaoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public AceitacaoResponseDTO criarAceitacao(AceitacaoDTO aceitacaoDTO) {
        Aceitacao aceitacao = modelMapper.map(aceitacaoDTO, Aceitacao.class);
        
        // Define a data de aceite como hoje se não fornecida
        if (aceitacao.getDataAceite() == null) {
            aceitacao.setDataAceite(LocalDate.now());
        }
        
        // Define status padrão se não fornecido
        if (aceitacao.getStatusAceite() == null) {
            aceitacao.setStatusAceite(1); // 1 = Pendente
        }
        
        // Version removido temporariamente
        // aceitacao.setVersion(0L);
        
        Aceitacao aceitacaoSalva = aceitacaoRepository.save(aceitacao);
        return modelMapper.map(aceitacaoSalva, AceitacaoResponseDTO.class);
    }

    public List<AceitacaoResponseDTO> listarTodasAceitacoes() {
        List<Aceitacao> aceitacoes = aceitacaoRepository.findAll();
        return aceitacoes.stream()
                .map(aceitacao -> modelMapper.map(aceitacao, AceitacaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<AceitacaoResponseDTO> buscarPorId(Long id) {
        Optional<Aceitacao> aceitacao = aceitacaoRepository.findById(id);
        return aceitacao.map(a -> modelMapper.map(a, AceitacaoResponseDTO.class));
    }

    public Optional<AceitacaoResponseDTO> buscarPorIdTesteSistema(Integer idTesteSistema) {
        Optional<Aceitacao> aceitacao = aceitacaoRepository.findByIdTesteSistema(idTesteSistema);
        return aceitacao.map(a -> modelMapper.map(a, AceitacaoResponseDTO.class));
    }

    public List<AceitacaoResponseDTO> buscarPorClienteAprovou(Boolean clienteAprovou) {
        List<Aceitacao> aceitacoes = aceitacaoRepository.findByClienteAprovou(clienteAprovou);
        return aceitacoes.stream()
                .map(aceitacao -> modelMapper.map(aceitacao, AceitacaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<AceitacaoResponseDTO> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Aceitacao> aceitacoes = aceitacaoRepository.findByDataAceiteBetween(dataInicio, dataFim);
        return aceitacoes.stream()
                .map(aceitacao -> modelMapper.map(aceitacao, AceitacaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<AceitacaoResponseDTO> buscarPorResponsavel(String responsavel) {
        List<Aceitacao> aceitacoes = aceitacaoRepository.findByResponsavelValidacao(responsavel);
        return aceitacoes.stream()
                .map(aceitacao -> modelMapper.map(aceitacao, AceitacaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public List<AceitacaoResponseDTO> buscarPorStatus(Integer status) {
        List<Aceitacao> aceitacoes = aceitacaoRepository.findByStatusAceite(status);
        return aceitacoes.stream()
                .map(aceitacao -> modelMapper.map(aceitacao, AceitacaoResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<AceitacaoResponseDTO> atualizarAceitacao(Long id, AceitacaoDTO aceitacaoDTO) {
        try {
            Optional<Aceitacao> aceitacaoExistente = aceitacaoRepository.findById(id);
            
            if (aceitacaoExistente.isPresent()) {
                Aceitacao aceitacao = aceitacaoExistente.get();
                
                // Preparar valores para atualização
                Integer idTesteSistema = aceitacaoDTO.getIdTesteSistema() != null ? 
                    aceitacaoDTO.getIdTesteSistema() : aceitacao.getIdTesteSistema();
                Boolean clienteAprovou = aceitacaoDTO.getClienteAprovou() != null ? 
                    aceitacaoDTO.getClienteAprovou() : aceitacao.getClienteAprovou();
                String feedbackCliente = aceitacaoDTO.getFeedbackCliente() != null ? 
                    aceitacaoDTO.getFeedbackCliente() : aceitacao.getFeedbackCliente();
                LocalDate dataAceite = aceitacaoDTO.getDataAceite() != null ? 
                    aceitacaoDTO.getDataAceite() : aceitacao.getDataAceite();
                String responsavelValidacao = aceitacaoDTO.getResponsavelValidacao() != null ? 
                    aceitacaoDTO.getResponsavelValidacao() : aceitacao.getResponsavelValidacao();
                Integer statusAceite = aceitacaoDTO.getStatusAceite() != null ? 
                    aceitacaoDTO.getStatusAceite() : aceitacao.getStatusAceite();
                
                // Executar atualização usando query nativa
                int rowsUpdated = aceitacaoRepository.updateAceitacao(
                    id, idTesteSistema, clienteAprovou, feedbackCliente, 
                    dataAceite, responsavelValidacao, statusAceite
                );
                
                if (rowsUpdated > 0) {
                    // Buscar a entidade atualizada
                    Optional<Aceitacao> aceitacaoAtualizada = aceitacaoRepository.findById(id);
                    return aceitacaoAtualizada.map(a -> modelMapper.map(a, AceitacaoResponseDTO.class));
                }
            }
            
            return Optional.empty();
        } catch (Exception e) {
            // Log do erro para debug
            System.err.println("Erro ao atualizar aceitação: " + e.getMessage());
            throw new RuntimeException("Erro ao atualizar aceitação: " + e.getMessage(), e);
        }
    }

    public boolean deletarAceitacao(Long id) {
        if (aceitacaoRepository.existsById(id)) {
            aceitacaoRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 