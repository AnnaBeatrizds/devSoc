package br.com.soc.sistema.filter;

import br.com.soc.sistema.infra.OpcoesComboBuscar;

public class EmpresaFilter {
	private OpcoesComboBuscar opcoesCombo;
	private String valorBusca;

	public String getValorBusca() {
		return valorBusca;
	}

	public EmpresaFilter setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
		return this;
	}

	public OpcoesComboBuscar getOpcoesCombo() {
		return opcoesCombo;
	}

	public EmpresaFilter setOpcoesCombo(String codigo) {
		this.opcoesCombo = OpcoesComboBuscar.buscarPor(codigo);
		return this;
	}	
	
	public boolean isNullOpcoesCombo() {
		return this.getOpcoesCombo() == null;
	}
	
	public static EmpresaFilter builder() {
		return new EmpresaFilter();
	}
}