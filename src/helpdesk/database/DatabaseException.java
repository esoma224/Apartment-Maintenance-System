package helpdesk.database;

/**
 * thrown when a database operation fails, for example when helpdesk.db
 * cannot be opened or a query has an error
 *
 * the database classes throw this instead of a plain RuntimeException
 * so the menus can catch database problems, show a clear message,
 * and keep the program running
 */
public class DatabaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
