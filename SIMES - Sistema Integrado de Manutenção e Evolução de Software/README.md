# SIMES - Sistema Integrado de Manutenção e Evolução de Software

## 📋 Descrição

O SIMES é um sistema completo para gerenciamento de testes de aceitação, desenvolvido com Spring Boot (Backend) e HTML/CSS/JavaScript (Frontend). O sistema permite criar, visualizar, editar e excluir testes de aceitação, além de fornecer estatísticas em tempo real.

## 🏗️ Arquitetura

### Backend (Spring Boot)
- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **MySQL Database**
- **ModelMapper** para mapeamento de objetos
- **Validação** com Bean Validation

### Frontend
- **HTML5** com design responsivo
- **CSS3** com gradientes e animações modernas
- **JavaScript ES6+** com async/await
- **Font Awesome** para ícones
- **Design System** próprio com componentes reutilizáveis

## 🚀 Funcionalidades

### ✅ Gestão de Testes de Aceitação
- **Criar** novos testes de aceitação
- **Listar** todos os testes com paginação
- **Editar** testes existentes via modal
- **Excluir** testes com confirmação
- **Buscar** por responsável
- **Filtrar** por status

### 📊 Dashboard com Estatísticas
- Total de testes de aceitação
- Testes aprovados pelo cliente
- Testes reprovados pelo cliente
- Testes pendentes de aprovação
- Taxa de aprovação em tempo real

### 🔍 Recursos Avançados
- **Validação** de dados no frontend e backend
- **Responsividade** para dispositivos móveis
- **Feedback visual** para todas as ações
- **Tratamento de erros** robusto
- **Interface moderna** com animações

## 📦 Instalação e Configuração

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+ (opcional, o projeto usa Maven Wrapper)
- Navegador web moderno
- **MySQL 8.0+** (opcional, o projeto usa H2 por padrão)

### 1. Configuração do Banco de Dados

O projeto está configurado para usar **H2 Database** (banco em memória) por padrão, que é mais simples para desenvolvimento.

**Para usar MySQL (opcional):**
1. Instale e configure o MySQL
2. Altere o arquivo `application.properties` para usar MySQL
3. Execute o script `init.sql` para criar a tabela

**H2 Console:** Acesse `http://localhost:8080/h2-console` para visualizar o banco
- JDBC URL: `jdbc:h2:mem:simes_db`
- Username: `sa`
- Password: (deixe em branco)

### 2. Configuração do Backend

1. **Navegue para a pasta do servidor:**
   ```bash
   cd server
   ```

2. **O banco H2 já está configurado** no arquivo `src/main/resources/application.properties`
   
   **Para usar MySQL, altere as configurações:**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/simes_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=sua_senha
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

3. **Execute o projeto:**
   ```bash
   # Windows
   mvnw.cmd spring-boot:run
   
   # Linux/Mac
   ./mvnw spring-boot:run
   ```

4. **Acesse a API:**
   - URL: `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - Health Check: `http://localhost:8080/actuator/health`

### 3. Configuração do Frontend

1. **Abra o arquivo `front/index.html`** em um navegador web
2. **Ou configure um servidor local:**
   ```bash
   cd front
   python -m http.server 8000
   # Acesse: http://localhost:8000
   ```

## 🔌 Endpoints da API

### Testes de Aceitação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/aceitacao` | Listar todos os testes |
| `GET` | `/api/aceitacao/{id}` | Buscar por ID |
| `GET` | `/api/aceitacao/teste-sistema/{id}` | Buscar por ID do teste do sistema |
| `GET` | `/api/aceitacao/cliente-aprovou/{boolean}` | Filtrar por aprovação do cliente |
| `GET` | `/api/aceitacao/periodo?dataInicio=X&dataFim=Y` | Filtrar por período |
| `GET` | `/api/aceitacao/responsavel/{nome}` | Buscar por responsável |
| `GET` | `/api/aceitacao/status/{status}` | Filtrar por status |
| `GET` | `/api/aceitacao/estatisticas` | Obter estatísticas |
| `POST` | `/api/aceitacao` | Criar novo teste |
| `PUT` | `/api/aceitacao/{id}` | Atualizar teste |
| `DELETE` | `/api/aceitacao/{id}` | Excluir teste |

### Exemplo de Payload para Criação

```json
{
  "idTesteSistema": 123,
  "responsavelValidacao": "João Silva",
  "clienteAprovou": true,
  "statusAceite": 3,
  "feedbackCliente": "Teste aprovado com sucesso!",
  "dataAceite": "2024-01-15"
}
```

## 🗄️ Estrutura do Banco de Dados

### Tabela: `teste_aceitacao`

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id_teste_aceitacao` | BIGINT | Chave primária (auto-incremento) |
| `id_teste_sistema` | INT | ID do teste do sistema |
| `cliente_aprovou` | BOOLEAN | Se o cliente aprovou (true/false/null) |
| `feedback_cliente` | VARCHAR(200) | Feedback do cliente |
| `data_aceite` | DATE | Data de aceite |
| `responsavel_validacao` | VARCHAR(50) | Nome do responsável |
| `status_teste` | INT | Status do teste (1-4) |

### Status dos Testes
- `1` - Pendente
- `2` - Em Análise
- `3` - Aprovado
- `4` - Reprovado

## 🎨 Interface do Usuário

### Características do Design
- **Design System** consistente
- **Cores** baseadas em gradientes modernos
- **Tipografia** clara e legível
- **Ícones** Font Awesome para melhor UX
- **Animações** suaves e responsivas
- **Layout** adaptativo para mobile

### Componentes Principais
- **Dashboard** com cards de estatísticas
- **Formulário** de criação com validação
- **Tabela** responsiva com ações
- **Modal** de edição
- **Filtros** e busca em tempo real

## 🔧 Tecnologias Utilizadas

### Backend
- **Spring Boot 3.5.0** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Validation** - Validação de dados
- **H2 Database** - Banco em memória (padrão)
- **MySQL Connector** - Driver do MySQL (opcional)
- **ModelMapper** - Mapeamento de objetos
- **Lombok** - Redução de boilerplate

### Frontend
- **HTML5** - Estrutura semântica
- **CSS3** - Estilização moderna
- **JavaScript ES6+** - Lógica da aplicação
- **Font Awesome** - Biblioteca de ícones
- **Fetch API** - Comunicação com backend

## 🚀 Como Usar

### 1. Criar um Teste de Aceitação
1. Preencha o formulário no lado esquerdo
2. Clique em "Salvar"
3. O teste será criado e aparecerá na lista

### 2. Editar um Teste
1. Clique no ícone de editar (lápis) na tabela
2. Modifique os dados no modal
3. Clique em "Atualizar"

### 3. Excluir um Teste
1. Clique no ícone de excluir (lixeira) na tabela
2. Confirme a exclusão
3. O teste será removido

### 4. Filtrar e Buscar
1. Use o campo de busca para encontrar por responsável
2. Use o filtro de status para filtrar por estado
3. Os resultados são atualizados em tempo real

## 🐛 Solução de Problemas

### Erro de Conexão com Banco
- O projeto usa H2 por padrão, não precisa de MySQL
- Se usar MySQL, verifique se está rodando
- Confirme as credenciais no `application.properties`
- Certifique-se de que o banco `simes_db` existe

### Erro de Concorrência (Row was updated or deleted)
- ✅ **Corrigido**: Removido controle de versão temporariamente
- Reinicie a aplicação se o erro persistir

### Erro de CORS
- O backend já está configurado com `@CrossOrigin(origins = "*")`
- Se persistir, verifique se o frontend está acessando a URL correta

### Erro de Validação
- Verifique se todos os campos obrigatórios estão preenchidos
- Confirme se os dados estão no formato correto

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👥 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para suporte, envie um email para [seu-email@exemplo.com] ou abra uma issue no repositório.

---

**Desenvolvido com ❤️ para o SIMES - Sistema Integrado de Manutenção e Evolução de Software** 