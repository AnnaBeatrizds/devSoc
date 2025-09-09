<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!-- =================== Cabeçalho da página =================== -->
<head>
    <meta charset="UTF-8">
    <title><s:text name="label.agendamento"/></title>

    <!-- Bootstrap para layout -->
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">

    <!-- Flatpickr para calendário -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
</head>

<body class="bg-light">
<div class="container mt-4 border">
    <div class="card">

        <!-- =================== Cabeçalho do card =================== -->
        <div class="card-header">
            <h4><s:text name="label.novoCompromisso"/></h4>
        </div>

        <!-- =================== Formulário principal =================== -->
        <div class="card-body">
            <s:form action="visualizarHorarios" theme="simple">

                <!-- Select de Agendas -->
                <fieldset class="border p-3 mb-3">
                    <legend><s:text name="label.selecioneAgenda"/></legend>
                    <s:select 
                        cssClass="form-select"
                        name="compromissoVo.idAgenda"
                        list="agendas"
                        listKey="codigo"
                        listValue="nmAgenda"
                        headerKey=""
                        headerValue="%{getText('label.escolhaAgenda')}"
                        id="selectAgenda"
                    />
                </fieldset>

                <!-- Select de Funcionário + Campo de Data -->
                <fieldset class="border p-3 mb-3">
                    <legend><s:text name="label.selecioneFuncionarioData"/></legend>
                    <div class="row align-items-end">
                        <!-- Funcionário -->
                        <div class="col-md-6">
                            <label for="funcionario"><s:text name="label.funcionario"/></label>
                            <s:select
                                id="funcionario"
                                cssClass="form-select"
                                name="compromissoVo.idFuncionario"
                                list="funcionarios"
                                listKey="rowid"
                                listValue="nmFuncionario"
                                headerKey=""
                                headerValue="%{getText('label.escolhaFuncionario')}"
                            />
                        </div>
                        <!-- Data -->
                        <div class="col-md-6">
                            <label for="dataCompromisso"><s:text name="label.dataCompromisso"/></label>
                            <s:textfield id="dataCompromisso" name="compromissoVo.dataCompromisso" 
                                         cssClass="form-control" 
                                         placeholder="%{getText('label.selecioneData')}"/>
                        </div>
                    </div>
                </fieldset>

                <!-- Botão Avançar -->
                <div class="d-grid gap-2">
                    <s:submit cssClass="btn btn-success btn-lg" value="%{getText('label.avancarHorarios')}"/>
                </div>
            </s:form>
        </div>

        <!-- =================== Rodapé do card =================== -->
        <div class="card-footer">
            <div class="d-flex justify-content-between">
                <s:url action="dashboard" namespace="/" var="dashboard"/>
                <a href="${dashboard}" class="btn btn-info text-white">
                    <s:text name="label.dashboard"/>
                </a>
            </div>
        </div>
    </div>
</div>

<!-- =================== Scripts =================== -->
<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
<script src="${bootstrap_js}"></script>

<script>
   const selectAgenda = document.getElementById('selectAgenda');
   const campoData = document.getElementById('dataCompromisso');
   let calendarioInstance = null;

   // Inicializa o calendário Flatpickr
   function inicializarCalendario(datasParaDesabilitar) {
       if (calendarioInstance) {
           calendarioInstance.destroy();
       }
       calendarioInstance = flatpickr("#dataCompromisso", {
           dateFormat: "Y-m-d",
           altInput: true,
           altFormat: "d/m/Y",
           placeholder: "Selecione uma data...",
           disable: datasParaDesabilitar
       });
   }

   // Página carregada → campo data começa desabilitado
   document.addEventListener('DOMContentLoaded', function() {
       inicializarCalendario([]);
       campoData.disabled = true;
   });

   // Requisição AJAX para buscar datas ocupadas
   function buscarDatasOcupadas(codigo) {
       const xhr = new XMLHttpRequest();
       xhr.open('GET', '<s:url action="visualizarHorarios" namespace="/compromisso"/>' 
                + '?compromissoVo.idAgenda=' + encodeURIComponent(codigo), true);
       xhr.onreadystatechange = function() {
           if (xhr.readyState === 4) {
               if (xhr.status === 200) {
                   const datasOcupadas = JSON.parse(xhr.responseText);
                   inicializarCalendario(datasOcupadas);
               } else {
                   console.error('Erro ao buscar datas ocupadas');
                   inicializarCalendario([]);
               }
           }
       };
       xhr.send();
   }

   // Evento → ao trocar de agenda, busca datas disponíveis
   selectAgenda.addEventListener('change', function() {
       const agendaId = this.value;

       if (!agendaId) {
           campoData.disabled = true;
           inicializarCalendario([]);
           return;
       }

       campoData.disabled = false;
       buscarDatasOcupadas(agendaId);
   });
</script>
</body>
