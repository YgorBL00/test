package com.example.newprojectbss.ui;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import com.example.newprojectbss.model.FormularioData;

public class Formulario extends StackPane {

    public Formulario(javafx.stage.Stage stage) {
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

        // Título
        Label titulo = new Label("Configuração Inicial da Câmara");
        titulo.setFont(Font.font("SansSerif", 24));
        titulo.setTextFill(Color.web("#23336f"));

        // Tipo de Câmara
        Label labelTipo = new Label("Tipo de câmara:");
        ComboBox<String> cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Resfriado", "Congelado");
        cbTipo.setPromptText("Selecione");

        Label labelPorta = new Label("Tipo de porta:");
        ComboBox<String> cbPorta = new ComboBox<>();
        cbPorta.getItems().addAll("Giratoria", "Correr");
        cbPorta.setPromptText("Selecione");
        HBox boxDimensoes0 = new HBox(10, cbTipo, cbPorta);

        // Dimensões
        Label labelDimensoes = new Label("Dimensões (m):");
        TextField tfComprimento = new TextField();
        tfComprimento.setPromptText("Comprimento");
        TextField tfLargura = new TextField();
        tfLargura.setPromptText("Largura");
        TextField tfAltura = new TextField();
        tfAltura.setPromptText("Altura");
        HBox boxDimensoes = new HBox(10, tfComprimento, tfLargura, tfAltura);

        // Piso
        Label labelPiso = new Label("A câmara terá piso?");
        ToggleGroup grupoPiso = new ToggleGroup();
        RadioButton rbSim = new RadioButton("Sim");
        RadioButton rbNao = new RadioButton("Não");
        rbSim.setToggleGroup(grupoPiso);
        rbNao.setToggleGroup(grupoPiso);
        HBox boxPiso = new HBox(10, rbSim, rbNao);

        // Área principal do formulário (tudo, exceto o botão)
        VBox conteudoFormulario = new VBox(15,
                titulo,
                labelTipo, cbTipo, labelPorta, cbPorta, boxDimensoes0,
                labelDimensoes, boxDimensoes,
                labelPiso, boxPiso
        );
        conteudoFormulario.setPadding(new Insets(30));
        conteudoFormulario.setAlignment(Pos.TOP_LEFT);

        // Botão "Avançar" posicionado na parte inferior
        // Criação dos botões
        Button btnAvancar = new Button("Avançar");
        btnAvancar.setFont(Font.font(14));
        btnAvancar.setStyle("-fx-background-color: #23336f; -fx-text-fill: white;");

        Button btnCalculo = new Button("Calculo de Paineis");
        btnCalculo.setFont(Font.font(14));
        btnCalculo.setStyle("-fx-background-color: #23336f; -fx-text-fill: white;");

// Containers individuais (opcional, caso queira margens diferentes)
        HBox boxBotao = new HBox(btnAvancar);
        boxBotao.setAlignment(Pos.CENTER_RIGHT);
        boxBotao.setPadding(new Insets(20, 20, 20, 20));

        HBox boxCalculo = new HBox(btnCalculo);
        boxCalculo.setAlignment(Pos.CENTER_LEFT);
        boxCalculo.setPadding(new Insets(20, 20, 20, 20));

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        HBox botoesInferiores = new HBox(20, boxCalculo, espaco, boxBotao);


// Layout final com formulário e botões
        BorderPane layoutFormulario = new BorderPane();
        layoutFormulario.setCenter(conteudoFormulario);
        layoutFormulario.setBottom(botoesInferiores);


        // Adiciona o layout principal ao StackPane
        getChildren().add(layoutFormulario);

        // Ação para o botão "Avançar"
        btnAvancar.setOnAction(e -> {
            btnAvancar.setDisable(true); // Evita múltiplos cliques

            // Animação de fade-out antes de trocar para PainelMaterialFront
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.6), layoutFormulario);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                // Limpa o conteúdo atual e chama PainelMaterialFront
                getChildren().clear();
                PainelMaterialFront painelMaterial = new PainelMaterialFront(stage);
                getChildren().add(painelMaterial);
            });
            fadeOut.play();
        });
    }
}


