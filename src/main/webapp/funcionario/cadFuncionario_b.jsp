<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF8">
		<title><s:text name="label.titulo.pagina.consulta"/></title>
		<s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
		<link rel='stylesheet' href='${bootstrap_css}'>
	</head>
	<body class="bg-secondary">	
		<div class="container">
			<s:actionerror cssClass="alert alert-danger mt-3"/>
			<s:actionmessage cssClass="alert alert-success mt-3"/>

			<div class="row mt-5 mb-2">
				<div class="col-sm p-0">
					<s:form action="filtrarFuncionarios">
						<div class="input-group">
							<span class="input-group-text">
								<strong><s:text name="label.buscar.por"/></strong>
							</span>	
								<s:select  
									cssClass="form-select" 
									name="filtrar.opcoesCombo" 
									list="listaOpcoesCombo"  
									headerKey=""  
									headerValue="Escolha..." 
									listKey="%{codigo}" 
									listValueKey="%{descricao}"
									value="filtrar.opcoesCombo.codigo"									
								/>
								<s:textfield cssClass="form-control" name="filtrar.valorBusca"/>
								<button class="btn btn-primary" type="submit" onclick="return validarBusca()">
    								<s:text name="label.pesquisar"/>
								</button>
						</div>
					</s:form>			
				</div>
				<s:if test="filtrar.valorBusca != null">
					<div class="col-sm p-0">
						<div class="input-group">
							<a href="todosFuncionarios.action" class="btn btn-primary">
								Voltar
							</a>
						</div>
					</div>
				</s:if>
			</div>

			<div class="row">
				<table class="table table-light table-striped align-middle">
					<thead>
						<tr>
							<th><s:text name="label.id"/></th>
							<th><s:text name="label.nome"/></th>
							<th class="text-end mt-5"><s:text name="label.acao"/></th>
						</tr>
					</thead>
					
					<tbody>
						<s:iterator value="funcionarios" >
							<tr>
								<td><s:property value="rowid"/></td>
								<td><s:property value="nmFuncionario"/></td>
								<td class="text-end">
									<s:url action="editarFuncionarios" var="editar">
										<s:param name="funcionarioVo.rowid" value="rowid"></s:param>
									</s:url>
									<a href="${editar}" class="btn btn-warning text-white">
										<s:text name="label.editar"/>
									</a>
									<a href="#" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#confirmarExclusao" data-rowid="${rowid}">
										<s:text name="label.excluir"/>
									</a>
								</td>
							</tr>
						</s:iterator>
					</tbody>
					
					<tfoot class="table-secondary">
						<tr>
							<td colspan="3">
								<s:url action="novoFuncionarios" var="novo"/>
								<a href="${novo}" class="btn btn-success">
									<s:text name="label.novo"/>
								</a>
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
				<s:a id="excluir" class="btn btn-primary" style="width: 75px;"><s:text name="label.sim"/></s:a>						
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
				var rowid = button.getAttribute('data-rowid');
				var confirmButton = myModal.querySelector('#excluir');
				confirmButton.href = 'excluirFuncionarios.action?rowid=' + rowid;
			});

			function validarBusca() {
			    var filtro = document.querySelector("select[name='filtrar.opcoesCombo']").value;
			    var valor = document.querySelector("input[name='filtrar.valorBusca']").value.trim();
			    if (filtro === "") {
			        alert("Selecione se deseja buscar por ID ou Nome!");
			        return false;
			    }
			    if (filtro === "1" && !/^[0-9]+$/.test(valor)) {
			        alert("Digite apenas números para buscar por ID!");
			        return false;
			    }
			    if (filtro === "2" && !/^[A-Za-zÀ-ÿ\\s]+$/.test(valor)) {
			        alert("Digite apenas letras para buscar por Nome!");
			        return false;
			    }
			    return true;
			}
		</script>
	</body>
</html>