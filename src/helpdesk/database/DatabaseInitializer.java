package helpdesk.database;

import helpdesk.model.Ticket;
import java.sql.Connection;
import java.sql.ResultSet;
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
     * new tickets the default status "New"
     */

        String sql = """
                CREATE TABLE IF NOT EXISTS tickets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    resident_name TEXT NOT NULL,
                    apartment_number TEXT NOT NULL,
                    category TEXT NOT NULL DEFAULT 'Other',
                    priority TEXT NOT NULL,
                    description TEXT NOT NULL,
                    status TEXT NOT NULL DEFAULT 'New'
                )
                """;

        /*
         * staff notes are kept in their own table so one request can have
         * many notes. ticket_id links each note to its request, and sqlite
         * fills in created_at with the current local date and time.
         */
        String notesSql = """
                CREATE TABLE IF NOT EXISTS ticket_notes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    ticket_id INTEGER NOT NULL REFERENCES tickets(id),
                    note TEXT NOT NULL,
                    created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
                )
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            stmt.execute(notesSql);
            addMissingColumns(conn, stmt);
            cleanUpOldValues(stmt);

        } catch (SQLException e) {
            throw new RuntimeException("Unable to initialize database.", e);
        }
    }

    /**
     * adds columns that newer versions of the program need
     *
     * CREATE TABLE IF NOT EXISTS does nothing when helpdesk.db already
     * exists, so a database made by an older version would be missing
     * these columns. requests saved before categories existed get "Other".
     */
    private static void addMissingColumns(Connection conn, Statement stmt) throws SQLException {
        if (!columnExists(conn, "tickets", "category")) {
            stmt.execute("ALTER TABLE tickets ADD COLUMN category TEXT NOT NULL DEFAULT 'Other'");
        }
    }

    private static boolean columnExists(Connection conn, String table, String column) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, table, column)) {
            return rs.next();
        }
    }

    /**
     * fixes requests saved by older versions of the program
     *
     * older versions saved priority and status exactly as typed
     * ("high", "HIGH", "in progress"), and used "Open" instead of "New".
     * this rewrites them to the official spelling in Ticket.PRIORITIES
     * and Ticket.STATUSES. it is safe to run every time the program starts.
     */
    private static void cleanUpOldValues(Statement stmt) throws SQLException {
        stmt.executeUpdate("UPDATE tickets SET status = 'New' WHERE lower(trim(status)) = 'open'");

        for (String status : Ticket.STATUSES) {
            stmt.executeUpdate("UPDATE tickets SET status = '" + status
                    + "' WHERE lower(trim(status)) = lower('" + status + "')");
        }
        for (String priority : Ticket.PRIORITIES) {
            stmt.executeUpdate("UPDATE tickets SET priority = '" + priority
                    + "' WHERE lower(trim(priority)) = lower('" + priority + "')");
        }
    }
}