package br.com.soc.sistema.filter;

import br.com.soc.sistema.infra.OpcoesComboBuscar;

public class CompromissoFilter {
	private OpcoesComboBuscar opcoesCombo;
	private String valorBusca;

	public String getValorBusca() {
		return valorBusca;
	}

	public CompromissoFilter setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
		return this;
	}

	public OpcoesComboBuscar getOpcoesCombo() {
		return opcoesCombo;
	}

	public CompromissoFilter setOpcoesCombo(String codigo) {
		if (codigo != null && !codigo.isEmpty()) {
			this.opcoesCombo = OpcoesComboBuscar.buscarPor(codigo);
		}
		return this;
	}	
	
	public boolean isNullOpcoesCombo() {
		return this.getOpcoesCombo() == null;
	}
	
	public static CompromissoFilter builder() {
		return new CompromissoFilter();
	}
}