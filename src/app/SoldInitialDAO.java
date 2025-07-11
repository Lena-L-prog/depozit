package app;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SoldInitialDAO {

    public static List<app.SoldInitial> getAll() {
        List<app.SoldInitial> list = new ArrayList<>();
        String sql = "SELECT s.id, s.quantity, p.id AS productId, p.denumirea, p.unit FROM solduri_initiale s JOIN produse p ON s.product_id = p.id";

        try (Connection conn = app.DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                app.SoldInitial sold = new app.SoldInitial(
                        rs.getInt("id"),
                        rs.getBigDecimal("quantity"),
                        rs.getInt("productId"),
                        rs.getString("denumirea"),
                        rs.getString("unit")
                );
                list.add(sold);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void insert(app.SoldInitial si) {
        String sql = "INSERT INTO solduri_initiale (product_id, quantity) VALUES (?, ?)";

        try (Connection conn = app.DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, si.getProductId());
            stmt.setBigDecimal(2, si.getQuantity());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(app.SoldInitial si) {
        String sql = "DELETE FROM solduri_initiale WHERE product_id = ?";

        try (Connection conn = app.DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, si.getProductId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean existsByProductId(int productId) {
        String sql = "SELECT COUNT(*) FROM solduri_initiale WHERE product_id = ?";
        try (Connection conn = app.DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
