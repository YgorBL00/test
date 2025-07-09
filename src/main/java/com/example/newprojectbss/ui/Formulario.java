package com.example.newprojectbss.ui;

import com.example.newprojectbss.model.*;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Formulario extends StackPane {

    private final ComboBox<String> cbTipo;
    private final ComboBox<String> cbPorta;
    private final TextField tfComprimento;
    private final TextField tfLargura;
    private final TextField tfAltura;
    private final ToggleGroup grupoPiso;

    public Formulario(Stage stage) {
        setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

        Label titulo = new Label("Configuração Inicial da Câmara");
        titulo.setFont(Font.font("SansSerif", 24));
        titulo.setTextFill(Color.web("#23336f"));

        Label labelTipo = new Label("Tipo de câmara:");
        cbTipo = new ComboBox<>();
        cbTipo.getItems().addAll("Resfriado", "Congelado");
        cbTipo.setPromptText("Selecione");

        Label labelPorta = new Label("Tipo de porta:");
        cbPorta = new ComboBox<>();
        cbPorta.getItems().addAll("Giratoria", "Correr");
        cbPorta.setPromptText("Selecione");
        HBox boxDimensoes0 = new HBox(10, cbTipo, cbPorta);

        Label labelDimensoes = new Label("Dimensões (m):");
        tfComprimento = new TextField();
        tfComprimento.setPromptText("Comprimento");
        tfLargura = new TextField();
        tfLargura.setPromptText("Largura");
        tfAltura = new TextField();
        tfAltura.setPromptText("Altura");
        HBox boxDimensoes = new HBox(10, tfComprimento, tfLargura, tfAltura);

        Label labelPiso = new Label("A câmara terá piso?");
        grupoPiso = new ToggleGroup();
        RadioButton rbSim = new RadioButton("Sim");
        RadioButton rbNao = new RadioButton("Não");
        rbSim.setToggleGroup(grupoPiso);
        rbNao.setToggleGroup(grupoPiso);
        HBox boxPiso = new HBox(10, rbSim, rbNao);

        VBox conteudoFormulario = new VBox(15, titulo, labelTipo, cbTipo, labelPorta, cbPorta, boxDimensoes0,
                labelDimensoes, boxDimensoes, labelPiso, boxPiso);
        conteudoFormulario.setPadding(new Insets(30));
        conteudoFormulario.setAlignment(Pos.TOP_LEFT);

        Button btnAvancar = new Button("Avançar");
        btnAvancar.setFont(Font.font(14));
        btnAvancar.setStyle("-fx-background-color: #23336f; -fx-text-fill: white;");

        Button btnCalculo = new Button("Calculo de Paineis");
        btnCalculo.setFont(Font.font(14));
        btnCalculo.setStyle("-fx-background-color: #23336f; -fx-text-fill: white;");

        HBox boxBotao = new HBox(btnAvancar);
        boxBotao.setAlignment(Pos.CENTER_RIGHT);
        boxBotao.setPadding(new Insets(20));

        HBox boxCalculo = new HBox(btnCalculo);
        boxCalculo.setAlignment(Pos.CENTER_LEFT);
        boxCalculo.setPadding(new Insets(20));

        Region espaco = new Region();
        HBox.setHgrow(espaco, Priority.ALWAYS);

        HBox botoesInferiores = new HBox(20, boxCalculo, espaco, boxBotao);

        BorderPane layoutFormulario = new BorderPane();
        layoutFormulario.setCenter(conteudoFormulario);
        layoutFormulario.setBottom(botoesInferiores);

        getChildren().add(layoutFormulario);

        btnAvancar.setOnAction(e -> {
            try {
                String tipoCamara = cbTipo.getValue();
                String tipoPorta = cbPorta.getValue();
                double comprimento = Double.parseDouble(tfComprimento.getText());
                double largura = Double.parseDouble(tfLargura.getText());
                double altura = Double.parseDouble(tfAltura.getText());
                boolean temPiso = ((RadioButton) grupoPiso.getSelectedToggle()).getText().equalsIgnoreCase("Sim");

                DadosCâmara dados = DadosCâmara.getInstancia();
                dados.setTipoCamara(tipoCamara);
                dados.setTipoPorta(tipoPorta);
                dados.setComprimento(comprimento);
                dados.setLargura(largura);
                dados.setAltura(altura);
                dados.setTemPiso(temPiso);

                double area = comprimento * largura;

                LogicaUC_Evaporadora.ResultadoUC_EV resultado =
                        LogicaUC_Evaporadora.calcular(area, tipoCamara);

                System.out.println("=== Resultado UC Evaporadora ===");
                System.out.println("Tipo de Câmara: " + tipoCamara);
                System.out.println("Área: " + area + " m²");
                System.out.println("Potência (HP): " + resultado.potenciaHP);
                System.out.println("Quantidade de Ventiladores: " + resultado.qtdVentiladores);

                CalculadoraMateriais calculadora = new CalculadoraMateriais();
                calculadora.calcular();
                calculadora.validar();

                // Converte para itens da tabela
                List<Item> listaMateriais = new ArrayList<>();
                listaMateriais.add(new Item("Painel Paredes", "PIR", "un", calculadora.getPaineisParedes(), 220.0));
                listaMateriais.add(new Item("Painel Teto", "PIR", "un", calculadora.getPaineisTeto(), 220.0));
                if (temPiso)
                    listaMateriais.add(new Item("Painel Piso", "PIR", "un", calculadora.getPaineisPiso(), 220.0));
                listaMateriais.add(new Item("Cantoneira Interna", "40x40mm", "un", calculadora.getCantoneirasInternas(), 30.0));
                listaMateriais.add(new Item("Cantoneira Externa", "40x4 0mm", "un", calculadora.getCantoneirasExternas(), 30.0));
                listaMateriais.add(new Item("Perfil U", "40x40mm", "un", calculadora.getPerfisU(), 40.0));

                // Preço da porta (m²): 250 para resfriado, 350 para congelado
                double precoPorta = tipoCamara.equalsIgnoreCase("Congelado") ? 350 : 250;
                listaMateriais.add(new Item("Porta Frigorífica", tipoPorta, "un", 1, precoPorta));


                List<Item> itensComplementares = RecomendacaoItensComplementares.recomendar();

                // Criar a janela nova com PainelMaterialFX
                PainelMaterialFX painelMaterial = new PainelMaterialFX();
                painelMaterial.setListaItens(itensComplementares);

                Scene novaCena = new Scene(painelMaterial, 950, 650);

                // Usa o stage existente (parâmetro do construtor, ou você pode guardar como atributo)
                stage.setScene(novaCena);
                stage.setTitle("Materiais Recomendados");
                stage.show();

                System.out.println("\n=== Itens Complementares Recomendados ===");
                for (Item item : itensComplementares) {
                    System.out.printf("%s (%s) - Qtde: %d - Valor unitário: R$ %.2f\n",
                            item.getNome(), item.getModelo(), item.getQuantidade(), item.getValor());
                }

                // Após calcular e imprimir no terminal




            } catch (Exception ex) {
                System.out.println("Erro ao calcular: " + ex.getMessage());
                ex.printStackTrace();
                mostrarAlerta("Erro", "Verifique os campos preenchidos!");
            }
        });






        btnCalculo.setOnAction(e -> {
            btnCalculo.setDisable(true);
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.6), layoutFormulario);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                getChildren().clear();
                PainelCalculoPaineisFX painelCalculo = new PainelCalculoPaineisFX();
                getChildren().add(painelCalculo);
            });
            fadeOut.play();
        });
    }


    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}