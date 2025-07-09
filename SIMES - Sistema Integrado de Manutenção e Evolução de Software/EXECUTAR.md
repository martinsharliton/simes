# 🚀 Guia Rápido de Execução - SIMES

## ⚡ Execução Rápida (H2 Database)

### 1. Iniciar o Backend
```bash
cd server
mvnw.cmd spring-boot:run  # Windows
./mvnw spring-boot:run    # Linux/Mac
```

### 2. Acessar a Aplicação
- **Frontend**: Abra `front/index.html` no navegador
- **API**: `http://localhost:8080/api/aceitacao`
- **H2 Console**: `http://localhost:8080/h2-console`
- **Health Check**: `http://localhost:8080/actuator/health`

### 3. Testar a API
- Abra `test-api.html` no navegador
- Clique em "Testar Conexão"

## 🔧 Configuração H2 Console
- **JDBC URL**: `jdbc:h2:mem:simes_db`
- **Username**: `sa`
- **Password**: (deixe em branco)

## 📊 Dados de Exemplo
O sistema já vem com 5 testes de exemplo carregados automaticamente:
- Testes aprovados
- Testes reprovados
- Testes pendentes

## 🐛 Problemas Comuns

### Erro de Concorrência
- ✅ **Corrigido**: Removido controle de versão temporariamente
- Reinicie a aplicação se necessário

### Erro de Conexão
- ✅ **Corrigido**: Usando H2 Database (não precisa MySQL)
- Não precisa instalar nada adicional

### Erro de CORS
- ✅ **Corrigido**: CORS configurado no backend
- Verifique se está acessando a URL correta

## 🎯 Pronto para Usar!
Agora você pode:
- ✅ Criar testes de aceitação
- ✅ Editar testes existentes
- ✅ Excluir testes
- ✅ Filtrar e buscar
- ✅ Ver estatísticas em tempo real

---
**Dúvidas?** Consulte o `README.md` completo. 