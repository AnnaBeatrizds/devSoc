package br.com.soc.sistema.vo;

import java.util.Date;
import java.util.List;

public class CompromissoVo {

    private Integer codigo;
    private Long idFuncionario;
    private Integer idAgenda;
    private String dataCompromisso;
    private String horaCompromisso;
    private List<Integer> examesIds;
    private Date dataCompromissoObjeto;
    private String nomeFuncionario;
    private String nomeAgenda;
    private List<Integer> examesSelecionados;
    public CompromissoVo() {}

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