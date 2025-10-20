package org.javafx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class DatabaseUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/carnedb";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean isDatabaseInitialized() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT 1 FROM departamento LIMIT 1")) {
            return rs.next(); // If query succeeds, database is initialized
        } catch (SQLException e) {
            return false; // If query fails, assume not initialized
        }
    }

    public static void initializeDatabase() {
        if (isDatabaseInitialized()) {
            System.out.println("Database already initialized. Skipping initialization.");
            return;
        }

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Read the SQL script from resources
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                DatabaseUtil.class.getResourceAsStream("/org/javafx/carnedb.sql")));
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            reader.close();

            // Execute the SQL script
            stmt.execute(sql.toString());
            System.out.println("Database initialized successfully.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
