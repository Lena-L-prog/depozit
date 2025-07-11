package app;

import javafx.beans.property.*;
import java.math.BigDecimal;

public class FacturaProdus {
    private ObjectProperty<Product> produs = new SimpleObjectProperty<>();
    private ObjectProperty<BigDecimal> cantitate = new SimpleObjectProperty<>(BigDecimal.ZERO);

    public FacturaProdus() {}

    public FacturaProdus(Product produs, BigDecimal cantitate) {
        this.produs.set(produs);
        this.cantitate.set(cantitate);
    }

    public Product getProdus() {
        return produs.get();
    }

    public void setProdus(Product produs) {
        this.produs.set(produs);
    }

    public ObjectProperty<Product> produsProperty() {
        return produs;
    }

    public BigDecimal getCantitate() {
        return cantitate.get();
    }

    public void setCantitate(BigDecimal cantitate) {
        this.cantitate.set(cantitate);
    }

    public ObjectProperty<BigDecimal> cantitateProperty() {
        return cantitate;
    }
}
