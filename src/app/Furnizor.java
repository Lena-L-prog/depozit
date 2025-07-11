package app;

import javafx.beans.property.*;

public class Furnizor {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty phone = new SimpleStringProperty();

    public Furnizor() {}


    public Furnizor(String name, String phone) {
        this.name.set(name);
        this.phone.set(phone);
    }

    public Furnizor(int id, String name, String phone) {
        this.id.set(id);
        this.name.set(name);
        this.phone.set(phone);
    }

    // --- ID ---
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // --- Name ---
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // --- Phone ---
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }
}
