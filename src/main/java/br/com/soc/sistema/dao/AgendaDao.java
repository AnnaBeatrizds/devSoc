package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.soc.sistema.vo.AgendaVo;
import br.com.soc.sistema.vo.ExameVo;

public class AgendaDao extends Dao {

	// Insere uma nova agenda no banco de dados
    public Integer inserirAgenda(AgendaVo agendaVo) {
        StringBuilder query = new StringBuilder("INSERT INTO agenda (nm_agenda, periodo) VALUES (?, ?)");
        try (
            Connection con = getConexao();
            PreparedStatement ps = con.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, agendaVo.getNmAgenda());
            ps.setString(2, agendaVo.getPeriodo());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lista todas as agendas cadastradas no banco de dados
    public List<AgendaVo> listarTodasAgendas() {
        List<AgendaVo> agendas = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT codigo, nm_agenda, periodo FROM agenda");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AgendaVo agenda = new AgendaVo();
                agenda.setCodigo(rs.getInt("codigo"));
                agenda.setNmAgenda(rs.getString("nm_agenda"));
                agenda.setPeriodo(rs.getString("periodo"));
                agendas.add(agenda);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return agendas;
    }

    // Busca agendas pelo nome
    public List<AgendaVo> buscarAgendaPorNome(String nome) {
        List<AgendaVo> agendas = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT codigo, nm_agenda, periodo FROM agenda WHERE lower(nm_agenda) like lower(?)");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AgendaVo vo = new AgendaVo();
                    vo.setCodigo(rs.getInt("codigo"));
                    vo.setNmAgenda(rs.getString("nm_agenda"));
                    vo.setPeriodo(rs.getString("periodo"));
                    agendas.add(vo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agendas.isEmpty() ? Collections.emptyList() : agendas;
    }

    //Busca uma agenda pelo seu código, incluindo a lista de exames associados
    public AgendaVo buscarAgendaPorCodigo(Integer codigo) {
        String sql = "SELECT a.codigo, a.nm_agenda, a.periodo, e.id AS exame_id, e.nm_exame " +
                     "FROM agenda a " +
                     "LEFT JOIN agenda_exames ae ON a.codigo = ae.agenda_codigo " +
                     "LEFT JOIN exame e ON ae.exame_id = e.id " +
                     "WHERE a.codigo = ?";

        AgendaVo agenda = null;

        try (Connection conn = getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                	// Preenche a agenda uma única vez, e adiciona os exames na lista
                    if (agenda == null) {
                        agenda = new AgendaVo();
                        agenda.setCodigo(rs.getInt("codigo"));
                        agenda.setNmAgenda(rs.getString("nm_agenda"));
                        agenda.setPeriodo(rs.getString("periodo"));
                        agenda.setExames(new ArrayList<>()); 
                    }
                    int exameId = rs.getInt("exame_id");
                    if (exameId > 0) {
                        ExameVo exame = new ExameVo();
                        exame.setId(exameId);
                        exame.setNmExame(rs.getString("nm_exame"));
                        agenda.getExames().add(exame);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agenda;
    }

    // Edita uma agenda existente, incluindo a atualização dos exames associados
    public void editarAgenda(AgendaVo agendaVo) {
        Connection con = null;
        try {
            con = getConexao();
            con.setAutoCommit(false);
            
            // Atualiza a agenda principal
            StringBuilder queryAgenda = new StringBuilder("UPDATE agenda SET nm_agenda = ?, periodo = ? WHERE codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryAgenda.toString())) {
                ps.setString(1, agendaVo.getNmAgenda());
                ps.setString(2, agendaVo.getPeriodo());
                ps.setInt(3, agendaVo.getCodigo());
                ps.executeUpdate();
            }
            
            // Exclui todos os exames associados antes de reinserir.
            StringBuilder queryDeleteExames = new StringBuilder("DELETE FROM agenda_exames WHERE agenda_codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryDeleteExames.toString())) {
                ps.setInt(1, agendaVo.getCodigo());
                ps.executeUpdate();
            }
            
            // Insere os novos exames selecionados
            if (agendaVo.getExamesIds() != null && !agendaVo.getExamesIds().isEmpty()) {
                StringBuilder queryInsertExame = new StringBuilder("INSERT INTO agenda_exames (agenda_codigo, exame_id) VALUES (?, ?)");
                try (PreparedStatement ps = con.prepareStatement(queryInsertExame.toString())) {
                    for (Integer exameId : agendaVo.getExamesIds()) {
                        ps.setInt(1, agendaVo.getCodigo());
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

    public void excluirAgenda(Integer codigo) {
        Connection con = null;
        try {
            con = getConexao();
            con.setAutoCommit(false);

            StringBuilder queryDeleteExames = new StringBuilder("DELETE FROM agenda_exames WHERE agenda_codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryDeleteExames.toString())) {
                ps.setInt(1, codigo);
                ps.executeUpdate();
            }

            StringBuilder queryDeleteAgenda = new StringBuilder("DELETE FROM agenda WHERE codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryDeleteAgenda.toString())) {
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
}
