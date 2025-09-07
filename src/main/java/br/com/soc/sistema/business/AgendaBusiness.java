package br.com.soc.sistema.business;

import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.AgendaDao;
import br.com.soc.sistema.dao.ExameDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.AgendaFilter;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.ExameVo;

public class AgendaBusiness {
    private static final String ERRO_CARACTER_NUMERO = "Foi informado um caracter no lugar de um numero";
    private static final String ERRO_LISTAR_AGENDAS = "Erro ao listar agendas";
    private static final String NOME_VAZIO = "Nome nao pode ser em branco";

    private AgendaDao agendaDao;
    private ExameDao exameDao;

    public AgendaBusiness() {
        this.agendaDao = new AgendaDao();
        this.exameDao = new ExameDao();
    }

    public List<AgendaVo> trazerTodasAsAgendas() {
        try {
            return agendaDao.listarTodasAgendas();
        } catch (Exception e) {
            throw new BusinessException(ERRO_LISTAR_AGENDAS, e);
        }
    }

    public AgendaVo buscarAgendaPor(Integer codigo) {
        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(codigo);
      
        if (agenda != null) {
            List<ExameVo> examesDaAgenda = exameDao.listarExamesPorAgenda(codigo);
            agenda.setExames(examesDaAgenda);

            List<Integer> idsDosExames = new ArrayList<>();
            for(ExameVo exame : examesDaAgenda) {
                idsDosExames.add(exame.getId());
            }
            agenda.setExamesIds(idsDosExames);
        }
        return agenda;
    }

    public void salvarAgenda(AgendaVo agenda) {
        if(agenda.getNmAgenda() == null || agenda.getNmAgenda().isEmpty()) {
            throw new BusinessException(NOME_VAZIO);
        }
        
        Integer codigoGerado = agendaDao.inserirAgenda(agenda);
        agenda.setCodigo(codigoGerado);

        if (agenda.getExamesIds() != null && !agenda.getExamesIds().isEmpty()) {
            exameDao.associarExamesAgenda(codigoGerado, agenda.getExamesIds());
        }
    }

    public void alterarAgenda(AgendaVo agenda) {
        if(agenda.getNmAgenda() == null || agenda.getNmAgenda().isEmpty()) {
            throw new BusinessException(NOME_VAZIO);
        }
        agendaDao.editarAgenda(agenda);
    }

    public void excluirAgenda(Integer codigo) {
        agendaDao.excluirAgenda(codigo);
    }

    public List<ExameVo> trazerTodosExames() {
        return exameDao.listarTodosExames();
    }

    public List<AgendaVo> filtrarAgendas(AgendaFilter filter) {
        if (filter == null || filter.isNullOpcoesCombo()) {
            return trazerTodasAsAgendas();
        }

        List<AgendaVo> agendas = new ArrayList<>();

        switch (filter.getOpcoesCombo()) {
            case ID:
                try {
                    Integer codigo = Integer.parseInt(filter.getValorBusca());
                    AgendaVo agenda = buscarAgendaPor(codigo); 
                    if (agenda != null) {
                        agendas.add(agenda);
                    }
                } catch (NumberFormatException e) {
                    throw new BusinessException(ERRO_CARACTER_NUMERO);
                }
                break;
            case NOME:
                agendas.addAll(agendaDao.buscarAgendaPorNome(filter.getValorBusca()));
                break;
            default:
                agendas = trazerTodasAsAgendas();
                break;
        }
        return agendas;
    }
}