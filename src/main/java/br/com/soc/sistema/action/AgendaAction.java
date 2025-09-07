package br.com.soc.sistema.action;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.ExameVo;

public class AgendaAction extends ActionSupport {

    private AgendaBusiness business = new AgendaBusiness();
    private List<AgendaVo> agendas = new ArrayList<>();
    private AgendaVo agendaVo;
    private AgendaFilter filter = new AgendaFilter();
    private List<OpcoesComboBuscar> listaOpcoesCombo = new ArrayList<>();
    private List<ExameVo> listaTodosExames = new ArrayList<>();

    public String listar() {
        agendas = business.trazerTodasAsAgendas();
        popularListas();
        return SUCCESS;
    }

    public String novo() {
        agendaVo = new AgendaVo();
        popularListas();
        return "input";
    }

    public String salvar() {
        try {
            if (agendaVo.getCodigo() == null) {
                business.salvarAgenda(agendaVo);
            } else {
                business.alterarAgenda(agendaVo);
            }
            return SUCCESS; 
        } catch (Exception e) {
            addActionError("Erro ao salvar agenda: " + e.getMessage());
            popularListas();
            return "input"; 
        }
    }

    public String editar() {
        agendaVo = business.buscarAgendaPor(agendaVo.getCodigo());
        popularListas();
        return "input";
    }

    public String excluir() {
        try {
            business.excluirAgenda(agendaVo.getCodigo());
            addActionMessage("Agenda excluída com sucesso!");
            return SUCCESS;
        } catch (Exception e) {
            addActionError("Não foi possível excluir a agenda. Verifique se ela não possui compromissos agendados.");
            e.printStackTrace();
            return ERROR;
        }
    }
    
    public String filtrar() {
        try {
            if (filter.isNullOpcoesCombo()) {
                return listar();
            }
            agendas = business.filtrarAgendas(filter);
            listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
            return SUCCESS;
        } catch (BusinessException e) {
            addActionError(e.getMessage());
            agendas = business.trazerTodasAsAgendas();
            popularListas();
            return ERROR;
        }
    }

    private void popularListas() {
        listaTodosExames = business.trazerTodosExames();
        listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
    }
    
    public List<AgendaVo> getAgendas() {
        return agendas;
    }

    public void setAgendas(List<AgendaVo> agendas) {
        this.agendas = agendas;
    }

    public AgendaVo getAgendaVo() {
        return agendaVo;
    }

    public void setAgendaVo(AgendaVo agendaVo) {
        this.agendaVo = agendaVo;
    }

    public AgendaFilter getFilter() {
        return filter;
    }

    public void setFilter(AgendaFilter filter) {
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