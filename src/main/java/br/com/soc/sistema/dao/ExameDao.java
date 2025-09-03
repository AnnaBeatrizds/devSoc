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
        StringBuilder sql = new StringBuilder("SELECT id, nome_exame FROM exames");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ExameVo vo = new ExameVo();
                vo.setId(rs.getInt("id"));
                vo.setNomeExame(rs.getString("nome_exame"));
                exames.add(vo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exames;
    }

    public void associarExamesEmpresa(int codigoEmpresa, List<Integer> examesIds) {
        StringBuilder sql = new StringBuilder("INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (?, ?)");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (Integer exameId : examesIds) {
                ps.setInt(1, codigoEmpresa);
                ps.setInt(2, exameId);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removerExamesDaEmpresa(int codigoEmpresa) {
        StringBuilder sql = new StringBuilder("DELETE FROM empresas_exames WHERE empresa_codigo = ?");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, codigoEmpresa);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ExameVo> listarExamesPorEmpresa(int codigoEmpresa) {
        List<ExameVo> exames = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.id, e.nome_exame FROM exames e " +
            "JOIN empresas_exames ee ON e.id = ee.exame_id " +
            "WHERE ee.empresa_codigo = ?"
        );

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt(1, codigoEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExameVo vo = new ExameVo();
                    vo.setId(rs.getInt("id"));
                    vo.setNomeExame(rs.getString("nome_exame"));
                    exames.add(vo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exames;
    }
}