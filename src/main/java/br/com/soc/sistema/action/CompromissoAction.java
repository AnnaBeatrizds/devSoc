package br.com.soc.sistema.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.ExameVo;
import br.com.soc.sistema.vo.FuncionarioVo;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.infra.OpcoesComboBuscar;

public class CompromissoAction extends ActionSupport {

    private FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
    private AgendaBusiness agendaBusiness = new AgendaBusiness();
    private CompromissoBusiness compromissoBusiness = new CompromissoBusiness();
    
    private List<FuncionarioVo> funcionarios = new ArrayList<>();
    private List<AgendaVo> agendas = new ArrayList<>();
    private CompromissoVo compromissoVo = new CompromissoVo();
    
    private Integer idAgenda;
    private List<String> datasOcupadas;
    
    private List<ExameVo> listaExamesDisponiveis = new ArrayList<>();
    private List<String> todosOsHorarios = new ArrayList<>();
    private List<String> horariosOcupados = new ArrayList<>();
    
    private List<CompromissoVo> compromissos = new ArrayList<>();
    private CompromissoFilter filter = new CompromissoFilter();
    private List<OpcoesComboBuscar> listaOpcoesCombo = new ArrayList<>();
    
    public String listar() {
        compromissos = compromissoBusiness.trazerTodosOsCompromissos();
        listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
        return SUCCESS;
    }
    
    public String filtrar() {
        try {
            if (filter.isNullOpcoesCombo()) {
                return listar();
            }
            compromissos = compromissoBusiness.filtrarCompromissos(filter);
            listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
            return SUCCESS;
        } catch (Exception e) {
            addActionError(e.getMessage());
            compromissos = compromissoBusiness.trazerTodosOsCompromissos();
            listaOpcoesCombo = OpcoesComboBuscar.getOpcoesComboBuscar();
            return ERROR;
        }
    }
    
    
    public String agendar() {
        funcionarios = funcionarioBusiness.trazerTodosOsFuncionarios();
        agendas = agendaBusiness.trazerTodasAsAgendas();
        return INPUT;
    }

    public String buscarDatasOcupadas() {
        try {
            if (idAgenda != null) {
                datasOcupadas = compromissoBusiness.buscarDatasOcupadas(idAgenda); 
            } else {
                datasOcupadas = Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            datasOcupadas = Collections.emptyList(); 
        }
        return SUCCESS;
    }
    
    public String visualizarHorarios() {
        try {
            if (compromissoVo.getIdAgenda() == null || compromissoVo.getIdFuncionario() == null || compromissoVo.getDataCompromisso() == null || compromissoVo.getDataCompromisso().isEmpty()) {
                addActionError("Por favor, selecione a agenda, o funcionário e a data.");
                return INPUT;
            }

            AgendaVo agenda = agendaBusiness.buscarAgendaPor(compromissoVo.getIdAgenda());
            FuncionarioVo funcionario = funcionarioBusiness.buscarFuncionarioPor(compromissoVo.getIdFuncionario());

            compromissoVo.setNomeAgenda(agenda.getNmAgenda());
            compromissoVo.setNomeFuncionario(funcionario.getNmFuncionario());

            this.listaExamesDisponiveis = agenda.getExames();
            this.todosOsHorarios = gerarHorariosParaPeriodo(agenda.getPeriodo());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataConvertida = sdf.parse(compromissoVo.getDataCompromisso());
            compromissoVo.setDataCompromissoObjeto(dataConvertida);
            
            this.horariosOcupados = compromissoBusiness.buscarHorariosOcupados(agenda.getCodigo(), dataConvertida);

        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Ocorreu um erro ao buscar os horários disponíveis.");
            return ERROR;
        }
        return SUCCESS;
    }
    
    public String salvar() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataConvertida = sdf.parse(compromissoVo.getDataCompromisso());
            compromissoVo.setDataCompromissoObjeto(dataConvertida);

            if (compromissoVo.getCodigo() != null) {
                compromissoBusiness.alterarCompromisso(compromissoVo);
            } else {
                compromissoBusiness.salvarCompromisso(compromissoVo);
            }
            addActionMessage("Compromisso salvo com sucesso!");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao salvar compromisso: " + e.getMessage());
            return INPUT;
        }
    }
    
    public String excluir() {
        try {
            compromissoBusiness.excluirCompromisso(compromissoVo.getCodigo());
            addActionMessage("Compromisso excluído com sucesso!");
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao excluir o compromisso.");
            return ERROR;
        }
    }

    public String editar() {
        compromissoVo = compromissoBusiness.buscarCompromissoPorCodigo(compromissoVo.getCodigo());
        funcionarios = funcionarioBusiness.trazerTodosOsFuncionarios();
        agendas = agendaBusiness.trazerTodasAsAgendas();
        AgendaVo agenda = agendaBusiness.buscarAgendaPor(compromissoVo.getIdAgenda());
        this.listaExamesDisponiveis = agenda.getExames();
        this.todosOsHorarios = gerarHorariosParaPeriodo(agenda.getPeriodo());
        return INPUT;
    }

    private List<String> gerarHorariosParaPeriodo(String periodo) {
        List<String> horariosManha = Arrays.asList("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30");
        List<String> horariosTarde = Arrays.asList("13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30");
        
        if ("Manhã".equals(periodo)) {
            return horariosManha;
        } else if ("Tarde".equals(periodo)) {
            return horariosTarde;
        } else if ("Ambos".equals(periodo)) {
            List<String> todos = new ArrayList<>(horariosManha);
            todos.addAll(horariosTarde);
            return todos;
        }
        return Collections.emptyList();
    }
    
    public List<FuncionarioVo> getFuncionarios() { 
    	return funcionarios; 
    	}
    public void setFuncionarios(List<FuncionarioVo> funcionarios) { 
    	this.funcionarios = funcionarios; 
    	}
    public List<AgendaVo> getAgendas() { 
    	return agendas; 
    	}
    public void setAgendas(List<AgendaVo> agendas) {
    	this.agendas = agendas; 
    	}
    public CompromissoVo getCompromissoVo() {
    	return compromissoVo; 
    	}
    public void setCompromissoVo(CompromissoVo compromissoVo) { 
    	this.compromissoVo = compromissoVo; 
    	}
    public Integer getIdAgenda() { 
    	return idAgenda; 
    	}
    public void setIdAgenda(Integer idAgenda) { 
    	this.idAgenda = idAgenda; 
    	}
    public List<String> getDatasOcupadas() { 
    	return datasOcupadas; 
    	}
    public List<ExameVo> getListaExamesDisponiveis() { 
    	return listaExamesDisponiveis;
    	}
    public List<String> getTodosOsHorarios() {
    	return todosOsHorarios; 
    	}
    public List<String> getHorariosOcupados() { 
    	return horariosOcupados; 
    	}
    public List<CompromissoVo> getCompromissos() { 
    	return compromissos; 
    	}
    public List<OpcoesComboBuscar> getListaOpcoesCombo(){ 
    	return OpcoesComboBuscar.getOpcoesComboBuscar();
    	}
    public CompromissoFilter getFilter() { 
    	return filter;
    	}
    public void setFilter(CompromissoFilter filter) {
    	this.filter = filter; 
    	}
}