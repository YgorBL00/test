package com.example.newprojectbss.db;

import com.example.newprojectbss.db.Database; // <-- CORRETO
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteDAO {

    public static void inserir(String nome, String email, String telefone) {
        String sql = "INSERT INTO clientes(nome, email, telefone) VALUES(?, ?, ?)";

        try (Connection conn = Database.conectar(); // <-- chamar o método correto
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, telefone);
            stmt.executeUpdate();
            System.out.println("Cliente inserido com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void listar() {
        String sql = "SELECT * FROM clientes";

        try (Connection conn = Database.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
