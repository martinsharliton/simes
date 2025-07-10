package com.api.api_simes.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * DTO para TesteAceitacao
 * Usado para transferência de dados entre camadas
 */
public class TesteAceitacaoDTO {
    
    private Integer idTesteAceitacao;
    
    @NotNull(message = "ID do teste do sistema é obrigatório")
    private Integer idTesteSistema;
    
    @NotNull(message = "Aprovação do cliente é obrigatória")
    private Boolean clienteAprovou;
    
    @NotBlank(message = "Feedback do cliente é obrigatório")
    @Size(max = 200, message = "Feedback deve ter no máximo 200 caracteres")
    private String feedbackCliente;
    
    //@NotNull(message = "Data de aceite é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAceite;
    
    @NotBlank(message = "Responsável pela validação é obrigatório")
    @Size(max = 50, message = "Responsável deve ter no máximo 50 caracteres")
    private String responsavelValidacao;
    
    @Size(max = 1, message = "Status deve ter no máximo 1 caractere")
    private String statusTeste;
    
    // Construtores
    public TesteAceitacaoDTO() {}
    
    public TesteAceitacaoDTO(Integer idTesteAceitacao, Integer idTesteSistema, Boolean clienteAprovou,
                            String feedbackCliente, LocalDate dataAceite, String responsavelValidacao, String statusTeste) {
        this.idTesteAceitacao = idTesteAceitacao;
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
        return "TesteAceitacaoDTO{" +
                "idTesteAceitacao=" + idTesteAceitacao +
                ", idTesteSistema=" + idTesteSistema +
                ", clienteAprovou=" + clienteAprovou +
                ", feedbackCliente='" + feedbackCliente + '\'' +
                ", dataAceite=" + dataAceite +
                ", responsavelValidacao='" + responsavelValidacao + '\'' +
                ", statusTeste='" + statusTeste + '\'' +
                '}';
    }
} 