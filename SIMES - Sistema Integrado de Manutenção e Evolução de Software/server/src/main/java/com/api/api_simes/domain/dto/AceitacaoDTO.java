package com.api.api_simes.domain.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AceitacaoDTO {

    @NotNull(message = "ID do teste é obrigatório")
    private Integer idTesteSistema;

    private Boolean clienteAprovou;

    @Size(max = 200, message = "A observação se limita a 200 caracteres")
    private String feedbackCliente;

    private LocalDate dataAceite;

    @NotBlank(message = "É obrigatório a inserção de um responsável pela validação")
    @Size(max = 50, message = "Tamanho máximo do nome restrito a 50 caracteres")
    private String responsavelValidacao;

    private Integer statusAceite;
} 