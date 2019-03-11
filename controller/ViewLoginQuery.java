package controller;

import java.util.List;

import database.*;

/**	
 * Class for the view to query the Database for logins
 * 
 * @author Regan Lynch
 *
 */

public class ViewLoginQuery {

	public static boolean usernameExists(String username) {
		Database db = new Database();
		List<String[]> users = db.search(TableType.users, null, username, null, null, null, null, null, null);		
		return users.size() != 0;
	}
	
	public static boolean validUser(String username, String password) {
		Database db = new Database();
		List<String[]> users = db.search(TableType.users, null, username, null, null, null, null, null, null);	
		//first check username is in the database
		if(users.size() != 0) {
			String salt = users.get(0)[7];
			String real_salted_pass = users.get(0)[6];
			String calc_salted_pass = Encryptor.createSaltedHash(password, salt);
			return real_salted_pass.equals(calc_salted_pass);
		}
		else {
			return false;
		}
	}
	
}
