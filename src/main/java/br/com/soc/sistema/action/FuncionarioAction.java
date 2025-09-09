package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.infra.Action;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioAction extends Action {
	
	private List<FuncionarioVo> funcionarios = new ArrayList<>();
	private FuncionarioBusiness business = new FuncionarioBusiness();
	private FuncionarioFilter filtrar = new FuncionarioFilter();
	private FuncionarioVo funcionarioVo = new FuncionarioVo();
	private String rowid;
	
	//  Lista todos os funcionários
	public String todos() {
		funcionarios.addAll(business.trazerTodosOsFuncionarios());	
		return SUCCESS;
	}
	
	// Filtra funcionários com base no critério de busca
	public String filtrar() {
		try {
			if(filtrar.isNullOpcoesCombo())
				return REDIRECT;
			
			funcionarios = business.filtrarFuncionarios(filtrar);
			return SUCCESS;
		} catch (BusinessException e) {
			addActionError(e.getMessage());
			funcionarios.addAll(business.trazerTodosOsFuncionarios());
			return ERROR;
		}
	}
	
	// Preparar a página de cadastro de um novo funcionário
	public String novo() {
		if(funcionarioVo == null) {
			funcionarioVo = new FuncionarioVo();
		}
		return INPUT;
	}
 
	// Salva um novo funcionário ou atualiza um existente
	public String salvar() {
		try {
			business.salvarFuncionario(funcionarioVo);
			return REDIRECT;
		} catch (Exception e) {
			addActionError("Erro ao salvar funcionário: " + e.getMessage());
			e.printStackTrace();
			return INPUT;
		}
	}

	// É executado automaticamente antes do método 'salvar' para validar o nome do funcionário
	public void validateSalvar() {
	    if (funcionarioVo.getNmFuncionario() == null || funcionarioVo.getNmFuncionario().trim().isEmpty()) {
	        addFieldError("funcionarioVo.nmFuncionario", "O nome do funcionário não pode ser vazio.");
	    }
	}

	// Carrega os dados de um funcionário para edição
	public String editar() {
		if(funcionarioVo.getRowid() == null)
			return REDIRECT;
		
		funcionarioVo = business.buscarFuncionarioPor(funcionarioVo.getRowid());
		return INPUT;
	}

	// Altera um funcionário existente
	public String alterar() {
	    try {
	        business.alterarFuncionario(funcionarioVo);
	        return REDIRECT;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return INPUT;
	    }
	}
	
	// Garante que o nome do funcionário não seja vazio
	public void validateAlterar() {
		if (funcionarioVo.getNmFuncionario() == null || funcionarioVo.getNmFuncionario().trim().isEmpty()) {
	        addFieldError("funcionarioVo.nmFuncionario", "O nome do funcionário não pode ser vazio.");
	    }
	}

	// Excluir um funcionário
	public String excluir() {
	    try {
	        business.excluirFuncionario(Long.parseLong(this.rowid));
	        addActionMessage("Funcionário excluído com sucesso!");
	        return REDIRECT;
	    } catch (NumberFormatException e) {
			addActionError("Código de funcionário inválido.");
			e.printStackTrace();
			return ERROR;
		} catch (Exception e) {
	        addActionError("Não foi possível excluir o funcionário. Verifique se ele não possui compromissos agendados.");
	        e.printStackTrace();
	        return ERROR;
	    }
	}

	//Getters e Setters
	public void setRowid(String rowid) {
	    this.rowid = rowid;
	}

	public List<OpcoesComboBuscar> getListaOpcoesCombo(){
		return Arrays.asList(OpcoesComboBuscar.values());
	}
	
	public List<FuncionarioVo> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<FuncionarioVo> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public FuncionarioFilter getFiltrar() {
		return filtrar;
	}

	public void setFiltrar(FuncionarioFilter filtrar) {
		this.filtrar = filtrar;
	}

	public FuncionarioVo getFuncionarioVo() {
		return funcionarioVo;
	}

	public void setFuncionarioVo(FuncionarioVo funcionarioVo) {
		this.funcionarioVo = funcionarioVo;
	}
}