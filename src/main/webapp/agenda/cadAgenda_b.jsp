<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF8">
		<title>Cadastro de Agendas</title>
		<s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
		<link rel='stylesheet' href='${bootstrap_css}'>
	</head>
	<body class="bg-light">	
		<div class="container">
			<s:actionerror cssClass="alert alert-danger mt-3"/>
			<s:actionmessage cssClass="alert alert-success mt-3"/>

			<div class="row mt-5 mb-2">
				<div class="col-sm p-0">
					<s:form action="filtrarAgendas">
						<div class="input-group">
							<span class="input-group-text">
								<strong><s:text name="label.buscar.por"/></strong>
							</span>	
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
								<s:textfield cssClass="form-control" id="nome" name="filter.valorBusca"/>
								<button class="btn btn-primary" type="submit" onclick="return validarBusca()">
    								<s:text name="label.pesquisar"/>
								</button>
						</div>
					</s:form>			
				</div>
				<s:if test="filter.valorBusca != null">
					<div class="col-sm p-0">
						<div class="input-group">
							<a href="<s:url action='todasAgendas' />" class="btn btn-primary">
								<s:text name="label.voltar"/>
							</a>
						</div>
					</div>
				</s:if>
			</div>
			<div class="row border">
				<table class="table table-light table-striped align-middle">
					<thead>
						<tr>
							<th><s:text name="label.codigo"/></th>
							<th><s:text name="label.nomeAgenda"/></th>
							<th><s:text name="label.periodoDisponivel"/></th>
							<th class="text-end mt-5"><s:text name="label.acao"/></th>
						</tr>
					</thead>
					<tbody>
						<s:iterator value="agendas" >
							<tr>
								<td><s:property value="codigo"/></td>
								<td><s:property value="nmAgenda"/></td>
								<td><s:property value="periodo"/></td>
								<td class="text-end">
									<s:url action="editarAgendas" var="editar">
										<s:param name="agendaVo.codigo" value="%{codigo}"></s:param>
									</s:url>
									<a href="${editar}" class="btn btn-warning text-white">
										<s:text name="label.editar"/>
									</a>
									<a href="#" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmarExclusao" data-codigo="<s:property value='codigo'/>">
										<s:text name="label.excluir"/>
									</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					<tfoot class="table-secondary">
						<tr>
							<td colspan="4">
								<div class="d-flex justify-content-between">
									<s:url action="novaAgenda" var="novo"/>
									<a href="${novo}" class="btn btn-success">
										<s:text name="label.novaAgenda"/>
									</a>
									<s:url action="dashboard" namespace="/" var="dashboard"/>
									<a href="${dashboard}" class="btn btn-info text-white">
										<s:text name="label.dashboard"/>
									</a>
								</div>
							</td>
						</tr>
					</tfoot>				
				</table>
			</div>
		</div>
		<div class="modal fade" id="confirmarExclusao" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
		  <div class="modal-dialog" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title"><s:text name="label.modal.titulo"/></h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		      	<span><s:text name="label.modal.corpo"/></span>
		      </div>
		      <div class="modal-footer">
	        	<a class="btn btn-secondary" data-bs-dismiss="modal" aria-label="Close"><s:text name="label.nao"/></a>
				<s:a id="excluir" action="excluirAgendas" class="btn btn-primary" style="width: 75px;"><s:text name="label.sim"/></s:a>						
		      </div>
		    </div>		    
		  </div>
		</div>
		<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
		<script src="${bootstrap_js}"></script>
		<script type="text/javascript">
			var myModal = document.getElementById('confirmarExclusao');
			myModal.addEventListener('show.bs.modal', function(event) {
				var button = event.relatedTarget;
				var codigo = button.getAttribute('data-codigo');
				var confirmButton = myModal.querySelector('#excluir');
				confirmButton.href = 'excluirAgendas.action?agendaVo.codigo=' + codigo;
			});

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