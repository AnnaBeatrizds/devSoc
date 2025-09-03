package br.com.soc.sistema.business;

import java.util.List;

import br.com.soc.sistema.dao.ExameDao;
import br.com.soc.sistema.vo.ExameVo;

public class ExameBusiness {
	private ExameDao dao;

    public ExameBusiness() {
        this.dao = new ExameDao();
    }

    public List<ExameVo> trazerTodosExames() {
        return dao.listarTodosExames();
    }
}
