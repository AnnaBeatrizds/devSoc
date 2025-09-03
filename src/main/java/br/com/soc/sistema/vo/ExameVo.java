package br.com.soc.sistema.vo;

public class ExameVo {
	private Integer id;
	private String nomeExame;
	
	public ExameVo() {}
	
	public ExameVo(Integer id, String nomeExame) {
		this.id = id;
		this.nomeExame = nomeExame;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomeExame() {
		return nomeExame;
	}

	public void setNomeExame(String nomeExame) {
		this.nomeExame = nomeExame;
	}

	@Override
	public String toString() {
		return "ExameVo [id=" + id + ", nomeExame=" + nomeExame + "]";
	}
}