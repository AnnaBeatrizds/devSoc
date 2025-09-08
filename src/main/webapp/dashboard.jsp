<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard SST</title>
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body { display: flex; min-height: 100vh; flex-direction: row; margin: 0; }
        .sidebar { width: 280px; background: #343a40; }
        .content { flex-grow: 1; padding: 30px; background-color: #f4f6f9; }
        .nav-link { font-size: 1.1em; }
    </style>
</head>
<body>

<div class="d-flex flex-column flex-shrink-0 p-3 text-white bg-dark sidebar">
    <a href="<s:url action='dashboard' namespace='/'/>" class="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
        <span class="fs-4">SST - Painel</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto">
        <li class="nav-item">
            <s:url action="todosFuncionarios" namespace="/funcionario" var="urlFuncionarios"/>
            <a href="${urlFuncionarios}" class="nav-link text-white">Funcionários</a>
        </li>
        <li>
            <s:url action="todasAgendas" namespace="/agenda" var="urlAgendas"/>
            <a href="${urlAgendas}" class="nav-link text-white">Lista de Agendas</a>
        </li>
        <li>
		    <s:url action="listarCompromissos" namespace="/compromisso" var="compromissoUrl"/>
			<a href="${compromissoUrl}" class="nav-link text-white">Lista de Compromissos</a>
		</li>
        <li>
            <s:url action="novoCompromisso" namespace="/compromisso" var="urlCompromisso"/>
            <a href="${urlCompromisso}" class="nav-link text-white">Novo Agendamento</a>
        </li>
    </ul>
</div>

<div class="content">
    <h1>Dashboard</h1>
    <p class="lead">Visão geral do sistema de Saúde e Segurança no Trabalho.</p>

    <div class="row">
        <div class="col-md-4"><div class="card text-white bg-primary mb-3"><div class="card-header">Funcionários Ativos</div><div class="card-body"><h5 class="card-title">152</h5><p class="card-text">Total de colaboradores cadastrados.</p></div></div></div>
        <div class="col-md-4"><div class="card text-white bg-success mb-3"><div class="card-header">Compromissos Hoje</div><div class="card-body"><h5 class="card-title">12</h5><p class="card-text">Exames e consultas agendadas.</p></div></div></div>
        <div class="col-md-4"><div class="card text-white bg-warning mb-3"><div class="card-header">Exames Periódicos a Vencer</div><div class="card-body"><h5 class="card-title">8</h5><p class="card-text">Nos próximos 30 dias.</p></div></div></div>
    </div>

    <div class="row mt-4">
        <div class="col-md-12">
            <div class="card">
                <div class="card-header">Exames Realizados por Mês</div>
                <div class="card-body">
                    <canvas id="myChart"></canvas>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
document.addEventListener("DOMContentLoaded", function() {
    const ctx = document.getElementById('myChart').getContext('2d');
    const myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho'],
            datasets: [{
                label: 'Nº de Exames',
                data: [65, 59, 80, 81, 56, 55],
                backgroundColor: 'rgba(0, 123, 255, 0.5)',
                borderColor: 'rgba(0, 123, 255, 1)',
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
});
</script>

<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
<script src="${bootstrap_js}"></script>

</body>
</html>