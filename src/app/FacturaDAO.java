package app;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;


public class FacturaDAO {

    // ----------- Factura Intrare ----------------

    public static int insertFacturaIntrare(String numar, String data, int furnizorId) {
        String sql = "INSERT INTO facturi_intrare (num_intrare, data_intrare, furnizor_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numar);
            stmt.setString(2, data);  // ✅ сохраняем строку, а не java.sql.Date
            stmt.setInt(3, furnizorId);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static void insertFacturaIntrareProdus(int facturaId, int produsId, BigDecimal cantitate) {
        String sql = "INSERT INTO facturi_intrare_produse (factura_id, produs_id, cantitate) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, facturaId);
            stmt.setInt(2, produsId);
            stmt.setBigDecimal(3, cantitate);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ----------- Factura Iesire ----------------

    public static int insertFacturaIesire(String numar, String data) {
        String sql = "INSERT INTO facturi_iesire (num_iesire, data_iesire) VALUES (?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, numar);
            stmt.setString(2, data);; // YYYY-MM-DD

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1); // ID-ul noii facturi
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void insertFacturaIesireProdus(int facturaId, int produsId, BigDecimal cantitate) {
        String sql = "INSERT INTO facturi_iesire_produse (factura_id, produs_id_ies, cantitate) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, facturaId); // <-- это правильно: связываем с таблицей facturi_iesire
            stmt.setInt(2, produsId);
            stmt.setBigDecimal(3, cantitate);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
