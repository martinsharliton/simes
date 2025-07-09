package com.api.api_simes.domain.config;

import com.api.api_simes.domain.entity.Aceitacao;
import com.api.api_simes.domain.repository.AceitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AceitacaoRepository aceitacaoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem dados
        if (aceitacaoRepository.count() == 0) {
            // Inserir dados de exemplo
            inserirDadosExemplo();
            System.out.println("✅ Dados de exemplo inseridos com sucesso!");
        } else {
            System.out.println("ℹ️ Dados já existem no banco.");
        }
    }

    private void inserirDadosExemplo() {
        // Criar objetos usando setters para evitar problemas de construtor
        Aceitacao aceitacao1 = new Aceitacao();
        aceitacao1.setIdTesteSistema(1001);
        aceitacao1.setClienteAprovou(true);
        aceitacao1.setFeedbackCliente("Sistema funcionando perfeitamente. Todas as funcionalidades testadas e aprovadas.");
        aceitacao1.setDataAceite(LocalDate.of(2024, 1, 15));
        aceitacao1.setResponsavelValidacao("João Silva");
        aceitacao1.setStatusAceite(3);
        aceitacaoRepository.save(aceitacao1);

        Aceitacao aceitacao2 = new Aceitacao();
        aceitacao2.setIdTesteSistema(1002);
        aceitacao2.setClienteAprovou(false);
        aceitacao2.setFeedbackCliente("Encontrados alguns bugs na interface. Necessita correções antes da aprovação.");
        aceitacao2.setDataAceite(LocalDate.of(2024, 1, 16));
        aceitacao2.setResponsavelValidacao("Maria Santos");
        aceitacao2.setStatusAceite(4);
        aceitacaoRepository.save(aceitacao2);

        Aceitacao aceitacao3 = new Aceitacao();
        aceitacao3.setIdTesteSistema(1003);
        aceitacao3.setClienteAprovou(null);
        aceitacao3.setFeedbackCliente("Teste em andamento. Aguardando feedback do cliente.");
        aceitacao3.setDataAceite(LocalDate.of(2024, 1, 17));
        aceitacao3.setResponsavelValidacao("Pedro Oliveira");
        aceitacao3.setStatusAceite(1);
        aceitacaoRepository.save(aceitacao3);

        Aceitacao aceitacao4 = new Aceitacao();
        aceitacao4.setIdTesteSistema(1004);
        aceitacao4.setClienteAprovou(true);
        aceitacao4.setFeedbackCliente("Excelente trabalho! Sistema atendeu todas as expectativas.");
        aceitacao4.setDataAceite(LocalDate.of(2024, 1, 18));
        aceitacao4.setResponsavelValidacao("Ana Costa");
        aceitacao4.setStatusAceite(3);
        aceitacaoRepository.save(aceitacao4);

        Aceitacao aceitacao5 = new Aceitacao();
        aceitacao5.setIdTesteSistema(1005);
        aceitacao5.setClienteAprovou(null);
        aceitacao5.setFeedbackCliente("Em análise técnica. Verificando conformidade com requisitos.");
        aceitacao5.setDataAceite(LocalDate.of(2024, 1, 19));
        aceitacao5.setResponsavelValidacao("Carlos Ferreira");
        aceitacao5.setStatusAceite(2);
        aceitacaoRepository.save(aceitacao5);
    }
} 