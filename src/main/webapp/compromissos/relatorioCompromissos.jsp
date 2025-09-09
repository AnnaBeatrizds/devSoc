<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- Título da página usando resource bundle do Struts -->
    <title><s:text name="label.relatorioCompromissos"/></title>

    <!-- Importa CSS do Bootstrap para layout -->
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">

    <!-- Flatpickr CSS para seleção de datas -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
</head>
<body class="bg-light">

<!-- =================== Container principal =================== -->
<div class="container mt-4 border">
    <div class="card">

        <!-- =================== Cabeçalho do card =================== -->
        <div class="card-header">
            <h4><s:text name="label.relatorioCompromissos"/></h4>
        </div>

        <!-- =================== Corpo do card =================== -->
        <div class="card-body">
            <!-- Mensagens de erro ou sucesso vindas do backend -->
            <s:actionerror cssClass="alert alert-danger mt-3"/>
            <s:actionmessage cssClass="alert alert-success mt-3"/>

            <!-- =================== Formulário de filtros =================== -->
            <div class="row mb-3">
                <s:form action="gerarRelatorioHTML" method="post" theme="simple">
                    <div class="row align-items-center mb-3">
                        <!-- Campo Data Inicial -->
                        <div class="col-md-3">
                            <label for="dataInicial" class="form-label"><s:text name="label.dataInicial"/></label>
                            <s:textfield id="dataInicial" name="dataInicial" cssClass="form-control" placeholder="YYYY-MM-DD"/>
                        </div>
                        <!-- Campo Data Final -->
                        <div class="col-md-3">
                            <label for="dataFinal" class="form-label"><s:text name="label.dataFinal"/></label>
                            <s:textfield id="dataFinal" name="dataFinal" cssClass="form-control" placeholder="YYYY-MM-DD"/>
                        </div>
                        <!-- Botões de ação: Gerar, Exportar Excel, Imprimir -->
                        <div class="col-md-6 d-flex justify-content-end align-items-end mt-3 mt-md-0">
                            <div class="d-grid gap-2 d-md-block">
                                <s:submit value="%{getText('label.gerarRelatorio')}" cssClass="btn btn-warning me-2"/>
                                <a href="#" class="btn btn-success me-2" onclick="exportarExcel()"><s:text name="label.exportarExcel"/></a>
                                <a href="#" class="btn btn-primary" onclick="window.print()"><s:text name="label.imprimir"/></a>
                            </div>
                        </div>
                    </div>
                </s:form>
            </div>

            <!-- =================== Tabela de resultados =================== -->
            <s:if test="compromissos != null && !compromissos.isEmpty()">
                <table class="table table-light table-striped align-middle">
                    <thead>
                        <tr>
                            <th><s:text name="label.codigo"/> (<s:text name="label.funcionario"/>)</th>
                            <th><s:text name="label.nome"/> (<s:text name="label.funcionario"/>)</th>
                            <th><s:text name="label.codigo"/> (<s:text name="label.agenda"/>)</th>
                            <th><s:text name="label.nomeAgenda"/></th>
                            <th><s:text name="label.data"/></th>
                            <th><s:text name="label.hora"/></th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="compromissos">
                            <tr>
                                <td><s:property value="idFuncionario"/></td>
                                <td><s:property value="nomeFuncionario"/></td>
                                <td><s:property value="idAgenda"/></td>
                                <td><s:property value="nomeAgenda"/></td>
                                <td><s:date name="dataCompromissoObjeto" format="dd/MM/yyyy"/></td>
                                <td><s:property value="horaCompromisso"/></td>
                            </tr>
                        </s:iterator>
                    </tbody>
                </table>
            </s:if>

            <!-- =================== Mensagem caso não haja resultados =================== -->
            <s:else>
                <div class="alert alert-info" role="alert">
                    Nenhum compromisso encontrado para o período selecionado.
                </div>
            </s:else>

        </div>

        <!-- =================== Rodapé do card =================== -->
        <div class="card-footer">
            <s:url action="dashboard" namespace="/" var="dashboard"/>
            <a href="${dashboard}" class="btn btn-info text-white">
                <s:text name="label.dashboard"/>
            </a>
        </div>

    </div>
</div>

<!-- =================== Scripts =================== -->
<!-- Flatpickr para campos de data -->
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

<!-- Bootstrap JS -->
<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
<script src="${bootstrap_js}"></script>

<script>
    // Inicializa Flatpickr nos campos de data
    document.addEventListener('DOMContentLoaded', function() {
        flatpickr("#dataInicial", {
            dateFormat: "Y-m-d",
            altInput: true,
            altFormat: "d/m/Y",
        });
        flatpickr("#dataFinal", {
            dateFormat: "Y-m-d",
            altInput: true,
            altFormat: "d/m/Y",
        });
    });

    // Função para exportar relatório para Excel
    function exportarExcel() {
        var dataInicial = document.getElementById('dataInicial').value;
        var dataFinal = document.getElementById('dataFinal').value;
        if (!dataInicial || !dataFinal) {
            alert('Por favor, selecione as datas de início e fim para exportar.');
            return false;
        }
        // Redireciona para ação que gera o Excel
        window.location.href = 'gerarRelatorioExcel.action?dataInicial=' + dataInicial + '&dataFinal=' + dataFinal;
    }
</script>

</body>
</html>
