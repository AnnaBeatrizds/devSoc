<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <!-- Título da página usando resource bundle do Struts -->
    <title><s:text name="label.listaCompromissos"/></title>

    <!-- Importa CSS do Bootstrap para layout -->
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
</head>
<body class="bg-light">

<!-- =================== Container principal =================== -->
<div class="container mt-4 border">
    <div class="card">

        <!-- =================== Cabeçalho do card =================== -->
        <div class="card-header">
            <h4><s:text name="label.listaCompromissos"/></h4>
        </div>

        <!-- =================== Corpo do card =================== -->
        <div class="card-body">
            <!-- Mensagens de erro ou sucesso vindas do backend -->
            <s:actionerror cssClass="alert alert-danger mt-3"/>
            <s:actionmessage cssClass="alert alert-success mt-3"/>

            <!-- =================== Filtro de busca =================== -->
            <div class="row mb-3">
                <div class="col-sm p-0">
                    <s:form action="filtrarCompromissos" namespace="/compromisso">
                        <div class="input-group">
                            <!-- Label do filtro -->
                            <span class="input-group-text">
                                <strong><s:text name="label.buscar.por.funcionario"/></strong>
                            </span>	
                            <!-- Select para escolher filtro (ID ou Nome) -->
                            <s:select  
                                cssClass="form-select" 
                                name="filter.opcoesCombo" 
                                list="listaOpcoesCombo"  
                                headerKey=""  
                                headerValue="%{getText('label.escolha')}" 
                                listKey="%{codigo}" 
                                listValueKey="%{descricao}"
                                value="filter.opcoesCombo.codigo"									
                            />
                            <!-- Campo de texto para digitar o valor da busca -->
                            <s:textfield cssClass="form-control" name="filter.valorBusca"/>
                            <!-- Botão de pesquisa com validação -->
                            <button class="btn btn-primary" type="submit" onclick="return validarBusca()">
                                <s:text name="label.pesquisar"/>
                            </button>
                        </div>
                    </s:form>			
                </div>

                <!-- Botão de voltar aparece apenas se houve filtro -->
                <s:if test="filter.valorBusca != null && !filter.valorBusca.isEmpty()">
                    <div class="col-sm-auto">
                        <s:url action="listarCompromissos" var="listarUrl"/>
                        <a href="${listarUrl}" class="btn btn-secondary">
                            <s:text name="label.voltar"/>
                        </a>
                    </div>
                </s:if>
            </div>

            <!-- =================== Tabela de compromissos =================== -->
            <table class="table table-light table-striped align-middle">
                <thead>
                    <tr>
                        <th><s:text name="label.codigo"/></th>
                        <th><s:text name="label.data"/></th>
                        <th><s:text name="label.hora"/></th>
                        <th><s:text name="label.funcionario"/></th>
                        <th><s:text name="label.agenda"/></th>
                        <th class="text-end"><s:text name="label.acao"/></th>
                    </tr>
                </thead>
                <tbody>
                    <s:iterator value="compromissos">
                        <tr>
                            <!-- Exibe informações do compromisso -->
                            <td><s:property value="codigo"/></td>
                            <td><s:date name="dataCompromissoObjeto" format="dd/MM/yyyy"/></td>
                            <td><s:property value="horaCompromisso"/></td>
                            <td><s:property value="nomeFuncionario"/></td>
                            <td><s:property value="nomeAgenda"/></td>

                            <!-- Ações: Editar e Excluir -->
                            <td class="text-end">
                                <!-- URL para edição do compromisso -->
                                <s:url action="editarCompromisso" namespace="/compromisso" var="editar">
                                    <s:param name="compromissoVo.codigo" value="codigo"></s:param>
                                </s:url>
                                <a href="${editar}" class="btn btn-warning text-white"><s:text name="label.editar"/></a>
                                
                                <!-- Botão para abrir modal de confirmação de exclusão -->
                                <a href="#" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmarExclusao" data-codigo="<s:property value='codigo'/>">
                                    <s:text name="label.excluir"/>
                                </a>
                            </td>
                        </tr>
                    </s:iterator>
                </tbody>

                <!-- Rodapé da tabela com botão para dashboard -->
                <tfoot class="table-secondary">
                    <tr>
                        <td colspan="6" class="text-end">
                            <s:url action="dashboard" namespace="/" var="dashboard"/>
                            <a href="${dashboard}" class="btn btn-info text-white">
                                <s:text name="label.dashboard"/>
                            </a>
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>

<!-- =================== Modal de confirmação de exclusão =================== -->
<div class="modal fade" id="confirmarExclusao" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <!-- Cabeçalho do modal -->
            <div class="modal-header">
                <h5 class="modal-title"><s:text name="label.modal.titulo"/></h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <!-- Corpo do modal -->
            <div class="modal-body">
                <span><s:text name="label.modal.corpo"/></span>
            </div>
            <!-- Rodapé do modal com botões Sim/Não -->
            <div class="modal-footer">
                <a class="btn btn-secondary" data-bs-dismiss="modal" aria-label="Close"><s:text name="label.nao"/></a>
                <s:a id="excluir" class="btn btn-primary" style="width: 75px;"><s:text name="label.sim"/></s:a>						
            </div>
        </div>		    
    </div>
</div>

<!-- =================== Scripts =================== -->
<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
<script src="${bootstrap_js}"></script>

<script type="text/javascript">
    // Configura o modal para pegar o código do compromisso a ser excluído
    var myModal = document.getElementById('confirmarExclusao');
    myModal.addEventListener('show.bs.modal', function(event) {
        var button = event.relatedTarget;
        var codigo = button.getAttribute('data-codigo');
        var confirmButton = myModal.querySelector('#excluir');
        confirmButton.href = 'excluirCompromisso.action?compromissoVo.codigo=' + codigo;
    });

    // Validação da busca antes de enviar o formulário
    function validarBusca() {
        let filtro = document.querySelector("select[name='filter.opcoesCombo']").value;
        let valor = document.querySelector("input[name='filter.valorBusca']").value.trim();

        if (filtro === "") {
            alert("Selecione se deseja buscar por ID ou Nome!");
            return false;
        }

        if (filtro === "1" && !/^[0-9]+$/.test(valor)) {
            alert("Digite apenas números para buscar por ID!");
            return false;
        }

        if (filtro === "2" && !/^[A-Za-zÀ-ÿ\s]+$/.test(valor)) {
            alert("Digite apenas letras para buscar por Nome!");
            return false;
        }

        return true;
    }
</script>

</body>
</html>
