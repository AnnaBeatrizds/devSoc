package br.com.soc.sistema.action;

import com.opensymphony.xwork2.ActionSupport;

public class DashboardAction extends ActionSupport {

    @Override
    public String execute() {
        //Nos leva para a página do dashboard.
        //Nenhum dado precisa ser carregado, já que o conteúdo será estático.
        return SUCCESS;
    }
}
