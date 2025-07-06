package com.example.newprojectbss.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

public class PainelMaterialFront extends VBox {

    private Label lblCusto;
    private Label lblSugerido;
    private static final NumberFormat formatoBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public PainelMaterialFront(Stage stage) {
        // Configuração padrão do layout root
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);

        // Adicionar os componentes na tela
        this.getChildren().addAll(
                criarPainelTitulo(),
                criarPainelCusto()
        );

        // Configuração da cena e exibição
        Scene cena = new Scene(this, 400, 400);
        stage.setScene(cena);
        stage.setTitle("Painel de Materiais (Sem Tabela)");
        stage.show();
    }

    private HBox criarPainelTitulo() {
        Label titulo = new Label("Painel de Materiais");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        HBox painelTitulo = new HBox(titulo);
        painelTitulo.setAlignment(Pos.CENTER);
        painelTitulo.setStyle("-fx-background-color: #e3f4ff; -fx-border-color: #cccccc;");
        painelTitulo.setPadding(new Insets(10));
        return painelTitulo;
    }

    private VBox criarPainelCusto() {
        // --- Labels para mostrar os valores ---
        Label lblCustoTitulo = new Label("Custo da Câmara:");
        lblCustoTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblCusto = new Label(formatoBR.format(0));

        Label lblSugeridoTitulo = new Label("Valor de Venda Sugerido:");
        lblSugeridoTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblSugerido = new Label(formatoBR.format(0) + " (30% de margem)");

        // --- Campo para inserir o valor de venda ---
        Label lblVendaTitulo = new Label("Digite o Valor de Venda:");
        lblVendaTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        TextField campoVenda = new TextField();
        campoVenda.setPromptText("Ex: 15000");

        // --- Botão para calcular ---
        Button btnCalcular = new Button("Calcular Lucro");
        btnCalcular.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Label lblResultado = new Label();
        lblResultado.setStyle("-fx-text-fill: #333333;");

        btnCalcular.setOnAction(e -> {
            try {
                double venda = Double.parseDouble(campoVenda.getText());
                double custoBase = 10000; // Exemplo de custo base fixo
                double lucro = venda - custoBase;
                double margem = custoBase > 0 ? (lucro / custoBase) * 100 : 0;

                lblResultado.setText(String.format("Lucro: %s\nMargem: %.1f%%", formatoBR.format(lucro), margem));
            } catch (NumberFormatException ex) {
                lblResultado.setText("Por favor, insira um valor válido!");
            }
        });

        // --- Montagem final ---
        VBox painelCusto = new VBox(10,
                lblCustoTitulo, lblCusto,
                lblSugeridoTitulo, lblSugerido,
                new Separator(),
                lblVendaTitulo, campoVenda,
                btnCalcular, lblResultado
        );
        painelCusto.setPadding(new Insets(15));
        painelCusto.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #cccccc; -fx-border-radius: 8; -fx-padding: 15px;");
        return painelCusto;
    }
}