package helpdesk.model;

/**
 * represents a single note that maintenance staff added to a request
 *
 * a request can have many notes, for example:
 *   "Ordered a new faucet part."
 *   "Part arrived, repair scheduled for Friday."
 *
 * like Ticket, this class does NOT talk to the database,
 * use TicketDAO.addNote() and TicketDAO.getNotesForTicket()
 */
public class Note {

    //assigned by sqlite when the note is saved
    private int id;

    //the request number this note belongs to
    private int ticketId;

    private String text;

    //date and time the note was saved, for example "2026-11-25 14:30:00"
    private String createdAt;

    public Note(int id, int ticketId, String text, String createdAt) {
        this.id = id;
        this.ticketId = ticketId;
        this.text = text;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getText() {
        return text;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
