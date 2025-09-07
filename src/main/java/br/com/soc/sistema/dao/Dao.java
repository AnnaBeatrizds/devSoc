package br.com.soc.sistema.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import br.com.soc.sistema.exception.TechnicalException;

public abstract class Dao {

	private static boolean primeiraInicializacao = true;
	
	private Connection getNewConexao() {
		StringBuilder urlBuilder = new StringBuilder("jdbc:h2:mem:avaliacao;")
										.append("DB_CLOSE_DELAY=-1;")
										.append("DATABASE_TO_UPPER=false;");
		
		if(primeiraInicializacao) {
			String caminhoAbsoluto = "C:/Users/CUMPADI WELLINGTON/Desktop/devSoc/AvaliacaoDev/src/main/resources/CRIA_TABELAS_E_INSERE_REGISTROS_INICIAIS.sql";
			urlBuilder.append("INIT=runscript from '").append(caminhoAbsoluto).append("';");
			primeiraInicializacao = false;
		}		
		
		 try {	 
			 Class.forName("org.h2.Driver");
			 return DriverManager.getConnection(urlBuilder.toString());
        } catch (SQLException ex) {
        	ex.printStackTrace();
            throw new TechnicalException("Ocorreu um problema na tentativa de conexao com o banco", ex);
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new TechnicalException("Ocorreu um problema na busca do driver de conexao", e);
		}
	}	
	
	protected Connection getConexao() {
		return getNewConexao();
	}	
}