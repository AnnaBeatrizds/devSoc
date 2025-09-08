<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmar Agendamento</title>
    <s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
    <link rel="stylesheet" href="${bootstrap_css}">
    <style>
        /* Estilo para o botão de ação aparecer ao passar o mouse na linha da tabela */
        .table-hover .action-button { visibility: hidden; }
        .table-hover tr:hover .action-button { visibility: visible; }
        .disabled-row { color: #adb5bd; text-decoration: line-through; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <div class="card shadow-sm">
        <div class="card-body p-4">

            <div class="row p-3 mb-4 bg-primary text-white rounded">
                <div class="col-md-6">
                    <strong>FUNCIONÁRIO:</strong><br>
                    <s:property value="compromissoVo.nomeFuncionario" /> 
                    (CÓDIGO: <s:property value="compromissoVo.idFuncionario" />)
                </div>
                <div class="col-md-6">
                    <strong>AGENDA:</strong><br>
                    <s:property value="compromissoVo.nomeAgenda" />
                    (CÓDIGO: <s:property value="compromissoVo.idAgenda" />)
                </div>
            </div>

            <s:form id="form-principal" action="salvarCompromisso" method="post">
                <s:hidden name="compromissoVo.idFuncionario" />
                <s:hidden name="compromissoVo.idAgenda" />
                <s:hidden name="compromissoVo.dataCompromisso" />
                <s:hidden id="horaSelecionada" name="compromissoVo.horaCompromisso" />

                <div class="mb-4">
                    <label class="form-label fw-bold">SELECIONE O(S) EXAME(S):</label>
                    <div class="border p-3 rounded bg-white">
                        
                        <div class="d-flex flex-wrap">
                            <s:iterator value="listaExamesDisponiveis" var="exame">
                                <div class="form-check form-check-inline me-3">
                                    <s:checkbox
                                        name="compromissoVo.examesIds"
                                        fieldValue="%{#exame.id}"
                                        cssClass="form-check-input"
                                        id="exame_%{#exame.id}"
                                    />
                                    <label class="form-check-label" for="exame_%{#exame.id}">
                                        <s:property value="%{#exame.nmExame}" />
                                    </label>
                                </div>
                            </s:iterator>
                        </div>
                        
                    </div>
                </div>

                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>DATA</th>
                            <th>HORA</th>
                            <th class="text-end">AÇÃO</th>
                        </tr>
                    </thead>
                    <tbody>
                        <s:iterator value="todosOsHorarios" var="hora">
                            <s:if test="%{#hora in horariosOcupados}">
                                <tr class="disabled-row">
                                    <td><s:date name="compromissoVo.dataCompromissoObjeto" format="dd/MM/yyyy"/></td>
                                    <td><s:property value="#hora" /></td>
                                    <td class="text-end">Indisponível</td>
                                </tr>
                            </s:if>
                            <s:else>
                                <tr>
                                    <td><s:date name="compromissoVo.dataCompromissoObjeto" format="dd/MM/yyyy"/></td>
                                    <td><s:property value="#hora" /></td>
                                    <td class="text-end">
                                        <button type="button" class="btn btn-success btn-sm action-button" onclick="marcarHorario('<s:property value="#hora"/>')">+</button>
                                    </td>
                                </tr>
                            </s:else>
                        </s:iterator>
                    </tbody>
                </table>
                 <s:url action="novoCompromisso" var="voltarUrl"/>
                 <a href="${voltarUrl}" class="btn btn-secondary">Voltar</a>
            </s:form>
        </div>
    </div>
</div>

<script>
function marcarHorario(hora) {
    const examesSelecionados = document.querySelectorAll('input[name="compromissoVo.examesIds"]:checked');
    if (examesSelecionados.length === 0) {
        alert('Por favor, selecione pelo menos um exame antes de marcar um horário.');
        return;
    }
    document.getElementById('horaSelecionada').value = hora;
    const form = document.getElementById('form-principal');
    const formData = new FormData(form);
    fetch(form.action, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            alert('Compromisso marcado com Sucesso!');
            window.location.href = '<s:url action="novoCompromisso"/>';
        } else {
            alert('Erro ao marcar o compromisso.');
        }
    })
    .catch(error => {
        console.error('Erro na requisição:', error);
        alert('Ocorreu um erro de comunicação ao tentar marcar o compromisso.');
    });
}
</script>

</body>
</html>