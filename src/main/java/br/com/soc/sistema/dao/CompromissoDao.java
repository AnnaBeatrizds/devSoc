package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.filter.CompromissoFilter;
import br.com.soc.sistema.infra.OpcoesComboBuscar;
import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoDao extends Dao {

	public List<CompromissoVo> filtrarCompromissos(CompromissoFilter filtro) {
	    List<CompromissoVo> compromissos = new ArrayList<>();
	    StringBuilder query = new StringBuilder()
	        .append("SELECT c.codigo, c.data_compromisso, c.hora_compromisso, ")
	        .append("f.rowid as id_funcionario, f.nm_funcionario, a.codigo as id_agenda, a.nm_agenda ")
	        .append("FROM compromisso c ")
	        .append("JOIN funcionario f ON c.id_funcionario = f.rowid ")
	        .append("JOIN agenda a ON c.id_agenda = a.codigo ");

	    if (filtro.getOpcoesCombo() != null && filtro.getValorBusca() != null && !filtro.getValorBusca().isEmpty()) {
	        switch (filtro.getOpcoesCombo()) {
	            case ID:
	                query.append("WHERE f.rowid = ? ");
	                break;
	            case NOME:
	                query.append("WHERE lower(f.nm_funcionario) like lower(?) ");
	                break;
	        }
	    }
	    query.append("ORDER BY c.data_compromisso, c.hora_compromisso");

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query.toString())) {

	        if (filtro.getOpcoesCombo() != null && filtro.getValorBusca() != null && !filtro.getValorBusca().isEmpty()) {
	             switch (filtro.getOpcoesCombo()) {
	                case NOME:
	                    ps.setString(1, "%" + filtro.getValorBusca() + "%");
	                    break;
	                case ID:
	                    ps.setInt(1, Integer.parseInt(filtro.getValorBusca()));
	                    break;
	            }
	        }

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                CompromissoVo vo = new CompromissoVo();
	                vo.setCodigo(rs.getInt("codigo"));
	                vo.setDataCompromissoObjeto(rs.getDate("data_compromisso"));
	                vo.setHoraCompromisso(rs.getString("hora_compromisso"));
	                vo.setIdFuncionario(rs.getLong("id_funcionario"));
	                vo.setNomeFuncionario(rs.getString("nm_funcionario"));
	                vo.setIdAgenda(rs.getInt("id_agenda"));
	                vo.setNomeAgenda(rs.getString("nm_agenda"));
	                compromissos.add(vo);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return compromissos;
	}
    
    public List<CompromissoVo> listarTodosCompromissos() {
        List<CompromissoVo> compromissos = new ArrayList<>();
        StringBuilder query = new StringBuilder()
            .append("SELECT c.codigo, c.data_compromisso, c.hora_compromisso, ")
            .append("f.rowid as id_funcionario, f.nm_funcionario, a.codigo as id_agenda, a.nm_agenda ")
            .append("FROM compromisso c ")
            .append("JOIN funcionario f ON c.id_funcionario = f.rowid ")
            .append("JOIN agenda a ON c.id_agenda = a.codigo ")
            .append("ORDER BY c.data_compromisso, c.hora_compromisso");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CompromissoVo vo = new CompromissoVo();
                vo.setCodigo(rs.getInt("codigo"));
                vo.setDataCompromissoObjeto(rs.getDate("data_compromisso"));
                vo.setHoraCompromisso(rs.getString("hora_compromisso"));
                vo.setIdFuncionario(rs.getLong("id_funcionario"));
                vo.setNomeFuncionario(rs.getString("nm_funcionario"));
                vo.setIdAgenda(rs.getInt("id_agenda"));
                vo.setNomeAgenda(rs.getString("nm_agenda"));
                compromissos.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compromissos;
    }

    public CompromissoVo buscarCompromissoPorCodigo(Integer codigo) {
        StringBuilder query = new StringBuilder(
            "SELECT c.codigo, c.id_funcionario, c.id_agenda, c.data_compromisso, c.hora_compromisso, f.nm_funcionario, a.nm_agenda " +
            "FROM compromisso c " +
            "JOIN funcionario f ON c.id_funcionario = f.rowid " +
            "JOIN agenda a ON c.id_agenda = a.codigo " +
            "WHERE c.codigo = ?"
        );
        CompromissoVo compromisso = null;
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    compromisso = new CompromissoVo();
                    compromisso.setCodigo(rs.getInt("codigo"));
                    compromisso.setIdFuncionario(rs.getLong("id_funcionario"));
                    compromisso.setIdAgenda(rs.getInt("id_agenda"));
                    compromisso.setDataCompromisso(rs.getString("data_compromisso"));
                    compromisso.setHoraCompromisso(rs.getString("hora_compromisso"));
                    compromisso.setNomeFuncionario(rs.getString("nm_funcionario"));
                    compromisso.setNomeAgenda(rs.getString("nm_agenda"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return compromisso;
    }

    public void alterar(CompromissoVo compromisso) {
        StringBuilder query = new StringBuilder(
            "UPDATE compromisso SET id_funcionario = ?, id_agenda = ?, data_compromisso = ?, hora_compromisso = ? WHERE codigo = ?"
        );
        Connection con = null;
        try {
            con = getConexao();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                ps.setLong(1, compromisso.getIdFuncionario());
                ps.setInt(2, compromisso.getIdAgenda());
                ps.setDate(3, new java.sql.Date(compromisso.getDataCompromissoObjeto().getTime()));
                ps.setString(4, compromisso.getHoraCompromisso());
                ps.setInt(5, compromisso.getCodigo());
                ps.executeUpdate();
            }

            StringBuilder queryDeleteExames = new StringBuilder("DELETE FROM compromisso_exames WHERE compromisso_codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryDeleteExames.toString())) {
                ps.setInt(1, compromisso.getCodigo());
                ps.executeUpdate();
            }

            if (compromisso.getExamesSelecionados() != null && !compromisso.getExamesSelecionados().isEmpty()) {
                StringBuilder queryInsertExames = new StringBuilder("INSERT INTO compromisso_exames (compromisso_codigo, exame_id) VALUES (?, ?)");
                try (PreparedStatement ps = con.prepareStatement(queryInsertExames.toString())) {
                    for (Integer exameId : compromisso.getExamesSelecionados()) {
                        ps.setInt(1, compromisso.getCodigo());
                        ps.setInt(2, exameId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            con.commit();
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException e1) { e1.printStackTrace(); }
            throw new RuntimeException("Erro ao alterar compromisso", e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void excluirCompromisso(Integer codigo) {
        Connection con = null;
        StringBuilder queryExames = new StringBuilder("DELETE FROM compromisso_exames WHERE compromisso_codigo = ?");
        StringBuilder queryCompromisso = new StringBuilder("DELETE FROM compromisso WHERE codigo = ?");

        try {
            con = getConexao();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(queryExames.toString())) {
                ps.setInt(1, codigo);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(queryCompromisso.toString())) {
                ps.setInt(1, codigo);
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void excluirPorFuncionarioId(Long idFuncionario) {
        StringBuilder query = new StringBuilder("DELETE FROM compromisso WHERE id_funcionario = ?");
        
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            ps.setLong(1, idFuncionario);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir compromissos por funcionário", e);
        }
    }

    public List<String> buscarDatasOcupadasPorAgenda(Integer idAgenda, int limiteDeVagas) {
        StringBuilder query = new StringBuilder()
            .append("SELECT data_compromisso FROM compromisso ")
            .append("WHERE id_agenda = ? ")
            .append("GROUP BY data_compromisso ")
            .append("HAVING COUNT(codigo) >= ?");
        
        List<String> datasOcupadas = new ArrayList<>();

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            ps.setInt(1, idAgenda);
            ps.setInt(2, limiteDeVagas);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    datasOcupadas.add(rs.getDate("data_compromisso").toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasOcupadas;
    }
    
    public void salvar(CompromissoVo compromisso) {
        Connection con = null;
        StringBuilder queryCompromisso = new StringBuilder()
            .append("INSERT INTO compromisso (id_funcionario, id_agenda, data_compromisso, hora_compromisso) ")
            .append("VALUES (?, ?, ?, ?)");
        
        StringBuilder queryExames = new StringBuilder("INSERT INTO compromisso_exames (compromisso_codigo, exame_id) VALUES (?, ?)");

        try {
            con = getConexao();
            con.setAutoCommit(false);
            
            int compromissoId = 0;
            try (PreparedStatement ps = con.prepareStatement(queryCompromisso.toString(), Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, compromisso.getIdFuncionario());
                ps.setInt(2, compromisso.getIdAgenda());
                ps.setDate(3, new Date(compromisso.getDataCompromissoObjeto().getTime()));
                ps.setString(4, compromisso.getHoraCompromisso());
                ps.executeUpdate();
                
                try(ResultSet rs = ps.getGeneratedKeys()){
                    if(rs.next()) {
                        compromissoId = rs.getInt(1);
                    }
                }
            }

            if (compromisso.getExamesIds() != null && !compromisso.getExamesIds().isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(queryExames.toString())) {
                    for (Integer exameId : compromisso.getExamesIds()) {
                        ps.setInt(1, compromissoId);
                        ps.setInt(2, exameId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }
            
            con.commit();
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException e1) { e1.printStackTrace(); }
            throw new RuntimeException("Erro ao salvar compromisso", e);
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    public List<String> buscarHorariosOcupados(Integer idAgenda, java.util.Date data) {
        return this.buscarHorariosOcupados(idAgenda, data, null);
    }
    
    public List<String> buscarHorariosOcupados(Integer idAgenda, java.util.Date data, Integer codigoCompromissoAExcluir) {
        List<String> horariosOcupados = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT hora_compromisso FROM compromisso WHERE id_agenda = ? AND data_compromisso = ?"
        );
        
        if (codigoCompromissoAExcluir != null) {
            query.append(" AND codigo != ?");
        }
        
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            ps.setInt(1, idAgenda);
            ps.setDate(2, new java.sql.Date(data.getTime()));
            
            if (codigoCompromissoAExcluir != null) {
                ps.setInt(3, codigoCompromissoAExcluir);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    horariosOcupados.add(rs.getString("hora_compromisso").substring(0, 5));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return horariosOcupados;
    }

    public int getContagemCompromissos(Integer idAgenda, java.util.Date data, Integer codigoCompromisso) {
        StringBuilder query = new StringBuilder(
            "SELECT COUNT(codigo) FROM compromisso WHERE id_agenda = ? AND data_compromisso = ?"
        );
        
        if (codigoCompromisso != null) {
            query.append(" AND codigo != ?");
        }

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {

            ps.setInt(1, idAgenda);
            ps.setDate(2, new java.sql.Date(data.getTime()));
            if (codigoCompromisso != null) {
                ps.setInt(3, codigoCompromisso);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Integer> buscarExamesIdsPorCompromisso(Integer codigoCompromisso) {
        List<Integer> examesIds = new ArrayList<>();
        String sql = "SELECT exame_id FROM compromisso_exames WHERE compromisso_codigo = ?";
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigoCompromisso);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    examesIds.add(rs.getInt("exame_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return examesIds;
    }
    public List<CompromissoVo> filtrarCompromissosPorPeriodo(java.util.Date dataInicial, java.util.Date dataFinal) {
        List<CompromissoVo> compromissos = new ArrayList<>();
        StringBuilder query = new StringBuilder()
            .append("SELECT c.codigo, c.data_compromisso, c.hora_compromisso, ")
            .append("f.rowid as id_funcionario, f.nm_funcionario, a.codigo as id_agenda, a.nm_agenda ")
            .append("FROM compromisso c ")
            .append("JOIN funcionario f ON c.id_funcionario = f.rowid ")
            .append("JOIN agenda a ON c.id_agenda = a.codigo ")
            .append("WHERE c.data_compromisso BETWEEN ? AND ? ")
            .append("ORDER BY c.data_compromisso, c.hora_compromisso");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            // Converte java.util.Date para java.sql.Date antes de passar para o PreparedStatement
            ps.setDate(1, new java.sql.Date(dataInicial.getTime()));
            ps.setDate(2, new java.sql.Date(dataFinal.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CompromissoVo vo = new CompromissoVo();
                    vo.setCodigo(rs.getInt("codigo"));
                    vo.setDataCompromissoObjeto(rs.getDate("data_compromisso"));
                    vo.setHoraCompromisso(rs.getString("hora_compromisso"));
                    vo.setIdFuncionario(rs.getLong("id_funcionario"));
                    vo.setNomeFuncionario(rs.getString("nm_funcionario"));
                    vo.setIdAgenda(rs.getInt("id_agenda"));
                    vo.setNomeAgenda(rs.getString("nm_agenda"));
                    compromissos.add(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compromissos;
    }
}