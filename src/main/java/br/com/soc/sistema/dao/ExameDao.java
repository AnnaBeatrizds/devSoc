package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.soc.sistema.vo.ExameVo;

public class ExameDao extends Dao {

    public List<ExameVo> listarTodosExames() {
        List<ExameVo> exames = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nm_exame FROM exame");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ExameVo vo = new ExameVo();
                vo.setId(rs.getInt("id"));
                vo.setNmExame(rs.getString("nm_exame"));
                exames.add(vo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exames;
    }

    public void associarExamesAgenda(Integer codigoAgenda, List<Integer> examesIds) {
        StringBuilder sql = new StringBuilder("INSERT INTO agenda_exames (agenda_codigo, exame_id) VALUES (?, ?)");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (Integer exameId : examesIds) {
                ps.setInt(1, codigoAgenda);
                ps.setInt(2, exameId);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerExamesDaAgenda(Integer codigoAgenda) {
        StringBuilder sql = new StringBuilder("DELETE FROM agenda_exames WHERE agenda_codigo = ?");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, codigoAgenda);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ExameVo> listarExamesPorAgenda(Integer codigoAgenda) {
        List<ExameVo> exames = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.id, e.nm_exame FROM exame e " +
            "JOIN agenda_exames ae ON e.id = ae.exame_id " +
            "WHERE ae.agenda_codigo = ?"
        );

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, codigoAgenda);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExameVo vo = new ExameVo();
                    vo.setId(rs.getInt("id"));
                    vo.setNmExame(rs.getString("nm_exame"));
                    exames.add(vo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exames;
    }
}