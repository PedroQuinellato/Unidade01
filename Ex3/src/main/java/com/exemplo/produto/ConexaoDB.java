package com.exemplo.produto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por gerenciar a conexão com o banco de dados PostgreSQL.
 * Utiliza o padrão Singleton para manter uma única conexão ativa.
 */
public class ConexaoDB {

    // -------------------------------------------------------
    // CONFIGURAÇÕES: altere aqui conforme seu ambiente local
    // -------------------------------------------------------
    private static final String URL      = "jdbc:postgresql://localhost:5432/exercicio3";
    private static final String USUARIO  = "postgres";
    private static final String SENHA    = "postgres";
    // -------------------------------------------------------

    private static Connection conexao = null;

    /**
     * Retorna a conexão ativa, criando uma nova se necessário.
     */
    public static Connection getConexao() throws SQLException {
        if (conexao == null || conexao.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("[DB] Conexão com PostgreSQL estabelecida com sucesso.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver PostgreSQL não encontrado: " + e.getMessage());
            }
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados.
     */
    public static void fecharConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("[DB] Conexão encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("[DB] Erro ao fechar conexão: " + e.getMessage());
        }
    }
}
