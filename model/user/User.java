package model.user;

import java.util.Calendar;
import java.util.HashMap;

import controller.Encryptor;
import database.*;
import model.trip.Trip;

/**
 * This class is the contact point that the Intermediary can use to access the data in the
 * Model (only one User is logged in at a time, and their info is necessary to access the
 * information, so it seemed a good focal point.)
 * 
 * TODO: Need some way that the User retrieves/stores data from/to the Database
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
	 * @param DOB - String object representing the Date of Birth of this user
	 */
	
	public User(String fname, String lname, String usernameIn, String passwordIn, String DOB){
		username = usernameIn;
		password = passwordIn;
		int ID = this.generateUserID();
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
		
		Database db = new Database();
		boolean result = db.addEntry(TableType.users, Integer.toString(ID), username, fname, lname, DOB, createdOn, hash[0], hash[1]);
		trips = new HashMap<String, Trip>();
		if(!result) {
			username = null;
			password = null;
			trips = null;
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
	
	private boolean retrieveData() {
		return false;
	}
	
	/**
	 * This method should create a new Trip object for the User to design/have access to.
	 * TODO: Do this
	 */
	
	private void makeTrip() {
		
		Trip t = new Trip(null);
		
	}
	
	/**
	 * This method should delete the defined Trip object from the list stored by this User.
	 * TODO: Do this, make sure we have confirmation messages before deletion
	 */
	
	private void deleteTrip() {
		
	}
	
	/**
	 * This method converts the Trip data stored by this User to a format that can be put
	 * into the Database.
	 * TODO: Do this
	 * @return - Returns a boolean value representing the success of this operation.
	 */
	
	private boolean saveData() {
		return false;
	}
	
	/* 
	 * This method generates a user ID based on their username.
	 * 
	 * TODO: if we want unique ID, it should in some way be separate from username otherwise it's
	 * two identifiers that are effectively the same (if I can derive one from the other, security wise
	 * it doesn't do anything.)
	 * 
	 */
	
	private int generateUserID() {
		int sum = 0;
		for(int i = 0; i < this.username.length(); i++) {
			sum += (int) this.username.charAt(i);
		}
		return sum;
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
	
}
