package com.api.api_simes.domain.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidade que representa a tabela teste_aceitacao
 */
@Entity
@Table(name = "teste_aceitacao")
public class TesteAceitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_teste_aceitacao")
    private Integer idTesteAceitacao;

    @NotNull(message = "ID do teste do sistema é obrigatório")
    @Column(name = "id_teste_sistema", nullable = false)
    private Integer idTesteSistema;

    @NotNull(message = "Aprovação do cliente é obrigatória")
    @Column(name = "cliente_aprovou", nullable = false)
    private Boolean clienteAprovou;

    @NotBlank(message = "Feedback do cliente é obrigatório")
    @Size(max = 200, message = "Feedback deve ter no máximo 200 caracteres")
    @Column(name = "feedback_cliente", nullable = false, length = 200)
    private String feedbackCliente;

    // @NotNull(message = "Data de aceite é obrigatória")
    @Column(name = "data_aceite", nullable = true)
    private LocalDate dataAceite;

    @NotBlank(message = "Responsável pela validação é obrigatório")
    @Size(max = 50, message = "Responsável deve ter no máximo 50 caracteres")
    @Column(name = "responsavel_validacao", nullable = false, length = 50)
    private String responsavelValidacao;

    @Column(name = "status_teste", length = 1)
    private String statusTeste;

    // Construtores
    public TesteAceitacao() {
    }

    public TesteAceitacao(Integer idTesteSistema, Boolean clienteAprovou, String feedbackCliente,
            LocalDate dataAceite, String responsavelValidacao, String statusTeste) {
        this.idTesteSistema = idTesteSistema;
        this.clienteAprovou = clienteAprovou;
        this.feedbackCliente = feedbackCliente;
        this.dataAceite = dataAceite;
        this.responsavelValidacao = responsavelValidacao;
        this.statusTeste = statusTeste;
    }

    // Getters e Setters
    public Integer getIdTesteAceitacao() {
        return idTesteAceitacao;
    }

    public void setIdTesteAceitacao(Integer idTesteAceitacao) {
        this.idTesteAceitacao = idTesteAceitacao;
    }

    public Integer getIdTesteSistema() {
        return idTesteSistema;
    }

    public void setIdTesteSistema(Integer idTesteSistema) {
        this.idTesteSistema = idTesteSistema;
    }

    public Boolean getClienteAprovou() {
        return clienteAprovou;
    }

    public void setClienteAprovou(Boolean clienteAprovou) {
        this.clienteAprovou = clienteAprovou;
    }

    public String getFeedbackCliente() {
        return feedbackCliente;
    }

    public void setFeedbackCliente(String feedbackCliente) {
        this.feedbackCliente = feedbackCliente;
    }

    public LocalDate getDataAceite() {
        return dataAceite;
    }

    public void setDataAceite(LocalDate dataAceite) {
        this.dataAceite = dataAceite;
    }

    public String getResponsavelValidacao() {
        return responsavelValidacao;
    }

    public void setResponsavelValidacao(String responsavelValidacao) {
        this.responsavelValidacao = responsavelValidacao;
    }

    public String getStatusTeste() {
        return statusTeste;
    }

    public void setStatusTeste(String statusTeste) {
        this.statusTeste = statusTeste;
    }

    @Override
    public String toString() {
        return "TesteAceitacao{" +
                "idTesteAceitacao=" + idTesteAceitacao +
                ", idTesteSistema=" + idTesteSistema +
                ", clienteAprovou=" + clienteAprovou +
                ", feedbackCliente='" + feedbackCliente + '\'' +
                ", dataAceite=" + dataAceite +
                ", responsavelValidacao='" + responsavelValidacao + '\'' +
                ", statusTeste='" + statusTeste + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TesteAceitacao that = (TesteAceitacao) o;

        return idTesteAceitacao != null ? idTesteAceitacao.equals(that.idTesteAceitacao)
                : that.idTesteAceitacao == null;
    }

    @Override
    public int hashCode() {
        return idTesteAceitacao != null ? idTesteAceitacao.hashCode() : 0;
    }
}