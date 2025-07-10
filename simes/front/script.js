// Configuração da API
const API_BASE_URL = CONFIG.API_BASE_URL;

// Elementos do DOM
const aceitacaoForm = document.getElementById('aceitacaoForm');
const editForm = document.getElementById('editForm');
const aceitacoesTableBody = document.getElementById('aceitacoesTableBody');
const searchInput = document.getElementById('searchInput');
const statusFilter = document.getElementById('statusFilter');
const editModal = document.getElementById('editModal');
const criacaoModal = document.getElementById('criacaoModal');

// Elementos de estatísticas
const totalAceitacoesEl = document.getElementById('totalAceitacoes');
const aprovadasEl = document.getElementById('aprovadas');
const reprovadasEl = document.getElementById('reprovadas');
const pendentesEl = document.getElementById('pendentes');

// Estado global
let aceitacoes = [];
let aceitacaoEmEdicao = null;
let testesAceitacao = []; // Nova lista de testes de aceitação

// Inicialização
document.addEventListener('DOMContentLoaded', function () {
  carregarAceitacoes();
  carregarEstatisticas();
  configurarEventListeners();
  gerarExemplosAleatorios(); // Gerar exemplos na inicialização
});

// Configurar event listeners
function configurarEventListeners() {
  // Formulário de criação
  aceitacaoForm.addEventListener('submit', handleSubmit);

  // Formulário de edição
  editForm.addEventListener('submit', handleEditSubmit);

  // Event listener para atualizar status baseado na aprovação do cliente
  document.getElementById('clienteAprovou').addEventListener('change', atualizarStatusBaseadoAprovacao);
  document.getElementById('editClienteAprovou').addEventListener('change', atualizarStatusBaseadoAprovacaoEdicao);

  // Modal de criação
  const closeCriacaoModal = criacaoModal.querySelector('.close');
  
  closeCriacaoModal.addEventListener('click', fecharModalCriacao);
  window.addEventListener('click', function (event) {
    if (event.target === criacaoModal) {
      fecharModalCriacao();
    }
  });

  // Modal de edição
  const closeModal = editModal.querySelector('.close');
  
  closeModal.addEventListener('click', fecharModal);
  window.addEventListener('click', function (event) {
    if (event.target === editModal) {
      fecharModal();
    }
  });

  // Busca
  searchInput.addEventListener('input', debounce(filtrarAceitacoes, 300));
  statusFilter.addEventListener('change', filtrarAceitacoes);
}

// Função para atualizar status baseado na aprovação do cliente
function atualizarStatusBaseadoAprovacao() {
  const clienteAprovou = document.getElementById('clienteAprovou').value;
  const statusField = document.getElementById('statusTeste');
  const dataField = document.getElementById('dataAceite');
  
  if (clienteAprovou === 'true') {
    statusField.value = 'Em Aceite';
    dataField.disabled = false;
    dataField.style.backgroundColor = '';
  } else if (clienteAprovou === 'false') {
    statusField.value = 'Em Andamento';
    dataField.disabled = true;
    dataField.style.backgroundColor = '#f8f9fa';
    dataField.value = '';
  } else {
    statusField.value = '';
    dataField.disabled = true;
    dataField.style.backgroundColor = '#f8f9fa';
    dataField.value = '';
  }
}

// Função para atualizar status baseado na aprovação do cliente (modal de edição)
function atualizarStatusBaseadoAprovacaoEdicao() {
  const clienteAprovou = document.getElementById('editClienteAprovou').value;
  const statusField = document.getElementById('editStatusTeste');
  const dataField = document.getElementById('editDataAceite');
  
  if (clienteAprovou === 'true') {
    statusField.value = 'Em Aceite';
    dataField.disabled = false;
    dataField.style.backgroundColor = '';
  } else if (clienteAprovou === 'false') {
    statusField.value = 'Em Andamento';
    dataField.disabled = true;
    dataField.style.backgroundColor = '#f8f9fa';
    dataField.value = '';
  } else {
    statusField.value = '';
    dataField.disabled = true;
    dataField.style.backgroundColor = '#f8f9fa';
    dataField.value = '';
  }
}

// Função debounce para otimizar busca
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

// Carregar todas as aceitações
async function carregarAceitacoes() {
  try {
    mostrarLoading();
    const response = await fetch(API_BASE_URL);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    aceitacoes = await response.json();
    renderizarTabela(aceitacoes);
    atualizarEstatisticas(aceitacoes);
  } catch (error) {
    console.error('Erro ao carregar aceitações:', error);
    mostrarErro(CONFIG.ERROR_MESSAGES.NETWORK_ERROR);
  } finally {
    ocultarLoading();
  }
}

// Carregar estatísticas
async function carregarEstatisticas() {
  try {
    const response = await fetch(`${API_BASE_URL}/estatisticas`);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const stats = await response.json();
    atualizarEstatisticasUI(stats);
  } catch (error) {
    console.error('Erro ao carregar estatísticas:', error);
  }
}

// Atualizar estatísticas na UI
function atualizarEstatisticasUI(stats) {
  totalAceitacoesEl.textContent = stats.totalAceitacoes || 0;
  aprovadasEl.textContent = stats.aprovadas || 0;
  reprovadasEl.textContent = stats.reprovadas || 0;
  pendentesEl.textContent = stats.pendentes || 0;
}

// Atualizar estatísticas baseadas nos dados locais
function atualizarEstatisticas(dados) {
  const total = dados.length;
  const aprovadas = dados.filter(a => a.clienteAprovou === true).length;
  const reprovadas = dados.filter(a => a.clienteAprovou === false).length;
  const pendentes = total - aprovadas - reprovadas;

  totalAceitacoesEl.textContent = total;
  aprovadasEl.textContent = aprovadas;
  reprovadasEl.textContent = reprovadas;
  pendentesEl.textContent = pendentes;
}

// Renderizar tabela
function renderizarTabela(dados) {
  aceitacoesTableBody.innerHTML = '';

  if (dados.length === 0) {
    aceitacoesTableBody.innerHTML = `
            <tr>
                <td colspan="7" style="text-align: center; padding: 40px; color: #666;">
                    <i class="fas fa-inbox" style="font-size: 2rem; margin-bottom: 10px; display: block;"></i>
                    Nenhum teste de aceitação encontrado
                </td>
            </tr>
        `;
    return;
  }

  dados.forEach(aceitacao => {
    const row = document.createElement('tr');
    row.innerHTML = `
            <td>${aceitacao.idTesteSistema}</td>
            <td>${aceitacao.responsavelValidacao}</td>
            <td>${getStatusBadge(aceitacao.statusTeste)}</td>
            <td>${getAprovacaoBadge(aceitacao.clienteAprovou)}</td>
            <td>${aceitacao.feedbackCliente || '-'}</td>
            <td>${formatarData(aceitacao.dataAceite)}</td>
            <td>
                <div class="action-buttons">
                    <button class="action-btn btn-success" onclick="editarAceitacao(${aceitacao.idTesteAceitacao})" title="Editar">
                        <i class="fas fa-edit"></i>
                    </button>
                    <button class="action-btn btn-danger" onclick="deletarAceitacao(${aceitacao.idTesteAceitacao})" title="Excluir">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        `;
    aceitacoesTableBody.appendChild(row);
  });
}

// Obter badge de status
function getStatusBadge(status) {
  const statusMap = {
    1: { text: 'Pendente', class: 'status-1' },
    2: { text: 'Em Análise', class: 'status-2' },
    3: { text: 'Aprovado', class: 'status-3' },
    4: { text: 'Reprovado', class: 'status-4' }
  };

  const statusInfo = statusMap[status] || { text: 'Desconhecido', class: 'status-1' };
  return `<span class="status-badge ${statusInfo.class}">${statusInfo.text}</span>`;
}

// Obter badge de aprovação
function getAprovacaoBadge(aprovou) {
  if (aprovou === null || aprovou === undefined) {
    return '<span class="pending-badge">Pendente</span>';
  }
  return aprovou
    ? '<span class="approved-badge">Aprovado</span>'
    : '<span class="rejected-badge">Reprovado</span>';
}

// Formatar data
function formatarData(dataString) {
  if (!dataString) return '-';
  const data = new Date(dataString);
  return data.toLocaleDateString('pt-BR');
}

// Filtrar aceitações
function filtrarAceitacoes() {
  const termoBusca = searchInput.value.toLowerCase();
  const statusFiltro = statusFilter.value;

  let dadosFiltrados = aceitacoes;

  // Filtro por termo de busca (responsável e feedback)
  if (termoBusca) {
    dadosFiltrados = dadosFiltrados.filter(aceitacao =>
      aceitacao.responsavelValidacao.toLowerCase().includes(termoBusca) ||
      (aceitacao.feedbackCliente && aceitacao.feedbackCliente.toLowerCase().includes(termoBusca))
    );
  }

  // Filtro por status
  if (statusFiltro) {
    dadosFiltrados = dadosFiltrados.filter(aceitacao =>
      aceitacao.statusTeste === parseInt(statusFiltro)
    );
  }

  renderizarTabela(dadosFiltrados);
}

// Handle submit do formulário
async function handleSubmit(event) {
  event.preventDefault();

  const formData = new FormData(aceitacaoForm);
  
  // Debug: mostrar todos os campos do FormData
  console.log('Todos os campos do FormData:');
  for (let [key, value] of formData.entries()) {
    console.log(`${key}: "${value}"`);
  }
  
  // Validar campos obrigatórios
  const idTesteSistema = formData.get('idTesteSistema');
  const responsavelValidacao = formData.get('responsavelValidacao');
  const statusTeste = formData.get('statusTeste');
  const clienteAprovou = formData.get('clienteAprovou');
  const dataAceite = formData.get('dataAceite');
  const feedbackCliente = formData.get('feedbackCliente');

  // Debug: mostrar valores dos campos
  console.log('Valores dos campos:', {
    idTesteSistema,
    responsavelValidacao,
    statusTeste,
    clienteAprovou,
    dataAceite,
    feedbackCliente
  });

  // Validar cada campo individualmente
  const camposVazios = [];
  
  if (!idTesteSistema || idTesteSistema.trim() === '') {
    camposVazios.push('ID do Teste do Sistema');
  }
  
  if (!responsavelValidacao || responsavelValidacao.trim() === '') {
    camposVazios.push('Responsável pela Validação');
  }
  
  if (!statusTeste || statusTeste === '') {
    camposVazios.push('Status');
  }
  
  if (!clienteAprovou || clienteAprovou === '') {
    camposVazios.push('Cliente Aprovou');
  }
  
  // if (!dataAceite || dataAceite === '') {
  //   camposVazios.push('Data de Aceite');
  // }
  
  if (!feedbackCliente || feedbackCliente.trim() === '') {
    camposVazios.push('Feedback do Cliente');
  }

  if (camposVazios.length > 0) {
    console.log('Campos vazios:', camposVazios);
    mostrarErro(`Campos obrigatórios não preenchidos: ${camposVazios.join(', ')}`);
    return;
  }

  // Mapear o status para o valor correto do backend
  let statusTesteValue;
  if (statusTeste === 'Em Aceite') {
    statusTesteValue = '3'; // Aprovado
  } else if (statusTeste === 'Em Andamento') {
    statusTesteValue = '2'; // Em Análise
  } else {
    statusTesteValue = '1'; // Pendente (padrão)
  }

  const novaAceitacao = {
    idTesteSistema: parseInt(idTesteSistema),
    responsavelValidacao: responsavelValidacao.trim(),
    statusTeste: statusTesteValue,
    clienteAprovou: clienteAprovou === 'true',
    dataAceite: dataAceite,
    feedbackCliente: feedbackCliente.trim()
  };

  try {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(novaAceitacao)
    });

    if (!response.ok) {
      const errorData = await response.json();
      if (errorData && typeof errorData === 'object') {
        // Erro de validação com detalhes
        const errorMessages = Object.values(errorData).join(', ');
        throw new Error(`Erro de validação: ${errorMessages}`);
      } else {
      throw new Error(errorData.message || 'Erro ao criar aceitação');
      }
    }

    const novaAceitacaoCriada = await response.json();
    mostrarSucesso(CONFIG.SUCCESS_MESSAGES.CREATED);
    fecharModalCriacao();
    carregarAceitacoes();
    carregarEstatisticas();
  } catch (error) {
    console.error('Erro ao criar aceitação:', error);
    mostrarErro(error.message);
  }
}

// Editar aceitação
async function editarAceitacao(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`);

    if (!response.ok) {
      throw new Error('Erro ao carregar dados para edição');
    }

    aceitacaoEmEdicao = await response.json();
    preencherFormularioEdicao(aceitacaoEmEdicao);
    abrirModal();
  } catch (error) {
    console.error('Erro ao carregar aceitação para edição:', error);
    mostrarErro('Erro ao carregar dados para edição');
  }
}

// Preencher formulário de edição
function preencherFormularioEdicao(aceitacao) {
  document.getElementById('editId').value = aceitacao.idTesteAceitacao;
  document.getElementById('editIdTesteSistema').value = aceitacao.idTesteSistema;
  document.getElementById('editResponsavelValidacao').value = aceitacao.responsavelValidacao;
  document.getElementById('editClienteAprovou').value = aceitacao.clienteAprovou === null ? '' : aceitacao.clienteAprovou.toString();
  document.getElementById('editFeedbackCliente').value = aceitacao.feedbackCliente || '';
  document.getElementById('editDataAceite').value = aceitacao.dataAceite || null;
  
  // Configurar o status baseado na aprovação do cliente
  if (aceitacao.clienteAprovou === true) {
    document.getElementById('editStatusTeste').value = 'Em Aceite';
    document.getElementById('editDataAceite').disabled = false;
    document.getElementById('editDataAceite').style.backgroundColor = '';
  } else if (aceitacao.clienteAprovou === false) {
    document.getElementById('editStatusTeste').value = 'Em Andamento';
    document.getElementById('editDataAceite').disabled = true;
    document.getElementById('editDataAceite').style.backgroundColor = '#f8f9fa';
    document.getElementById('editDataAceite').value = '';
  } else {
    document.getElementById('editStatusTeste').value = '';
    document.getElementById('editDataAceite').disabled = true;
    document.getElementById('editDataAceite').style.backgroundColor = '#f8f9fa';
    document.getElementById('editDataAceite').value = '';
  }
  
  // Verificar se os campos foram preenchidos
  console.log('Campos preenchidos:', {
    editId: document.getElementById('editId').value,
    editIdTesteSistema: document.getElementById('editIdTesteSistema').value,
    editResponsavelValidacao: document.getElementById('editResponsavelValidacao').value,
    editClienteAprovou: document.getElementById('editClienteAprovou').value,
    editStatusTeste: document.getElementById('editStatusTeste').value,
    editFeedbackCliente: document.getElementById('editFeedbackCliente').value,
    editDataAceite: document.getElementById('editDataAceite').value
  });
}

// Handle submit do formulário de edição
async function handleEditSubmit(event) {
  event.preventDefault();

  const formData = new FormData(editForm);
  
  // Debug: mostrar todos os campos do FormData
  console.log('Todos os campos do FormData (edição):');
  for (let [key, value] of formData.entries()) {
    console.log(`${key}: "${value}"`);
  }
  
  // Validar campos obrigatórios
  const idTesteSistema = formData.get('idTesteSistema');
  const responsavelValidacao = formData.get('responsavelValidacao');
  const statusTeste = formData.get('editStatusTeste');
  const clienteAprovou = formData.get('editClienteAprovou');
  const dataAceite = formData.get('editDataAceite');
  const feedbackCliente = formData.get('editFeedbackCliente');

  // Debug: mostrar valores dos campos
  console.log('Valores dos campos (edição):', {
    idTesteSistema,
    responsavelValidacao,
    statusTeste,
    clienteAprovou,
    dataAceite,
    feedbackCliente
  });

  // Validar cada campo individualmente
  const camposVazios = [];
  
  if (!idTesteSistema || idTesteSistema.trim() === '') {
    camposVazios.push('ID do Teste do Sistema');
  }
  
  if (!responsavelValidacao || responsavelValidacao.trim() === '') {
    camposVazios.push('Responsável pela Validação');
  }
  
  if (!statusTeste || statusTeste === '') {
    camposVazios.push('Status');
  }
  
  if (!clienteAprovou || clienteAprovou === '') {
    camposVazios.push('Cliente Aprovou');
  }
  
  // if (!dataAceite || dataAceite === '') {
  //   camposVazios.push('Data de Aceite');
  // }
  
  if (!feedbackCliente || feedbackCliente.trim() === '') {
    camposVazios.push('Feedback do Cliente');
  }

  if (camposVazios.length > 0) {
    console.log('Campos vazios (edição):', camposVazios);
    mostrarErro(`Campos obrigatórios não preenchidos: ${camposVazios.join(', ')}`);
    return;
  }

  // Mapear o status para o valor correto do backend
  let statusTesteValue;
  if (statusTeste === 'Em Aceite') {
    statusTesteValue = '3'; // Aprovado
  } else if (statusTeste === 'Em Andamento') {
    statusTesteValue = '2'; // Em Análise
  } else {
    statusTesteValue = '1'; // Pendente (padrão)
  }

  const aceitacaoEditada = {
    idTesteSistema: parseInt(idTesteSistema),
    responsavelValidacao: responsavelValidacao.trim(),
    statusTeste: statusTesteValue,
    clienteAprovou: clienteAprovou === 'true',
    dataAceite: dataAceite,
    feedbackCliente: feedbackCliente.trim()
  };

  try {
    const response = await fetch(`${API_BASE_URL}/${aceitacaoEmEdicao.idTesteAceitacao}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(aceitacaoEditada)
    });

    if (!response.ok) {
      const errorData = await response.json();
      if (errorData && typeof errorData === 'object') {
        // Erro de validação com detalhes
        const errorMessages = Object.values(errorData).join(', ');
        throw new Error(`Erro de validação: ${errorMessages}`);
      } else {
      throw new Error(errorData.message || 'Erro ao atualizar aceitação');
      }
    }

    mostrarSucesso(CONFIG.SUCCESS_MESSAGES.UPDATED);
    fecharModal();
    carregarAceitacoes();
    carregarEstatisticas();
  } catch (error) {
    console.error('Erro ao atualizar aceitação:', error);
    mostrarErro(error.message);
  }
}

// Deletar aceitação
async function deletarAceitacao(id) {
  if (!confirm('Tem certeza que deseja excluir este teste de aceitação?')) {
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      throw new Error('Erro ao deletar aceitação');
    }

            mostrarSucesso(CONFIG.SUCCESS_MESSAGES.DELETED);
    carregarAceitacoes();
    carregarEstatisticas();
  } catch (error) {
    console.error('Erro ao deletar aceitação:', error);
    mostrarErro('Erro ao excluir teste de aceitação');
  }
}

// Abrir modal de criação
function abrirModalCriacao() {
  criacaoModal.style.display = 'block';
  document.body.style.overflow = 'hidden';
  
  // Configurar data atual como padrão
  const hoje = new Date().toISOString().split('T')[0];
  document.getElementById('dataAceite').value = hoje;
  
  // Limpar formulário
  aceitacaoForm.reset();
  document.getElementById('dataAceite').value = hoje;
  // Garantir que o campo ID do Teste do Sistema não seja somente leitura
  document.getElementById('idTesteSistema').readOnly = false;
  document.getElementById('idTesteSistema').style.backgroundColor = '';
  // Limpar o campo de status
  document.getElementById('statusTeste').value = '';
  // Desabilitar campo de data inicialmente
  document.getElementById('dataAceite').disabled = true;
  document.getElementById('dataAceite').style.backgroundColor = '#f8f9fa';
}

// Fechar modal de criação
function fecharModalCriacao() {
  criacaoModal.style.display = 'none';
  document.body.style.overflow = 'auto';
  aceitacaoForm.reset();
  // Resetar o campo ID do Teste do Sistema
  document.getElementById('idTesteSistema').readOnly = false;
  document.getElementById('idTesteSistema').style.backgroundColor = '';
}

// Abrir modal de edição
function abrirModal() {
  editModal.style.display = 'block';
  document.body.style.overflow = 'hidden';
}

// Fechar modal de edição
function fecharModal() {
  editModal.style.display = 'none';
  document.body.style.overflow = 'auto';
  aceitacaoEmEdicao = null;
  editForm.reset();
}

// Limpar formulário
function limparFormulario() {
  aceitacaoForm.reset();
  // Configurar data atual como padrão
  const hoje = new Date().toISOString().split('T')[0];
  document.getElementById('dataAceite').value = hoje;
  // Resetar o campo ID do Teste do Sistema
  document.getElementById('idTesteSistema').readOnly = false;
  document.getElementById('idTesteSistema').style.backgroundColor = '';
  // Limpar o campo de status
  document.getElementById('statusTeste').value = '';
  // Desabilitar campo de data
  document.getElementById('dataAceite').disabled = true;
  document.getElementById('dataAceite').style.backgroundColor = '#f8f9fa';
}

// Mostrar loading
function mostrarLoading() {
  aceitacoesTableBody.innerHTML = `
        <tr>
            <td colspan="7" class="loading">
                <i class="fas fa-spinner"></i>
                <p>Carregando dados...</p>
            </td>
        </tr>
    `;
}

// Ocultar loading
function ocultarLoading() {
  // Loading é removido quando a tabela é renderizada
}

// Mostrar mensagem de sucesso
function mostrarSucesso(mensagem) {
  // Implementar toast ou notificação
  alert(mensagem);
}

// Mostrar mensagem de erro
function mostrarErro(mensagem) {
  // Implementar toast ou notificação
  alert('Erro: ' + mensagem);
}

// Funções globais para uso no HTML
window.editarAceitacao = editarAceitacao;
window.deletarAceitacao = deletarAceitacao;
window.carregarAceitacoes = carregarAceitacoes;
window.limparFormulario = limparFormulario;
window.fecharModal = fecharModal;
window.abrirModalCriacao = abrirModalCriacao;
window.fecharModalCriacao = fecharModalCriacao;
window.gerarExemplosAleatorios = gerarExemplosAleatorios;
window.criarAceitacaoParaSistema = criarAceitacaoParaSistema;

// Função para criar aceitação para um sistema específico
function criarAceitacaoParaSistema(idSistema) {
  // Abrir o modal de criação
  criacaoModal.style.display = 'block';
  document.body.style.overflow = 'hidden';
  
  // Limpar formulário primeiro
  aceitacaoForm.reset();
  
  // Preencher o campo ID do Teste do Sistema automaticamente
  document.getElementById('idTesteSistema').value = idSistema;
  document.getElementById('idTesteSistema').readOnly = true; // Tornar o campo somente leitura
  document.getElementById('idTesteSistema').style.backgroundColor = '#f8f9fa'; // Visual indicativo de campo não editável
  
  // Configurar data atual como padrão e desabilitar inicialmente
  const hoje = new Date().toISOString().split('T')[0];
  document.getElementById('dataAceite').value = hoje;
  document.getElementById('dataAceite').disabled = true;
  document.getElementById('dataAceite').style.backgroundColor = '#f8f9fa';
  
  // Limpar o campo de status inicialmente
  document.getElementById('statusTeste').value = '';
}

// Funções para a nova lista de testes de aceitação
function gerarExemplosAleatorios() {
  const exemplos = [];
  
  for (let i = 0; i < 3; i++) {
    exemplos.push(gerarTesteAleatorio());
  }
  
  testesAceitacao = exemplos;
  renderizarTabelaTestesAceitacao();
}

function gerarTesteAleatorio() {
  const idSistema = Math.floor(Math.random() * 1000) + 1;
  const resultados = ['Sucesso', 'Falha', 'Parcial'];
  const detalhes = [
    'Teste de funcionalidade de login realizado com sucesso',
    'Falha na validação de formulário de cadastro',
    'Teste de integração com API externa parcialmente aprovado',
    'Verificação de responsividade em dispositivos móveis',
    'Teste de performance da aplicação',
    'Validação de regras de negócio complexas',
    'Teste de segurança e autenticação',
    'Verificação de compatibilidade com diferentes navegadores',
    'Teste de upload de arquivos e validação',
    'Análise de usabilidade da interface do usuário'
  ];
  
  // Gerar data aleatória nos últimos 30 dias
  const dataAleatoria = new Date();
  dataAleatoria.setDate(dataAleatoria.getDate() - Math.floor(Math.random() * 30));
  
  // Escolher resultado aleatório
  const resultado = resultados[Math.floor(Math.random() * resultados.length)];
  
  // Definir status baseado no resultado
  let status;
  if (resultado === 'Sucesso') {
    status = 'Concluído';
  } else {
    // Para 'Falha' e 'Parcial', status é 'Em Andamento'
    status = 'Em Andamento';
  }
  
  return {
    idSistema: idSistema,
    resultado: resultado,
    detalhes: detalhes[Math.floor(Math.random() * detalhes.length)],
    dataTeste: dataAleatoria.toISOString().split('T')[0],
    status: status
  };
}

function renderizarTabelaTestesAceitacao() {
  const tbody = document.getElementById('testesAceitacaoTableBody');
  
  if (!tbody) return;
  
  tbody.innerHTML = '';

  if (testesAceitacao.length === 0) {
    tbody.innerHTML = `
      <tr>
        <td colspan="6" style="text-align: center; padding: 40px; color: #666;">
          <i class="fas fa-inbox" style="font-size: 2rem; margin-bottom: 10px; display: block;"></i>
          Nenhum teste de aceitação encontrado
        </td>
      </tr>
    `;
    return;
  }

  testesAceitacao.forEach(teste => {
    const row = document.createElement('tr');
    
    // Verificar se o status é "Concluído" para mostrar o botão
    const botaoAcao = teste.status === 'Concluído' 
      ? `<div class="action-buttons">
          <button class="action-btn btn-success" onclick="criarAceitacaoParaSistema(${teste.idSistema})" title="Criar Aceitação">
            <i class="fas fa-plus"></i>
          </button>
        </div>`
      : `<div class="action-buttons">
          <span class="text-muted" style="font-size: 0.8rem; color: #6c757d;">Aguardando conclusão</span>
        </div>`;
    
    row.innerHTML = `
      <td>${teste.idSistema}</td>
      <td>${getResultadoBadge(teste.resultado)}</td>
      <td>${teste.detalhes}</td>
      <td>${formatarData(teste.dataTeste)}</td>
      <td>${getStatusTesteBadge(teste.status)}</td>
      <td>${botaoAcao}</td>
    `;
    tbody.appendChild(row);
  });
}

function getResultadoBadge(resultado) {
  const badgeMap = {
    'Sucesso': { text: 'Sucesso', class: 'resultado-sucesso' },
    'Falha': { text: 'Falha', class: 'resultado-falha' },
    'Parcial': { text: 'Parcial', class: 'resultado-parcial' }
  };

  const badgeInfo = badgeMap[resultado] || { text: resultado, class: 'resultado-parcial' };
  return `<span class="resultado-badge ${badgeInfo.class}">${badgeInfo.text}</span>`;
}

function getStatusTesteBadge(status) {
  const badgeMap = {
    'Em Andamento': { text: 'Em Andamento', class: 'status-andamento' },
    'Concluído': { text: 'Concluído', class: 'status-concluido' }
  };

  const badgeInfo = badgeMap[status] || { text: status, class: 'status-andamento' };
  return `<span class="status-teste-badge ${badgeInfo.class}">${badgeInfo.text}</span>`;
}
