package app;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.cell.TextFieldTableCell;
import java.io.FileOutputStream;
import java.util.List;

public class FurnizoriWindow extends Stage {
   private TableView<app.Furnizor> tableView = new TableView<>();

    public FurnizoriWindow() {
        setTitle("Indrumar furnizorilor");

        // ===== Заголовок =====
        Label titleLabel = new Label("Indrumar furnizorilor");
        titleLabel.getStyleClass().add("title-label");

        // ===== Таблица =====
        TableColumn<app.Furnizor, String> nameCol = new TableColumn<>("Nume");
        nameCol.setCellValueFactory(cell -> cell.getValue().nameProperty());

        TableColumn<app.Furnizor, String> phoneCol = new TableColumn<>("Telefon");
        phoneCol.setCellValueFactory(cell -> cell.getValue().phoneProperty());

        tableView.getColumns().addAll(nameCol, phoneCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.setEditable(true);

        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            app.Furnizor f = event.getRowValue();
            System.out.println("Before update: " + f.getId() + ", oldName=" + f.getName() + ", newName=" + event.getNewValue());
            f.setName(event.getNewValue());
            app.FurnizorDAO.updateFurnizor(f);
            System.out.println("After update called.");
        });

        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());

        phoneCol.setOnEditCommit(event -> {
            app.Furnizor f = event.getRowValue();
            f.setPhone(event.getNewValue());
            app.FurnizorDAO.updateFurnizor(f);
        });

        // ===== Кнопки =====

        Button deleteButton = new Button("Sterge");

        deleteButton.setOnAction(e -> {
            app.Furnizor selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                app.FurnizorDAO.deleteFurnizor(selected.getId());
                loadFurnizori(); // обновляем список после удаления
            } else {
                // Можно вывести предупреждение:
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selectați un furnizor pentru a șterge.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        Button addButton = new Button("Adaugă furnizor nou");

        addButton.setOnAction(e -> {
            app.NewFurnizorWindow newFurnizorWindow = new app.NewFurnizorWindow();
            newFurnizorWindow.showAndWait();
            loadFurnizori();

        });
            //loadFurnizori(); // Обновим список после добавления

        Button printButton = new Button("Printează");
        printButton.setOnAction(e -> exportFurnizoriToPDF());

        addButton.getStyleClass().add("action-button");
        printButton.getStyleClass().add("action-button");

        deleteButton.getStyleClass().add("cancel-button");
        HBox buttonBox = new HBox(10, printButton,  deleteButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10));

        // ===== Layout =====
        VBox mainLayout = new VBox(10, titleLabel, addButton, tableView, buttonBox);
        mainLayout.setPadding(new Insets(15));
        mainLayout.getStyleClass().add("main-window");

        Scene scene = new Scene(mainLayout, 600, 400);
        scene.getStylesheets().add("style.css");

        setScene(scene);

        // Загрузим данные
        loadFurnizori();
    }

    private void loadFurnizori() {
        ObservableList<app.Furnizor> lista = FXCollections.observableArrayList(app.FurnizorDAO.getAllFurnizori());
        tableView.setItems(lista);
    }
    //==================Печать документа====================================================

    private void exportFurnizoriToPDF() {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream("furnizori.pdf"));
            document.open();

            document.add(new Paragraph("Lista furnizorilor"));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(2); // Nume + Telefon
            table.addCell("Nume");
            table.addCell("Telefon");

            List<app.Furnizor> furnizori = app.FurnizorDAO.getAllFurnizori();
            for (app.Furnizor f : furnizori) {
                table.addCell(f.getName());
                table.addCell(f.getPhone());
            }

            document.add(table);
            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF exportat");
            alert.setHeaderText(null);
            alert.setContentText("Fișierul furnizori.pdf a fost creat cu succes.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);
            alert.setContentText("Eroare la crearea fișierului PDF.");
            alert.showAndWait();
        }
    }

}
