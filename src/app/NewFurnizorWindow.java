package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class NewFurnizorWindow extends Stage {

    public NewFurnizorWindow() {
        setTitle("Furnizor nou");

        Label nameLabel = new Label("Denumirea:");
        TextField nameField = new TextField();

        Label phoneLabel = new Label("Telefon:");
        TextField phoneField = new TextField();

        Button clearButton = new Button("Curăță");
        clearButton.getStyleClass().add("add-line-button");

        Button cancelButton = new Button("Anulează");
        cancelButton.getStyleClass().add("cancel-button");

        Button saveButton = new Button("Salvează");
        saveButton.getStyleClass().add("save-button");

        clearButton.setOnAction(e -> {
            nameField.clear();
            phoneField.clear();
        });

        cancelButton.setOnAction(e -> close());


        saveButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();

            if (!name.isEmpty() && !phone.isEmpty()) {
                app.Furnizor furnizor = new app.Furnizor(0,name, phone);
                app.FurnizorDAO.insertFurnizor(furnizor);  // Сохраняем в БД
                close(); // Закрываем окно
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Eroare");
                alert.setHeaderText(null);
                alert.setContentText("Completează toate câmpurile!");
                alert.showAndWait();
            }
        });


        VBox form = new VBox(10,
                new HBox(10, nameLabel, nameField),
                new HBox(10, phoneLabel, phoneField),
                new HBox(10, clearButton, cancelButton, saveButton)
        );
        form.setPadding(new Insets(15));
        form.setAlignment(Pos.CENTER);

        Scene scene = new Scene(form, 350, 200);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        setScene(scene);
    }
}
