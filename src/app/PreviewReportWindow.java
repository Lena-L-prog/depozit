package app;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PreviewReportWindow {

    private Stage stage;
    private TableView<ProductSummary> table;

    public PreviewReportWindow(List<ProductSummary> reportData, LocalDate fromDate, LocalDate toDate) {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Previzualizare raport");

        Label titleLabel = new Label("Darea de seamă pentru produse pe perioada din " + fromDate + " până la " + toDate);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleLabel.setPadding(new Insets(10));

        table = new TableView<>();
        setupTableColumns();
        table.setItems(FXCollections.observableArrayList(reportData));

        Button saveBtn = new Button("Salvează PDF");
        Button closeBtn = new Button("Închide");

        saveBtn.setOnAction(e -> savePdf(reportData, fromDate, toDate));
        closeBtn.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, saveBtn, closeBtn);
        buttons.setPadding(new Insets(10));
        buttons.setStyle("-fx-alignment: center;");

        VBox root = new VBox(10, titleLabel, table, buttons);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
    }

    private void setupTableColumns() {
        TableColumn<ProductSummary, String> produsCol = new TableColumn<>("Produs");
        produsCol.setCellValueFactory(new PropertyValueFactory<>("denumirea"));
        produsCol.prefWidthProperty().bind(table.widthProperty().multiply(0.40));

        TableColumn<ProductSummary, Integer> soldInitCol = new TableColumn<>("Sold inițial");
        soldInitCol.setCellValueFactory(new PropertyValueFactory<>("soldInitial"));
        soldInitCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        TableColumn<ProductSummary, Integer> venitCol = new TableColumn<>("Venituri");
        venitCol.setCellValueFactory(new PropertyValueFactory<>("venituri"));
        venitCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        TableColumn<ProductSummary, Integer> cheltCol = new TableColumn<>("Cheltuieli");
        cheltCol.setCellValueFactory(new PropertyValueFactory<>("cheltuieli"));
        cheltCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        TableColumn<ProductSummary, Integer> soldFinalCol = new TableColumn<>("Sold final");
        soldFinalCol.setCellValueFactory(new PropertyValueFactory<>("soldFinal"));
        soldFinalCol.prefWidthProperty().bind(table.widthProperty().multiply(0.15));

        table.getColumns().addAll(produsCol, soldInitCol, venitCol, cheltCol, soldFinalCol);
    }

    private void savePdf(List<ProductSummary> reportData, LocalDate fromDate, LocalDate toDate) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvează raport PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            boolean success = PdfReportExporter.exportReportToPdf(file, reportData, fromDate, toDate);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Raportul a fost salvat cu succes!");
                alert.showAndWait();
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Eroare la salvarea raportului.");
                alert.showAndWait();
            }
        }
    }

    public void show() {
        stage.showAndWait();
    }
}
