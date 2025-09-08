<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF8">
		<title><s:text name="label.titulo.pagina.cadastro"/></title>
		<s:url value="/webjars/bootstrap/5.1.3/css/bootstrap.min.css" var="bootstrap_css" />
		<link rel='stylesheet' href='${bootstrap_css}'>
	</head>
	<body class="bg-light">

		<div class="container">
			<s:url action="%{funcionarioVo.rowid != null ? 'alterarFuncionarios' : 'salvarFuncionarios'}" var="formAction"/>
			<s:form id="formFuncionario" action="%{formAction}">

				<div class="card mt-5">
					<div class="card-header">
						<div class="row">
							<div class="col-sm-5">
								<s:url action="todosFuncionarios" var="todos"/>
								<a href="${todos}" class="btn btn-success"><s:text name="label.funcionarios"/></a>
							</div>
							
							<div class="col-sm">
								<s:if test="funcionarioVo.rowid != null">
									<h5 class="card-title"><s:text name="label.editarFuncionario"/></h5>
								</s:if>
								<s:else>
									<h5 class="card-title"><s:text name="label.novoFuncionario"/></h5>
								</s:else>
							</div>
						</div>
					</div>
					
					<div class="card-body">
						<s:if test="hasFieldErrors()">
							<div class="alert alert-danger">
								<s:fielderror />
							</div>
						</s:if>
					
						<div class="row align-items-center">
							<label for="id" class="col-sm-1 col-form-label text-center">
    							<s:text name="label.codigo"/>
							</label>	

							<div class="col-sm-2">
								<s:textfield cssClass="form-control" id="id" value="%{funcionarioVo.rowid}" readonly="true"/>
								<s:hidden name="funcionarioVo.rowid" />
							</div>	
						</div>
						
						<div class="row align-items-center mt-3">
							<label for="nome" class="col-sm-1 col-form-label text-center">
    							<s:text name="label.nome"/>
							</label>
	
							<div class="col-sm-5">
								<s:textfield cssClass="form-control" id="nome" name="funcionarioVo.nmFuncionario" required="true"/>							
							</div>	
						</div>
					</div>

					<div class="card-footer">
						<div class="form-row">
							<s:submit value="%{getText('label.salvar')}" cssClass="btn btn-primary col-sm-4 offset-sm-1"/>
							<button type="reset" class="btn btn-secondary col-sm-4 offset-sm-2"><s:text name="label.limparFormulario"/></button>
						</div>
					</div>
				</div>
			</s:form>			
		</div>
		
		<s:url value="/webjars/bootstrap/5.1.3/js/bootstrap.bundle.min.js" var="bootstrap_js" />
		<script src="${bootstrap_js}"></script>
	</body>
</html>