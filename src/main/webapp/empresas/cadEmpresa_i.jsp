<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><s:text name="label.titulo.pagina.cadastroEmpresa"/></title>

    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
</head>
<body class="bg-secondary">

<div class="container">
    <s:form action="salvar" namespace="/empresa" id="formEmpresa">
        <s:hidden name="empresaVo.codigo"/>

        <div class="card mt-5">
            <div class="card-header">
                <div class="row">
                    <div class="col-sm-5">
                        <s:url action="todasEmpresas" namespace="/empresa" var="todas"/>
                        <a href="${todas}" class="btn btn-success"><s:text name="label.empresas"/></a>
                    </div>

                    <div class="col-sm">
                        <s:if test="empresaVo.codigo != null">
                            <h5 class="card-title"><s:text name="label.editarEmpresa"/></h5>
                        </s:if>
                        <s:else>
                            <h5 class="card-title"><s:text name="label.novaEmpresa"/></h5>
                        </s:else>
                    </div>
                </div>
            </div>

            <div class="card-body">
                <div class="row align-items-center">
                    <label for="id" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.codigo"/>
                    </label>
                    <div class="col-sm-2">
                        <s:textfield cssClass="form-control" id="id" value="%{empresaVo.codigo}" readonly="true"/>
                    </div>
                </div>

                <div class="row align-items-center mt-3">
                    <label for="nomeEmpresa" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.nomeEmpresa"/>
                    </label>
                    <div class="col-sm-5">
                        <s:textfield cssClass="form-control" id="nomeEmpresa" name="empresaVo.nome" required="true"/>
                    </div>
                </div>

                <div class="row align-items-center mt-3">
                    <label for="periodo" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.periodoDisponivel"/>
                    </label>
                    <div class="col-sm-5">
                        <s:select cssClass="form-control"
                                  list="#{'Manhã':'Manhã', 'Tarde':'Tarde', 'Ambos':'Ambos'}"
                                  name="empresaVo.periodo"
                                  required="true"
                                  value="%{empresaVo.periodo}"/>
                    </div>
                </div>

                <div class="row align-items-center mt-3">
                    <label for="exames" class="col-sm-1 col-form-label text-center">
                        <s:text name="label.exames"/>
                    </label>
                    <div class="col-sm-5">
                        <s:checkboxlist name="empresaVo.examesIds"
                                        list="listaTodosExames"
                                        listKey="id"
                                        listValue="nomeExame"
                                        value="%{empresaVo.examesIds}"/>
                    </div>
                </div>
            </div>

            <div class="card-footer">
                <div class="form-row">
                    <s:submit value="%{getText('label.salvar')}" cssClass="btn btn-primary col-sm-4 offset-sm-1"/>
                    <button type="reset" class="btn btn-secondary col-sm-4 offset-sm-2" onclick="limparFormulario()">
                        <s:text name="label.limparFormulario"/>
                    </button>
                </div>
            </div>
        </div>
    </s:form>
</div>

<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js"/>
<script src="${bootstrap_js}"></script>

<script>
    function limparFormulario() {
        document.getElementById("formEmpresa").reset();
    }
</script>

</body>
</html>