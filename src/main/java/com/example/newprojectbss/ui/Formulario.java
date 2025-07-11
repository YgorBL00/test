    package com.example.newprojectbss.ui;

    import com.example.newprojectbss.model.*;
    import javafx.animation.FadeTransition;
    import javafx.application.Platform;
    import javafx.geometry.Insets;
    import javafx.geometry.Pos;
    import javafx.scene.Node;
    import javafx.scene.Parent;
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
        private Stage stage;
        private StackPane root;

        public Formulario(Stage stage, StackPane root) {
            this.stage = stage;
            this.root = root;

            setStyle("-fx-background-color: linear-gradient(from 0% 100% to 0% 0%, #b3e0ff, white);");

            Label titulo = new Label("Configuração Inicial da Câmara");
            titulo.setFont(Font.font("SansSerif", 24));
            titulo.setTextFill(Color.web("#23336f"));

            Label labelTipo = new Label("Tipo de câmara:");
            cbTipo = new ComboBox<>();
            cbTipo.getItems().addAll("Resfriado", "Congelado");
            cbTipo.setPromptText("Selecione");
            cbTipo.setPrefWidth(110);  // largura fixa

            Label labelPorta = new Label("Tipo de porta:");
            cbPorta = new ComboBox<>();
            cbPorta.getItems().addAll("Giratoria", "Correr");
            cbPorta.setPromptText("Selecione");
            cbPorta.setPrefWidth(110); // mesma largura

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
                btnAvancar.setDisable(true);

                try {
                    /* --- validação dos campos --- */
                    if (cbTipo.getValue() == null || cbPorta.getValue() == null ||
                            tfComprimento.getText().isEmpty() || tfLargura.getText().isEmpty() ||
                            tfAltura.getText().isEmpty() || grupoPiso.getSelectedToggle() == null) {

                        mostrarAlerta("Erro", "Preencha todos os campos antes de avançar!");
                        btnAvancar.setDisable(false);
                        return;
                    }

                    double comprimento = Double.parseDouble(tfComprimento.getText());
                    double largura     = Double.parseDouble(tfLargura.getText());
                    double altura      = Double.parseDouble(tfAltura.getText());

                    String  tipoCamara = cbTipo.getValue();
                    String  tipoPorta  = cbPorta.getValue();
                    boolean temPiso    = ((RadioButton) grupoPiso.getSelectedToggle())
                            .getText().equalsIgnoreCase("Sim");

                    /* --- guarda dados no singleton --- */
                    DadosCâmara dados = DadosCâmara.getInstancia();
                    dados.setTipoCamara(tipoCamara);
                    dados.setTipoPorta(tipoPorta);
                    dados.setComprimento(comprimento);
                    dados.setLargura(largura);
                    dados.setAltura(altura);
                    dados.setTemPiso(temPiso);

                    /* --- gera lista de materiais --- */
                    double area = comprimento * largura;

                    CalculadoraMateriais calculadora = new CalculadoraMateriais();
                    calculadora.calcular();
                    calculadora.validar();

                    List<Item> itensEvaporadora   = LogicaUC_Evaporadora.gerarItensUCeEV(area, tipoCamara);
                    List<Item> listaMateriais     = calculadora.gerarListaMateriais(tipoPorta);
                    List<Item> itensComplementares= RecomendacaoItensComplementares.recomendar();

                    List<Item> todosItens = new ArrayList<>();
                    todosItens.addAll(itensEvaporadora);
                    todosItens.addAll(listaMateriais);
                    todosItens.addAll(itensComplementares);

                    /* --- cria painel final --- */
                    PainelMaterialFX painelMaterial = new PainelMaterialFX(stage, root);
                    painelMaterial.setListaItens(todosItens);

                    /* --- animação: fade‑out no formulário (this) --- */
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.4), this);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    fadeOut.setOnFinished(ev -> {
                        /* troca o conteúdo do root */
                        root.getChildren().setAll(painelMaterial);
                        stage.setTitle("Materiais Recomendados");
                        painelMaterial.setOpacity(0);

                        /* fade‑in no novo painel */
                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.4), painelMaterial);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });

                    fadeOut.play();

                } catch (NumberFormatException ex) {
                    mostrarAlerta("Erro",
                            "Digite valores numéricos válidos para comprimento, largura e altura.");
                    btnAvancar.setDisable(false);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarAlerta("Erro", "Erro inesperado: " + ex.getMessage());
                    btnAvancar.setDisable(false);
                }
            });



            btnCalculo.setOnAction(e -> {
                btnCalculo.setDisable(true);   // evita cliques múltiplos

                try {
                    // Cria o painel de cálculo
                    PainelCalculoPaineisFX painelCalculo = new PainelCalculoPaineisFX(stage, root);

                    /* Anima fade‑out no formulário (this) */
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.4), this);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    fadeOut.setOnFinished(ev -> {
                        /* Substitui o conteúdo do root */
                        root.getChildren().setAll(painelCalculo);
                        stage.setTitle("Cálculo de Painéis");
                        painelCalculo.setOpacity(0);

                        /* Anima fade‑in no novo painel */
                        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.4), painelCalculo);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);
                        fadeIn.play();
                    });

                    fadeOut.play();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    mostrarAlerta("Erro", "Erro ao abrir o cálculo de painéis: " + ex.getMessage());
                    btnCalculo.setDisable(false);
                }
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