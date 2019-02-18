package model.user;

import java.util.HashMap;
import model.trip.Trip;

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
	 * 
	 * 
	 */
	
	public User() {
		
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
	
}
