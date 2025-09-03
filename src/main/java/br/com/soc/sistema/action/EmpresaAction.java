package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import br.com.soc.sistema.business.EmpresaBusiness;
import br.com.soc.sistema.filter.EmpresaFilter;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.EmpresaVo;
import br.com.soc.sistema.vo.ExameVo;

public class EmpresaAction extends ActionSupport {

    private EmpresaBusiness business = new EmpresaBusiness();
    private List<EmpresaVo> empresas = new ArrayList<>();
    private EmpresaVo empresaVo;
    private EmpresaFilter filter = new EmpresaFilter();
    private List<OpcoesComboBuscar> listaOpcoesCombo = new ArrayList<>();
    private List<ExameVo> listaTodosExames = new ArrayList<>();

    public String listar() {
        empresas = business.trazerTodasAsEmpresas();
        popularListas();
        return SUCCESS;
    }

    public String novo() {
        empresaVo = new EmpresaVo();
        popularListas();
        return "input";
    }

    public String salvar() {
        try {
            if (empresaVo.getCodigo() == null) {
                business.salvarEmpresa(empresaVo);
            } else {
                business.alterarEmpresa(empresaVo);
            }
            return SUCCESS; 
        } catch (Exception e) {
            addActionError("Erro ao salvar empresa: " + e.getMessage());
            popularListas();
            return "input"; 
        }
    }

    public String editar() {
        empresaVo = business.buscarEmpresaPor(empresaVo.getCodigo());
        popularListas();
        return "input";
    }

    public String excluir() {
        business.excluirEmpresa(empresaVo.getCodigo());
        return SUCCESS;
    }

    public String filtrar() {
        if (filter.isNullOpcoesCombo()) {
            return listar();
        }
        empresas = business.filtrarEmpresas(filter);
        listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
        return SUCCESS;
    }

    private void popularListas() {
        listaTodosExames = business.trazerTodosExames();
        listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
    }

    public List<EmpresaVo> getEmpresas() {
    	return empresas;
    	}
    public EmpresaVo getEmpresaVo() {
    	return empresaVo;
    	}
    public void setEmpresaVo(EmpresaVo empresaVo) { 
    	this.empresaVo = empresaVo; 
    	}
    public EmpresaFilter getFilter() { 
    	return filter; 
    	}
    public void setFilter(EmpresaFilter filter) { 
    	this.filter = filter; 
    	}
    public List<OpcoesComboBuscar> getListaOpcoesCombo() { 
    	return listaOpcoesCombo; 
    	}
    public void setListaOpcoesCombo(List<OpcoesComboBuscar> listaOpcoesCombo) {
    	this.listaOpcoesCombo = listaOpcoesCombo;
    	}
    public List<ExameVo> getListaTodosExames() {
    	return listaTodosExames;
    	}
    public void setListaTodosExames(List<ExameVo> listaTodosExames) {
    	this.listaTodosExames = listaTodosExames;
    	}
}
