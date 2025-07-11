package app;
import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:warehouse.db";
    public static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL);
    }

}



