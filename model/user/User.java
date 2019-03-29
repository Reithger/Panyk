package model.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import controller.Encryptor;
import database.*;
import model.trip.Trip;
import model.trip.schedule.Schedulable;
import model.trip.schedule.SchedulableType;
import model.trip.schedule.ScheduledItem;

/**
 * This class is the contact point that the Intermediary can use to access the data in the
 * Model (only one User is logged in at a time, and their info is necessary to access the
 * information, so it seemed a good focal point.)
 * 
 * @author Mac Clevinger
 * @author Regan Lynch
 */

public class User {

//---  Instance Variables   -------------------------------------------------------------------
	
	/** HashMap<<r>String, Trip> object containing the trips associated to this User object*/
	private HashMap<String, Trip> trips;
	/** String object representing the username for which data has been stored or will be stored for later access*/
	private String username;
	/** String object representing the provided password for decrypting data stored under the username heading*/
	private String password;
	
	private HashMap<String, SchedulableType> scheduleTypes;
	
//---  Constructors   -------------------------------------------------------------------------
		
	/**	
	 * Constructor for objects of the User type that creates an entry in the database for this User using
	 * the provided input to the constructor; input is assumed to be perfect.
	 * 
	 * TODO: Handle bad input
	 * 
	 * @param fname - String object representing the First Name of this user
	 * @param lname - String object representing the Last Name of this user
	 * @param usernameIn - String object representing the Username of this user
	 * @param passwordIn - String object representing the Password of this user
	 */
	
 	public User(String fname, String lname, String usernameIn, String passwordIn){
		username = usernameIn;
		password = passwordIn;
		String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH));
		String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		if(month.length() == 1) {
			month = "0" + month;
		}
		if(day.length() == 1) {
			day = "0" + day;
		}
		String createdOn = year + "-" + month + "-" + day;
		
		String[] hash = Encryptor.createSaltedHash(password);
		
		boolean result = Database.addEntry(TableType.users, username, fname, lname, createdOn, hash[0], hash[1]);
		trips = new HashMap<String, Trip>();
		scheduleTypes = new HashMap<String, SchedulableType>();
		if(!result) {
			username = null;
			password = null;
			trips = null;
			scheduleTypes = null;
		}
	}
	
	/**
	 * Constructor for objects of the User type that assumes an entry is extant for this user
	 * and initializes the User object to have the provided username and password.
	 * 
	 * @param usernameIn - String object representing the Username of this user
	 * @param passwordIn - String object representing the Password of this user
	 */
	
	public User(String usernameIn, String passwordIn) {
		username = usernameIn;
		password = passwordIn;
		trips = new HashMap<String, Trip>();
		scheduleTypes = new HashMap<String, SchedulableType>();
		retrieveData();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method accesses the database entries associated to this User and constructs Trip
	 * objects from that data for display/manipulation. 
	 * 
	 * TODO: this!
	 * 
	 * @return - Returns a boolean value describing the success of retrieving and using data from the database. 
	 */
	
	private void retrieveData() {
		List<String[]> tripsIn = Database.search(TableType.trips, getUsername(), null, null, null, null, null);
		ArrayList<Trip> out = new ArrayList<Trip>();
		for(String[] rawData : tripsIn) {
			System.out.println(Arrays.toString(rawData));
			out.add(new Trip(rawData[1], rawData[2], rawData[5], rawData[3], rawData[4]));
		}
		for(Trip t : out) {
			if(t != null)
				trips.put(t.getTitle(), t);
		}
	}
	
	/**
	 * This method should create a new Trip object for the User to design/have access to.
	 */
	
	public boolean makeTrip(String title, String destination, String description, String dateStart, String dateEnd) {
		if(trips.get(title) != null) {
			return false;
		}
		Trip t = new Trip(title, destination, description, dateStart, dateEnd);
		if(t.getTitle() != null) {
			trips.put(t.getTitle(), t);
			t.saveToDatabase(username);
			return true;
		}
		return false;
		
	}
	
	/**
	 * This method should delete the defined Trip object from the list stored by this User.
	 * TODO: Do this, make sure we have confirmation messages before deletion But confirmation is
	 * not handled by the User object, it is handled by the user interface.
	 */
	
	private boolean deleteTrip(String tripName) {
		if(trips.get(tripName) == null) {
			return false;
		}
		trips.get(tripName).deleteTrip();
		Database.deleteEntry(TableType.trips, username, tripName, null, null, null, null);
		trips.remove(tripName);
		return true;
	}
	
	/**
	 * This method converts the Trip data stored by this User to a format that can be put
	 * into the Database.
	 * TODO: Do this
	 * @return - Returns a boolean value representing the success of this operation.
	 */
	
	private boolean saveData() {
		boolean result = true;
		for(Trip t : trips.values()) {
			result = t.saveToDatabase(username);
			if(!result) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addSchedulableItem(String tripName, String type, String ... data) {
		trips.get(tripName).addScheduledItem(data[0], new ScheduledItem(scheduleTypes.get(type), data, 2));
		return false;
	}
	
	public void addSchedulableType(String header, String[] titles, String[] types) {
		scheduleTypes.put(header, new SchedulableType(header, titles, types));
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method that queries whether or not the data stored by this User object
	 * is valid; i.e, that it is not null and has been assigned properly.
	 * 
	 * @return - Returns a boolean value describing the state of this User; true if 'valid', false otherwise.
	 */
	
	public boolean validate() {
		return username != null && password != null;
	}
	
	/**
	 * Getter method that requests the user's username.
	 * 
	 * @return - Returns a String object representing the User object's username.
	 */
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * Getter method that requests the user's password.
	 * 
	 * @return - Returns a String object representing the User object's password.
	 */
	
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @return
	 */
	
	public ArrayList<Trip> getTrips(){
		return new ArrayList<Trip>(trips.values());
	}
	
	public ArrayList<Schedulable> getSchedulables(String tripName, String schedulableType){
		return trips.get(tripName).getSchedulables(schedulableType);
	}
	
	public String[] getSchedulableTypeTitles(String header) {
		return scheduleTypes.get(header).getTitles();
	}

	public HashMap<String, HashMap<String, String>> getDisplaySchedulablesData(String tripName, String schedulableType){
		ArrayList<Schedulable> sched = getSchedulables(tripName, schedulableType);
		HashMap<String, HashMap<String, String>> out = new HashMap<String, HashMap<String, String>>();
		for(Schedulable sc : sched) {
			HashMap<String,String> in = new HashMap<String, String>();
			out.put(sc.getTitle(), sc.getDisplayData(in));
		}
		return out;
	}
	
	public HashMap<String, String> getCreateSchedulablesData(String schedulableType){
		System.out.println(schedulableType + " " + scheduleTypes.get(schedulableType).getSchedulableFormatted());
		return scheduleTypes.get(schedulableType).getSchedulableFormatted();
	}
	
	public ArrayList<String> getSchedulableTypes(){
		return new ArrayList<String>(scheduleTypes.keySet());
	}
	
}
