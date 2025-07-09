@echo off
echo ========================================
echo    SIMES - Sistema de Teste de Aceitacao
echo ========================================
echo.

echo [1/3] Verificando Java...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Java nao encontrado. Instale o Java 17 ou superior.
    pause
    exit /b 1
)
echo Java encontrado!

echo.
echo [2/3] Verificando MySQL...
echo Certifique-se de que o MySQL esta rodando na porta 3306
echo Usuario: root (sem senha)
echo.

echo [3/3] Iniciando o servidor Spring Boot...
cd server
echo Executando: mvnw.cmd spring-boot:run
echo.
echo Aguarde... O servidor sera iniciado em http://localhost:8080
echo.
mvnw.cmd spring-boot:run

pause 