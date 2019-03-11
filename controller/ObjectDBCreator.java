package controller;

import model.user.User;

/**	
 * This class is used to create entries in the database, from the view.
 * 
 * Note: whenever possible, will use methods in the model to accomplish this
 * 
 * @author Regan
 *
 */
public class ObjectDBCreator {
	
	//username, password, fname, lname, DOB
	public static void createUser(String fname, String lname, String username, String password, String DOB) {
		User newUser = new User(fname, lname, username, password, DOB);	
	}

}
