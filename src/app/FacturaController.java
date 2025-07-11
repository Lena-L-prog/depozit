package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class FacturaController {

    private app.TipFactura tipFactura;
    private String numarFactura;
    private LocalDate dataFactura;
    private String furnizor; // Только для PRIMIRE
    private List<LinieFactura> produse;

    public FacturaController(app.TipFactura tipFactura) {
        this.tipFactura = tipFactura;
        this.produse = new ArrayList<>();
    }

    public app.TipFactura getTipFactura() {
        return tipFactura;
    }

    public String getNumarFactura() {
        return numarFactura;
    }

    public void setNumarFactura(String numarFactura) {
        this.numarFactura = numarFactura;
    }

    public LocalDate getDataFactura() {
        return dataFactura;
    }

    public void setDataFactura(LocalDate dataFactura) {
        this.dataFactura = dataFactura;
    }

    public String getFurnizor() {
        return furnizor;
    }

    public void setFurnizor(String furnizor) {
        this.furnizor = furnizor;
    }

    public List<LinieFactura> getProduse() {
        return produse;
    }

    public void adaugaProdus(String denumire, double cantitate) {
        produse.add(new LinieFactura(denumire, cantitate));
    }

    public void stergeProdus(LinieFactura linie) {
        produse.remove(linie);
    }

    public void clearProduse() {
        produse.clear();
    }

    // Заглушка для сохранения
    public void saveFactura() {
        System.out.println("Se salvează factura:");
        System.out.println("Tip: " + tipFactura);
        System.out.println("Număr: " + numarFactura);
        System.out.println("Data: " + dataFactura);
        if (tipFactura == app.TipFactura.PRIMIRE) {
            System.out.println("Furnizor: " + furnizor);
        }
        for (LinieFactura linie : produse) {
            System.out.println("Produs: " + linie.getDenumire() + ", Cantitate: " + linie.getCantitate());
        }
        // Здесь будет SQL вставка
    }

    // Заглушка для загрузки (будет использоваться позже)
    public void loadFactura(int id) {
        // Здесь будет SQL SELECT
        System.out.println("Se încarcă factura cu ID: " + id);
    }

    // Вложенный класс строки накладной
    public static class LinieFactura {
        private String denumire;
        private double cantitate;

        public LinieFactura(String denumire, double cantitate) {
            this.denumire = denumire;
            this.cantitate = cantitate;
        }

        public String getDenumire() {
            return denumire;
        }

        public void setDenumire(String denumire) {
            this.denumire = denumire;
        }

        public double getCantitate() {
            return cantitate;
        }

        public void setCantitate(double cantitate) {
            this.cantitate = cantitate;
        }
    }
}
