package dao;
import java.sql.*;

public class DAO {
	protected Connection conexao;

	public DAO() {
		conexao = null;
	}

	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String mydatabase = "postgres"; // Se você criou um banco com outro nome, mude aqui!
		int serverPort = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + serverPort + "/" + mydatabase;
		String username = "ti2cc";
		String password = "ti@cc";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao != null);
		} catch (ClassNotFoundException e) {
			System.err.println("Conexão falhou: Driver não encontrado.");
		} catch (SQLException e) {
			System.err.println("Conexão falhou: " + e.getMessage());
		}
		return status;
	}

	public boolean close() {
		boolean status = false;
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}
}