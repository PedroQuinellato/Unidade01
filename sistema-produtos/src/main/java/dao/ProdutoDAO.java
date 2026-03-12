package dao;
import model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO extends DAO {

	public ProdutoDAO() {
		super();
		conectar();
	}

	public void finalize() {
		close();
	}

	// 1) Inserir
	public boolean inserir(Produto produto) {
		boolean status = false;
		try {
			String sql = "INSERT INTO produto (nome, preco) VALUES ('" + produto.getNome() + "', " + produto.getPreco() + ");";
			Statement st = conexao.createStatement();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	// 2) Listar
	public List<Produto> listar() {
		List<Produto> produtos = new ArrayList<Produto>();
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM produto";
			ResultSet rs = st.executeQuery(sql);
			while (rs.next()) {
				Produto p = new Produto(rs.getInt("id"), rs.getString("nome"), rs.getDouble("preco"));
				produtos.add(p);
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return produtos;
	}

	// 3) Atualizar
	public boolean atualizar(Produto produto) {
		boolean status = false;
		try {
			String sql = "UPDATE produto SET nome = '" + produto.getNome() + "', preco = " + produto.getPreco() + " WHERE id = " + produto.getId();
			Statement st = conexao.createStatement();
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	// 4) Excluir
	public boolean excluir(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			String sql = "DELETE FROM produto WHERE id = " + id;
			st.executeUpdate(sql);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
}