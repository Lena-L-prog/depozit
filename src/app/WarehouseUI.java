package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WarehouseUI {

    private BorderPane root;

    // 1. Сделали поле класса
    private TableView<ProductSummary> table;

    public WarehouseUI() {
        root = new BorderPane(); //главный корень

        // ===== ВЕРХНЯЯ ПАНЕЛЬ =====
        HBox topBar = new HBox(30);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");

        // Часы
        Label currentDate = new Label();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    currentDate.setText("Data de azi: " + LocalDateTime.now().format(formatter));
                }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
        currentDate.setStyle("-fx-text-fill: yellow;");

        // Календарь
        DatePicker fromDate = new DatePicker();
        DatePicker toDate = new DatePicker();

        fromDate.setPromptText("dd.MM.yy");
        toDate.setPromptText("dd.MM.yy");

        Label fromLabel = new Label("din");
        fromLabel.setStyle("-fx-text-fill: white;");

        Label toLabel = new Label("pina la");
        toLabel.setStyle("-fx-text-fill: white;");

        // Кнопка "Генерировать отчет"
        Button genBtn = new Button("Generează raport");
        genBtn.getStyleClass().add("save-button");

        // 3. Обработчик кнопки с использованием поля table
        genBtn.setOnAction(e -> {
            LocalDate from = fromDate.getValue();
            LocalDate to = toDate.getValue();

            if (from == null || to == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Alegeți perioada corectă.");
                alert.show();
                return;
            }

            List<ProductSummary> summaryList = ProductSummaryDAO.getProductSummary(from, to);
            table.setItems(FXCollections.observableArrayList(summaryList));
        });

        // Добавляем кнопку в верхнюю панель
        topBar.getChildren().addAll(currentDate, fromLabel, fromDate, toLabel, toDate, genBtn);

        // ===== ЛЕВАЯ ПАНЕЛЬ =====
        VBox sideBar = new VBox(10);
        sideBar.setPrefWidth(300);
        sideBar.setPadding(new Insets(50));
        sideBar.setAlignment(Pos.TOP_CENTER);
        sideBar.setId("side-bar");
        sideBar.setSpacing(50);
        sideBar.setPrefHeight(Double.MAX_VALUE);

        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/icons/v.png")));
        imageView1.setFitWidth(40);
        imageView1.setFitHeight(40);

        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/icons/r.png")));
        imageView2.setFitWidth(40);
        imageView2.setFitHeight(40);

        Button incomeBtn = new Button("Recepție", imageView1);
        incomeBtn.getStyleClass().add("side-button");
        incomeBtn.setContentDisplay(ContentDisplay.LEFT);
        Button outcomeBtn = new Button("Livrare", imageView2);
        outcomeBtn.getStyleClass().add("side-button");
        outcomeBtn.setContentDisplay(ContentDisplay.LEFT);
        Button openWindowButton = new Button("Sold inițial");
        openWindowButton.getStyleClass().add("initial-sold-button");
        Button furnizoriButton = new Button("Furnizori");
        furnizoriButton.getStyleClass().add("furnizori-button");

        openWindowButton.setOnAction(e -> {
            AddProductWindow window = new AddProductWindow();
            window.show();
        });
        incomeBtn.setOnAction(e -> {
            AddProductWindow window = new AddProductWindow();
            window.show();
        });
        furnizoriButton.setOnAction(e -> new FurnizoriWindow().show());

        Button prtBtn = new Button("Printeaza");
        prtBtn.getStyleClass().add("print-button");

        //===================Print==================================
        prtBtn.setOnAction(e -> {
            // Здесь должен быть метод, который собирает данные для отчета
            List<ProductSummary> reportData = ProductSummaryDAO.getProductSummary(fromDate.getValue(), toDate.getValue());

            if (reportData == null || reportData.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nu sunt date pentru perioada selectată.");
                alert.showAndWait();
                return;
            }

            // Показываем окно предварительного просмотра
            PreviewReportWindow preview = new PreviewReportWindow(reportData, fromDate.getValue(), toDate.getValue());
            preview.show();
        });


        //++++++++++++++++++Print++++++++++++++++++++++++++++++++++

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideBar.getChildren().addAll(incomeBtn, outcomeBtn, openWindowButton, furnizoriButton, prtBtn);

        incomeBtn.setOnAction(e -> FacturaWindow.showFacturaWindow(TipFactura.PRIMIRE));
        outcomeBtn.setOnAction(e -> FacturaWindow.showFacturaWindow(TipFactura.LIVRARE));

        // ===== ТАБЛИЦА =====

        // 2. Инициализация поля класса
        table = new TableView<>();

        TableColumn<ProductSummary, String> produsCol = new TableColumn<>("Produs");
        produsCol.setCellValueFactory(new PropertyValueFactory<>("denumirea"));

        TableColumn<ProductSummary, Integer> soldInitCol = new TableColumn<>("Sold inițial");
        soldInitCol.setCellValueFactory(new PropertyValueFactory<>("soldInitial"));

        TableColumn<ProductSummary, Integer> venitCol = new TableColumn<>("Venituri");
        venitCol.setCellValueFactory(new PropertyValueFactory<>("venituri"));

        TableColumn<ProductSummary, Integer> cheltCol = new TableColumn<>("Cheltuieli");
        cheltCol.setCellValueFactory(new PropertyValueFactory<>("cheltuieli"));

        TableColumn<ProductSummary, Integer> soldFinalCol = new TableColumn<>("Sold final");
        soldFinalCol.setCellValueFactory(new PropertyValueFactory<>("soldFinal"));

        produsCol.prefWidthProperty().bind(table.widthProperty().multiply(0.40));
        soldInitCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        venitCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        cheltCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
        soldFinalCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        table.getColumns().addAll(produsCol, soldInitCol, venitCol, cheltCol, soldFinalCol);
        table.setItems(FXCollections.observableArrayList());

        // ===== Расстановка =====



        root.setTop(topBar);
        root.setLeft(sideBar);
        root.setCenter(table);

    }

    public BorderPane getRoot() {
        return root;
    }
}
