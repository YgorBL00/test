package com.example.newprojectbss.ui;

import com.example.newprojectbss.model.Item;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PainelMaterialFX extends SplitPane {

    private final TableView<Item> tabela = new TableView<>();
    private final ObservableList<Item> observableItensTabela = FXCollections.observableArrayList();

    private final Label lblCusto = new Label();
    private final Label lblSugerido = new Label();

    private static final NumberFormat formatoBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public PainelMaterialFX() {
        this.setPrefSize(950, 650);

        configurarTabelaMateriais();
        tabela.setItems(observableItensTabela);

        VBox painelTabela = criarPainelTabela();
        VBox painelLateral = criarPainelLateral();

        this.getItems().addAll(painelTabela, painelLateral);
        this.setDividerPositions(0.7);

        VBox.setVgrow(tabela, Priority.ALWAYS);
        VBox.setVgrow(painelTabela, Priority.ALWAYS);
        VBox.setVgrow(painelLateral, Priority.ALWAYS);

        atualizarCusto();
    }

    // Configura colunas da tabela
    private void configurarTabelaMateriais() {
        tabela.getColumns().clear();

        TableColumn<Item, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNome.setPrefWidth(160);

        TableColumn<Item, String> colunaModelo = new TableColumn<>("Modelo");
        colunaModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colunaModelo.setPrefWidth(140);

        TableColumn<Item, String> colunaUnidade = new TableColumn<>("Unidade");
        colunaUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colunaUnidade.setPrefWidth(70);

        TableColumn<Item, Integer> colunaQuantidade = new TableColumn<>("Quantidade");
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaQuantidade.setPrefWidth(90);

        TableColumn<Item, Double> colunaValorUnitario = new TableColumn<>("Valor Unitário");
        colunaValorUnitario.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colunaValorUnitario.setPrefWidth(120);
        colunaValorUnitario.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? null : formatoBR.format(valor));
            }
        });

        TableColumn<Item, Double> colunaValorTotal = new TableColumn<>("Valor Total");
        colunaValorTotal.setPrefWidth(120);
        colunaValorTotal.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getQuantidade() * cellData.getValue().getValor()));
        colunaValorTotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double valor, boolean empty) {
                super.updateItem(valor, empty);
                setText(empty || valor == null ? null : formatoBR.format(valor));
            }
        });

        tabela.getColumns().addAll(colunaNome, colunaModelo, colunaUnidade, colunaQuantidade, colunaValorUnitario, colunaValorTotal);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabela.setPrefHeight(520);
    }

    // Cria painel que contém a tabela e botões relacionados
    private VBox criarPainelTabela() {
        Label titulo = new Label("Lista de materiais");
        titulo.setFont(Font.font("System", FontWeight.BOLD, 18));
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");


        HBox barraTopo = new HBox(titulo, new Region());
        HBox.setHgrow(barraTopo.getChildren().get(1), Priority.ALWAYS);
        barraTopo.setAlignment(Pos.CENTER_LEFT);
        barraTopo.setPadding(new Insets(8, 12, 8, 12));
        barraTopo.setSpacing(12);

        ScrollPane scrollPane = new ScrollPane(tabela);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setPrefHeight(580);

        Button btnValores = new Button("Valores");
        btnValores.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnValores.setPadding(new Insets(8, 14, 8, 14));
        btnValores.setOnAction(e -> abrirJanelaItens1());

        HBox rodape = new HBox(new Region(), btnValores);
        HBox.setHgrow(rodape.getChildren().get(0), Priority.ALWAYS);
        rodape.setPadding(new Insets(8, 12, 8, 12));

        VBox painelTabela = new VBox(barraTopo, scrollPane, rodape);
        painelTabela.setPadding(new Insets(12));
        painelTabela.setSpacing(12);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return painelTabela;
    }

    // Cria painel lateral com custo, valor sugerido, venda e botões
    private VBox criarPainelLateral() {
        VBox painelLateral = new VBox(12);
        painelLateral.setPadding(new Insets(15));
        painelLateral.setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

        Label lblCustoTitulo = new Label("Custo da câmara");
        lblCustoTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblCusto.setFont(Font.font(14));
        lblCusto.setText(formatoBR.format(0));

        Label lblSugeridoTitulo = new Label("Valor de venda sugerido");
        lblSugeridoTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblSugerido.setFont(Font.font(14));
        lblSugerido.setText(formatoBR.format(0) + " (30% de margem)");

        Label lblVendaTitulo = new Label("Valor da venda");
        lblVendaTitulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        TextField campoVenda = new TextField();
        campoVenda.setPromptText("Digite o valor de venda");

        Button btnCalcular = new Button("Calcular");
        btnCalcular.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnCalcular.setMaxWidth(Double.MAX_VALUE);
        btnCalcular.setPadding(new Insets(10, 0, 10, 0));

        Label lblResultado = new Label();
        btnCalcular.setOnAction(e -> {
            try {
                double venda = Double.parseDouble(campoVenda.getText().replace(",", "."));
                double custoBaseAtual = calcularCustoTotal();
                double lucro = venda - custoBaseAtual;
                double porcentagem = custoBaseAtual > 0 ? (lucro / custoBaseAtual) * 100 : 0;
                lblResultado.setText(String.format("Lucro: R$ %.2f\nMargem: %.1f%%", lucro, porcentagem));
            } catch (Exception ex) {
                lblResultado.setText("Valor inválido.");
            }
        });


        Button btnCustos = new Button("Custos operacionais");
        btnCustos.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnCustos.setMaxWidth(Double.MAX_VALUE);
        btnCustos.setPadding(new Insets(10, 0, 10, 0));
        btnCustos.setOnAction(e -> abrirJanelaCustos2());

        HBox barraInferior2 = new HBox(btnCustos);
        barraInferior2.setAlignment(Pos.BOTTOM_RIGHT);

        painelLateral.getChildren().addAll(
                lblCustoTitulo, lblCusto,
                lblSugeridoTitulo, lblSugerido,
                new Separator(),
                lblVendaTitulo, campoVenda,
                btnCalcular, lblResultado,
                new Separator(),
                barraInferior2
        );

        VBox.setVgrow(painelLateral, Priority.ALWAYS);

        return painelLateral;
    }

    // Método para calcular custo total somando valor * quantidade dos itens
    private double calcularCustoTotal() {
        return observableItensTabela.stream()
                .mapToDouble(item -> item.getQuantidade() * item.getValor())
                .sum();
    }

    // Atualiza os labels de custo e valor sugerido
    private void atualizarCusto() {
        double custo = calcularCustoTotal();
        lblCusto.setText(formatoBR.format(custo));
        double valorSugerido = custo * 1.3; // margem 30%
        lblSugerido.setText(formatoBR.format(valorSugerido) + " (30% de margem)");
    }

    // Atualiza a tabela e labels adicionando um item
    public void adicionarItem(Item item) {
        observableItensTabela.add(item);
        System.out.println("Item adicionado: " + item.getNome());
        System.out.println("Itens totais na lista: " + observableItensTabela.size());
        atualizarCusto();
    }

    // Define uma nova lista de itens (substitui a existente)
    public void setListaItens(List<Item> itens) {
        observableItensTabela.setAll(itens);
        atualizarCusto();
    }

    // Placeholder para futuras janelas
    private void abrirJanelaItens1() {
        // Implementar janela para valores se necessário
    }

    private void abrirJanelaCustos2() {
        // Implementar janela para custos operacionais se necessário
    }
}
