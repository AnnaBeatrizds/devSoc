package br.com.soc.sistema.business;

import java.util.Date;
import java.util.Collections;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.CompromissoDao;
import br.com.soc.sistema.exception.BusinessException;
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
        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(compromisso.getIdAgenda());
        if (agenda == null) {
            throw new BusinessException("Agenda selecionada não foi encontrada.");
        }
        validarHorarioPorPeriodo(compromisso.getHoraCompromisso(), agenda.getPeriodo());
        
        dao.salvar(compromisso);
    }

    public List<String> buscarHorariosOcupados(Integer idAgenda, Date data) {
        if (idAgenda == null || data == null) {
            throw new IllegalArgumentException("Agenda e data são obrigatórios para buscar horários.");
        }
        return dao.buscarHorariosOcupados(idAgenda, data);
    }

    public List<String> buscarHorariosOcupados(Integer idAgenda, Date data, Integer codigoCompromisso) {
        if (idAgenda == null || data == null) {
            throw new IllegalArgumentException("Agenda e data são obrigatórios para buscar horários.");
        }
        return dao.buscarHorariosOcupados(idAgenda, data, codigoCompromisso);
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

        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(compromisso.getIdAgenda());
        if (agenda == null) {
            throw new BusinessException("Agenda selecionada não foi encontrada.");
        }

        validarHorarioPorPeriodo(compromisso.getHoraCompromisso(), agenda.getPeriodo());

        int limiteDeVagas = calcularLimiteDeVagas(agenda.getPeriodo());
        int contagemAtual = dao.getContagemCompromissos(compromisso.getIdAgenda(), compromisso.getDataCompromissoObjeto(), compromisso.getCodigo());

        if (contagemAtual >= limiteDeVagas) {
            throw new BusinessException("Não há vagas disponíveis para a clínica na data selecionada.");
        }

        dao.alterar(compromisso);
    }
    
    private void validarHorarioPorPeriodo(String hora, String periodo) {
        if (hora == null || hora.trim().isEmpty()) {
            throw new BusinessException("O horário do compromisso não foi informado.");
        }
        
        int horaInt = Integer.parseInt(hora.split(":")[0]);

        if ("Manhã".equals(periodo) && horaInt >= 12) {
            throw new BusinessException("O horário '" + hora + "' não é compatível com o período da Manhã.");
        }

        if ("Tarde".equals(periodo) && horaInt < 12) {
            throw new BusinessException("O horário '" + hora + "' não é compatível com o período da Tarde.");
        }
    }
   
    public List<Integer> buscarExamesIdsPorCompromisso(Integer codigoCompromisso) {
        if (codigoCompromisso == null) {
            return Collections.emptyList();
        }
        return dao.buscarExamesIdsPorCompromisso(codigoCompromisso);
    }
}