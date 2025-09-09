<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Agenda</title>

    <!-- Importa o CSS do Bootstrap -->
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
</head>
<body class="bg-light">
<div class="container">

    <!-- Formulário principal: usa "salvarAgenda" para salvar ou atualizar -->
    <s:form action="salvarAgenda" id="formAgenda">
        <!-- Campo oculto para manter o ID da agenda -->
        <s:hidden name="agendaVo.codigo"/>

        <!-- =================== CARD PRINCIPAL =================== -->
        <div class="card mt-5">

            <!-- Cabeçalho: botão de voltar + título dinâmico (novo ou editar) -->
            <div class="card-header">
                <div class="row">
                    <div class="col-sm-5">
                        <s:url action="todasAgendas" var="todas"/>
                        <a href="${todas}" class="btn btn-success"><s:text name="label.agendas"/></a>
                    </div>
                    <div class="col-sm">
                        <s:if test="agendaVo.codigo != null">
                            <h5 class="card-title"><s:text name="label.editarAgenda"/></h5>
                        </s:if>
                        <s:else>
                            <h5 class="card-title"><s:text name="label.novaAgenda"/></h5>
                        </s:else>
                    </div>
                </div>
            </div>

            <!-- Corpo do formulário -->
            <div class="card-body">

                <!-- Código (ID da agenda) -->
                <div class="row align-items-center">
                    <label for="id" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.codigo"/>
                    </label>
                    <div class="col-sm-2">
                        <s:textfield cssClass="form-control" id="id" value="%{agendaVo.codigo}" readonly="true"/>
                    </div>
                </div>

                <!-- Nome da agenda -->
                <div class="row align-items-center mt-3">
                    <label for="nomeAgenda" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.nomeAgenda"/>
                    </label>
                    <div class="col-sm-5">
                        <s:textfield cssClass="form-control" id="nomeAgenda" name="agendaVo.nmAgenda" required="true"/>
                    </div>
                </div>

                <!-- Período disponível (select) -->
                <div class="row align-items-center mt-3">
                    <label for="periodo" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.periodoDisponivel"/>
                    </label>
                    <div class="col-sm-5">
                        <s:select cssClass="form-control"
                                  list="#{'Manhã':'Manhã', 'Tarde':'Tarde', 'Ambos':'Ambos'}"
                                  name="agendaVo.periodo"
                                  required="true"
                                  value="%{agendaVo.periodo}"/>
                    </div>
                </div>

                <!-- Lista de exames relacionados à agenda -->
                <div class="row align-items-center mt-3">
                    <label for="exames" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.exames"/>
                    </label>
                    <div class="col-sm-5">
                        <s:checkboxlist name="agendaVo.examesIds"
                                        list="listaTodosExames"
                                        listKey="id"
                                        listValue="nmExame"
                                        value="%{agendaVo.examesIds}"/>
                    </div>
                </div>
            </div>

            <!-- Rodapé com botões -->
            <div class="card-footer">
                <div class="form-row">
                    <!-- Botão salvar -->
                    <s:submit value="%{getText('label.salvar')}" cssClass="btn btn-primary col-sm-4 offset-sm-1"/>
                    <!-- Botão limpar -->
                    <button type="reset" class="btn btn-secondary col-sm-4 offset-sm-2"><s:text name="label.limparFormulario"/></button>
                </div>
            </div>
        </div>
    </s:form>
</div>

<!-- Script do Bootstrap -->
<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js"/>
<script src="${bootstrap_js}"></script>
</body>
</html>
