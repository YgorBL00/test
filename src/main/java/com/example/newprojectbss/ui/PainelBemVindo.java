package com.example.newprojectbss.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PainelBemVindo extends BorderPane {

    private final Stage stage;
    private final StackPane root;

    public PainelBemVindo(Stage stage, StackPane root) {
        this.stage = stage;
        this.root = root;

        setStyle("-fx-background-color: transparent;"); // transparente para deixar o fundo do root visível

        // Topo: LOGO CENTRALIZADO
        VBox topo = new VBox();
        topo.setAlignment(Pos.CENTER);

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/logo.png")));
        logo.setPreserveRatio(true);
        logo.setFitHeight(80);
        topo.getChildren().add(logo);
        topo.setPrefHeight(160);
        setTop(topo);

        // Centro: MENSAGEM + BOTÃO
        Label mensagem = new Label("Novo Projeto");
        mensagem.setFont(Font.font("SansSerif", 27));
        mensagem.setTextFill(Color.web("#23336f"));
        mensagem.setOpacity(0);

        Button iniciar = new Button("Iniciar");
        iniciar.setFont(Font.font("SansSerif", 17));
        iniciar.setStyle("-fx-background-color: #245edb; -fx-text-fill: white; -fx-background-radius: 24;");
        iniciar.setPrefWidth(170);
        iniciar.setOpacity(0);

        VBox centro = new VBox(28, mensagem, iniciar);
        centro.setAlignment(Pos.TOP_CENTER);
        centro.setPadding(new Insets(40, 0, 0, 0));
        setCenter(centro);

        // Animação de fade-in da mensagem e botão
        FadeTransition ftMensagem = new FadeTransition(Duration.seconds(1.5), mensagem);
        ftMensagem.setFromValue(0);
        ftMensagem.setToValue(1);
        ftMensagem.setOnFinished(ev -> {
            FadeTransition ftBtn = new FadeTransition(Duration.millis(700), iniciar);
            ftBtn.setFromValue(0);
            ftBtn.setToValue(1);
            ftBtn.play();
        });



        mensagem.setTranslateY(30);
        ftMensagem.currentTimeProperty().addListener((obs, o, n) -> {
            if (n != null && ftMensagem.getDuration().greaterThan(Duration.ZERO)) {
                double frac = n.toMillis() / ftMensagem.getDuration().toMillis();
                mensagem.setTranslateY(30 - frac * 30);
            }
        });
        ftMensagem.play();

        // Fade-out ao clicar em iniciar, trocando a tela
        FadeTransition fadeOutTopo = new FadeTransition(Duration.millis(500), topo);
        fadeOutTopo.setFromValue(1);
        fadeOutTopo.setToValue(0);

        FadeTransition fadeOutCentro = new FadeTransition(Duration.millis(500), centro);
        fadeOutCentro.setFromValue(1);
        fadeOutCentro.setToValue(0);

        iniciar.setOnAction(e -> {
            iniciar.setDisable(true);

            fadeOutTopo.play();
            fadeOutCentro.play();

            fadeOutCentro.setOnFinished(ev -> {
                // Troca o conteúdo do root pelo formulário
                root.getChildren().setAll(new Formulario(stage, root));

                // Aplica fade-in no formulário
                Node formulario = root.getChildren().get(0);
                formulario.setOpacity(0);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), formulario);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
            });
    });
    }
}
