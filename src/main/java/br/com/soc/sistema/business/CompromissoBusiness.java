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
    
    private static final int VAGAS_MANHA = 8;
    private static final int VAGAS_TARDE = 8;
    private static final int VAGAS_AMBOS = 16;
    
    private static final String ERRO_AGENDA_NAO_ENCONTRADA = "Agenda selecionada não foi encontrada.";
    private static final String ERRO_DADOS_INCOMPLETOS_SALVAR = "Dados do compromisso estão incompletos para salvar.";
    private static final String ERRO_CODIGO_OBRIGATORIO = "Código do compromisso é obrigatório para a busca.";
    private static final String ERRO_HORARIO_NAO_INFORMADO = "O horário do compromisso não foi informado.";
    private static final String ERRO_VAGAS_ESGOTADAS = "Não há vagas disponíveis para a clínica na data selecionada.";
    private static final String ERRO_AGENDA_E_DATA_OBRIGATORIAS = "Agenda e data são obrigatórios para buscar horários.";
    private static final String ERRO_CODIGO_OBRIGATORIO_EXCLUSAO = "Código do compromisso é obrigatório para a exclusão.";
    private static final String ERRO_DADOS_INCOMPLETOS_ALTERAR = "Dados do compromisso estão incompletos para a alteração.";
    // Construtor 
    public CompromissoBusiness() {
        this.dao = new CompromissoDao();
        this.agendaDao = new AgendaDao();
    }
    
    // Busca as datas em que uma agenda específica já atingiu o limite de vagas
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

    // Calcula o número máximo de vagas disponíveis com base no período da agenda.
    private int calcularLimiteDeVagas(String periodo) {
        if (periodo == null) return 0;

        switch (periodo) {
            case "Manhã": return VAGAS_MANHA;
            case "Tarde": return VAGAS_TARDE;
            case "Ambos": return VAGAS_AMBOS;
            default: return 0;
        }
    }
    
    // Salva um novo compromisso no sistema
    public void salvarCompromisso(CompromissoVo compromisso) {
        if (compromisso == null || compromisso.getIdFuncionario() == null || compromisso.getIdAgenda() == null || compromisso.getDataCompromisso() == null || compromisso.getHoraCompromisso() == null) {
            throw new IllegalArgumentException(ERRO_DADOS_INCOMPLETOS_SALVAR);
        }
        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(compromisso.getIdAgenda());
        if (agenda == null) {
            throw new BusinessException(ERRO_AGENDA_NAO_ENCONTRADA );
        }
        validarHorarioPorPeriodo(compromisso.getHoraCompromisso(), agenda.getPeriodo());
        
        dao.salvar(compromisso);
    }

    // Busca os horários que já estão ocupados em uma agenda e data específicas
    public List<String> buscarHorariosOcupados(Integer idAgenda, Date data) {
        if (idAgenda == null || data == null) {
            throw new IllegalArgumentException(ERRO_AGENDA_E_DATA_OBRIGATORIAS);
        }
        return dao.buscarHorariosOcupados(idAgenda, data);
    }

    
    public List<String> buscarHorariosOcupados(Integer idAgenda, Date data, Integer codigoCompromisso) {
        if (idAgenda == null || data == null) {
            throw new IllegalArgumentException(ERRO_AGENDA_E_DATA_OBRIGATORIAS);
        }
        return dao.buscarHorariosOcupados(idAgenda, data, codigoCompromisso);
    }
    
    // Traz todos os compromissos cadastrados
    public List<CompromissoVo> trazerTodosOsCompromissos() {
        return dao.listarTodosCompromissos();
    }
    
    // Exclui um compromisso pelo seu código
    public void excluirCompromisso(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException(ERRO_CODIGO_OBRIGATORIO_EXCLUSAO );
        }
        dao.excluirCompromisso(codigo);
    }
    
    // Filtra os compromissos com base em um critério (ID ou Nome do funcionário)
    public List<CompromissoVo> filtrarCompromissos(CompromissoFilter filtro) {
        return dao.filtrarCompromissos(filtro);
    }
    
    // Busca um compromisso específico pelo seu código
    public CompromissoVo buscarCompromissoPorCodigo(Integer codigo) {
        if (codigo == null) {
            throw new IllegalArgumentException(ERRO_CODIGO_OBRIGATORIO);
        }
        return dao.buscarCompromissoPorCodigo(codigo);
    }

    // Altera um compromisso existente, incluindo validações de dados e de vagas
    public void alterarCompromisso(CompromissoVo compromisso) {
        if (compromisso == null || compromisso.getCodigo() == null) {
            throw new IllegalArgumentException(ERRO_DADOS_INCOMPLETOS_ALTERAR);
        }

        AgendaVo agenda = agendaDao.buscarAgendaPorCodigo(compromisso.getIdAgenda());
        if (agenda == null) {
            throw new BusinessException(ERRO_AGENDA_NAO_ENCONTRADA);
        }

        validarHorarioPorPeriodo(compromisso.getHoraCompromisso(), agenda.getPeriodo());

        int limiteDeVagas = calcularLimiteDeVagas(agenda.getPeriodo());
        int contagemAtual = dao.getContagemCompromissos(compromisso.getIdAgenda(), compromisso.getDataCompromissoObjeto(), compromisso.getCodigo());

        if (contagemAtual >= limiteDeVagas) {
            throw new BusinessException(ERRO_VAGAS_ESGOTADAS );
        }

        dao.alterar(compromisso);
    }
    
    // Valida se o horário escolhido é compatível com o período da agenda
    private void validarHorarioPorPeriodo(String hora, String periodo) {
        if (hora == null || hora.trim().isEmpty()) {
            throw new BusinessException(ERRO_HORARIO_NAO_INFORMADO );
        }
        
        int horaInt = Integer.parseInt(hora.split(":")[0]);

        if ("Manhã".equals(periodo) && horaInt >= 12) {
            throw new BusinessException("O horário '" + hora + "' não é compatível com o período da Manhã.");
        }

        if ("Tarde".equals(periodo) && horaInt < 12) {
            throw new BusinessException("O horário '" + hora + "' não é compatível com o período da Tarde.");
        }
    }
   
    // Busca os IDs dos exames associados a um compromisso
    public List<Integer> buscarExamesIdsPorCompromisso(Integer codigoCompromisso) {
        if (codigoCompromisso == null) {
            return Collections.emptyList();
        }
        return dao.buscarExamesIdsPorCompromisso(codigoCompromisso);
    }
    public List<CompromissoVo> filtrarCompromissosPorPeriodo(java.util.Date dataInicial, java.util.Date dataFinal) {
        return dao.filtrarCompromissosPorPeriodo(dataInicial, dataFinal);
    }
}