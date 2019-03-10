package controller;

import java.util.List;

import database.*;

public class ViewLoginQuery {

	public static boolean usernameExists(String username) {
		Database db = new Database();
		List<String[]> users = db.search(TableType.users, null, username, null, null, null, null, null);
		return users.size() != 0;
	}
	
	public static boolean validUser(String username, String password) {
		Database db = new Database();
		List<String[]> users = db.search(TableType.users, null, username, password, null, null, null, null);
		return users.size() != 0;
	}
	
}
