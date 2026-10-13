package helpdesk.database;

import helpdesk.model.Ticket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * handles all the database operations for the tickets
 *
 * dont write your own sql, just use these methods
 *
 * these are the functions you can use:
 *  addTicket(): save a new request
 *  getAllTickets(): retrieve all requests
 *  getTicketById(): retrieve a specific request
 *  updateTicketStatus(): change and save a request's status
 */

public class TicketDAO {

    /**
     * saves a request to SQLite.
     *
     * the ticket ID is generated automatically by sqlite
     * new tickets will be set to "Open" status
     *
     * Example:
     * Ticket ticket = new Ticket(
     *     0, "name", "204B", "High",
     *     "Kitchen sink is leaking.", "Open"
     * );
     * ticketDAO.addTicket(ticket);
     */

    public void addTicket(Ticket ticket) {

    String sql = """
            INSERT INTO tickets
            (resident_name, apartment_number, priority, description)
            VALUES (?, ?, ?, ?)
            """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, ticket.getResidentName());
        pstmt.setString(2, ticket.getApartmentNumber());
        pstmt.setString(3, ticket.getPriority());
        pstmt.setString(4, ticket.getDescription());

        int rowsInserted = pstmt.executeUpdate();

        System.out.println("Tickets inserted: " + rowsInserted);

    } catch (SQLException e) {
        throw new RuntimeException("Unable to save ticket.", e);
    }
}


    /**
     * self explainatory, retrieves every ticket stored in sqlite
     *
     * used to display all current requests
     */

    public List<Ticket> getAllTickets() {

        List<Ticket> tickets = new ArrayList<>();

        String sql = "SELECT * FROM tickets";

        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                tickets.add(mapResultSetToTicket(rs));
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve tickets.");
            throw new RuntimeException("Unable to load tickets.", e);
        }

        return tickets;
}


    /**
     * also self explainatory, retrieves a specific ticket
     *
     * used when you need to see the details of a ticket
     */

    public Ticket getTicketById(int id) {

        String sql = "SELECT * FROM tickets WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {

                    return mapResultSetToTicket(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to retrieve ticket.");
            throw new RuntimeException("Unable to load ticket.", e);
        }

        return null;
}

     /**
     * this updates the status of a ticket
     *
     * used so you can set the status of a ticket to
     * "Open", "In Progress", "Completed", etc
     *
     * Example:
     * ticketDAO.updateTicketStatus(1, "In Progress");\
     * 
     * returns true if the ticket was found and updated
     * false if the ticket doesnt exist
     */

    public boolean updateTicketStatus(int id, String status) {

        String sql = """
                UPDATE tickets
                SET status = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, id);

            int rowsUpdated = pstmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {
            System.out.println("Failed to update ticket.");
            throw new RuntimeException("Unable to update ticket.", e);
        }
    }

    /**
     * this converts a row of the database into a ticket object
     */
    private Ticket mapResultSetToTicket(ResultSet rs) throws SQLException {

        return new Ticket(
            rs.getInt("id"),
            rs.getString("resident_name"),
            rs.getString("apartment_number"),
            rs.getString("priority"),
            rs.getString("description"),
            rs.getString("status")
        );
    }
}
