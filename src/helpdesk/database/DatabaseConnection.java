package helpdesk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * this provides connections to the SQLite database
 *
 * any database classes should call getConnection()
 * 
 * The database file is created automatically if it does
 * not already exist.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:helpdesk.db";

      /**
     * opens and returns a connection to the sqlite database
     *
     * the calling code is responsible for closing the connection
     * so use try-with-resources whenever possible to auto close the conn to the database
     */

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}