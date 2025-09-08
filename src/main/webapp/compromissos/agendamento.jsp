<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
	    <title><s:text name="label.agendamento"/></title>
	    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
	    <link rel="stylesheet" href="${bootstrap_css}">
	    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
	    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	</head>
	
	<body class="bg-light">
	<div class="container mt-4 border">
    	<div class="card">
        	<div class="card-header">
            	<h4><s:text name="label.novoCompromisso"/></h4>
        </div>
        <div class="card-body">
            <s:form action="visualizarHorarios" theme="simple">
                <fieldset class="border p-3 mb-3">
                    <legend class="float-none w-auto px-2"><s:text name="label.selecioneAgenda"/></legend>
                    <div class="row">
                        <div class="col-md-12">
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
                        </div>
                    </div>
                </fieldset>

                <fieldset class="border p-3 mb-3">
                    <legend class="float-none w-auto px-2"><s:text name="label.selecioneFuncionarioData"/></legend>
                    <div class="row align-items-end">
                        <div class="col-md-6">
                            <label for="funcionario" class="form-label"><s:text name="label.funcionario"/></label>
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
                        	<div class="col-md-6">
                            	<label for="dataCompromisso" class="form-label"><s:text name="label.dataCompromisso"/></label>
                            	<s:textfield id="dataCompromisso" name="compromissoVo.dataCompromisso" cssClass="form-control" placeholder="%{getText('label.selecioneData')}"/>
                        	</div>
                    	</div>
                	</fieldset>

                	<div class="d-grid gap-2">
                    	<s:submit cssClass="btn btn-success btn-lg" value="%{getText('label.avancarHorarios')}"/>
                	</div>
            	</s:form>
        	</div>
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
    <s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
    <script src="${bootstrap_js}"></script>
    <script>
       const selectAgenda = document.getElementById('selectAgenda');
       const campoData = document.getElementById('dataCompromisso');
       let calendarioInstance = null;

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

       document.addEventListener('DOMContentLoaded', function() {
           inicializarCalendario([]);
           campoData.disabled = true;
       });

       function buscarDatasOcupadas(codigo) {
           const xhr = new XMLHttpRequest();
			xhr.open('GET', '<s:url action="visualizarHorarios" namespace="/compromisso"/>' + '?compromissoVo.idAgenda=' + encodeURIComponent(codigo), true);
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
</html>