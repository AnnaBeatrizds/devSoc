package br.com.soc.sistema.vo;

import java.util.List;

public class AgendaVo {
    private Integer codigo;
    private String nmAgenda;
    private String periodo;
    private List<Integer> examesIds;
    private List<ExameVo> exames; 

    public AgendaVo() {}

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNmAgenda() {
        return nmAgenda;
    }

    public void setNmAgenda(String nmAgenda) {
        this.nmAgenda = nmAgenda;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<Integer> getExamesIds() {
        return examesIds;
    }

    public void setExamesIds(List<Integer> examesIds) {
        this.examesIds = examesIds;
    }

    public List<ExameVo> getExames() {
        return exames;
    }

    public void setExames(List<ExameVo> exames) {
        this.exames = exames;
    }

    @Override
    public String toString() {
        return "AgendaVo [codigo=" + codigo + ", nmAgenda=" + nmAgenda + ", periodo=" + periodo + ", examesIds="
                + examesIds + ", exames=" + exames + "]";
    }
}