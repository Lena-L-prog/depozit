package app;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

public class FacturaWindow {

    public static void showFacturaWindow(TipFactura tip) {
        Stage stage = new Stage();

        String titlu = (tip == TipFactura.PRIMIRE)
                ? "Introducerea facturilor primite"
                : "Introducerea livrărilor";
        stage.setTitle(titlu);

        Label titleLabel = new Label(titlu);
        titleLabel.getStyleClass().add("window-title");

        // Верхняя панель
        Label numarLabel = new Label("Număr:");
        TextField numarField = new TextField();
        Label dataLabel = new Label("Data:");
        DatePicker datePicker = new DatePicker();

        HBox headerBox = new HBox(10, numarLabel, numarField, dataLabel, datePicker);
        headerBox.setPadding(new Insets(5));

        ComboBox<Furnizor> furnizorComboBox = null;
        if (tip == TipFactura.PRIMIRE) {
            furnizorComboBox = new ComboBox<>(FXCollections.observableArrayList(FurnizorDAO.getAllFurnizori()));
            furnizorComboBox.setPromptText("Alegeți furnizorul");
            furnizorComboBox.setMaxWidth(Double.MAX_VALUE);

            furnizorComboBox.setCellFactory(cb -> new ListCell<>() {
                @Override
                protected void updateItem(Furnizor item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });
            furnizorComboBox.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Furnizor item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

            headerBox.getChildren().addAll(new Label("Furnizor:"), furnizorComboBox);
        }

        VBox topBox = new VBox(10, titleLabel, headerBox);
        topBox.setPadding(new Insets(10));

        // Таблица
        ObservableList<FacturaProdus> produseList = FXCollections.observableArrayList();
        TableView<FacturaProdus> tableView = new TableView<>(produseList);
        ObservableList<Product> allProducts = FXCollections.observableArrayList(ProductDAO.getAllProducts());
        tableView.setItems(produseList);
        tableView.setItems(produseList);
        tableView.setEditable(true);

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        TableColumn<FacturaProdus, Product> denumireaCol = new TableColumn<>("Denumirea");
        denumireaCol.setCellValueFactory(data -> data.getValue().produsProperty());
        denumireaCol.setCellFactory(ComboBoxTableCell.forTableColumn(allProducts));
        denumireaCol.setOnEditCommit(event -> {
            FacturaProdus produs = event.getRowValue();
            produs.setProdus(event.getNewValue());
        });


        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        TableColumn<FacturaProdus, String> unitateaCol = new TableColumn<>("Unitatea");
        unitateaCol.setCellValueFactory(data -> {
            Product produs = data.getValue().getProdus();
            String unit = (produs != null) ? produs.getUnit() : "";
            return new SimpleStringProperty(unit);
        });
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        /*TableColumn<FacturaProdus, BigDecimal> cantCol = new TableColumn<>("Cantitatea");
        cantCol.setCellValueFactory(data -> data.getValue().cantitateProperty());

        cantCol.setCellFactory(tc -> new TableCell<>() {
            private final TextField textField = new TextField();

            {
                textField.setMaxWidth(Double.MAX_VALUE);

                // Ввод только допустимых чисел
               /* textField.textProperty().addListener((obs, oldVal, newVal) -> {
                    String cleaned = newVal.replace(",", ".");
                    if (!cleaned.matches("\\d*(\\.\\d*)?")) {
                        textField.setText(oldVal);
                    } else {
                        try {
                            BigDecimal val = new BigDecimal(cleaned);
                            getTableView().getItems().get(getIndex()).setCantitate(val);
                        } catch (NumberFormatException ignored) {}
                    }
                });

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText().replace(",", ".");
                    if (newText.matches("\\d*(\\.\\d*)?")) {
                        return change;
                    }
                    return null;
                };

                TextFormatter<String> formatter = new TextFormatter<>(filter);
                textField.setTextFormatter(formatter);

// Обновляем модель при каждом изменении
                formatter.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.isEmpty()) {
                        try {
                            BigDecimal val = new BigDecimal(newVal.replace(",", "."));
                            getTableView().getItems().get(getIndex()).setCantitate(val);
                        } catch (NumberFormatException ignored) {}
                    }
                });


                // Обработка Enter
                textField.setOnAction(e -> {
                    int currentIndex = getIndex();
                    TableView<FacturaProdus> table = getTableView();
                    if (currentIndex < table.getItems().size() - 1) {
                        table.edit(currentIndex + 1, tc); // переход к следующей строке в той же колонке
                    } else {
                        // если последняя строка — можно автоматически добавить новую
                        // table.getItems().add(new FacturaProdus());
                        // table.edit(currentIndex + 1, tc);
                        textField.getParent().requestFocus(); // снять фокус, если последняя строка
                    }
                });
            }

            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    String currentText = textField.getText();
                    String newText = item != null ? item.toPlainString() : "";
                    if (!currentText.equals(newText)) {
                        textField.setText(newText);
                    }
                    setGraphic(textField);
                }
            }
        });*/
        TableColumn<FacturaProdus, BigDecimal> cantCol = new TableColumn<>("Cantitatea");
        cantCol.setCellValueFactory(data -> data.getValue().cantitateProperty());
        cantCol.setCellFactory(tc -> new TableCell<>() {
            private TextField textField;

            @Override
            public void startEdit() {
                super.startEdit();
                if (textField == null) {
                    createTextField();
                }

                BigDecimal value = getItem();
                textField.setText(value != null ? value.toPlainString() : "");
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                textField.selectAll();
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() != null ? getItem().toPlainString() : "");
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }

            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        if (textField != null) {
                            textField.setText(item != null ? item.toPlainString() : "");
                        }
                        setGraphic(textField);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                    } else {
                        setText(item != null ? item.toPlainString() : "");
                        setContentDisplay(ContentDisplay.TEXT_ONLY);
                    }
                }
            }

            private void createTextField() {
                textField = new TextField();

                UnaryOperator<TextFormatter.Change> filter = change -> {
                    String newText = change.getControlNewText().replace(",", ".");
                    if (newText.matches("\\d*(\\.\\d*)?")) {
                        return change;
                    }
                    return null;
                };

                TextFormatter<String> formatter = new TextFormatter<>(filter);
                textField.setTextFormatter(formatter);

                textField.setOnAction(e -> {
                    commitEdit(parseBigDecimal(textField.getText()));
                });

                textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused) {
                        commitEdit(parseBigDecimal(textField.getText()));
                    }
                });
            }

            private BigDecimal parseBigDecimal(String text) {
                try {
                    return new BigDecimal(text.replace(",", "."));
                } catch (Exception e) {
                    return getItem(); // если не парсится, возвращаем старое значение
                }
            }

            @Override
            public void commitEdit(BigDecimal newValue) {
                super.commitEdit(newValue);
                FacturaProdus produs = getTableView().getItems().get(getIndex());
                produs.setCantitate(newValue); // обновляем модель
                getTableView().refresh(); // гарантируем обновление таблицы
            }
        });



        //**********************************

        TableColumn<FacturaProdus, Void> actionCol = new TableColumn<>("");

       actionCol.setCellFactory(col -> new TableCell<>() {

            private final Button addBtn = new Button("\u2795");
            private final Button delBtn = new Button("\u274C");
            private final HBox box = new HBox(3, addBtn, delBtn);

            {
                addBtn.setStyle("-fx-text-fill: green; -fx-font-size: 18px; -fx-background-color: transparent;");
                delBtn.setStyle("-fx-text-fill: red; -fx-font-size: 18px; -fx-background-color: transparent;");

                addBtn.setOnAction(e -> {
                    int index = getIndex();
                    FacturaProdus newItem = new FacturaProdus();
                    tableView.getItems().add(index + 1, newItem);
                });

                delBtn.setOnAction(e -> {
                    FacturaProdus selected = getTableView().getItems().get(getIndex());
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




        //***********************************



        // Подгонка ширин колонок при изменении ширины таблицы
        tableView.widthProperty().addListener((obs, oldVal, newVal) -> {
            double width = newVal.doubleValue();
            denumireaCol.setPrefWidth(width * 0.50);
            unitateaCol.setPrefWidth(width * 0.10);
            cantCol.setPrefWidth(width * 0.20);
            actionCol.setPrefWidth(width * 0.20);
        });
        tableView.getColumns().addAll(denumireaCol, unitateaCol, cantCol, actionCol);

        Button addButton = new Button("Adaugă rind");
        addButton.getStyleClass().add("add-line-button");

        addButton.setOnAction(e -> {
            FacturaProdus newRow = new FacturaProdus(); // Конструктор без параметров обязателен
            produseList.add(newRow);
            tableView.getSelectionModel().select(newRow);
            tableView.scrollTo(newRow);
        });
        //addButton.setOnAction(e -> produseList.add(new FacturaProdus()));

        VBox tableBox = new VBox(10, tableView, addButton);
        tableBox.setPadding(new Insets(10));

        // Кнопки сохранения и отмены
        Button saveBtn = new Button("Salvează");
        saveBtn.getStyleClass().add("save-button");
        ComboBox<Furnizor> finalFurnizorComboBox = furnizorComboBox;

        saveBtn.setOnAction(e -> {
            String numar = numarField.getText();
            LocalDate data = datePicker.getValue();

            if (numar.isEmpty() || data == null || (tip == TipFactura.PRIMIRE && (finalFurnizorComboBox == null || finalFurnizorComboBox.getValue() == null))) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Completați toate câmpurile!");
                alert.show();
                return;
            }

            int facturaId;
            if (tip == TipFactura.PRIMIRE) {
                Furnizor furnizor = finalFurnizorComboBox.getValue();
                facturaId = FacturaDAO.insertFacturaIntrare(numar, data.toString(), furnizor.getId());
            } else {
                facturaId = FacturaDAO.insertFacturaIesire(numar, data.toString());
            }

            for (FacturaProdus fp : produseList) {
                Product produs = fp.getProdus();
                BigDecimal cantitate = fp.getCantitate();

                if (produs != null && cantitate != null) {
                    if (tip == TipFactura.PRIMIRE) {
                        FacturaDAO.insertFacturaIntrareProdus(facturaId, produs.getId(), cantitate);
                    } else {
                        FacturaDAO.insertFacturaIesireProdus(facturaId, produs.getId(), cantitate);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Factura a fost salvată cu succes!");
            alert.show();
            stage.close();
        });

        Button cancelBtn = new Button("Renunță");
        cancelBtn.getStyleClass().add("cancel-button");
        cancelBtn.setOnAction(e -> stage.close());

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-alignment: center;");
        buttonBox.getChildren().addAll(cancelBtn, saveBtn);

        if (tip == TipFactura.PRIMIRE) {
            Button addNewProductBtn = new Button("Adaugă produs nou");
            addNewProductBtn.getStyleClass().add("save-button");
            addNewProductBtn.setOnAction(e -> new NewProductWindow().show());
            buttonBox.getChildren().add(0, addNewProductBtn);
        }

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(tableBox);
        root.setBottom(buttonBox);
        root.setPadding(new Insets(10));
        root.getStyleClass().add("main-pane");

        Scene scene = new Scene(root, 700, 700);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
    }
}
