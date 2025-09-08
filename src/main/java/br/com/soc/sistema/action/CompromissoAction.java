package br.com.soc.sistema.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;

import br.com.soc.sistema.business.AgendaBusiness;
import br.com.soc.sistema.business.CompromissoBusiness;
import br.com.soc.sistema.business.FuncionarioBusiness;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.CompromissoVo;
import br.com.soc.sistema.vo.ExameVo;
import br.com.soc.sistema.vo.FuncionarioVo;

public class CompromissoAction extends ActionSupport {
    
    private FuncionarioBusiness funcionarioBusiness = new FuncionarioBusiness();
    private AgendaBusiness agendaBusiness = new AgendaBusiness();
    private CompromissoBusiness compromissoBusiness = new CompromissoBusiness();
    

    private List<FuncionarioVo> funcionarios = new ArrayList<>();
    private List<AgendaVo> agendas = new ArrayList<>();
    private CompromissoVo compromissoVo = new CompromissoVo();
    
    private List<ExameVo> listaExamesDisponiveis = new ArrayList<>();
    private List<String> todosOsHorarios = new ArrayList<>();
    private List<String> horariosOcupados = new ArrayList<>();
    

    private List<CompromissoVo> compromissos = new ArrayList<>();
    private CompromissoFilter filter = new CompromissoFilter();
    
    private Date dataInicial;
    private Date dataFinal;
    private InputStream inputStream;
    private String fileName;

    public String editar() {
        try {
            
            CompromissoVo compromissoDB = compromissoBusiness.buscarCompromissoPorCodigo(compromissoVo.getCodigo());
            if (compromissoDB == null) {
                addActionError("Compromisso não encontrado.");
                return ERROR;
            }
            this.compromissoVo = compromissoDB;

            List<Integer> examesSelecionados = compromissoBusiness.buscarExamesIdsPorCompromisso(compromissoVo.getCodigo());
            this.compromissoVo.setExamesSelecionados(examesSelecionados);

            this.funcionarios = funcionarioBusiness.trazerTodosOsFuncionarios();
            this.agendas = agendaBusiness.trazerTodasAsAgendas();
            
            AgendaVo agenda = agendaBusiness.buscarAgendaPor(compromissoVo.getIdAgenda());
            this.listaExamesDisponiveis = agenda.getExames();
            this.todosOsHorarios = gerarHorariosParaPeriodo(agenda.getPeriodo());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dataOriginal = sdf.parse(compromissoVo.getDataCompromisso());
            this.horariosOcupados = compromissoBusiness.buscarHorariosOcupados(compromissoVo.getIdAgenda(), dataOriginal, compromissoVo.getCodigo());
            
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao carregar os dados para edição.");
        }
        return INPUT;
    }

    public String recarregarHorariosEdicao() {
        try {
            
            this.funcionarios = funcionarioBusiness.trazerTodosOsFuncionarios();
            this.agendas = agendaBusiness.trazerTodasAsAgendas();
            
            AgendaVo agenda = agendaBusiness.buscarAgendaPor(compromissoVo.getIdAgenda());
            this.listaExamesDisponiveis = agenda.getExames();
            this.todosOsHorarios = gerarHorariosParaPeriodo(agenda.getPeriodo());
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date novaData = sdf.parse(compromissoVo.getDataCompromisso());
            this.horariosOcupados = compromissoBusiness.buscarHorariosOcupados(compromissoVo.getIdAgenda(), novaData, compromissoVo.getCodigo());

        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao recarregar os horários.");
        }
        return INPUT;
    }

    public String salvar() {
        try {
            if (compromissoVo.getExamesSelecionados() != null) {
                compromissoVo.setExamesIds(compromissoVo.getExamesSelecionados());
            }

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
        } catch (BusinessException e) {
            addActionError(e.getMessage()); 
            recarregarHorariosEdicao(); 
            return INPUT; 
        } catch (Exception e) {
            addActionError("Erro ao salvar compromisso: " + e.getMessage());
            recarregarHorariosEdicao();
            return INPUT;
        }
    }


    public String listar() {
        compromissos = compromissoBusiness.trazerTodosOsCompromissos();
        return SUCCESS;
    }
    
    public String filtrar() {
        try {
            if (filter.isNullOpcoesCombo()) return listar();
            compromissos = compromissoBusiness.filtrarCompromissos(filter);
            return SUCCESS;
        } catch (Exception e) {
            addActionError(e.getMessage());
            compromissos = compromissoBusiness.trazerTodosOsCompromissos();
            return ERROR;
        }
    }
    
    public String agendar() {
        funcionarios = funcionarioBusiness.trazerTodosOsFuncionarios();
        agendas = agendaBusiness.trazerTodasAsAgendas();
        return INPUT;
    }
    
    public String visualizarHorarios() {
        try {
            if (compromissoVo.getIdAgenda() == null || compromissoVo.getIdFuncionario() == null || compromissoVo.getDataCompromisso() == null || compromissoVo.getDataCompromisso().isEmpty()) {
                addActionError("Por favor, selecione a agenda, o funcionário e a data.");
                agendar();
                return INPUT;
            }

            AgendaVo agenda = agendaBusiness.buscarAgendaPor(compromissoVo.getIdAgenda());
          
            if (agenda == null) {
                addActionError("Agenda selecionada não foi encontrada.");
                agendar();
                return ERROR;
            }
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

    private List<String> gerarHorariosParaPeriodo(String periodo) {
        List<String> horariosManha = Arrays.asList("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30");
        List<String> horariosTarde = Arrays.asList("13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30");
        if ("Manhã".equals(periodo)) return horariosManha;
        if ("Tarde".equals(periodo)) return horariosTarde;
        if ("Ambos".equals(periodo)) {
            List<String> todos = new ArrayList<>(horariosManha);
            todos.addAll(horariosTarde);
            return todos;
        }
        return Collections.emptyList();
    }
    public String relatorio() {
        return "relatorio";
    }

    public String gerarRelatorioHTML() {
        try {
            if (dataInicial == null || dataFinal == null) {
                addActionError("As datas de início e fim são obrigatórias.");
                return "relatorio";
            }
            compromissos = compromissoBusiness.filtrarCompromissosPorPeriodo(dataInicial, dataFinal);
            return "relatorio";
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao gerar o relatório HTML.");
            return "relatorio";
        }
    }

 
    public String gerarRelatorioExcel() {
        try {
            if (dataInicial == null || dataFinal == null) {
                addActionError("As datas de início e fim são obrigatórias.");
                return "relatorio";
            }

            compromissos = compromissoBusiness.filtrarCompromissosPorPeriodo(dataInicial, dataFinal);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Relatório de Compromissos");

            int rowNum = 0;
            XSSFRow headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("Código Funcionário");
            headerRow.createCell(1).setCellValue("Nome Funcionário");
            headerRow.createCell(2).setCellValue("Código Agenda");
            headerRow.createCell(3).setCellValue("Nome Agenda");
            headerRow.createCell(4).setCellValue("Data Compromisso");
            headerRow.createCell(5).setCellValue("Hora Compromisso");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (CompromissoVo compromisso : compromissos) {
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(compromisso.getIdFuncionario());
                row.createCell(1).setCellValue(compromisso.getNomeFuncionario());
                row.createCell(2).setCellValue(compromisso.getIdAgenda());
                row.createCell(3).setCellValue(compromisso.getNomeAgenda());
                row.createCell(4).setCellValue(sdf.format(compromisso.getDataCompromissoObjeto()));
                row.createCell(5).setCellValue(compromisso.getHoraCompromisso());
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                workbook.write(bos);
            } finally {
                bos.close();
                workbook.close();
            }

            byte[] bytes = bos.toByteArray();
            inputStream = new ByteArrayInputStream(bytes);
            fileName = "relatorio_compromissos_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".xlsx";

            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            addActionError("Erro ao gerar o relatório Excel.");
            return "relatorio";
        }
    }

    public Date getDataInicial() { return dataInicial; }
    public void setDataInicial(Date dataInicial) { this.dataInicial = dataInicial; }
    public Date getDataFinal() { return dataFinal; }
    public void setDataFinal(Date dataFinal) { this.dataFinal = dataFinal; }
    public InputStream getInputStream() { return inputStream; }
    public String getFileName() { return fileName; }
    
    public List<FuncionarioVo> getFuncionarios() { return funcionarios; }
    public void setFuncionarios(List<FuncionarioVo> f) { this.funcionarios = f; }
    public List<AgendaVo> getAgendas() { return agendas; }
    public void setAgendas(List<AgendaVo> a) { this.agendas = a; }
    public CompromissoVo getCompromissoVo() { return compromissoVo; }
    public void setCompromissoVo(CompromissoVo c) { this.compromissoVo = c; }
    public List<ExameVo> getListaExamesDisponiveis() { return listaExamesDisponiveis; }
    public List<String> getTodosOsHorarios() { return todosOsHorarios; }
    public List<String> getHorariosOcupados() { return horariosOcupados; }
    public List<CompromissoVo> getCompromissos() { return compromissos; }
    public CompromissoFilter getFilter() { return filter; }
    public void setFilter(CompromissoFilter f) { this.filter = f; }
    public List<OpcoesComboBuscar> getListaOpcoesCombo(){ return OpcoesComboBuscar.getOpcoesComboBuscar(); }
}