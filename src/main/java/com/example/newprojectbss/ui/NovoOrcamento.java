package com.example.newprojectbss.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NovoOrcamento extends VBox {

    private final Stage stage;
    private final StackPane root;

    private final Button btnVoltar = new Button("Voltar");
    private final Button btnEnviar = new Button("Enviar");

    public NovoOrcamento(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;

        setSpacing(20);
        setPadding(new Insets(30));
        // =============================
        // Campos do formulário
        // =============================
        VBox campos = new VBox(15);

        TextField tfNome = new TextField();
        tfNome.setPromptText("Nome do Cliente");

        TextField tfEmpresa = new TextField();
        tfEmpresa.setPromptText("Empresa");

        TextField tfTelefone = new TextField();
        tfTelefone.setPromptText("Telefone");

        TextField tfEmail = new TextField();
        tfEmail.setPromptText("E-mail");

        campos.getChildren().addAll(
                new Label("Novo Orçamento"),
                tfNome, tfEmpresa, tfTelefone, tfEmail
        );

        // =============================
        // Botões
        // =============================
        HBox botoes = new HBox(20);
        botoes.setAlignment(Pos.CENTER_LEFT);

        btnVoltar.setPrefWidth(100);
        btnVoltar.setFont(Font.font(14));

        btnEnviar.setPrefWidth(100);
        btnEnviar.setFont(Font.font(14));

        botoes.getChildren().addAll(btnVoltar, btnEnviar);

        getChildren().addAll(campos, botoes);

        // =============================
        // Ação do botão Voltar
        // =============================
        btnVoltar.setOnAction(e -> {
            Inicio inicio = new Inicio(stage, root);
            trocarTela(inicio);
        });

        btnEnviar.setOnAction(e -> {
            String nome = tfNome.getText().trim();
            String empresa = tfEmpresa.getText().trim();
            String telefone = tfTelefone.getText().trim();
            String email = tfEmail.getText().trim();

            if(nome.isEmpty() || empresa.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nome e Empresa são obrigatórios!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            boolean sucesso = com.example.newprojectbss.db.Database.inserirOrcamento(nome, empresa, telefone, email);
            if(sucesso) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Orçamento salvo com sucesso!", ButtonType.OK);
                alert.showAndWait();
                tfNome.clear();
                tfEmpresa.clear();
                tfTelefone.clear();
                tfEmail.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar o orçamento.", ButtonType.OK);
                alert.showAndWait();
            }
        });


        // =============================
        // Fade-in ao abrir
        // =============================
        setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    // =============================
    // Método para trocar de tela com fade
    // =============================
    private void trocarTela(Node novaTela) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(ev -> {
            root.getChildren().setAll(novaTela);
            novaTela.setOpacity(0);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), novaTela);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
