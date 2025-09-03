package br.com.soc.sistema.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.dao.EmpresaDao;
import br.com.soc.sistema.dao.ExameDao;
import br.com.soc.sistema.exception.BusinessException;
import br.com.soc.sistema.filter.EmpresaFilter;
import br.com.soc.sistema.vo.EmpresaVo;
import br.com.soc.sistema.vo.ExameVo;

public class EmpresaBusiness {
    private static final String ERRO_CARACTER_NUMERO = "Foi informado um caracter no lugar de um numero";
    private static final String ERRO_LISTAR_EMPRESAS = "Erro ao listar empresas";
    private static final String NOME_VAZIO = "Nome nao pode ser em branco";

    private EmpresaDao empresaDao;
    private ExameDao exameDao;

    public EmpresaBusiness() {
        this.empresaDao = new EmpresaDao();
        this.exameDao = new ExameDao();
    }

    public List<EmpresaVo> trazerTodasAsEmpresas() {
        try {
            return empresaDao.listarAllEmpresas();
        } catch (Exception e) {
            throw new BusinessException(ERRO_LISTAR_EMPRESAS, e);
        }
    }

    public EmpresaVo buscarEmpresaPor(Integer codigo) {
        EmpresaVo empresa = empresaDao.buscarEmpresaCodigo(codigo);
      
        if (empresa != null) {
            // Lógica para obter a lista de exames da empresa (para a tela de edição ou detalhes)
            List<ExameVo> exames = exameDao.listarExamesPorEmpresa(codigo);
         
            // empresa.setExames(exames);
        }

        return empresa;
    }

    public void salvarEmpresa(EmpresaVo empresa) {
        
        if(empresa.getNome() == null || empresa.getNome().isEmpty()) {
            throw new BusinessException(NOME_VAZIO);
        }
        
        Integer codigoGerado = empresaDao.inserirEmpresa(empresa);
        empresa.setCodigo(codigoGerado);

        if (empresa.getExamesIds() != null && !empresa.getExamesIds().isEmpty()) {
            exameDao.associarExamesEmpresa(codigoGerado, empresa.getExamesIds());
        }
    }

    public void alterarEmpresa(EmpresaVo empresa) {
       
        if(empresa.getNome() == null || empresa.getNome().isEmpty()) {
            throw new BusinessException(NOME_VAZIO);
        }
        
        empresaDao.editarEmpresa(empresa);
        exameDao.removerExamesDaEmpresa(empresa.getCodigo());

        if (empresa.getExamesIds() != null && !empresa.getExamesIds().isEmpty()) {
            exameDao.associarExamesEmpresa(empresa.getCodigo(), empresa.getExamesIds());
        }
    }

    public void excluirEmpresa(Integer codigo) {
        exameDao.removerExamesDaEmpresa(codigo);
        empresaDao.excluirEmpresa(codigo);
    }

    public List<ExameVo> trazerTodosExames() {
        return exameDao.listarTodosExames();
    }

    public List<EmpresaVo> filtrarEmpresas(EmpresaFilter filter) {
        if (filter == null || filter.isNullOpcoesCombo()) {
            return trazerTodasAsEmpresas();
        }

        List<EmpresaVo> empresas = new ArrayList<>();

        switch (filter.getOpcoesCombo()) {
            case ID:
                try {
                    Integer codigo = Integer.parseInt(filter.getValorBusca());
                    EmpresaVo empresa = empresaDao.buscarEmpresaCodigo(codigo);
                    if (empresa != null) empresas.add(empresa);
                } catch (NumberFormatException e) {
                    throw new BusinessException(ERRO_CARACTER_NUMERO);
                }
                break;

            case NOME:
                empresas.addAll(empresaDao.buscaEmpresaNome(filter.getValorBusca()));
                break;

            default:
                empresas = trazerTodasAsEmpresas();
                break;
        }

        return empresas;
    }
}