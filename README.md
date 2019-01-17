# Panyk
Comp-4721 Software Design Term Project

Team:
 - Mac Clevinger - Architect/Archivist
 - Sienna Collette - Process Coordinator/Documentation
 - Nick Cuthbertson - Product Owner/Quality Assurance
 - Regan Lynch - Team Leader

Working Title: Panyk, a Travel Planner

Milestones:
 - January 31: Task identified, Data Flow, Design
 - February 14: Prototypes, Refinement of Design
 - February 28: Prototypes, Refinement of Design
 - March 14: Prototypes, Refinement of Design
 - April 4: Final Presentation and Report

Application (Desktop)
 - Java
 - Need to design a file structure (read-in, write out)
 - Phonebook of contacts to relate to itinerary contents
  - Factory based on provided values for which contact
 - Flight Website info (Nick is on this)
 - Suggestions/tips for trip preparation, To-Do list that serves as a tutorial to the program
 - Itineraries as structured
 - Alert system? Text phone with appointments, etc.
 - Blue color scheme?
 - Allow great control of data input, but detect invalid input (do not let them break it)
 - Visual tab system with subcategories
  - Trip
   - calendar
   - reservations
   - transportation
   - photos album
   - etc.
 - User added photos linked to specific events they scheduled; display in separate tab?

Client: Olivier Bourgeois
 - software engineer at Dell
 - enjoys traveling, photos, experiences
 - olivier@bourgeois.io (and Discord)
Wants software to:
 - keep track of logistics
  - airline bookins (tickets/dates)
  - accomodations
  - what to do? (during trip)
  - many services and websites; too many to keep track of!
 - keep track of location, date, plans, etc. in one piece of software
 - all info for trip in one place

Travel Planner Application to keep track of logistics over multiple trips (itinerary, reservations, ...)
 - Desktop or Mobile
 - Any language/framework
 - Application Flow not provided

Core Functionalities:
 - Persistent data across launches
 - List, create, delete trips (and edit)
 - Trips
  - title
  - start/end dates
  - list of destinations
  - text area for more info
 - Per trip: list, create, delete, and edit the following:
  - Itineraries (1 per day of the trip)
   - textarea to write plan
  - Reservations
   - title
   - type (e.g; transportation, museum, ...)
   - Date and Time
   - Text area
  -Accommodations
   - title
   - check-in/out dates
   - Text area

Additional Features:
 - Diary/Book of Memories, can add info
 - Analytics
 - Exportable .txt file with info of one trip
 - Display user images
 - 3'rd party API functionality
 - Budgeting feature
 - Options menu (what to display? Analytics; what to store)

Prefer GUI implementation.
One user with information stored locally
 - can extend either feature if interested
Color: Plaid
Security is desired, but not necessary at first
