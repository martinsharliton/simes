// Configuração do Frontend SIMES
const CONFIG = {
     // URL da API
     API_BASE_URL: 'http://localhost:8080/api/teste-aceitacao',

     // Configurações da aplicação
     APP_NAME: 'SIMES - Sistema Integrado de Manutenção e Evolução de Software',
     APP_VERSION: '1.0.0',

     // Configurações de timeout
     REQUEST_TIMEOUT: 10000, // 10 segundos

     // Configurações de paginação
     ITEMS_PER_PAGE: 10,

     // Configurações de busca
     SEARCH_DELAY: 300, // 300ms para debounce

     // Status dos testes
     STATUS: {
          PENDENTE: 1,
          EM_ANALISE: 2,
          APROVADO: 3,
          REPROVADO: 4
     },

     // Mapeamento de status para texto
     STATUS_LABELS: {
          1: 'Pendente',
          2: 'Em Análise',
          3: 'Aprovado',
          4: 'Reprovado'
     },

     // Cores dos status
     STATUS_COLORS: {
          1: '#ffc107', // Amarelo
          2: '#17a2b8', // Azul
          3: '#28a745', // Verde
          4: '#dc3545'  // Vermelho
     },

     // Mensagens de erro
     ERROR_MESSAGES: {
          NETWORK_ERROR: 'Erro de conexão. Verifique se o servidor está rodando.',
          VALIDATION_ERROR: 'Dados inválidos. Verifique os campos obrigatórios.',
          SERVER_ERROR: 'Erro no servidor. Tente novamente mais tarde.',
          NOT_FOUND: 'Recurso não encontrado.',
          UNAUTHORIZED: 'Acesso não autorizado.',
          FORBIDDEN: 'Acesso negado.'
     },

     // Mensagens de sucesso
     SUCCESS_MESSAGES: {
          CREATED: 'Teste de aceitação criado com sucesso!',
          UPDATED: 'Teste de aceitação atualizado com sucesso!',
          DELETED: 'Teste de aceitação excluído com sucesso!',
          LOADED: 'Dados carregados com sucesso!'
     }
};

// Exportar configuração para uso global
if (typeof module !== 'undefined' && module.exports) {
     module.exports = CONFIG;
} else {
     window.CONFIG = CONFIG;
} 