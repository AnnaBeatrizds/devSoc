package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.FuncionarioDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.FuncionarioFilter;
import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioBusiness {
    private static final String ERRO_CARACTER_NUMERO = "Foi informado um caracter no lugar de um numero";
    private static final String NOME_VAZIO = "Nome nao pode ser em branco";
    private static final String ERRO_INCLUSAO = "Nao foi possivel realizar a inclusao do registro";
    private static final String ERRO_ALTERACAO = "Nao foi possivel realizar a alteracao do registro";

	private FuncionarioDao dao;
	
	public FuncionarioBusiness() {
		this.dao = new FuncionarioDao();
	}
	
	public List<FuncionarioVo> trazerTodosOsFuncionarios(){
		return dao.findAllFuncionarios();
	}	
	
	public void salvarFuncionario(FuncionarioVo funcionarioVo) {
		try {
			if(funcionarioVo.getNome().isEmpty())
				throw new IllegalArgumentException(NOME_VAZIO);
			
			dao.insertFuncionario(funcionarioVo);
		} catch (Exception e) {
			throw new BusinessException(ERRO_INCLUSAO);
		}
	}	
	
	public List<FuncionarioVo> filtrarFuncionarios(FuncionarioFilter filter){
		List<FuncionarioVo> funcionarios = new ArrayList<>();
		
		switch (filter.getOpcoesCombo()) {
			case ID:
				try {
					Integer codigo = Integer.parseInt(filter.getValorBusca());
					funcionarios.add(dao.findByCodigo(codigo));
				}catch (NumberFormatException e) {
					throw new BusinessException(ERRO_CARACTER_NUMERO);
				}
			break;

			case NOME:
				funcionarios.addAll(dao.findAllByNome(filter.getValorBusca()));
			break;
		}
		
		return funcionarios;
	}
	
	public FuncionarioVo buscarFuncionarioPor(String codigo) {
		try {
			Integer cod = Integer.parseInt(codigo);
			return dao.findByCodigo(cod);
		}catch (NumberFormatException e) {
			throw new BusinessException(ERRO_CARACTER_NUMERO);
		}
	}
	
	public void excluirFuncionario(String rowid) throws Exception {
	    //compromissoBusiness.excluirCompromissosPorFuncionario(rowid);

	    dao.excluir(rowid);
	}
	public void alterarFuncionario(FuncionarioVo funcionarioVo) throws Exception {
	    try {
	        if(funcionarioVo.getNome().isEmpty()) {
	            throw new IllegalArgumentException(NOME_VAZIO);
	        }
	        dao.atualizarFuncionario(funcionarioVo);
	    } catch (Exception e) {
	        throw new Exception(ERRO_ALTERACAO);
	    }
	}
}