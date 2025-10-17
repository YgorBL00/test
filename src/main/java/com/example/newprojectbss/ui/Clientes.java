package com.example.newprojectbss.ui;

import com.example.newprojectbss.db.ClienteDAO;
import com.example.newprojectbss.db.Database;
import com.example.newprojectbss.model.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Clientes extends VBox {

    private final Stage stage;
    private final StackPane root;
    private final TableView<Cliente> table = new TableView<>();
    private final ObservableList<Cliente> dados = FXCollections.observableArrayList();

    public Clientes(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;

        setSpacing(10);
        setPadding(new Insets(20));

        Label titulo = new Label("Lista de Clientes");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Colunas da tabela
        TableColumn<Cliente, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colId.setPrefWidth(50);

        TableColumn<Cliente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colNome.setPrefWidth(200);

        TableColumn<Cliente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colEmail.setPrefWidth(200);

        TableColumn<Cliente, String> colTelefone = new TableColumn<>("Telefone");
        colTelefone.setCellValueFactory(cellData -> cellData.getValue().telefoneProperty());
        colTelefone.setPrefWidth(150);

        table.getColumns().addAll(colId, colNome, colEmail, colTelefone);

        carregarClientes();

        getChildren().addAll(titulo, table);
    }

    private void carregarClientes() {
        dados.clear();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = Database.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dados.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(dados);
    }
}
