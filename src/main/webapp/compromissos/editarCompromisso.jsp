<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Compromisso</title>
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
</head>
<body class="bg-light">
    <div class="container mt-4 border">
        <div class="card">
            <div class="card-header">
                <h4><s:text name="label.editarCompromisso"/></h4>
            </div>
            <div class="card-body">
                <s:form action="salvarCompromisso" id="formCompromisso" theme="simple">
                    <s:hidden name="compromissoVo.codigo" />
                    
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
                    
                    <fieldset class="border p-3 mb-3">
                        <legend class="float-none w-auto px-2"><s:text name="label.selecioneHorarioExames"/></legend>
                        <div class="row">
                            <div class="col-md-6">
                                <label for="horaCompromisso" class="form-label">Hora do Compromisso</label>
                                <s:select cssClass="form-control"
                                          name="compromissoVo.horaCompromisso"
                                          list="todosOsHorarios"
                                          value="compromissoVo.horaCompromisso"
                                          headerKey=""
                                          headerValue="Selecione a hora..."
                                          id="horaCompromisso"
                                />
                            </div>
                            <div class="col-md-6">
                                <label for="examesSelecionados" class="form-label">Exames</label>
                                <s:checkboxlist name="compromissoVo.examesSelecionados"
                                                list="listaExamesDisponiveis"
                                                listKey="id"
                                                listValue="nmExame"
                                                value="%{compromissoVo.examesSelecionados}"
                                />
                            </div>
                        </div>
                    </fieldset>

                    <div class="d-grid gap-2">
                        <s:submit cssClass="btn btn-primary btn-lg" value="%{getText('label.salvarAlteracoes')}"/>
                    </div>
                </s:form>
            </div>
            <div class="card-footer">
                <div class="d-flex justify-content-between">
                    <s:url action="listarCompromissos" namespace="/compromisso" var="listar"/>
                    <a href="${listar}" class="btn btn-secondary">
                        <s:text name="label.voltar"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js"/>
    <script src="${bootstrap_js}"></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
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
        });
        
        // Lógica para buscar os horários disponíveis ao selecionar a data
        document.getElementById('dataCompromisso').addEventListener('change', function() {
             const agendaId = selectAgenda.value;
             const data = this.value;
             if (agendaId && data) {
                 fetch('<s:url action="buscarHorariosOcupados" namespace="/compromisso"/>' + '?idAgenda=' + agendaId + '&data=' + data)
                    .then(response => response.json())
                    .then(horariosOcupados => {
                        // Colocar lógica para desabilitar horários ocupados depois
                    });
             }
        });
    </script>
</body>
</html>