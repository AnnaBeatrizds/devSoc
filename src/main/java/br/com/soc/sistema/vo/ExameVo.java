package br.com.soc.sistema.vo;

public class ExameVo {
	private Integer id;
	private String nmExame;
	
	public ExameVo() {}
	
	public ExameVo(Integer id, String nmExame) {
		this.id = id;
		this.nmExame = nmExame;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNmExame() {
		return nmExame;
	}

	public void setNmExame(String nmExame) {
		this.nmExame = nmExame;
	}

	@Override
	public String toString() {
		return "ExameVo [id=" + id + ", nmExame=" + nmExame + "]";
	}
}