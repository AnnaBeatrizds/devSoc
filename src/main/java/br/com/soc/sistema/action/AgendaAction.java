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

    // Lista todas as agendas
    public String listar() {
        agendas = business.trazerTodasAsAgendas();
        popularListas();
        return SUCCESS;
    }

    // Prepara o formulário de cadastro de uma nova agenda
    public String novo() {
        agendaVo = new AgendaVo();
        popularListas();
        return "input";
    }

    // Salva ou alterar uma agenda
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

    // Carregar os dados de uma agenda para edição
    public String editar() {
        agendaVo = business.buscarAgendaPor(agendaVo.getCodigo());
        popularListas();
        return "input";
    }

    // Exclui uma agenda
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
    
    // Filtra as agendas com base em no critério de busca
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

    // Popula as listas de opções (exames e combos de busca)
    private void popularListas() {
        listaTodosExames = business.trazerTodosExames();
        listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
    }
    
    // Getters e Setters
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