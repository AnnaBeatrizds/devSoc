package br.com.soc.sistema.business;

import java.util.List;

import br.com.soc.sistema.dao.ExameDao;
import br.com.soc.sistema.vo.ExameVo;

public class ExameBusiness {
	
	// Instância da classe de acesso a dados de Exame.
	private ExameDao dao;

	// Construtor 
    public ExameBusiness() {
        this.dao = new ExameDao();
    }
    
    // Busca e retorna uma lista de todos os exames
    public List<ExameVo> trazerTodosExames() {
        return dao.listarTodosExames();
    }
}
