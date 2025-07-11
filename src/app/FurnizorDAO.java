package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import app.Furnizor;
import app.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class FurnizorDAO {

    public static ObservableList<Furnizor> getAllFurnizori() {
                ObservableList<Furnizor> list = FXCollections.observableArrayList();
        String sql = "SELECT id, nume, telefon FROM furnizori";

        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Furnizor f = new Furnizor();
                f.setId(rs.getInt("id"));
                f.setName(rs.getString("nume"));
                f.setPhone(rs.getString("telefon"));
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void insertFurnizor(Furnizor f) {
        String sql = "INSERT INTO furnizori(nume, telefon) VALUES(?, ?)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, f.getName());
            pstmt.setString(2, f.getPhone());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateFurnizor(Furnizor f) {
        System.out.println("Вызов updateFurnizor для id=" + f.getId() + " с name=" + f.getName() + ", phone=" + f.getPhone());
        String sql = "UPDATE furnizori SET nume = ?, telefon = ? WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, f.getName());
            pstmt.setString(2, f.getPhone());
            pstmt.setInt(3, f.getId());
            pstmt.executeUpdate();
            int rows = pstmt.executeUpdate();
            System.out.println("Обновлено строк: " + rows);

        } catch (SQLException e) {
            System.out.println("Ошибка в updateFurnizor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteFurnizor(int id) {
        String sql = "DELETE FROM furnizori WHERE id = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
