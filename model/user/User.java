package model.user;

import java.util.Calendar;
import java.util.HashMap;

import database.*;
import model.trip.Trip;

/**
 * This class is the contact point that the Intermediary can use to access the data in the
 * Model (only one User is logged in at a time, and their info is necessary to access the
 * information, so it seemed a good focal point.)
 * 
 * @author Mac Clevinger
 *
 */

/**
 * @author Regan
 *
 */
/**
 * @author Regan
 *
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
		
	
	/**	creates a user object, also creates an entry in the database for this user
	 * 
	 * - values ARE NOT CHECKED before entering them into the db 
	 * 				-> make sure values are in proper format prior to creating a user object
	 * 
	 * @param fname
	 * @param lname
	 * @param username
	 * @param password
	 * @param DOB
	 */
	public User(String fname, String lname, String username, String password, String DOB) 
	{
		this.username = username;
		this.password = password;
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
		
		Database db = new Database();
		db.addEntry(TableType.users, Integer.toString(ID), username, password, fname, lname, DOB, createdOn);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * Attempts to retrieve data corresponding to the username associated to this User object and
	 * decrypt it using the provided password. To check that the provided password correctly decoded
	 * the data (and, thus, should permit access to that data), a term derived from the username and
	 * password will be encrypted and embedded in the data to permit checking that the decrypted form
	 * matches that which can be generated from the information provided by the user.
	 * 
	 * TODO: encryption algorithm and data storage formatting
	 * 
	 * @return
	 */
	
	private boolean retrieveData() {
		
		return false;
	}
	
	/**
	 * 
	 */
	
	private void makeTrip() {
		
		Trip t = new Trip(null);
		
	}
	
	/**
	 * 
	 */
	
	private void deleteTrip() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	
	private boolean saveData() {
		
		return false;
	}
	
	/* - generate a user ID based on the username
	 * - we base the ID off the user name as we want each ID to be distinct from the others (just like username)
	 * 
	 * -technically this algorithm doesnt produce unique user IDs (if two usernames have the same letters in them then the userIDS will be the same ) //TODO: fix this!
	 * */
	private int generateUserID() {
		int sum = 0;
		for(int i = 0; i < this.username.length(); i++) {
			sum += (int) this.username.charAt(i);
		}
		return sum;
	}

}
