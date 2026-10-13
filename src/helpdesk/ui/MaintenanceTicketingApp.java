package helpdesk.ui;

import helpdesk.database.DatabaseInitializer;
import helpdesk.database.TicketDAO;
import helpdesk.model.Ticket;
import java.util.List;
import java.util.Scanner;

public class MaintenanceTicketingApp {
    private static final Scanner input = new Scanner(System.in);
    private static final TicketDAO ticketDAO = new TicketDAO();

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();

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

            if (choice == 1) {
                submitRequest();
            } else if (choice == 2) {
                checkRequestStatus();
            } else if (choice == 3) {
                back = true;
            } else {
                System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    private static void staffMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\nMaintenance Staff Menu");
            System.out.println("1. View all requests");
            System.out.println("2. Update a request status");
            System.out.println("3. Return to main menu");
            int choice = readNumber("Choose an option: ");

            if (choice == 1) {
                viewAllRequests();
            } else if (choice == 2) {
                updateStatus();
            } else if (choice == 3) {
                back = true;
            } else {
                System.out.println("Please choose 1, 2, or 3.");
            }
        }
    }

    private static void submitRequest() {
        System.out.println("\nSubmit Maintenance Request");
        String apartment = readRequiredText("Apartment or unit number: ");
        String resident = readRequiredText("Resident name: ");

        System.out.println("Priority options: Low, Medium, High");
        String priority = readRequiredText("Priority: ");
        String description = readRequiredText("Short description of the problem: ");

        if (!priority.equalsIgnoreCase("Low")
                && !priority.equalsIgnoreCase("Medium")
                && !priority.equalsIgnoreCase("High")) {
            System.out.println("Please enter Low, Medium, or High.");
            return;
        }

        Ticket ticket = new Ticket(0, resident, apartment, priority, description, "Open");
        ticketDAO.addTicket(ticket);

        int requestNumber = getNewestRequestNumber();
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
            System.out.println("Request number: " + ticket.getId());
            System.out.println("Status: " + ticket.getStatus());
        }
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

        System.out.println("Status options: Open, In Progress, Completed");
        String status = readRequiredText("Enter the new status: ");
        if (!status.equalsIgnoreCase("Open")
                && !status.equalsIgnoreCase("In Progress")
                && !status.equalsIgnoreCase("Completed")) {
            System.out.println("Please enter Open, In Progress, or Completed.");
            return;
        }

        if (ticketDAO.updateTicketStatus(number, status)) {
            System.out.println("The request status was updated.");
        } else {
            System.out.println("The request status could not be updated.");
        }
    }

    private static int getNewestRequestNumber() {
        List<Ticket> tickets = ticketDAO.getAllTickets();
        int newestNumber = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getId() > newestNumber) {
                newestNumber = ticket.getId();
            }
        }
        return newestNumber;
    }

    private static String readRequiredText(String message) {
        while (true) {
            System.out.print(message);
            String value = input.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("This information is required.");
        }
    }

    private static int readNumber(String message) {
        while (true) {
            System.out.print(message);
            String value = input.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException error) {
                System.out.println("Please enter a whole number.");
            }
        }
    }
}
