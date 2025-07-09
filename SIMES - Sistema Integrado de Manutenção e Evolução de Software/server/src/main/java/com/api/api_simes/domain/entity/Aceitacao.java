package com.api.api_simes.domain.entity;

import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teste_aceitacao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aceitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_teste_aceitacao")
    private Long idTesteAceitacao;

    @Column(name="id_teste_sistema", nullable = false)
    @NotNull(message = "ID do teste é obrigatório")
    private Integer idTesteSistema;

    @Column(name="cliente_aprovou")
    private Boolean clienteAprovou;

    @Column(name="feedback_cliente", length = 200)
    @Length(message="A observação se limita a 200 caracteres", max=200)
    private String feedbackCliente;

    @Column(name="data_aceite") 
    private LocalDate dataAceite;

    @Column(name="responsavel_validacao", nullable = false, length = 50)
    @NotNull(message = "É obrigatório a inserção de um responsável pela validação")
    @Length(message = "Tamanho máximo do nome restrito a 50 caracteres", max=50)
    private String responsavelValidacao;

    @Column(name="status_teste")
    private Integer statusAceite;

    // @Version - Removido temporariamente para resolver problema de inicialização
    // @Column(name = "version")
    // private Long version = 0L;
}
