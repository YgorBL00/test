package com.example.newprojectbss.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PainelBemVindo extends BorderPane {

    public PainelBemVindo(Stage stage) {
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

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

        // ANIMAÇÕES

        FadeTransition ftMensagem = new FadeTransition(Duration.seconds(1.5), mensagem);
        ftMensagem.setFromValue(0);
        ftMensagem.setToValue(1);
        ftMensagem.setOnFinished(ev -> {
            FadeTransition ftBtn = new FadeTransition(Duration.millis(700), iniciar);
            ftBtn.setFromValue(0);
            ftBtn.setToValue(1);
            ftBtn.setOnFinished(ev2 -> {
                // Caso queira alguma animação extra no rodapé, adicione aqui.
            });
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

        // Declaração correta das variáveis de fade-out para o botão iniciar
        FadeTransition fadeOutTopo = new FadeTransition(Duration.millis(800), topo);
        fadeOutTopo.setFromValue(1);
        fadeOutTopo.setToValue(0);

        FadeTransition fadeOutCentro = new FadeTransition(Duration.millis(800), centro);
        fadeOutCentro.setFromValue(1);
        fadeOutCentro.setToValue(0);

        FadeTransition fadeOutRodape = new FadeTransition(Duration.millis(800));
        fadeOutRodape.setFromValue(1);
        fadeOutRodape.setToValue(0);

        fadeOutRodape.setOnFinished(ev -> {
            // Trocar a cena para o formulário
            Formulario telaSimples = new Formulario(stage);
            Scene novaCena = new Scene(telaSimples, 950, 650);
            stage.setScene(novaCena);
        });

        // Evento do botão iniciar
        iniciar.setOnAction(e -> {
            iniciar.setDisable(true);

            fadeOutTopo.play();
            fadeOutCentro.play();
            fadeOutRodape.play();
        });
    }
}
