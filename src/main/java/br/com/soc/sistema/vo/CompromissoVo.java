package br.com.soc.sistema.vo;

import java.util.Date;
import java.util.List;

public class CompromissoVo {
	
	// Atributos
    private Integer codigo;
    private Long idFuncionario;
    private Integer idAgenda;
    private String dataCompromisso;
    private String horaCompromisso;
    
    // Lista de IDs dos exames associados ao compromisso
    private List<Integer> examesIds;
    
    // Representação da data do compromisso
    private Date dataCompromissoObjeto;
    
    // Atributos para informaçoes adicionais
    private String nomeFuncionario;
    private String nomeAgenda;
    
    // Lista de IDs dos exames selecionado
    private List<Integer> examesSelecionados;
    
    // Construtor Vazio
    public CompromissoVo() {}

    // Getters e Setters
    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Integer getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(Integer idAgenda) {
        this.idAgenda = idAgenda;
    }

    public String getHoraCompromisso() {
        return horaCompromisso;
    }

    public void setHoraCompromisso(String horaCompromisso) {
        this.horaCompromisso = horaCompromisso;
    }

    public List<Integer> getExamesIds() {
        return examesIds;
    }

    public void setExamesIds(List<Integer> examesIds) {
        this.examesIds = examesIds;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public String getNomeAgenda() {
        return nomeAgenda;
    }

    public void setNomeAgenda(String nomeAgenda) {
        this.nomeAgenda = nomeAgenda;
    }
    public String getDataCompromisso() { 
    	return dataCompromisso; 
    	}
    public void setDataCompromisso(String dataCompromisso) { 
    	this.dataCompromisso = dataCompromisso; 
    	}
    public Date getDataCompromissoObjeto() { 
    	return dataCompromissoObjeto; 
    	}
    public void setDataCompromissoObjeto(Date dataCompromissoObjeto) {
    	this.dataCompromissoObjeto = dataCompromissoObjeto; 
    	}
    public List<Integer> getExamesSelecionados() { 
    	return examesSelecionados; 
    	}
    public void setExamesSelecionados(List<Integer> examesSelecionados) { 
    	this.examesSelecionados = examesSelecionados; 
    	}
}