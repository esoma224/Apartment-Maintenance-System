CS401 Group 1
Mahmood Akhtar Abdul Qayyum - QF2085
Menu and User Interface Developer

Build from the project folder:
javac -cp "lib/sqlite-jdbc-3.53.4.0.jar" -d out $(find src -name "*.java")

Run:
java -cp "out:lib/sqlite-jdbc-3.53.4.0.jar" helpdesk.ui.MaintenanceTicketingApp

This part includes the resident menu, maintenance staff menu, request-entry
prompts, instructions, input checks, and status messages. It uses the group's
TicketDAO and Ticket classes so requests are saved in the SQLite database.
