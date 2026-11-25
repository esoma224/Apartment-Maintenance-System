APARTMENT MAINTENANCE TICKETING SYSTEM
CS401-03 - Group 1

A Java console application for apartment maintenance requests. Residents
submit maintenance problems and check their status; maintenance staff view
all requests and update their status. Requests are saved in a SQLite
database, so they are still available after the program closes.


TEAM
----
Han Bui                         PL8421   Project Manager and Developer
Mahmood Akhtar Abdul Qayyum     QF2085   User Interface and Menu Developer
Gabriel Avelar                  ZK4591   Data and Backend Developer
Esma Murshed                    WK2335   QA and Developer


REQUIREMENTS
------------
1. Java Development Kit (JDK) 15 or newer.
   The code uses Java text blocks ("""), which older versions cannot compile.
   Check your version with:
       java -version
       javac -version
   If needed, download a JDK from https://adoptium.net

2. SQLite JDBC driver - already included.
   lib/sqlite-jdbc-3.53.4.0.jar is part of the project, so nothing else
   needs to be installed. You do NOT need to install SQLite separately.


BUILD AND RUN
-------------
Run these commands from the project folder (the folder containing src/ and lib/).

macOS / Linux:
    javac -cp "lib/sqlite-jdbc-3.53.4.0.jar" -d out $(find src -name "*.java")
    java -cp "out:lib/sqlite-jdbc-3.53.4.0.jar" helpdesk.ui.MaintenanceTicketingApp

Windows (Command Prompt):
    dir /s /b src\*.java > sources.txt
    javac -cp "lib\sqlite-jdbc-3.53.4.0.jar" -d out @sources.txt
    java -cp "out;lib\sqlite-jdbc-3.53.4.0.jar" helpdesk.ui.MaintenanceTicketingApp

Note: Windows uses ";" between classpath entries, macOS/Linux use ":".

On the first run the program creates a database file named helpdesk.db in the
folder you run it from. To start over with no requests, close the program and
delete helpdesk.db.


HOW TO USE
----------
Main Menu
  1. Resident menu
  2. Maintenance staff menu
  3. Exit

Resident menu
  - Submit a maintenance request: enter apartment number, name, category
    (Plumbing, Electrical, Appliance, Heating/AC, or Other), priority
    (Low, Medium, or High), and a short description. The program shows a
    request number - keep it to check the status later.
  - Check request status: enter your request number to see the request's
    details and current status.

Maintenance staff menu
  - View all requests: lists every request with its apartment, resident,
    category, priority, status, and description.
  - View request details: enter a request number to see all of its
    information, including staff notes.
  - Update a request status: enter a request number and pick the new status
    (New, In Progress, or Completed). New requests start as New.
  - Add a note to a request: enter a request number and a note, for example
    "Ordered a new faucet part." A request can have many notes; each one is
    saved with the date and time it was added.

Enter menu choices, category, priority, and status as numbers from the list shown.
Required fields cannot be left blank; the program asks again if input is
missing or invalid, without losing what you already typed.


PROJECT STRUCTURE
-----------------
src/helpdesk/
  ui/MaintenanceTicketingApp.java      Console menus and user input (program entry point)
  model/Ticket.java                    A single maintenance request
  model/Note.java                      A staff note on a request
  database/TicketDAO.java              Saves, finds, and updates tickets (all SQL lives here)
  database/DatabaseConnection.java     Opens connections to helpdesk.db
  database/DatabaseInitializer.java    Creates/updates the tables on startup
lib/
  sqlite-jdbc-3.53.4.0.jar             SQLite database driver
