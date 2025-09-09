package br.com.soc.sistema.vo;

public class ExameVo {
	
	// Atributos
	private Integer id;
	private String nmExame;
	
	// Construtor Vazio
	public ExameVo() {}
	
	//Construtor
	public ExameVo(Integer id, String nmExame) {
		this.id = id;
		this.nmExame = nmExame;
	}
	
	// Getters e Setters
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