package com.example.newprojectbss;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.newprojectbss.ui.PainelBemVindo;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        // Define o painel principal
        PainelBemVindo painel = new PainelBemVindo(stage);

        // Cria a cena com tamanho fixo
        Scene scene = new Scene(painel, 950, 650);
        stage.setScene(scene);

        // Configurações do stage
        stage.setTitle("Novo Projeto");
        stage.setResizable(false); // Impede redimensionamento da janela
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}