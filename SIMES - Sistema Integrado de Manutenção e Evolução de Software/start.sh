#!/bin/bash

echo "========================================"
echo "   SIMES - Sistema de Teste de Aceitação"
echo "========================================"
echo

echo "[1/3] Verificando Java..."
if ! command -v java &> /dev/null; then
    echo "ERRO: Java não encontrado. Instale o Java 17 ou superior."
    exit 1
fi
echo "Java encontrado!"

echo
echo "[2/3] Verificando MySQL..."
echo "Certifique-se de que o MySQL está rodando na porta 3306"
echo "Usuário: root (sem senha)"
echo

echo "[3/3] Iniciando o servidor Spring Boot..."
cd server
echo "Executando: ./mvnw spring-boot:run"
echo
echo "Aguarde... O servidor será iniciado em http://localhost:8080"
echo

# Tornar o script executável se necessário
chmod +x mvnw

# Executar o projeto
./mvnw spring-boot:run 