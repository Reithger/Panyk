package database;


import java.util.Arrays;
import java.util.List;

public class DBTester {

	public static void main(String[] args) {
		
		Database db = new Database();
		
		//test insertion
		db.addEntry(TableType.users, "2", "reganlynch", "pass", "regan", "lynch", "1996", "1996");		
		db.addEntry(TableType.users,  "1", "reganlynch", "word", "regan", "lynch", "1996", "1996");
		db.printTable(TableType.users);
		
		//test deletion
		db.deleteEntry(TableType.users, "2", null, null, null, null, null, null);
		db.printTable(TableType.users);
		
		//test searching 
		db.addEntry(TableType.users, "2", "reganlynch", "pass", "regan", "lynch", "1996", "1996");	
		List<String[]> foundEntries = db.search(TableType.users, null, "reganlynch", null, null, null, null, null);
		for(String[] entry : foundEntries) {
			System.out.println(Arrays.toString(entry));
		}
		System.out.println();
		
		//---------------
		
		//test the same things on another table..
		db.addEntry(TableType.contacts, "1", "2", "regan", "lynch", "dnd", "dev", "cool guy", "dunno", "web page dummy", "911", "123blvd");
		db.addEntry(TableType.contacts, "4", "6", "regan", "lynch", "dnd", "dev", "cool guy", "dunno", "web page dummy", "911", "123blvd");
		db.printTable(TableType.contacts);
		
		//test deletion
		db.deleteEntry(TableType.contacts, null, "6", null, "lynch", "dnd", null, "cool guy", "dunno", null, "911", "123blvd");
		db.printTable(TableType.contacts);
		
		//test searching 
		db.addEntry(TableType.contacts, "4", "6", "regan", "lynch", "dnd", "dev", "cool guy", "dunno", "web page dummy", "911", "123blvd");
		foundEntries = db.search(TableType.contacts,  null, null, null, "lynch", "dnd", null, "cool guy", null, null, "911", "123blvd");
		for(String[] entry : foundEntries) {
			System.out.println(Arrays.toString(entry));
		}	
	
	}//end main

}//end class
