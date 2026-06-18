package com.exemplo.produto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Produto.
 * Contém todos os métodos de acesso ao banco de dados (CRUD).
 */
public class ProdutoDAO {

    // ── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Insere um novo produto no banco de dados.
     * @return true se inserido com sucesso, false caso contrário.
     */
    public boolean inserir(Produto p) {
        String sql = "INSERT INTO produto (nome, descricao, preco, quantidade) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setDouble(3, p.getPreco());
            ps.setInt(4, p.getQuantidade());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Erro ao inserir produto: " + e.getMessage());
            return false;
        }
    }

    // ── READ (todos) ─────────────────────────────────────────────────────────

    /**
     * Retorna todos os produtos cadastrados.
     */
    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, quantidade FROM produto ORDER BY id";

        try (Connection conn = ConexaoDB.getConexao();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Produto p = new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("descricao"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Erro ao listar produtos: " + e.getMessage());
        }
        return lista;
    }

    // ── READ (por ID) ────────────────────────────────────────────────────────

    /**
     * Busca um produto pelo seu ID.
     * @return Produto encontrado ou null.
     */
    public Produto buscarPorId(int id) {
        String sql = "SELECT id, nome, descricao, preco, quantidade FROM produto WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descricao"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("[DAO] Erro ao buscar produto por ID: " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Atualiza os dados de um produto existente.
     * @return true se atualizado com sucesso.
     */
    public boolean atualizar(Produto p) {
        String sql = "UPDATE produto SET nome=?, descricao=?, preco=?, quantidade=? WHERE id=?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNome());
            ps.setString(2, p.getDescricao());
            ps.setDouble(3, p.getPreco());
            ps.setInt(4, p.getQuantidade());
            ps.setInt(5, p.getId());

            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Erro ao atualizar produto: " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Remove um produto pelo seu ID.
     * @return true se removido com sucesso.
     */
    public boolean excluir(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = ConexaoDB.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int linhas = ps.executeUpdate();
            return linhas > 0;

        } catch (SQLException e) {
            System.err.println("[DAO] Erro ao excluir produto: " + e.getMessage());
            return false;
        }
    }
}
