package com.example.newprojectbss.db;

import java.sql.*;

public class Database {

    private static final String URL = "jdbc:sqlite:orcamentos.db";

    // Conexão
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Criar tabela se não existir
    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS orcamentos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                empresa TEXT NOT NULL,
                telefone TEXT,
                email TEXT
            );
        """;

        try (Connection conn = conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Inserir orçamento
    public static boolean inserirOrcamento(String nome, String empresa, String telefone, String email) {
        String sql = "INSERT INTO orcamentos(nome, empresa, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, empresa);
            pstmt.setString(3, telefone);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
