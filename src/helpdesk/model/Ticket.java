package helpdesk.model;

/**
 * represents a single maintenance request
 * 
 * this class stores ticket information in an object
 * it does NOT directly interact with the database
 * 
 * use TicketDAO to save or retrieve Ticket objects
 */

public class Ticket {

    //this id is assigned by SQLite automatically when a ticket is saved
    private int id;

    //all the ticket info
    private String residentName;
    private String apartmentNumber;
    private String priority;
    private String description;
    private String status;

    /**
     * creates a Ticket object with the provided information
     * 
     * when creating a new ticket, use 0 as the temporary ID
     * sqlite will generate the actual ID when it is inserted
     * 
     * when retrieving a ticket, the DAO supplies its saved ID
     */
    public Ticket(int id, String residentName, String apartmentNumber,
                  String priority, String description, String status) {

        this.id = id;
        this.residentName = residentName;
        this.apartmentNumber = apartmentNumber;
        this.priority = priority;
        this.description = description;
        this.status = status;
    }

    //these are all the getters so the DAO can read the ticket info
    public int getId() {
        return id;
    }

    public String getResidentName() {
        return residentName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    //these are the setters, they modify the ticket object but the changes do NOT get saved to sqlite
    //you need to call TicketDAO.updateTicketStatus() to make a permanent change
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}