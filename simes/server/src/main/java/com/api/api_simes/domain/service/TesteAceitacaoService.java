package com.api.api_simes.domain.service;

import com.api.api_simes.domain.entity.TesteAceitacao;
import com.api.api_simes.domain.repository.TesteAceitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer para TesteAceitacao
 * Implementa a lógica de negócio e orquestra as operações CRUD
 */
@Service
@Transactional
public class TesteAceitacaoService {
    
    private final TesteAceitacaoRepository testeAceitacaoRepository;
    
    @Autowired
    public TesteAceitacaoService(TesteAceitacaoRepository testeAceitacaoRepository) {
        this.testeAceitacaoRepository = testeAceitacaoRepository;
    }
    
    /**
     * Salva um novo teste de aceitação
     * @param testeAceitacao Teste de aceitação a ser salvo
     * @return Teste de aceitação salvo
     */
    public TesteAceitacao salvar(TesteAceitacao testeAceitacao) {
        return testeAceitacaoRepository.save(testeAceitacao);
    }
    
    /**
     * Busca um teste de aceitação por ID
     * @param id ID do teste de aceitação
     * @return Optional contendo o teste de aceitação se encontrado
     */
    @Transactional(readOnly = true)
    public Optional<TesteAceitacao> buscarPorId(Integer id) {
        return testeAceitacaoRepository.findByIdTesteAceitacao(id);
    }
    
    /**
     * Lista todos os testes de aceitação
     * @return Lista de todos os testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> listarTodos() {
        return testeAceitacaoRepository.findAll();
    }
    
    /**
     * Atualiza um teste de aceitação existente
     * @param id ID do teste de aceitação
     * @param testeAceitacao Dados atualizados
     * @return Optional contendo o teste de aceitação atualizado
     */
    public Optional<TesteAceitacao> atualizar(Integer id, TesteAceitacao testeAceitacao) {
        return testeAceitacaoRepository.findByIdTesteAceitacao(id)
                .map(existente -> {
                    existente.setIdTesteSistema(testeAceitacao.getIdTesteSistema());
                    existente.setClienteAprovou(testeAceitacao.getClienteAprovou());
                    existente.setFeedbackCliente(testeAceitacao.getFeedbackCliente());
                    existente.setDataAceite(testeAceitacao.getDataAceite());
                    existente.setResponsavelValidacao(testeAceitacao.getResponsavelValidacao());
                    existente.setStatusTeste(testeAceitacao.getStatusTeste());
                    return testeAceitacaoRepository.save(existente);
                });
    }
    
    /**
     * Exclui um teste de aceitação por ID
     * @param id ID do teste de aceitação a ser excluído
     */
    public void excluir(Integer id) {
        testeAceitacaoRepository.deleteById(id);
    }
    
    /**
     * Busca testes de aceitação por ID do teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return Lista de testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorIdTesteSistema(Integer idTesteSistema) {
        return testeAceitacaoRepository.findByIdTesteSistema(idTesteSistema);
    }
    
    /**
     * Busca testes de aceitação por status
     * @param status Status do teste
     * @return Lista de testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorStatus(String status) {
        return testeAceitacaoRepository.findByStatusTeste(status);
    }
    
    /**
     * Busca testes de aceitação por responsável
     * @param responsavel Nome do responsável
     * @return Lista de testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorResponsavel(String responsavel) {
        return testeAceitacaoRepository.findByResponsavelValidacao(responsavel);
    }
    
    /**
     * Busca testes de aceitação por data
     * @param dataAceite Data de aceite
     * @return Lista de testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorData(LocalDate dataAceite) {
        return testeAceitacaoRepository.findByDataAceite(dataAceite);
    }
    
    /**
     * Busca testes de aceitação por período
     * @param dataInicio Data de início
     * @param dataFim Data de fim
     * @return Lista de testes de aceitação no período
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return testeAceitacaoRepository.findByPeriodoDataAceite(dataInicio, dataFim);
    }
    
    /**
     * Busca testes aprovados pelo cliente
     * @param aprovado true para aprovados, false para reprovados
     * @return Lista de testes de aceitação
     */
    @Transactional(readOnly = true)
    public List<TesteAceitacao> buscarPorAprovacaoCliente(Boolean aprovado) {
        return testeAceitacaoRepository.findByClienteAprovou(aprovado);
    }
    
    /**
     * Busca o teste de aceitação mais recente por teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return Optional contendo o teste mais recente
     */
    @Transactional(readOnly = true)
    public Optional<TesteAceitacao> buscarMaisRecentePorTesteSistema(Integer idTesteSistema) {
        return testeAceitacaoRepository.findLatestByIdTesteSistema(idTesteSistema);
    }
    
    /**
     * Conta testes por status
     * @param status Status do teste
     * @return Número de testes com o status
     */
    @Transactional(readOnly = true)
    public long contarPorStatus(String status) {
        return testeAceitacaoRepository.countByStatusTeste(status);
    }
    
    /**
     * Verifica se existe teste para um teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return true se existir, false caso contrário
     */
    @Transactional(readOnly = true)
    public boolean existePorTesteSistema(Integer idTesteSistema) {
        return testeAceitacaoRepository.existsByIdTesteSistema(idTesteSistema);
    }
} 