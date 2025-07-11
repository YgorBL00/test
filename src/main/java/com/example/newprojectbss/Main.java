package com.example.newprojectbss;

import com.example.newprojectbss.ui.PainelBemVindo;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Root principal que será usado por todas as telas
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

        // Tela inicial
        PainelBemVindo painelBemVindo = new PainelBemVindo(primaryStage, root);
        root.getChildren().add(painelBemVindo);

        // Cena com o root fixo
        Scene cena = new Scene(root, 950, 650);
        primaryStage.setScene(cena);
        primaryStage.setTitle("Bem-vindo");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
