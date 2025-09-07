package br.com.soc.sistema.vo;

public class FuncionarioVo {
	private Long rowid;
	private String nmFuncionario;	
	
	public FuncionarioVo() {}
		
	public FuncionarioVo(Long rowid, String nmFuncionario) {
		this.rowid = rowid;
		this.nmFuncionario = nmFuncionario;
	}

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