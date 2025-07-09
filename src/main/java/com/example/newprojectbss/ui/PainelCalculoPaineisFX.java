package com.example.newprojectbss.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import com.example.newprojectbss.model.LogicaDoCalculodePaineis;

public class PainelCalculoPaineisFX extends VBox {

    private final TextField tfEspessura = novoCampo();
    private final TextField tfLargura = novoCampo();
    private final TextField tfAltura = novoCampo();
    private final TextField tfComprimento = novoCampo();
    private final TextField tfLarguraPainel = novoCampo();
    private final RadioButton rbPisoSim = new RadioButton("Sim");
    private final RadioButton rbPisoNao = new RadioButton("Não");

    private final TableView<LogicaDoCalculodePaineis> tabela = new TableView<>();
    private final Label lbTotalPaineis = new Label("Total painéis: -");
    private final Label lbTotalM2 = new Label("Total m²: -");

    public PainelCalculoPaineisFX() {
        setSpacing(0);
        setPadding(new Insets(18, 16, 12, 16));
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

        HBox linha = new HBox(30);
        linha.setAlignment(Pos.TOP_LEFT);

        VBox painelCampos = montarPainelCampos();
        VBox painelResultados = montarPainelResultados();
        linha.getChildren().addAll(painelCampos, painelResultados);

        getChildren().add(linha);
    }

    private VBox montarPainelCampos() {
        VBox campos = new VBox(11);
        campos.setPadding(new Insets(13));
        campos.setPrefWidth(262);

        tfLarguraPainel.setText("1.15");

        ToggleGroup grupoPiso = new ToggleGroup();
        rbPisoSim.setToggleGroup(grupoPiso);
        rbPisoNao.setToggleGroup(grupoPiso);
        rbPisoNao.setSelected(true);

        campos.getChildren().addAll(
                new Label("Espessura do painel (mm):"), tfEspessura,
                new Label("Largura (m):"), tfLargura,
                new Label("Altura (m):"), tfAltura,
                new Label("Comprimento (m):"), tfComprimento,
                new Label("Possui piso?"), new HBox(14, rbPisoSim, rbPisoNao),
                new Label("Largura do painel (m):"), tfLarguraPainel
        );

        Button calcular = new Button("Calcular");
        calcular.setMaxWidth(Double.MAX_VALUE);
        calcular.setOnAction(e -> calcularPaineis());
        campos.getChildren().add(calcular);

        TitledPane titulo = new TitledPane("Dados da Câmara", campos);
        titulo.setCollapsible(false);

        VBox painel = new VBox(titulo);
        painel.setAlignment(Pos.TOP_LEFT);
        return painel;
    }

    private VBox montarPainelResultados() {
        TableColumn<LogicaDoCalculodePaineis, String> colLocal   = new TableColumn<>("Local");
        TableColumn<LogicaDoCalculodePaineis, String> colQtd     = new TableColumn<>("Qtd. Painéis");
        TableColumn<LogicaDoCalculodePaineis, String> colAltura  = new TableColumn<>("Altura (m)");
        TableColumn<LogicaDoCalculodePaineis, String> colLargura = new TableColumn<>("Largura (m)");
        TableColumn<LogicaDoCalculodePaineis, String> colArea    = new TableColumn<>("Área (m²)");
        colLocal.setCellValueFactory(data -> data.getValue().localProperty());
        colQtd.setCellValueFactory(data -> data.getValue().qtdProperty());
        colAltura.setCellValueFactory(data -> data.getValue().alturaProperty());
        colLargura.setCellValueFactory(data -> data.getValue().larguraProperty());
        colArea.setCellValueFactory(data -> data.getValue().areaProperty());
        tabela.getColumns().setAll(colLocal, colQtd, colAltura, colLargura, colArea);
        tabela.setPrefHeight(170);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TitledPane detailingPane = new TitledPane("Detalhamento dos Painéis", tabela);
        detailingPane.setCollapsible(false);

        HBox totais = new HBox();
        totais.setPadding(new Insets(10, 10, 2, 4));
        totais.setPrefWidth(600);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        lbTotalPaineis.setFont(Font.font("SansSerif", 16));
        lbTotalM2.setFont(Font.font("SansSerif", 16));
        totais.getChildren().addAll(lbTotalPaineis, spacer, lbTotalM2);

        VBox painel = new VBox(12);
        painel.setPadding(new Insets(2,10,2,2));
        painel.getChildren().addAll(detailingPane, totais);
        painel.setPrefWidth(700);
        return painel;
    }

    private TextField novoCampo() {
        TextField tf = new TextField();
        tf.setPrefWidth(75);
        return tf;
    }

    private void calcularPaineis() {
        try {
            double espessura = Double.parseDouble(tfEspessura.getText().replace(",", ".").trim());
            double largura = Double.parseDouble(tfLargura.getText().replace(",", ".").trim());
            double altura = Double.parseDouble(tfAltura.getText().replace(",", ".").trim());
            double comprimento = Double.parseDouble(tfComprimento.getText().replace(",", ".").trim());
            boolean temPiso = rbPisoSim.isSelected();
            double larguraPainel = Double.parseDouble(tfLarguraPainel.getText().replace(",", ".").trim());

            tabela.getItems().clear();

            double alturaPisoETeto = Math.min(largura, comprimento);

            double perimetro = 2 * (largura + comprimento);
            int qtdPainelParedes = (int) Math.ceil(perimetro / larguraPainel);
            double areaTotalParedes = qtdPainelParedes * altura * larguraPainel;

            double areaTeto = largura * comprimento;
            int qtdPainelTeto = (int) Math.ceil(areaTeto / (alturaPisoETeto * larguraPainel));
            double areaTotalTeto = qtdPainelTeto * alturaPisoETeto * larguraPainel;

            int qtdPainelPiso = 0;
            double areaTotalPiso = 0;
            if (temPiso) {
                int qtd = (int) Math.ceil(areaTeto / (alturaPisoETeto * larguraPainel));
                qtdPainelPiso = qtd;
                areaTotalPiso = qtd * alturaPisoETeto * larguraPainel;
            }

            tabela.getItems().add(new LogicaDoCalculodePaineis("Paredes", String.valueOf(qtdPainelParedes),
                    format(altura), format(larguraPainel), format(areaTotalParedes)));
            tabela.getItems().add(new LogicaDoCalculodePaineis("Teto", String.valueOf(qtdPainelTeto),
                    format(alturaPisoETeto), format(larguraPainel), format(areaTotalTeto)));
            tabela.getItems().add(new LogicaDoCalculodePaineis("Piso", temPiso ? String.valueOf(qtdPainelPiso) : "-",
                    temPiso ? format(alturaPisoETeto) : "-", temPiso ? format(larguraPainel) : "-", temPiso ? format(areaTotalPiso) : "-"));

            int qtdTotal = qtdPainelParedes + qtdPainelTeto + (temPiso ? qtdPainelPiso : 0);
            double areaTotal = areaTotalParedes + areaTotalTeto + (temPiso ? areaTotalPiso : 0);

            lbTotalPaineis.setText("Total painéis: " + qtdTotal);
            lbTotalM2.setText("Total m²: " + format(areaTotal));

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Preencha todos os campos corretamente.", ButtonType.OK).showAndWait();
        }
    }

    private static String format(double valor) {
        return String.format("%.2f", valor).replace('.', ',');
    }
}
