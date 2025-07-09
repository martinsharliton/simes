package com.api.api_simes.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AceitacaoResponseDTO {

    private Long idTesteAceitacao;
    private Integer idTesteSistema;
    private Boolean clienteAprovou;
    private String feedbackCliente;
    private LocalDate dataAceite;
    private String responsavelValidacao;
    private Integer statusAceite;
    // private Long version; // Removido temporariamente
} 