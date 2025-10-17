package com.example.newprojectbss.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Inicio extends StackPane {

    private final Stage stage;
    private final StackPane root;

    public Inicio(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;

        setAlignment(Pos.CENTER);
        setPadding(new Insets(40));

        // Título
        javafx.scene.control.Label titulo = new javafx.scene.control.Label("Menu Principal");
        titulo.setFont(Font.font("SansSerif", 28));
        titulo.setStyle("-fx-text-fill: #23336f;");

        // Botões principais
        Button btnOrcamento = new Button("Novo Orçamento");
        Button btnMaterial = new Button("Cálculo de Material de Montagem");
        Button btnMaquinario = new Button("Cálculo de Maquinário");
        Button btnClientes = new Button("Orçamento de Clientes");

        // Estilo visual comum aos botões
        for (Button b : new Button[]{btnOrcamento, btnMaterial, btnMaquinario, btnClientes}) {
            b.setFont(Font.font("SansSerif", 17));
            b.setPrefWidth(320);
            b.setStyle("""
                -fx-background-color: #245edb;
                -fx-text-fill: white;
                -fx-background-radius: 24;
                -fx-cursor: hand;
                -fx-padding: 10 20 10 20;
            """);
            b.setOnMouseEntered(e -> b.setStyle(b.getStyle() + "-fx-opacity: 0.8;"));
            b.setOnMouseExited(e -> b.setStyle(b.getStyle().replace("-fx-opacity: 0.8;", "")));
        }

        // Layout vertical centralizado
        VBox box = new VBox(20, titulo, btnOrcamento, btnMaterial, btnMaquinario, btnClientes);
        box.setAlignment(Pos.CENTER);

        getChildren().add(box);

        // Animação de fade-in suave
        setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Ações dos botões -> trocam de tela
        btnOrcamento.setOnAction(e -> trocarTela(new NovoOrcamento(stage, root)));
        btnMaterial.setOnAction(e -> trocarTela(new PainelCalculoPaineisFX(stage, root)));
        //btnMaquinario.setOnAction(e -> trocarTela(new CalculoMaquinario(stage, root)));
        btnClientes.setOnAction(e -> trocarTela(new Clientes(stage, root)));
    }



    /** Faz a transição de tela com animação de fade */
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
