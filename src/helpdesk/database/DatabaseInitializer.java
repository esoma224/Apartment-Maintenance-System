package helpdesk.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * creates the database tables that we need
 *
 * call initializeDatabase() once when the application starts,
 * before the GUI attempts to create or retrieve tickets
 *
 * existing tables and their data are saved
 */

public class DatabaseInitializer {

    public static void initializeDatabase() {

    /**
     * creates the tickets table if it does not already exist
     *
     * sqlite automatically generates ticket IDs and assigns
     * new tickets the default status "Open"
     */

        String sql = """
                CREATE TABLE IF NOT EXISTS tickets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    resident_name TEXT NOT NULL,
                    apartment_number TEXT NOT NULL,
                    priority TEXT NOT NULL,
                    description TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'Open'
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize database.", e);
        }
    }
}