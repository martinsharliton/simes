// Configuração da API
const API_BASE_URL = CONFIG.API_BASE_URL;

// Elementos do DOM
const aceitacaoForm = document.getElementById('aceitacaoForm');
const editForm = document.getElementById('editForm');
const aceitacoesTableBody = document.getElementById('aceitacoesTableBody');
const searchInput = document.getElementById('searchInput');
const statusFilter = document.getElementById('statusFilter');
const editModal = document.getElementById('editModal');
const closeModal = document.querySelector('.close');

// Elementos de estatísticas
const totalAceitacoesEl = document.getElementById('totalAceitacoes');
const aprovadasEl = document.getElementById('aprovadas');
const reprovadasEl = document.getElementById('reprovadas');
const pendentesEl = document.getElementById('pendentes');

// Estado global
let aceitacoes = [];
let aceitacaoEmEdicao = null;

// Inicialização
document.addEventListener('DOMContentLoaded', function () {
  carregarAceitacoes();
  carregarEstatisticas();
  configurarEventListeners();
});

// Configurar event listeners
function configurarEventListeners() {
  // Formulário de criação
  aceitacaoForm.addEventListener('submit', handleSubmit);

  // Formulário de edição
  editForm.addEventListener('submit', handleEditSubmit);

  // Modal
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
            <td>${aceitacao.idTesteAceitacao}</td>
            <td>${aceitacao.idTesteSistema}</td>
            <td>${aceitacao.responsavelValidacao}</td>
            <td>${getStatusBadge(aceitacao.statusAceite)}</td>
            <td>${getAprovacaoBadge(aceitacao.clienteAprovou)}</td>
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

  // Filtro por termo de busca (responsável)
  if (termoBusca) {
    dadosFiltrados = dadosFiltrados.filter(aceitacao =>
      aceitacao.responsavelValidacao.toLowerCase().includes(termoBusca)
    );
  }

  // Filtro por status
  if (statusFiltro) {
    dadosFiltrados = dadosFiltrados.filter(aceitacao =>
      aceitacao.statusAceite === parseInt(statusFiltro)
    );
  }

  renderizarTabela(dadosFiltrados);
}

// Handle submit do formulário
async function handleSubmit(event) {
  event.preventDefault();

  const formData = new FormData(aceitacaoForm);
  const dados = {
    idTesteSistema: parseInt(formData.get('idTesteSistema')),
    responsavelValidacao: formData.get('responsavelValidacao'),
    clienteAprovou: formData.get('clienteAprovou') ? formData.get('clienteAprovou') === 'true' : null,
    statusAceite: parseInt(formData.get('statusAceite')),
    feedbackCliente: formData.get('feedbackCliente') || null,
    dataAceite: formData.get('dataAceite') || null
  };

  try {
    const response = await fetch(API_BASE_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(dados)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Erro ao criar aceitação');
    }

    const novaAceitacao = await response.json();
    mostrarSucesso(CONFIG.SUCCESS_MESSAGES.CREATED);
    aceitacaoForm.reset();
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
  document.getElementById('editStatusAceite').value = aceitacao.statusAceite;
  document.getElementById('editFeedbackCliente').value = aceitacao.feedbackCliente || '';
  document.getElementById('editDataAceite').value = aceitacao.dataAceite || '';
}

// Handle submit do formulário de edição
async function handleEditSubmit(event) {
  event.preventDefault();

  const formData = new FormData(editForm);
  const dados = {
    idTesteSistema: parseInt(formData.get('idTesteSistema')),
    responsavelValidacao: formData.get('responsavelValidacao'),
    clienteAprovou: formData.get('clienteAprovou') ? formData.get('clienteAprovou') === 'true' : null,
    statusAceite: parseInt(formData.get('statusAceite')),
    feedbackCliente: formData.get('feedbackCliente') || null,
    dataAceite: formData.get('dataAceite') || null
  };

  try {
    const response = await fetch(`${API_BASE_URL}/${aceitacaoEmEdicao.idTesteAceitacao}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(dados)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Erro ao atualizar aceitação');
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

// Abrir modal
function abrirModal() {
  editModal.style.display = 'block';
  document.body.style.overflow = 'hidden';
}

// Fechar modal
function fecharModal() {
  editModal.style.display = 'none';
  document.body.style.overflow = 'auto';
  aceitacaoEmEdicao = null;
  editForm.reset();
}

// Limpar formulário
function limparFormulario() {
  aceitacaoForm.reset();
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
