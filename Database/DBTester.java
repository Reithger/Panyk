import java.sql.Date;

public class DBTester {

	public static void main(String[] args) {
		
		Database test = new Database("test_db");
		
		//test users table
		//id, username, password, fname, lname, DOB, createdAt
		test.addUser(1, "reganlynch", "pass", "regan", "lynch", new Date(0), new Date(1));
		test.printUsers();
		System.out.println();
		
		//test trips table
		//id, userID, tripTitle, destination, startDate, endDate, 
		test.addTrip(3, 4, "my mexican trip", "mexico", new Date(0), new Date(1));
		test.printTrips();
		System.out.println();
		
		//test schedule items table
		//id, userID, type
		test.addScheduleItem(1, 2, "trip type");
		test.printScheduleItems();
		System.out.println();
		
		//test contacts table
		//id, scheduleItemID, fname, lname, company, jobTitle, displayAs, email, webpage, PhoneNumber, Address
		test.addContact(1, 2, "regan", "lynch", "dnd", "dev", "cool guy", "dunno", "web page dummy", "911", "123blvd");
		test.printContacts();
		System.out.println();
		
		//test transportation table
		//id, scheduleItemID, startTime, endTime
		test.addTransportation(2, 3, new Date(0), new Date(1));
		test.printTransportation();
		System.out.println();
		
		//test accommodations table
		//id, tripID, scheduleItemID, name, checkIn, checkOut, paid, address, contact
		test.addAccommodation(1, 2, 3, "regan", new Date(1), new Date(1), true, "123Blvd", "steve");
		test.printAccommodations();
		System.out.println();
		
		//test reservations table
		//id, scheduleItemID, tripID, contact, participants, startDate, endDate, address, type
		test.addReservation(1, 2, 3, "steve", "regan", new Date(1), new Date(3), "123 blvd", "motel");
		test.printReservations();
		System.out.println();
		
		
	}

}
