package com.example.newprojectbss.ui;

import com.example.newprojectbss.model.Item;
import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PainelMaterialFX extends SplitPane {

    private final TableView<Item> tabela = new TableView<>();
    private final ObservableList<Item> observableItensTabela = FXCollections.observableArrayList();

    private Stage stage;
    private StackPane root;

    private final Label lblCusto = new Label();
    private final Label lblSugerido = new Label();

    private static final NumberFormat formatoBR = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));


    public PainelMaterialFX(Stage stage, StackPane root) {
        this.stage = this.stage;
        this.root = root;
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

        Button btnVoltar = new Button("Voltar");
        btnVoltar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnVoltar.setPadding(new Insets(8, 14, 8, 14));
        btnVoltar.setOnAction(e -> {
            try {
                Formulario formulario = new Formulario(stage, root);
                FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.4), this);
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.setOnFinished(ev -> {
                    root.getChildren().setAll(formulario);
                    stage.setTitle("Configuração Inicial da Câmara");
                    formulario.setOpacity(0);
                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.4), formulario);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.play();
                });
                fadeOut.play();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button btnValores = new Button("Valores");
        btnValores.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnValores.setPadding(new Insets(8, 14, 8, 14));
        btnValores.setOnAction(e -> abrirJanelaItens1());

        // Usando StackPane para rodapé com posicionamento livre
        StackPane rodape = new StackPane();
        rodape.setPrefHeight(50); // altura suficiente para os botões
        rodape.setPadding(new Insets(8, 12, 8, 12));

        rodape.getChildren().addAll(btnVoltar, btnValores);

        // Posiciona os botões manualmente no eixo X (horizontal)
        btnVoltar.setTranslateX(-288);  // move o btnVoltar 150px para a esquerda
        btnValores.setTranslateX(282);  // move o btnValores 150px para a direita

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
        btnCustos.setPadding(new Insets(10, 14, 10, 14));
        btnCustos.setOnAction(e -> abrirJanelaCustos2());

        // Troca HBox por StackPane para permitir posicionamento livre do botão
        StackPane barraInferior2 = new StackPane();
        barraInferior2.setPrefHeight(50);  // altura suficiente para o botão

        barraInferior2.getChildren().add(btnCustos);

        // Posiciona o botão no StackPane com translate (exemplo: 0 horizontal, 0 vertical)
        btnCustos.setTranslateX(60);  // Ajuste horizontal pixel a pixel
        btnCustos.setTranslateY(265);  // Ajuste vertical pixel a pixel

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
        Stage janelaPopup = new Stage();
        janelaPopup.initModality(Modality.APPLICATION_MODAL);
        janelaPopup.setTitle("Editar Lista de Materiais");

        // TableView para mostrar e editar lista de itens
        TableView<Item> tabelaItens = new TableView<>();

        // Colunas: Nome, Modelo, Unidade, Quantidade (editável), Valor (editável)
        TableColumn<Item, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Item, String> colModelo = new TableColumn<>("Modelo");
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));

        TableColumn<Item, String> colUnidade = new TableColumn<>("Unidade");
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));

        TableColumn<Item, Integer> colQuantidade = new TableColumn<>("Quantidade");
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colQuantidade.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colQuantidade.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setQuantidade(event.getNewValue());
        });

        TableColumn<Item, Double> colValor = new TableColumn<>("Valor Unitário");
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValor.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colValor.setOnEditCommit(event -> {
            Item item = event.getRowValue();
            item.setValor(event.getNewValue());
        });

        tabelaItens.getColumns().addAll(colNome, colModelo, colUnidade, colQuantidade, colValor);
        tabelaItens.setEditable(true);

        // Aqui você deve passar a lista atual de itens (exemplo: 'listaMateriais')
        // Faça uma cópia para editar sem afetar a original até salvar
        ObservableList<Item> itensEditaveis = FXCollections.observableArrayList(observableItensTabela);
        tabelaItens.setItems(itensEditaveis);

        // Botão salvar
        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            // Atualizar lista principal com as alterações feitas
            observableItensTabela.clear();
            observableItensTabela.addAll(itensEditaveis);

            // Atualizar UI principal, recalcular valores etc
            atualizarCusto();

            janelaPopup.close();
        });

        VBox layout = new VBox(10, tabelaItens, btnSalvar);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 700, 400);
        janelaPopup.setScene(scene);
        janelaPopup.showAndWait();
    }


    private void abrirJanelaCustos2() {
        Stage janelaPopup = new Stage();
        janelaPopup.initModality(Modality.APPLICATION_MODAL);
        janelaPopup.setTitle("Custos Operacionais");

        Label lblDias = new Label("Quantidade de Dias:");
        TextField tfDias = new TextField();
        tfDias.setPromptText("Digite a quantidade de dias");

        Label lblCustoFuncionario = new Label("Custo dos Funcionários (R$):");
        TextField tfCustoFuncionario = new TextField();
        tfCustoFuncionario.setPromptText("Digite o custo dos funcionários");

        Label lblErro = new Label();
        lblErro.setStyle("-fx-text-fill: red;");

        Button btnSalvar = new Button("Salvar");
        btnSalvar.setOnAction(e -> {
            try {
                int dias = Integer.parseInt(tfDias.getText());
                double custoFuncionario = Double.parseDouble(tfCustoFuncionario.getText().replace(",", "."));

                if (dias < 0 || custoFuncionario < 0) {
                    lblErro.setText("Valores não podem ser negativos.");
                    return;
                }

                double custoOperacional = dias * custoFuncionario;

                // Atualiza o label de custo somando o custo operacional
                double custoAtual = calcularCustoTotal();
                double novoCusto = custoAtual + custoOperacional;

                lblCusto.setText(formatoBR.format(novoCusto));

                // Atualiza valor sugerido também com margem 30%
                double valorSugerido = novoCusto * 1.3;
                lblSugerido.setText(formatoBR.format(valorSugerido) + " (30% de margem)");

                lblErro.setText("Salvo com sucesso!");  // Mensagem de confirmação

                // NÃO FECHAR a janela aqui para permitir múltiplas alterações

            } catch (NumberFormatException ex) {
                lblErro.setText("Por favor, insira valores numéricos válidos.");
            }
        });


        VBox layout = new VBox(10,
                lblDias, tfDias,
                lblCustoFuncionario, tfCustoFuncionario,
                lblErro,
                btnSalvar
        );
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER_LEFT);

        Scene scene = new Scene(layout, 300, 220);
        janelaPopup.setScene(scene);
        janelaPopup.showAndWait();
    }
}
