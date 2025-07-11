package app;



public class ProductSummary {
    private String denumirea;
    private int soldInitial;
    private int venituri;
    private int cheltuieli;
    private int soldFinal;

    public ProductSummary(String denumirea, int soldInitial, int venituri, int cheltuieli) {
        this.denumirea = denumirea;
        this.soldInitial = soldInitial;
        this.venituri = venituri;
        this.cheltuieli = cheltuieli;
        this.soldFinal = soldInitial + venituri - cheltuieli;
    }

    public String getDenumirea() {
        return denumirea;
    }

    public int getSoldInitial() {
        return soldInitial;
    }

    public int getVenituri() {
        return venituri;
    }

    public int getCheltuieli() {
        return cheltuieli;
    }

    public int getSoldFinal() {
        return soldFinal;
    }

}
