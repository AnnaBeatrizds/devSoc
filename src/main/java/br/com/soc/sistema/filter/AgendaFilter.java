package br.com.soc.sistema.filter;

import br.com.soc.sistema.infra.OpcoesComboBuscar;

public class AgendaFilter {
	private OpcoesComboBuscar opcoesCombo;
	private String valorBusca;

	public String getValorBusca() {
		return valorBusca;
	}

	public AgendaFilter setValorBusca(String valorBusca) {
		this.valorBusca = valorBusca;
		return this;
	}

	public OpcoesComboBuscar getOpcoesCombo() {
		return opcoesCombo;
	}

	public AgendaFilter setOpcoesCombo(String codigo) {
		if (codigo != null && !codigo.isEmpty()) {
			this.opcoesCombo = OpcoesComboBuscar.buscarPor(codigo);
		}
		return this;
	}	
	
	public boolean isNullOpcoesCombo() {
		return this.getOpcoesCombo() == null;
	}
	
	public static AgendaFilter builder() {
		return new AgendaFilter();
	}
}