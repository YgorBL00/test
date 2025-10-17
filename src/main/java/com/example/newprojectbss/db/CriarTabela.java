package com.example.newprojectbss.db;

import java.sql.Connection;
import java.sql.Statement;

public class CriarTabela {

    public static void criar() {
        String sql = """
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    email TEXT,
                    telefone TEXT
                );
                """;

        try (Connection conn = Database.conectar(); // <-- método correto
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela clientes criada/verificada com sucesso.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
