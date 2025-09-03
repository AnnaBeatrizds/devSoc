package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.soc.sistema.vo.EmpresaVo;
import br.com.soc.sistema.vo.ExameVo;

public class EmpresaDao extends Dao {

    public Integer inserirEmpresa(EmpresaVo empresaVo) {
        StringBuilder query = new StringBuilder("INSERT INTO empresas (nome_empresa, periodo) VALUES (?, ?)");
        try (
            Connection con = getConexao();
            PreparedStatement ps = con.prepareStatement(query.toString(), PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, empresaVo.getNome());
            ps.setString(2, empresaVo.getPeriodo());
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

    public List<EmpresaVo> listarAllEmpresas() throws SQLException {
        List<EmpresaVo> empresas = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM empresas");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString());
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EmpresaVo e = new EmpresaVo();
                e.setCodigo(rs.getInt("codigo"));
                e.setNome(rs.getString("nome_empresa"));
                e.setPeriodo(rs.getString("periodo"));
                empresas.add(e);
            }
        }
        return empresas;
    }

    public List<EmpresaVo> buscaEmpresaNome(String nome) {
        List<EmpresaVo> empresas = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT codigo, nome_empresa, periodo FROM empresas WHERE lower(nome_empresa) like lower(?)");

        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EmpresaVo vo = new EmpresaVo();
                    vo.setCodigo(rs.getInt("codigo"));
                    vo.setNome(rs.getString("nome_empresa"));
                    vo.setPeriodo(rs.getString("periodo"));
                    empresas.add(vo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return empresas.isEmpty() ? Collections.emptyList() : empresas;
    }

    public EmpresaVo buscarEmpresaCodigo(Integer codigo) {
        StringBuilder query = new StringBuilder("SELECT codigo, nome_empresa, periodo FROM empresas WHERE codigo = ?");
        try (Connection con = getConexao();
             PreparedStatement ps = con.prepareStatement(query.toString())) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    EmpresaVo vo = new EmpresaVo();
                    vo.setCodigo(rs.getInt("codigo"));
                    vo.setNome(rs.getString("nome_empresa"));
                    vo.setPeriodo(rs.getString("periodo"));
                    return vo;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editarEmpresa(EmpresaVo empresaVo) {
        StringBuilder queryEmpresa = new StringBuilder("UPDATE empresas SET nome_empresa = ?, periodo = ? WHERE codigo = ?");

        try (Connection con = getConexao()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(queryEmpresa.toString())) {
                ps.setString(1, empresaVo.getNome());
                ps.setString(2, empresaVo.getPeriodo());
                ps.setInt(3, empresaVo.getCodigo());
                ps.executeUpdate();
            }

            StringBuilder queryDeleteExames = new StringBuilder("DELETE FROM empresas_exames WHERE empresa_codigo = ?");
            try (PreparedStatement ps = con.prepareStatement(queryDeleteExames.toString())) {
                ps.setInt(1, empresaVo.getCodigo());
                ps.executeUpdate();
            }

            if (empresaVo.getExamesIds() != null) {
                StringBuilder queryInsertExame = new StringBuilder("INSERT INTO empresas_exames (empresa_codigo, exame_id) VALUES (?, ?)");
                try (PreparedStatement ps = con.prepareStatement(queryInsertExame.toString())) {
                    for (Integer exameId : empresaVo.getExamesIds()) {
                        ps.setInt(1, empresaVo.getCodigo());
                        ps.setInt(2, exameId);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluirEmpresa(Integer codigo) {
        StringBuilder queryDeleteExames = new StringBuilder("DELETE FROM empresas_exames WHERE empresa_codigo = ?");
        StringBuilder queryDeleteEmpresa = new StringBuilder("DELETE FROM empresas WHERE codigo = ?");

        try (Connection con = getConexao()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(queryDeleteExames.toString())) {
                ps.setInt(1, codigo);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(queryDeleteEmpresa.toString())) {
                ps.setInt(1, codigo);
                ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}