package app;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;

import java.math.BigDecimal;

public class AddProductWindow extends Stage {
    private final TableView<app.SoldInitial> tableView = new TableView<>();
    private final ObservableList<app.SoldInitial> data;
    private final ObservableList<app.Product> products;

    public AddProductWindow() {
        setTitle("Introducerea soldurilor");
        Label titleLabel = new Label("Inroducerea soldurilor initiale");
        titleLabel.getStyleClass().add("title-label");

        products = FXCollections.observableArrayList(app.ProductDAO.getAllProducts());
        data = FXCollections.observableArrayList(app.SoldInitialDAO.getAll());

        tableView.setItems(data);
        tableView.setEditable(true);

        TableColumn<app.SoldInitial, app.Product> productCol = new TableColumn<>("Produs");
        productCol.setCellValueFactory(param -> param.getValue().productProperty());
        productCol.setCellFactory(ComboBoxTableCell.forTableColumn(products));
        productCol.setOnEditCommit(event -> {
            app.SoldInitial si = event.getRowValue();
            app.Product selected = event.getNewValue();

            if (selected != null) {
                boolean exists = data.stream()
                        .anyMatch(item -> item != si
                                && item.getProduct() != null
                                && item.getProduct().getId() == selected.getId());
                if (exists) {
                    showAlert("Acest produs are deja un sold introdus!");
                    tableView.refresh(); // чтобы откатить выбор обратно
                } else {
                    si.setProduct(selected);
                }
            }
        });

        TableColumn<app.SoldInitial, BigDecimal> qtyCol = new TableColumn<>("Cantitate");
        qtyCol.setCellValueFactory(param -> param.getValue().quantityProperty());
        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        qtyCol.setOnEditCommit(event -> {
            app.SoldInitial si = event.getRowValue();
            si.setQuantity(event.getNewValue());
        });
        TableColumn<SoldInitial, Void> actionCol = new TableColumn<>("");

        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            productCol.setPrefWidth(width * 0.40);
            qtyCol.setPrefWidth(width * 0.15);
            actionCol.setPrefWidth(width*0.15);

        });
            //tableView.getColumns().addAll(productCol, qtyCol, actionCol);


        //+++++++++++++++
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button addBtn = new Button("\u2795");
            private final Button delBtn = new Button("\u274C");
            private final HBox box = new HBox(5, addBtn, delBtn);

            {
                addBtn.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-background-color: transparent;");
                delBtn.setStyle("-fx-text-fill: red; -fx-font-size: 18px; -fx-background-color: transparent;");
                addBtn.setOnAction(e -> {
                    int index = getIndex();
                    SoldInitial newItem = new SoldInitial();
                    tableView.getItems().add(index + 1, newItem);
                });

                delBtn.setOnAction(e -> {
                    SoldInitial selected = getTableView().getItems().get(getIndex());
                    SoldInitialDAO.delete(selected);
                    tableView.getItems().remove(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });


        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
                    double width = newVal.doubleValue();
                    productCol.setPrefWidth(width * 0.50);
                    qtyCol.setPrefWidth(width * 0.20);
                    actionCol.setPrefWidth(width*0.30);

                });
                tableView.getColumns().addAll(productCol, qtyCol, actionCol);



                //==============================================



                Button addRow = new Button("Adaugă rând");
        addRow.getStyleClass().add("save-button");
        addRow.setOnAction(e -> data.add(new app.SoldInitial()));

        Button deleteRow = new Button("Șterge rând");
        deleteRow.getStyleClass().add("cancel-button");
        deleteRow.setOnAction(e -> {
            app.SoldInitial selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                app.SoldInitialDAO.delete(selected);
                data.remove(selected);
            }
        });

        Button saveBtn = new Button("Salvează");
        saveBtn.getStyleClass().add("cancel-button");
        saveBtn.setOnAction(e -> {
            for (app.SoldInitial si : data) {
                if (si.getProduct() != null && si.getQuantity().compareTo(BigDecimal.ZERO) > 0 &&
                        !app.SoldInitialDAO.existsByProductId(si.getProductId())) {
                    app.SoldInitialDAO.insert(si);
                }
            }
            showAlert("Datele au fost salvate.");
            close();
        });
        Button newBtn = new  Button ("Adauga produs nou");
        newBtn.getStyleClass().add("save-button");
        newBtn.setOnAction(e -> {
            app.NewProductWindow window = new app.NewProductWindow();
            window.show();
        });


        HBox btnBox = new HBox(10, addRow, deleteRow, saveBtn);
        btnBox.setPadding(new Insets(10));

        VBox root = new VBox(10,titleLabel, newBtn,tableView, btnBox);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 450, 500);
        setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
}
