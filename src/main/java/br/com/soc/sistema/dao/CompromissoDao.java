package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.vo.CompromissoVo;

public class CompromissoDao extends Dao {

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
        StringBuilder query = new StringBuilder(
            "SELECT data_compromisso FROM compromisso " +
            "WHERE id_agenda = ? " +
            "GROUP BY data_compromisso " +
            "HAVING COUNT(codigo) >= ?"
        );
        
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
        try {
            con = getConexao();
            con.setAutoCommit(false);

            StringBuilder queryCompromisso = new StringBuilder(
                "INSERT INTO compromisso (id_funcionario, id_agenda, data_compromisso, hora_compromisso) VALUES (?, ?, ?, ?)"
            );
            
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
                StringBuilder queryExames = new StringBuilder("INSERT INTO compromisso_exames (compromisso_codigo, exame_id) VALUES (?, ?)");
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
        List<String> horariosOcupados = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT hora_compromisso FROM compromisso WHERE id_agenda = ? AND data_compromisso = ?"
        );
        
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            
            ps.setInt(1, idAgenda);
            ps.setDate(2, new Date(data.getTime()));
            
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
}