package br.com.soc.sistema.business;

import java.util.Date;
import java.util.Collections;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoBusiness {

    private CompromissoDao dao;
    private AgendaDao agendaDao;

    public CompromissoBusiness() {
        this.dao = new CompromissoDao();
        this.agendaDao = new AgendaDao();
    }

    public List<String> buscarDatasOcupadas(Integer idAgenda) {
        if (idAgenda == null) {
            return Collections.emptyList();
        }

        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(idAgenda);
        if (agenda == null) {
            return Collections.emptyList();
        }

        int limiteDeVagas = calcularLimiteDeVagas(agenda.getPeriodo());
        
        return dao.buscarDatasOcupadasPorAgenda(idAgenda, limiteDeVagas);
    }

    private int calcularLimiteDeVagas(String periodo) {
        if (periodo == null) return 0;

        switch (periodo) {
            case "Manhã":
                return 8;
            case "Tarde":
                return 8;
            case "Ambos":
                return 16;
            default:
                return 0;
        }
    }
    
    public void salvarCompromisso(CompromissoVo compromisso) {
        if (compromisso == null || compromisso.getIdFuncionario() == null || compromisso.getIdAgenda() == null || compromisso.getDataCompromisso() == null || compromisso.getHoraCompromisso() == null) {
            throw new IllegalArgumentException("Dados do compromisso estão incompletos para salvar.");
        }
        dao.salvar(compromisso);
    }
    public List<String> buscarHorariosOcupados(Integer idAgenda, Date data) {
        if (idAgenda == null || data == null) {
            throw new IllegalArgumentException("Agenda e data são obrigatórios para buscar horários.");
        }
        return dao.buscarHorariosOcupados(idAgenda, data);
    }
    public List<CompromissoVo> trazerTodosOsCompromissos() {
        return dao.listarTodosCompromissos();
    }

    public void excluirCompromisso(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Código do compromisso é obrigatório para a exclusão.");
        }
        dao.excluirCompromisso(codigo);
    }
    public List<CompromissoVo> filtrarCompromissos(CompromissoFilter filtro) {

        return dao.filtrarCompromissos(filtro);
    }
    public CompromissoVo buscarCompromissoPorCodigo(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException("Código do compromisso é obrigatório para a busca.");
        }
        return dao.buscarCompromissoPorCodigo(codigo);
    }

    public void alterarCompromisso(CompromissoVo compromisso) {
        if (compromisso == null || compromisso.getCodigo() == null) {
            throw new IllegalArgumentException("Dados do compromisso estão incompletos para a alteração.");
        }
        dao.alterar(compromisso);
    }
    
}