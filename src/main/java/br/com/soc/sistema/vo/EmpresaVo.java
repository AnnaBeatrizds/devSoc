package br.com.soc.sistema.vo;

import java.util.List;

public class EmpresaVo {
    private Integer codigo;
    private String nome;
    private String periodo;
    private List<Integer> examesIds;

    public EmpresaVo() {}

    public EmpresaVo(Integer codigo, String nome, String periodo, List<Integer> examesIds) {
        super();
        this.codigo = codigo;
        this.nome = nome;
        this.periodo = periodo;
        this.examesIds = examesIds;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Override
    public String toString() {
        return "EmpresaVo [codigo=" + codigo + ", nome=" + nome + ", periodo=" + periodo + ", examesIds=" + examesIds + "]";
    }
}