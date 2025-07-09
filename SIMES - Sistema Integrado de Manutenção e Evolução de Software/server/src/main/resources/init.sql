-- Script de inicialização do banco de dados SIMES
-- Este script cria a tabela e insere dados de exemplo

-- Criar banco de dados se não existir
CREATE DATABASE IF NOT EXISTS simes_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE simes_db;

-- Criar tabela de teste de aceitação
CREATE TABLE IF NOT EXISTS teste_aceitacao (
    id_teste_aceitacao BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_teste_sistema INT NOT NULL,
    cliente_aprovou BOOLEAN,
    feedback_cliente VARCHAR(200),
    data_aceite DATE,
    responsavel_validacao VARCHAR(50) NOT NULL,
    status_teste INT DEFAULT 1
);

-- Inserir dados de exemplo
INSERT INTO teste_aceitacao (id_teste_sistema, cliente_aprovou, feedback_cliente, data_aceite, responsavel_validacao, status_teste) VALUES
(1001, true, 'Sistema funcionando perfeitamente. Todas as funcionalidades testadas e aprovadas.', '2024-01-15', 'João Silva', 3),
(1002, false, 'Encontrados alguns bugs na interface. Necessita correções antes da aprovação.', '2024-01-16', 'Maria Santos', 4),
(1003, null, 'Teste em andamento. Aguardando feedback do cliente.', '2024-01-17', 'Pedro Oliveira', 1),
(1004, true, 'Excelente trabalho! Sistema atendeu todas as expectativas.', '2024-01-18', 'Ana Costa', 3),
(1005, null, 'Em análise técnica. Verificando conformidade com requisitos.', '2024-01-19', 'Carlos Ferreira', 2),
(1006, false, 'Problemas de performance identificados. Necessita otimização.', '2024-01-20', 'Lucia Mendes', 4),
(1007, true, 'Aprovado com sucesso. Sistema pronto para produção.', '2024-01-21', 'Roberto Lima', 3),
(1008, null, 'Aguardando validação do cliente final.', '2024-01-22', 'Fernanda Rocha', 1),
(1009, true, 'Teste de aceitação concluído com êxito.', '2024-01-23', 'Ricardo Alves', 3),
(1010, false, 'Interface não está intuitiva. Sugestões de melhoria enviadas.', '2024-01-24', 'Patricia Souza', 4);

-- Criar índices para melhor performance
CREATE INDEX idx_id_teste_sistema ON teste_aceitacao(id_teste_sistema);
CREATE INDEX idx_cliente_aprovou ON teste_aceitacao(cliente_aprovou);
CREATE INDEX idx_data_aceite ON teste_aceitacao(data_aceite);
CREATE INDEX idx_responsavel_validacao ON teste_aceitacao(responsavel_validacao);
CREATE INDEX idx_status_teste ON teste_aceitacao(status_teste);

-- Verificar dados inseridos
SELECT 
    'Dados de exemplo inseridos com sucesso!' as mensagem,
    COUNT(*) as total_registros
FROM teste_aceitacao;

-- Mostrar estatísticas iniciais
SELECT 
    'Estatísticas Iniciais' as titulo,
    COUNT(*) as total_teste,
    SUM(CASE WHEN cliente_aprovou = true THEN 1 ELSE 0 END) as aprovados,
    SUM(CASE WHEN cliente_aprovou = false THEN 1 ELSE 0 END) as reprovados,
    SUM(CASE WHEN cliente_aprovou IS NULL THEN 1 ELSE 0 END) as pendentes
FROM teste_aceitacao; 