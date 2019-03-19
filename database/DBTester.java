package database;


public class DBTester {

	public static void main(String[] args) {
		
		/** username, tripTitle, destination, startDate, endDate */	
		Database.addEntry(TableType.trips, "reganlynch", "my mexican trip", "mexico", "date 1", "date 2");
		
		System.out.println("Trips table after insertion:");
		Database.printTable(TableType.trips);
		
		Database.editEntry(TableType.trips, 1, "TRIP TITLE HAS CHANGED", "reganlynch", null, null, null, null);
		System.out.println("Trips table after editing:");
		Database.printTable(TableType.trips);
		
		//delete entry
		Database.deleteEntry(TableType.trips, "reganlynch", null, null, null, null);
	}//end main

}//end class
