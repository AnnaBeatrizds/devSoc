package br.com.soc.sistema.vo;

public class FuncionarioVo {
	
	// Atributos
	private Long rowid;
	private String nmFuncionario;	
	
	// Construtor vazio para o Struts
	public FuncionarioVo() {}
	
	// Construtor
	public FuncionarioVo(Long rowid, String nmFuncionario) {
		this.rowid = rowid;
		this.nmFuncionario = nmFuncionario;
	}
	
	// Getters e Setters
	public Long getRowid() {
		return rowid;
	}
    
	public void setRowid(Long rowid) {
		this.rowid = rowid;
	}
    
	public String getNmFuncionario() {
		return nmFuncionario;
	}
    
	public void setNmFuncionario(String nmFuncionario) {
		this.nmFuncionario = nmFuncionario;
	}
	
	@Override
	public String toString() {
		return "FuncionarioVo [rowid=" + rowid + ", nmFuncionario=" + nmFuncionario + "]";
	}
}