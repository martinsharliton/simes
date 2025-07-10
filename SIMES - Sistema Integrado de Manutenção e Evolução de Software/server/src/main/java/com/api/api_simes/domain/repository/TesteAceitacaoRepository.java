package com.api.api_simes.domain.repository;

import com.api.api_simes.domain.entity.TesteAceitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório CRUD para a entidade TesteAceitacao
 * Implementa operações básicas de Create, Read, Update, Delete
 */
@Repository
public interface TesteAceitacaoRepository extends JpaRepository<TesteAceitacao, Integer> {
    
    /**
     * Busca um teste de aceitação por ID
     * @param id ID do teste de aceitação
     * @return Optional contendo o teste de aceitação se encontrado
     */
    Optional<TesteAceitacao> findByIdTesteAceitacao(Integer id);
    
    /**
     * Busca todos os testes de aceitação por ID do teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return Lista de testes de aceitação
     */
    List<TesteAceitacao> findByIdTesteSistema(Integer idTesteSistema);
    
    /**
     * Busca testes de aceitação por status
     * @param status Status do teste (A=Aprovado, R=Reprovado, P=Pendente)
     * @return Lista de testes de aceitação com o status especificado
     */
    List<TesteAceitacao> findByStatusTeste(String status);
    
    /**
     * Busca testes de aceitação por responsável pela validação
     * @param responsavel Nome do responsável pela validação
     * @return Lista de testes de aceitação
     */
    List<TesteAceitacao> findByResponsavelValidacao(String responsavel);
    
    /**
     * Busca testes de aceitação por data de aceite
     * @param dataAceite Data de aceite
     * @return Lista de testes de aceitação
     */
    List<TesteAceitacao> findByDataAceite(LocalDate dataAceite);
    
    /**
     * Busca testes de aceitação aprovados pelo cliente
     * @param clienteAprovou true se aprovado, false se reprovado
     * @return Lista de testes de aceitação
     */
    List<TesteAceitacao> findByClienteAprovou(Boolean clienteAprovou);
    
    /**
     * Busca testes de aceitação por período de data
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @return Lista de testes de aceitação no período especificado
     */
    @Query("SELECT ta FROM TesteAceitacao ta WHERE ta.dataAceite BETWEEN :dataInicio AND :dataFim")
    List<TesteAceitacao> findByPeriodoDataAceite(@Param("dataInicio") LocalDate dataInicio, 
                                                  @Param("dataFim") LocalDate dataFim);
    
    /**
     * Busca testes de aceitação por responsável e status
     * @param responsavel Nome do responsável pela validação
     * @param status Status do teste
     * @return Lista de testes de aceitação
     */
    List<TesteAceitacao> findByResponsavelValidacaoAndStatusTeste(String responsavel, String status);
    
    /**
     * Conta quantos testes de aceitação existem por status
     * @param status Status do teste
     * @return Número de testes com o status especificado
     */
    long countByStatusTeste(String status);
    
    /**
     * Verifica se existe teste de aceitação para um teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return true se existir, false caso contrário
     */
    boolean existsByIdTesteSistema(Integer idTesteSistema);
    
    /**
     * Busca testes de aceitação com feedback contendo determinada palavra
     * @param palavraChave Palavra-chave para buscar no feedback
     * @return Lista de testes de aceitação
     */
    @Query("SELECT ta FROM TesteAceitacao ta WHERE ta.feedbackCliente LIKE %:palavraChave%")
    List<TesteAceitacao> findByFeedbackContaining(@Param("palavraChave") String palavraChave);
    
    /**
     * Busca o teste de aceitação mais recente por teste do sistema
     * @param idTesteSistema ID do teste do sistema
     * @return Optional contendo o teste de aceitação mais recente
     */
    @Query("SELECT ta FROM TesteAceitacao ta WHERE ta.idTesteSistema = :idTesteSistema ORDER BY ta.dataAceite DESC")
    Optional<TesteAceitacao> findLatestByIdTesteSistema(@Param("idTesteSistema") Integer idTesteSistema);
} 