package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.soc.sistema.vo.FuncionarioVo;

public class FuncionarioDao extends Dao {
	
	// Insere um novo funcionário no banco de dados
	public void insertFuncionario(FuncionarioVo funcionarioVo){
		StringBuilder query = new StringBuilder("INSERT INTO funcionario (nm_funcionario) values (?)");
		try(
			Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query.toString())){
			
			ps.setString(1, funcionarioVo.getNmFuncionario());
			ps.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
            throw new RuntimeException("Erro ao inserir funcionário", e);
		}
	}
	
	// Busca todos os funcionários cadastrados no banco de dados
	public List<FuncionarioVo> findAllFuncionarios(){
		StringBuilder query = new StringBuilder("SELECT rowid, nm_funcionario FROM funcionario");
		try(
			Connection con = getConexao();
			PreparedStatement  ps = con.prepareStatement(query.toString());
			ResultSet rs = ps.executeQuery()){
			
			List<FuncionarioVo> funcionarios = new ArrayList<>();
			while (rs.next()) {
				FuncionarioVo vo = new FuncionarioVo();
				vo.setRowid(rs.getLong("rowid"));
				vo.setNmFuncionario(rs.getString("nm_funcionario"));	
				funcionarios.add(vo);
			}
			return funcionarios;
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
	
	// Busca funcionarios pelo nome
	public List<FuncionarioVo> findAllByNome(String nome){
		StringBuilder query = new StringBuilder("SELECT rowid, nm_funcionario FROM funcionario ")
								.append("WHERE lower(nm_funcionario) like lower(?)");
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())){
			
			ps.setString(1, "%"+nome+"%");
			
			try(ResultSet rs = ps.executeQuery()){
				List<FuncionarioVo> funcionarios = new ArrayList<>();
				while (rs.next()) {
					FuncionarioVo vo = new FuncionarioVo();
					vo.setRowid(rs.getLong("rowid"));
					vo.setNmFuncionario(rs.getString("nm_funcionario"));	
					funcionarios.add(vo);
				}
				return funcionarios;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}		
		return Collections.emptyList();
	}
	
	// Busca funcionarios pelo ID
	public FuncionarioVo findByCodigo(Long codigo){
		StringBuilder query = new StringBuilder("SELECT rowid, nm_funcionario FROM funcionario ")
								.append("WHERE rowid = ?");
		
		try(Connection con = getConexao();
			PreparedStatement ps = con.prepareStatement(query.toString())){
			
			ps.setLong(1, codigo);
			
			try(ResultSet rs = ps.executeQuery()){
				FuncionarioVo vo =  null;
				if (rs.next()) {
					vo = new FuncionarioVo();
					vo.setRowid(rs.getLong("rowid"));
					vo.setNmFuncionario(rs.getString("nm_funcionario"));	
				}
				return vo;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}
	
	// Exclui um funcionario pelo id
	public void excluir(Long rowid) {
	    StringBuilder query = new StringBuilder("DELETE FROM funcionario WHERE rowid = ?");
	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query.toString())) {
	        ps.setLong(1, rowid);
	        ps.executeUpdate();
	    } catch (SQLException e) {
	        throw new RuntimeException("Erro ao excluir funcionário", e);
	    }
	}
	
	// Atualiza os dados de um funcionário no banco de dados
	public void atualizarFuncionario(FuncionarioVo funcionarioVo) {
	    StringBuilder query = new StringBuilder("UPDATE funcionario SET nm_funcionario = ? WHERE rowid = ?");

	    try (Connection con = getConexao();
	         PreparedStatement ps = con.prepareStatement(query.toString())) {

	        ps.setString(1, funcionarioVo.getNmFuncionario());
	        ps.setLong(2, funcionarioVo.getRowid());
	        ps.executeUpdate();

	    } catch (SQLException e) {
	        throw new RuntimeException("Erro ao atualizar funcionário", e);
	    }
	}
}