package app;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class Product {
    private final SimpleStringProperty name;
    private final SimpleStringProperty unit;
    private final SimpleObjectProperty<BigDecimal> quantity;
    private final SimpleIntegerProperty id;
    // Конструктор для добавления нового продукта (без id, без quantity)
    public Product(String name, String unit) {
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleObjectProperty<>(BigDecimal.ZERO);
        this.id = new SimpleIntegerProperty(0);
    }
    // Конструктор для создания продукта с id из БД и unit, name
    public Product(int id, String name, String unit) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleObjectProperty<>(BigDecimal.ZERO);
    }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getUnit() { return unit.get(); }
    public void setUnit(String unit) { this.unit.set(unit); }
    public StringProperty unitProperty() { return unit; }

    public BigDecimal getQuantity() { return quantity.get(); }
    public void setQuantity(BigDecimal quantity) { this.quantity.set(quantity); }
    public ObjectProperty<BigDecimal> quantityProperty() { return quantity; }

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    @Override
    public String toString() {
        return getName();
}
}