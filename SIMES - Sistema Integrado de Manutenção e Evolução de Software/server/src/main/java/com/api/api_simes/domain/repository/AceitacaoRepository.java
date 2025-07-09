package com.api.api_simes.domain.repository;

import com.api.api_simes.domain.entity.Aceitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AceitacaoRepository extends JpaRepository<Aceitacao, Long> {

    @Query("SELECT a FROM Aceitacao a WHERE a.idTesteSistema = :idTesteSistema")
    Optional<Aceitacao> findByIdTesteSistema(@Param("idTesteSistema") Integer idTesteSistema);

    @Query("SELECT a FROM Aceitacao a WHERE a.clienteAprovou = :clienteAprovou")
    List<Aceitacao> findByClienteAprovou(@Param("clienteAprovou") Boolean clienteAprovou);

    @Query("SELECT a FROM Aceitacao a WHERE a.dataAceite BETWEEN :dataInicio AND :dataFim")
    List<Aceitacao> findByDataAceiteBetween(@Param("dataInicio") LocalDate dataInicio, 
                                           @Param("dataFim") LocalDate dataFim);

    @Query("SELECT a FROM Aceitacao a WHERE a.responsavelValidacao = :responsavel")
    List<Aceitacao> findByResponsavelValidacao(@Param("responsavel") String responsavel);

    @Query("SELECT a FROM Aceitacao a WHERE a.statusAceite = :status")
    List<Aceitacao> findByStatusAceite(@Param("status") Integer status);

    @Query(value = "UPDATE teste_aceitacao SET " +
                   "id_teste_sistema = :idTesteSistema, " +
                   "cliente_aprovou = :clienteAprovou, " +
                   "feedback_cliente = :feedbackCliente, " +
                   "data_aceite = :dataAceite, " +
                   "responsavel_validacao = :responsavelValidacao, " +
                   "status_teste = :statusAceite " +
                   "WHERE id_teste_aceitacao = :id", nativeQuery = true)
    @Modifying
    @Transactional
    int updateAceitacao(@Param("id") Long id,
                       @Param("idTesteSistema") Integer idTesteSistema,
                       @Param("clienteAprovou") Boolean clienteAprovou,
                       @Param("feedbackCliente") String feedbackCliente,
                       @Param("dataAceite") LocalDate dataAceite,
                       @Param("responsavelValidacao") String responsavelValidacao,
                       @Param("statusAceite") Integer statusAceite);
}