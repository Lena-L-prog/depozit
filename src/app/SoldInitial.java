package app;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class SoldInitial {
    private final ObjectProperty<Product> product = new SimpleObjectProperty<>();
    private final IntegerProperty productId = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> quantity = new SimpleObjectProperty<>(BigDecimal.ZERO);

    public SoldInitial() {
    }

    // Конструктор для создания SoldInitial из данных БД

    public SoldInitial(int id, BigDecimal quantity, int productId, String productName, String unit) {
        this.productId.set(productId);
        this.quantity.set(quantity);

        Product p = new Product(productId, productName, unit);
        this.product.set(p);
    }

    // Конструктор для использования в UI
    public SoldInitial(Product product, BigDecimal quantity) {
        this.product.set(product);
        if (product != null) {
            this.productId.set(product.getId());
        }
        this.quantity.set(quantity);
    }

    public Product getProduct() {
        return product.get();
    }

    public void setProduct(Product product) {
        this.product.set(product);
        if (product != null) {
            this.productId.set(product.getId());
        }
    }

    public ObjectProperty<Product> productProperty() {
        return product;
    }

    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int id) {
        this.productId.set(id);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    public BigDecimal getQuantity() {
        return quantity.get();
    }

    public void setQuantity(BigDecimal q) {
        this.quantity.set(q);
    }

    public ObjectProperty<BigDecimal> quantityProperty() {
        return quantity;
    }
}
