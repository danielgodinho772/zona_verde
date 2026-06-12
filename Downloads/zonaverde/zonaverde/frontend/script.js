const API = 'http://localhost:8094/api';

function showPage(name) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    document.querySelectorAll('nav button').forEach(b => b.classList.remove('active'));
    document.getElementById('page-' + name).classList.add('active');
    event.currentTarget.classList.add('active');

    const loaders = { dashboard: loadDashboard, espacos: carregarEspacos, eventos: carregarEventos, reportes: carregarReportes, usuarios: carregarUsuarios };
    if (loaders[name]) loaders[name]();
}

function toast(msg, ok = true) {
    const t = document.getElementById('toast');
    t.textContent = msg;
    t.style.background = ok ? '#2d6a4f' : '#e63946';
    t.classList.add('show');
    setTimeout(() => t.classList.remove('show'), 3000);
}

const statusBadge = s => {
    const map = { 'Proposto': 'badge-azul', 'Em Analise': 'badge-amarelo', 'Aprovado': 'badge-verde', 'Em Andamento': 'badge-verde', 'Concluido': 'badge-cinza', 'Cancelado': 'badge-vermelho' };
    return `<span class="badge ${map[s] || 'badge-cinza'}">${s}</span>`;
};

const tipoBadge = t => {
    const map = { ILUMINACAO: '💡 Iluminação', LIXO: '🗑️ Lixo', EQUIPAMENTO: '🔧 Equipamento', OUTRO: '📌 Outro' };
    return map[t] || t;
};

const tipoEspaco = t => ({ PRACA: '🌳 Praça', QUADRA: '⚽ Quadra', PARQUE: '🏞️ Parque', CENTRO_COMUNITARIO: '🏛️ C. Comunitário' }[t] || t);

function fmtDate(str) {
    if (!str) return '–';
    return new Date(str).toLocaleString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
}

async function loadDashboard() {
    try {
        const [esp, ev, rep, usr] = await Promise.all([
            fetch(`${API}/espacos`).then(r => r.json()),
            fetch(`${API}/eventos`).then(r => r.json()),
            fetch(`${API}/reportes/prioridade`).then(r => r.json()),
            fetch(`${API}/usuarios`).then(r => r.json()),
        ]);

        document.getElementById('dash-espacos').textContent = esp.length;
        document.getElementById('dash-eventos').textContent = ev.length;
        document.getElementById('dash-reportes').textContent = rep.length;
        document.getElementById('dash-usuarios').textContent = usr.length;

        const rt = document.getElementById('dash-reportes-table');
        rt.innerHTML = rep.length === 0
            ? '<tr><td colspan="4" class="loading">Nenhum reporte registrado.</td></tr>'
            : rep.slice(0, 5).map(r => `<tr>
          <td>${r.descricao}</td>
          <td>${tipoBadge(r.tipo)}</td>
          <td>${r.confirmacoes}</td>
          <td><strong style="color:var(--vermelho)">${r.prioridade}</strong></td>
        </tr>`).join('');

        const et = document.getElementById('dash-eventos-table');
        et.innerHTML = ev.length === 0
            ? '<tr><td colspan="3" class="loading">Nenhum evento registrado.</td></tr>'
            : ev.slice(0, 5).map(e => `<tr>
          <td>${e.nome}</td>
          <td>${statusBadge(e.statusEvento)}</td>
          <td>${fmtDate(e.dataHoraInicio)}</td>
        </tr>`).join('');
    } catch(e) {
        toast('Erro ao conectar na API. Verifique se o back-end está rodando na porta 8094.', false);
    }
}

async function carregarEspacos() {
    const tb = document.getElementById('espacos-table');
    tb.innerHTML = '<tr><td colspan="4" class="loading">Carregando...</td></tr>';
    try {
        const data = await fetch(`${API}/espacos`).then(r => r.json());
        tb.innerHTML = data.length === 0
            ? '<tr><td colspan="4" class="loading">Nenhum espaço cadastrado.</td></tr>'
            : data.map(e => `<tr>
          <td><strong>${e.nome}</strong></td>
          <td>${tipoEspaco(e.tipo)}</td>
          <td>${e.endereco || '–'}</td>
          <td>${e.capacidadeEstimada ?? '–'}</td>
        </tr>`).join('');
    } catch { tb.innerHTML = '<tr><td colspan="4" class="loading">Erro ao carregar.</td></tr>'; }
}

async function criarEspaco() {
    const body = {
        nome: document.getElementById('esp-nome').value,
        endereco: document.getElementById('esp-end').value,
        tipo: document.getElementById('esp-tipo').value,
        capacidadeEstimada: parseInt(document.getElementById('esp-cap').value) || 100,
        latitude: parseFloat(document.getElementById('esp-lat').value) || -23.4218,
        longitude: parseFloat(document.getElementById('esp-lng').value) || -51.9383,
    };
    if (!body.nome) { toast('Preencha o nome do espaço.', false); return; }
    try {
        await fetch(`${API}/espacos`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        toast('Espaço cadastrado com sucesso!');
        carregarEspacos();
        ['esp-nome','esp-end','esp-cap','esp-lat','esp-lng'].forEach(id => document.getElementById(id).value = '');
    } catch { toast('Erro ao cadastrar espaço.', false); }
}

async function carregarEventos() {
    const tb = document.getElementById('eventos-table');
    tb.innerHTML = '<tr><td colspan="5" class="loading">Carregando...</td></tr>';
    try {
        const data = await fetch(`${API}/eventos`).then(r => r.json());
        tb.innerHTML = data.length === 0
            ? '<tr><td colspan="5" class="loading">Nenhum evento registrado.</td></tr>'
            : data.map(e => `<tr>
          <td><strong>${e.nome}</strong></td>
          <td>${statusBadge(e.statusEvento)}</td>
          <td>${fmtDate(e.dataHoraInicio)}</td>
          <td>${fmtDate(e.dataHoraFim)}</td>
          <td>
            <button class="btn btn-sm btn-success" onclick="avancarEvento(${e.id})">Avançar</button>
            <button class="btn btn-sm btn-danger" onclick="cancelarEvento(${e.id})" style="margin-left:4px">Cancelar</button>
          </td>
        </tr>`).join('');
    } catch { tb.innerHTML = '<tr><td colspan="5" class="loading">Erro ao carregar.</td></tr>'; }
}

async function criarEvento() {
    const inicio = document.getElementById('ev-inicio').value;
    const fim = document.getElementById('ev-fim').value;
    const body = {
        nome: document.getElementById('ev-nome').value,
        descricao: document.getElementById('ev-desc').value,
        dataHoraInicio: inicio ? new Date(inicio).toISOString().slice(0,16) : null,
        dataHoraFim: fim ? new Date(fim).toISOString().slice(0,16) : null,
        espacoPublicoId: parseInt(document.getElementById('ev-espaco').value),
        proponenteId: parseInt(document.getElementById('ev-prop').value),
    };
    if (!body.nome || !body.espacoPublicoId || !body.proponenteId) { toast('Preencha todos os campos obrigatórios.', false); return; }
    try {
        const res = await fetch(`${API}/eventos`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        if (!res.ok) { const err = await res.text(); toast('Conflito: ' + err, false); return; }
        toast('Evento proposto com sucesso!');
        carregarEventos();
    } catch { toast('Erro ao propor evento.', false); }
}

async function avancarEvento(id) {
    try {
        await fetch(`${API}/eventos/${id}/avancar`, { method: 'PATCH' });
        toast('Estado do evento avançado!');
        carregarEventos();
    } catch { toast('Erro ao avançar evento.', false); }
}

async function cancelarEvento(id) {
    try {
        const res = await fetch(`${API}/eventos/${id}/cancelar`, { method: 'PATCH' });
        if (!res.ok) { toast('Este evento não pode ser cancelado.', false); return; }
        toast('Evento cancelado.');
        carregarEventos();
    } catch { toast('Erro ao cancelar evento.', false); }
}

async function carregarReportes() {
    const tb = document.getElementById('reportes-table');
    tb.innerHTML = '<tr><td colspan="6" class="loading">Carregando...</td></tr>';
    try {
        const data = await fetch(`${API}/reportes/prioridade`).then(r => r.json());
        tb.innerHTML = data.length === 0
            ? '<tr><td colspan="6" class="loading">Nenhum reporte registrado.</td></tr>'
            : data.map(r => `<tr>
          <td>${r.descricao}</td>
          <td>${tipoBadge(r.tipo)}</td>
          <td>${r.dataAbertura || '–'}</td>
          <td>${r.confirmacoes}</td>
          <td><strong style="color:var(--vermelho)">${r.prioridade}</strong></td>
          <td><button class="btn btn-sm btn-warn" onclick="confirmarReporte(${r.id})">+ Confirmar</button></td>
        </tr>`).join('');
    } catch { tb.innerHTML = '<tr><td colspan="6" class="loading">Erro ao carregar.</td></tr>'; }
}

async function criarReporte() {
    const body = {
        descricao: document.getElementById('rep-desc').value,
        tipo: document.getElementById('rep-tipo').value,
        espacoPublicoId: parseInt(document.getElementById('rep-espaco').value),
        autorId: parseInt(document.getElementById('rep-autor').value),
    };
    if (!body.descricao || !body.espacoPublicoId || !body.autorId) { toast('Preencha todos os campos.', false); return; }
    try {
        await fetch(`${API}/reportes`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        toast('Reporte registrado!');
        carregarReportes();
        ['rep-desc','rep-espaco','rep-autor'].forEach(id => document.getElementById(id).value = '');
    } catch { toast('Erro ao registrar reporte.', false); }
}

async function confirmarReporte(id) {
    try {
        await fetch(`${API}/reportes/${id}/confirmar`, { method: 'PATCH' });
        toast('Reporte confirmado!');
        carregarReportes();
    } catch { toast('Erro ao confirmar reporte.', false); }
}

async function carregarUsuarios() {
    const tb = document.getElementById('usuarios-table');
    tb.innerHTML = '<tr><td colspan="4" class="loading">Carregando...</td></tr>';
    try {
        const data = await fetch(`${API}/usuarios`).then(r => r.json());
        tb.innerHTML = data.length === 0
            ? '<tr><td colspan="4" class="loading">Nenhum usuário cadastrado.</td></tr>'
            : data.map(u => `<tr>
          <td>${u.id}</td>
          <td>${u.nome}</td>
          <td>${u.email}</td>
          <td><span class="badge ${u.tipo === 'GESTOR' ? 'badge-verde' : 'badge-azul'}">${u.tipo}</span></td>
        </tr>`).join('');
    } catch { tb.innerHTML = '<tr><td colspan="4" class="loading">Erro ao carregar.</td></tr>'; }
}

async function criarUsuario() {
    const body = {
        nome: document.getElementById('usr-nome').value,
        email: document.getElementById('usr-email').value,
        tipo: document.getElementById('usr-tipo').value,
    };
    if (!body.nome || !body.email) { toast('Preencha nome e e-mail.', false); return; }
    try {
        await fetch(`${API}/usuarios`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        toast('Usuário cadastrado!');
        carregarUsuarios();
        ['usr-nome','usr-email'].forEach(id => document.getElementById(id).value = '');
    } catch { toast('Erro ao cadastrar usuário.', false); }
}

loadDashboard();