package com.imc.View;

import com.imc.Util.ArquivoUtil;
import com.imc.Model.Pessoa;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class IMCApp extends Application {

    private TextField txtNome;
    private TextField txtAltura;
    private TextField txtPeso;
    private Label lblResultado;
    private TableView<Pessoa> tablePessoas;
    private ObservableList<Pessoa> listaPessoas;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Calculadora de IMC");

        // Inicializa a lista observável
        listaPessoas = FXCollections.observableArrayList();

        // Layout principal
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Painel de formulário (topo)
        VBox formulario = criarFormulario();
        root.setTop(formulario);

        // Tabela (centro)
        VBox tabelaContainer = criarTabela();
        root.setCenter(tabelaContainer);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox criarFormulario() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-radius: 5;");

        Label titulo = new Label("Cadastro de Pessoa");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Grid para os campos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 0, 10, 0));

        // Nome
        Label lblNome = new Label("Nome:");
        txtNome = new TextField();
        txtNome.setPromptText("Digite o nome");
        grid.add(lblNome, 0, 0);
        grid.add(txtNome, 1, 0);

        // Altura
        Label lblAltura = new Label("Altura (m):");
        txtAltura = new TextField();
        txtAltura.setPromptText("Ex: 1.75");
        grid.add(lblAltura, 0, 1);
        grid.add(txtAltura, 1, 1);

        // Peso
        Label lblPeso = new Label("Peso (kg):");
        txtPeso = new TextField();
        txtPeso.setPromptText("Ex: 70.5");
        grid.add(lblPeso, 0, 2);
        grid.add(txtPeso, 1, 2);

        // Label de resultado
        lblResultado = new Label("");
        lblResultado.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #0066cc;");

        // Botões
        HBox botoes = new HBox(10);
        botoes.setAlignment(Pos.CENTER);

        Button btnCalcular = new Button("Calcular IMC");
        btnCalcular.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnCalcular.setOnAction(e -> calcularIMC());

        Button btnAdicionar = new Button("Adicionar à Lista");
        btnAdicionar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnAdicionar.setOnAction(e -> adicionarPessoa());

        Button btnSalvar = new Button("Salvar Arquivo");
        btnSalvar.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        btnSalvar.setOnAction(e -> salvarArquivo());

        Button btnCarregar = new Button("Carregar Arquivo");
        btnCarregar.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white;");
        btnCarregar.setOnAction(e -> carregarArquivo());

        Button btnLimpar = new Button("Limpar");
        btnLimpar.setOnAction(e -> limparCampos());

        botoes.getChildren().addAll(btnCalcular, btnAdicionar, btnSalvar, btnCarregar, btnLimpar);

        vbox.getChildren().addAll(titulo, grid, lblResultado, botoes);
        return vbox;
    }

    private VBox criarTabela() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 0, 0, 0));

        Label titulo = new Label("Pessoas Cadastradas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        tablePessoas = new TableView<>();
        tablePessoas.setItems(listaPessoas);

        // Colunas
        TableColumn<Pessoa, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(200);

        TableColumn<Pessoa, Double> colAltura = new TableColumn<>("Altura (m)");
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colAltura.setPrefWidth(100);

        TableColumn<Pessoa, Double> colPeso = new TableColumn<>("Peso (kg)");
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colPeso.setPrefWidth(100);

        TableColumn<Pessoa, Double> colIMC = new TableColumn<>("IMC");
        colIMC.setCellValueFactory(new PropertyValueFactory<>("imc"));
        colIMC.setPrefWidth(100);

        TableColumn<Pessoa, String> colClassificacao = new TableColumn<>("Classificação");
        colClassificacao.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
        colClassificacao.setPrefWidth(200);

        tablePessoas.getColumns().addAll(colNome, colAltura, colPeso, colIMC, colClassificacao);

        vbox.getChildren().addAll(titulo, tablePessoas);
        VBox.setVgrow(tablePessoas, Priority.ALWAYS);
        return vbox;
    }

    private void calcularIMC() {
        try {
            String nome = txtNome.getText().trim();
            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));

            if (nome.isEmpty()) {
                mostrarAlerta("Erro", "Por favor, digite o nome!");
                return;
            }

            if (altura <= 0 || peso <= 0) {
                mostrarAlerta("Erro", "Altura e peso devem ser valores positivos!");
                return;
            }

            Pessoa pessoa = new Pessoa(nome, altura, peso);
            lblResultado.setText(String.format("IMC: %.2f - %s",
                    pessoa.getImc(), pessoa.getClassificacao()));

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Por favor, digite valores numéricos válidos para altura e peso!");
        }
    }

    private void adicionarPessoa() {
        try {
            String nome = txtNome.getText().trim();
            double altura = Double.parseDouble(txtAltura.getText().replace(",", "."));
            double peso = Double.parseDouble(txtPeso.getText().replace(",", "."));

            if (nome.isEmpty()) {
                mostrarAlerta("Erro", "Por favor, digite o nome!");
                return;
            }

            if (altura <= 0 || peso <= 0) {
                mostrarAlerta("Erro", "Altura e peso devem ser valores positivos!");
                return;
            }

            Pessoa pessoa = new Pessoa(nome, altura, peso);
            listaPessoas.add(pessoa);
            limparCampos();
            mostrarInformacao("Sucesso", "Pessoa adicionada à lista com sucesso!");

        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Por favor, digite valores numéricos válidos!");
        }
    }

    private void salvarArquivo() {
        try {
            if (listaPessoas.isEmpty()) {
                mostrarAlerta("Aviso", "Não há pessoas na lista para salvar!");
                return;
            }

            ArquivoUtil.salvarPessoas(listaPessoas);
            mostrarInformacao("Sucesso", "Dados salvos com sucesso no arquivo 'dados_pessoas.txt'!");

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao salvar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void carregarArquivo() {
        try {
            listaPessoas.clear();
            listaPessoas.addAll(ArquivoUtil.carregarPessoas());

            if (listaPessoas.isEmpty()) {
                mostrarInformacao("Informação", "Nenhum dado encontrado no arquivo.");
            } else {
                mostrarInformacao("Sucesso",
                        "Dados carregados com sucesso! " + listaPessoas.size() + " pessoa(s) encontrada(s).");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao carregar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNome.clear();
        txtAltura.clear();
        txtPeso.clear();
        lblResultado.setText("");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInformacao(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
