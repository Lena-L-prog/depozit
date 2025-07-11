package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NewProductWindow extends Stage {
    private TextField denumireTextField;
    private TextField unitateTextField;

    public NewProductWindow() {

        setTitle("Produs nou");

        denumireTextField = new TextField();
        unitateTextField = new TextField();

        Label nameLabel = new Label("Denumire:");
        Label unitLabel = new Label("Unitatea:");

        Button clearButton = new Button("Curăță");
        clearButton.getStyleClass().add("add-line-button");
        Button cancelButton = new Button("Anulează");
        cancelButton.getStyleClass().add("cancel-button");
        Button saveButton = new Button("Salvează");
        saveButton.getStyleClass().add("save-button");

        clearButton.setOnAction(e -> {
            denumireTextField.clear();
            unitateTextField.clear();
        });

        cancelButton.setOnAction(e -> close());

        saveButton.setOnAction(e -> {
            String denumire = denumireTextField.getText().trim();
            String unitate = unitateTextField.getText().trim();

            if (denumire.isEmpty() || unitate.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Completați toate câmpurile!");
                alert.showAndWait();
                return;
            }

            app.Product newProduct = new app.Product(denumire, unitate);

            try {
                app.ProductDAO.insertProduct(newProduct);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produsul a fost adăugat cu succes!");
                alert.showAndWait();
                this.close();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Eroare la salvarea produsului: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        VBox form = new VBox(10,
                new HBox(10, nameLabel, denumireTextField),
                new HBox(10, unitLabel, unitateTextField),
                new HBox(10, clearButton, cancelButton, saveButton)
        );
        form.setPadding(new Insets(15));
        form.setAlignment(Pos.CENTER);

        Scene scene = new Scene(form, 350, 200);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        setScene(scene);
    }
}