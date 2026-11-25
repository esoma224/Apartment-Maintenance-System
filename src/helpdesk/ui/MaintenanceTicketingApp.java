package helpdesk.ui;

import helpdesk.database.DatabaseException;
import helpdesk.database.DatabaseInitializer;
import helpdesk.database.TicketDAO;
import helpdesk.model.Note;
import helpdesk.model.Ticket;
import java.util.List;
import java.util.Scanner;

public class MaintenanceTicketingApp {
    private static final Scanner input = new Scanner(System.in);
    private static final TicketDAO ticketDAO = new TicketDAO();

    public static void main(String[] args) {
        try {
            DatabaseInitializer.initializeDatabase();
        } catch (DatabaseException error) {
            System.out.println("The program could not open its database (helpdesk.db).");
            System.out.println("Details: " + describe(error));
            System.out.println("Check that helpdesk.db and its folder are not read-only, then try again.");
            return;
        }

        boolean running = true;
        System.out.println("Apartment Maintenance Ticketing System");
        System.out.println("Use the menu to submit or check maintenance requests.");

        while (running) {
            System.out.println("\nMain Menu");
            System.out.println("1. Resident menu");
            System.out.println("2. Maintenance staff menu");
            System.out.println("3. Exit");
            int choice = readNumber("Choose an option: ");

            if (choice == 1) {
                residentMenu();
            } else if (choice == 2) {
                staffMenu();
            } else if (choice == 3) {
                running = false;
                System.out.println("Thank you. Goodbye.");
            } else {
                System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    private static void residentMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nResident Menu");
            System.out.println("1. Submit a maintenance request");
            System.out.println("2. Check request status");
            System.out.println("3. Return to main menu");
            int choice = readNumber("Choose an option: ");

            try {
                if (choice == 1) {
                    submitRequest();
                } else if (choice == 2) {
                    checkRequestStatus();
                } else if (choice == 3) {
                    back = true;
                } else {
                    System.out.println("Please choose 1, 2, or 3.");
                }
            } catch (DatabaseException error) {
                showDatabaseError(error);
            }
        }
    }

    private static void staffMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nMaintenance Staff Menu");
            System.out.println("1. View all requests");
            System.out.println("2. View request details");
            System.out.println("3. Update a request status");
            System.out.println("4. Add a note to a request");
            System.out.println("5. Return to main menu");
            int choice = readNumber("Choose an option: ");

            try {
                if (choice == 1) {
                    viewAllRequests();
                } else if (choice == 2) {
                    viewRequestDetails();
                } else if (choice == 3) {
                    updateStatus();
                } else if (choice == 4) {
                    addNote();
                } else if (choice == 5) {
                    back = true;
                } else {
                    System.out.println("Please choose a number from 1 to 5.");
                }
            } catch (DatabaseException error) {
                showDatabaseError(error);
            }
        }
    }

    private static void submitRequest() {
        System.out.println("\nSubmit Maintenance Request");
        String apartment = readRequiredText("Apartment or unit number: ");
        String resident = readRequiredText("Resident name: ");

        String category = readChoice("Category", Ticket.CATEGORIES);
        String priority = readChoice("Priority", Ticket.PRIORITIES);
        String description = readRequiredText("Short description of the problem: ");

        Ticket ticket = new Ticket(0, resident, apartment, category, priority,
                description, Ticket.DEFAULT_STATUS);
        int requestNumber = ticketDAO.addTicket(ticket);

        System.out.println("Request submitted successfully.");
        System.out.println("Your request number is: " + requestNumber);
        System.out.println("Keep this number to check the request status.");
    }

    private static void checkRequestStatus() {
        int number = readNumber("Enter your request number: ");
        Ticket ticket = ticketDAO.getTicketById(number);

        if (ticket == null) {
            System.out.println("No request was found with that number.");
        } else {
            printTicketDetails(ticket);
        }
    }

    //staff version of the details screen, also shows the staff notes
    private static void viewRequestDetails() {
        int number = readNumber("Enter the request number: ");
        Ticket ticket = ticketDAO.getTicketById(number);

        if (ticket == null) {
            System.out.println("No request was found with that number.");
            return;
        }

        printTicketDetails(ticket);

        List<Note> notes = ticketDAO.getNotesForTicket(number);
        if (notes.isEmpty()) {
            System.out.println("Notes: none");
        } else {
            System.out.println("Notes:");
            for (Note note : notes) {
                System.out.println("  [" + note.getCreatedAt() + "] " + note.getText());
            }
        }
    }

    private static void printTicketDetails(Ticket ticket) {
        System.out.println("\nRequest number: " + ticket.getId());
        System.out.println("Apartment:      " + ticket.getApartmentNumber());
        System.out.println("Resident:       " + ticket.getResidentName());
        System.out.println("Category:       " + ticket.getCategory());
        System.out.println("Priority:       " + ticket.getPriority());
        System.out.println("Status:         " + ticket.getStatus());
        System.out.println("Description:    " + ticket.getDescription());
    }

    private static void viewAllRequests() {
        List<Ticket> tickets = ticketDAO.getAllTickets();

        if (tickets.isEmpty()) {
            System.out.println("There are no maintenance requests yet.");
            return;
        }

        System.out.println("\nAll Maintenance Requests");
        for (Ticket ticket : tickets) {
            System.out.println("Request " + ticket.getId()
                    + " | Apartment: " + ticket.getApartmentNumber()
                    + " | Resident: " + ticket.getResidentName()
                    + " | Category: " + ticket.getCategory()
                    + " | Priority: " + ticket.getPriority()
                    + " | Status: " + ticket.getStatus());
            System.out.println("Description: " + ticket.getDescription());
        }
    }

    private static void updateStatus() {
        int number = readNumber("Enter the request number: ");
        Ticket ticket = ticketDAO.getTicketById(number);

        if (ticket == null) {
            System.out.println("No request was found with that number.");
            return;
        }

        System.out.println("Current status: " + ticket.getStatus());
        String status = readChoice("New status", Ticket.STATUSES);

        if (ticketDAO.updateTicketStatus(number, status)) {
            System.out.println("The request status was updated.");
        } else {
            System.out.println("The request status could not be updated.");
        }
    }

    private static void addNote() {
        int number = readNumber("Enter the request number: ");
        Ticket ticket = ticketDAO.getTicketById(number);

        if (ticket == null) {
            System.out.println("No request was found with that number.");
            return;
        }

        System.out.println("Request " + ticket.getId() + " (" + ticket.getCategory()
                + "): " + ticket.getDescription());
        String text = readRequiredText("Note: ");

        if (ticketDAO.addNote(number, text)) {
            System.out.println("The note was saved.");
        } else {
            System.out.println("The note could not be saved.");
        }
    }

    /**
     * shows a numbered list of options and keeps asking until the user
     * picks a valid number, then returns that option's exact spelling
     * (so "High" is always saved as "High", never "high" or "HIGH")
     */
    private static String readChoice(String title, List<String> options) {
        System.out.println(title + " options:");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        while (true) {
            int choice = readNumber("Choose 1-" + options.size() + ": ");
            if (choice >= 1 && choice <= options.size()) {
                return options.get(choice - 1);
            }
            System.out.println("Please choose a number from 1 to " + options.size() + ".");
        }
    }

    //shown when a database operation fails, the user goes back to the menu
    private static void showDatabaseError(DatabaseException error) {
        System.out.println("Sorry, that could not be completed because of a database problem.");
        System.out.println("Details: " + describe(error));
        System.out.println("Please try again.");
    }

    //the database's own error message, for example "database is locked"
    private static String describe(DatabaseException error) {
        Throwable cause = error.getCause();
        if (cause != null && cause.getMessage() != null) {
            return error.getMessage() + " " + cause.getMessage();
        }
        return error.getMessage();
    }

    /**
     * reads one line of input
     *
     * if the input ends (Ctrl+D on macOS/Linux, Ctrl+Z then Enter on Windows)
     * the program says goodbye and closes instead of crashing
     */
    private static String readLine() {
        if (!input.hasNextLine()) {
            System.out.println("\nInput ended. Goodbye.");
            System.exit(0);
        }
        return input.nextLine().trim();
    }

    private static String readRequiredText(String message) {
        while (true) {
            System.out.print(message);
            String value = readLine();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This information is required.");
        }
    }

    private static int readNumber(String message) {
        while (true) {
            System.out.print(message);
            String value = readLine();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException error) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
