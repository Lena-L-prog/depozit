package app;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductSummaryDAO {

    public static List<ProductSummary> getProductSummary(LocalDate from, LocalDate to) {
        List<ProductSummary> summaryList = new ArrayList<>();

        String sql = """
               
                             SELECT
                                 p.denumirea,
                                 IFNULL(s.quantity, 0) AS sold_initial,
                                 IFNULL(fi_sum.venituri, 0) AS venituri,
                                 IFNULL(fo_sum.cheltuieli, 0) AS cheltuieli
                             FROM produse p
                             LEFT JOIN solduri_initiale s
                                 ON p.ID = s.product_id
                             LEFT JOIN (
                                 SELECT
                                     fip.produs_id,
                                     SUM(fip.cantitate) AS venituri
                                 FROM facturi_intrare_produse fip
                                 JOIN facturi_intrare fi
                                     ON fip.factura_id = fi.ID
                                 WHERE fi.data_intrare BETWEEN ? AND ?
                                 GROUP BY fip.produs_id
                             ) fi_sum
                                 ON p.ID = fi_sum.produs_id
                             LEFT JOIN (
                                 SELECT
                                     fop.produs_id_ies,
                                     SUM(fop.cantitate) AS cheltuieli
                                 FROM facturi_iesire_produse fop
                                 JOIN facturi_iesire fo
                                     ON fop.factura_id = fo.ID
                                 WHERE fo.data_iesire BETWEEN ? AND ?
                                 GROUP BY fop.produs_id_ies
                             ) fo_sum
                                 ON p.ID = fo_sum.produs_id_ies;
                         """;
                            


        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String fromStr = from.toString(); // формат YYYY-MM-DD
            String toStr = to.toString();

            // Параметры для приходов и расходов
            stmt.setString(1, fromStr);
            stmt.setString(2, toStr);
            stmt.setString(3, fromStr);
            stmt.setString(4, toStr);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String denumirea = rs.getString("denumirea");
                int soldInitial = rs.getInt("sold_initial");
                int venituri = rs.getInt("venituri");
                int cheltuieli = rs.getInt("cheltuieli");

                summaryList.add(new ProductSummary(denumirea, soldInitial, venituri, cheltuieli));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summaryList;
    }
}