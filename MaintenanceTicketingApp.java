import java.util.ArrayList;
import java.util.Scanner;

public class MaintenanceTicketingApp {
    private static final Scanner input = new Scanner(System.in);
    private static final ArrayList<MaintenanceRequest> requests = new ArrayList<>();
    private static int nextRequestNumber = 1001;

    public static void main(String[] args) {
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
            System.out.println("3. Add a note to a request");
            System.out.println("4. Return to main menu");
            int choice = readNumber("Choose an option: ");

            if (choice == 1) {
                viewAllRequests();
            } else if (choice == 2) {
                updateStatus();
            } else if (choice == 3) {
                addNote();
            } else if (choice == 4) {
                back = true;
            } else {
                System.out.println("Please choose 1, 2, 3, or 4.");
            }
        }
    }

    private static void submitRequest() {
        System.out.println("\nSubmit Maintenance Request");
        String apartment = readRequiredText("Apartment or unit number: ");
        String resident = readRequiredText("Resident name: ");

        System.out.println("Category options: Plumbing, Electrical, Appliance, Heating/Air Conditioning, Other");
        String category = readRequiredText("Problem category: ");
        String description = readRequiredText("Short description of the problem: ");

        MaintenanceRequest request = new MaintenanceRequest(
                nextRequestNumber++, apartment, resident, category, description);
        requests.add(request);

        System.out.println("Request submitted successfully.");
        System.out.println("Your request number is: " + request.number);
        System.out.println("Keep this number to check the request status.");
    }

    private static void checkRequestStatus() {
        int number = readNumber("Enter your request number: ");
        MaintenanceRequest request = findRequest(number);

        if (request == null) {
            System.out.println("No request was found with that number.");
        } else {
            System.out.println("Request number: " + request.number);
            System.out.println("Status: " + request.status);
            if (!request.note.isEmpty()) {
                System.out.println("Note: " + request.note);
            }
        }
    }

    private static void viewAllRequests() {
        if (requests.isEmpty()) {
            System.out.println("There are no maintenance requests yet.");
            return;
        }

        System.out.println("\nAll Maintenance Requests");
        for (MaintenanceRequest request : requests) {
            System.out.println("Request " + request.number + " | Apartment: " + request.apartment
                    + " | Resident: " + request.resident + " | Category: " + request.category
                    + " | Status: " + request.status);
            System.out.println("Description: " + request.description);
            if (!request.note.isEmpty()) {
                System.out.println("Note: " + request.note);
            }
        }
    }

    private static void updateStatus() {
        int number = readNumber("Enter the request number: ");
        MaintenanceRequest request = findRequest(number);

        if (request == null) {
            System.out.println("No request was found with that number.");
            return;
        }

        System.out.println("Status options: New, In Progress, Completed");
        String status = readRequiredText("Enter the new status: ");
        if (!status.equalsIgnoreCase("New")
                && !status.equalsIgnoreCase("In Progress")
                && !status.equalsIgnoreCase("Completed")) {
            System.out.println("Please enter New, In Progress, or Completed.");
            return;
        }

        request.status = status;
        System.out.println("The request status was updated.");
    }

    private static void addNote() {
        int number = readNumber("Enter the request number: ");
        MaintenanceRequest request = findRequest(number);

        if (request == null) {
            System.out.println("No request was found with that number.");
            return;
        }

        request.note = readRequiredText("Enter a note: ");
        System.out.println("The note was added.");
    }

    private static MaintenanceRequest findRequest(int number) {
        for (MaintenanceRequest request : requests) {
            if (request.number == number) {
                return request;
            }
        }
        return null;
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

    private static class MaintenanceRequest {
        private final int number;
        private final String apartment;
        private final String resident;
        private final String category;
        private final String description;
        private String status = "New";
        private String note = "";

        private MaintenanceRequest(int number, String apartment, String resident,
                                   String category, String description) {
            this.number = number;
            this.apartment = apartment;
            this.resident = resident;
            this.category = category;
            this.description = description;
        }
    }
}
