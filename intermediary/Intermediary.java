package intermediary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import database.Database;
import database.TableType;
import input.Communication;
import model.trip.Trip;
import model.user.User;
import view.Display;

/**
 * This class serves as the link between the Model and the View, facilitating the movement
 * of information back and forth via user Input (using the static Communication object and
 * the clock() method) or telling the Display to construct visible objects from Model info
 * dumps.
 * 
 * As little work as possible should be done here, this is the fulcrum point where info is
 * moved and instructions are given, not where computation is done. Abstract it away.
 * 
 * Which is to say, let the Model do Model things and the View do View things, and let this
 * class do the communication between the two.
 * 
 * @author Mac Clevinger
 *
 */

public class Intermediary {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** Constant value representing the speed at which the program responds to user input (call rate of clock())*/
	private final static int REFRESH_RATE = 1000/60;
	
	//-- Control  -------------------------------------------
	
	public final static String CONTROL = "Control";
	public final static String CONTROL_NULL = null;
	public final static String CONTROL_LOGIN_SCREEN = "next";
	public final static String CONTROL_USER_CREATE = "createUser";
	public final static String CONTROL_TRIP_SELECT = "trip_select";
	public final static String CONTROL_TRIP_CREATION = "trip_creation";
	public final static String CONTROL_ATTEMPT_LOGIN = "login_fn";
	public final static String CONTROL_ATTEMPT_USER_CREATE = "user_create_fn";
	public final static String CONTROL_ATTEMPT_CREATE_TRIP = "create_trip";
	public final static String CONTROL_RESERVATIONS = "reservations";
	public final static String CONTROL_RES_CREATION = "newRes";
	public final static String CONTROL_SAVE_RES = "save me save me";
	public final static String CONTROL_ACCOM_LIST = "a list of accomodations";
	public final static String CONTROL_ACCOM_CREATE = "the grand act of creation";
	public final static String CONTROL_SAVE_ACCOM = "save accom";
	
	//-- Value Storage  ---------------------------------------
	
	/*
	 * These are labels for storage, not the actual values that will be retrieved
	 * from using them; make sure they're unique from one another.
	 * 
	 */
	
	public final static String LOGIN_USERNAME = "login_username";
	public final static String LOGIN_PASSWORD = "login_password";
	public final static String CREATE_USER_USERNAME = "create_username";
	public final static String CREATE_USER_PASSWORD = "create_password";
	public final static String CREATE_USER_FIRSTNAME = "create_firstname";
	public final static String CREATE_USER_LASTNAME = "create_lastname";
	
	public final static String CREATE_TRIP_TITLE = "trip_title";
	public final static String CREATE_TRIP_DEST = "trip_dest";
	public final static String CREATE_TRIP_START = "trip_start_date";
	public final static String CREATE_TRIP_END = "trip_end_date";
	
	public final static String CREATE_RES_TITLE = "res_title";
	public final static String CREATE_RES_LOC = "res_loc";
	public final static String CREATE_RES_START = "res_start_date";
	public final static String CREATE_RES_END = "res_end_date";
	
	public final static String CREATE_ACCOM_TITLE = "accom_title";
	public final static String CREATE_ACCOM_LOC = "accom_loc";
	public final static String CREATE_ACCOM_START = "accoms_start_date";
	public final static String CREATE_ACCOM_END = "accoms_end_date";
	
	public final static String CURR_TRIP = "current_trip";
	
	
	

//---  Instance Variables   -------------------------------------------------------------------
	
	/** The Timer object periodically calls the clock() method of this Intermediary object*/
	private Timer timer;
	/** The Display object is the contact point this Intermediary object has to the View for Input/Output*/
	private Display display;
	/** The User object is the contact point this Intermediary object has to the Model for data access/manipulation*/
	private static User user;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Intermediary type: sets the size of the Display and
	 * starts the Timer for having the clock() method be called repeatedly.
	 * 
	 */
	
	public Intermediary() {
		display = new Display(1000, 600);
		timer = new Timer();
		timer.schedule(new TimerRepeat(this), 0, REFRESH_RATE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method represents the control structure of the program, constantly querying the Communication
	 * singleton object for updates as to what behaviors should occur in the Display and Model objects. 
	 * 
	 * Based on the values assigned to the CONTROL key in the Communication object's HashMap<<r>String, String>,
	 * different methods in the Intermediary class are called to perform certain actions (ideally queries and
	 * commands are sent along to the Display and Model, but some things are convenient to do here.)
	 * 
	 * Uses the TimerRepeat class to have a Timer repeatedly order this class to call clock().
	 * 
	 */
	
	public void clock() {
		String happen = Communication.get(CONTROL);
		Communication.set(CONTROL, CONTROL_NULL);
		if(happen == null)
			return;
		switch(happen) {
			case CONTROL_ATTEMPT_LOGIN:			//Attempts to log-in with the provided information 
				attemptLogin(); break;
			case CONTROL_ATTEMPT_USER_CREATE: 	//Attempts to create a new user with the provided information
				createNewUser(); break;
			case CONTROL_LOGIN_SCREEN: 			//Orders display to show the log-in screen
				goToLogin(); break;
			case CONTROL_USER_CREATE: 			//Orders display to show the create account screen
				goToCreateAccount(); break;
			case CONTROL_TRIP_SELECT: 			//Orders display to show the trip select screen
				goToTripSelect(); break;
			case CONTROL_TRIP_CREATION:			//Orders display to show the trip creation screen
				goToTripCreation(); break;
			case CONTROL_ATTEMPT_CREATE_TRIP:	//Attempts to create a new Trip with the provided information
				addTrip(); break;
			case CONTROL_RESERVATIONS:
				showRes(); break;
			case CONTROL_RES_CREATION:
				goToMakeRes(); break;
			case CONTROL_SAVE_RES:
				saveRes(); break;
			case CONTROL_ACCOM_LIST:
				goToAccom(); break;
			case CONTROL_ACCOM_CREATE:
				goToNewAccom(); break;
			case CONTROL_SAVE_ACCOM:
				saveAccom(); break;
			default: break;
		}
	}
	
	/**
	 * This method requests the information stored by the Display in Communication's LOGIN_USERNAME
	 * and LOGIN_PASSWORD key-values, passing that information to the Database to ensure that
	 * the username already exists and that the password is valid.
	 * 
	 * Errors are shown to the user if either input is invalid, otherwise the User object is initialized
	 * and CONTROL is set to CONTROL_TRIP_SELECT.
	 * 
	 */
	
	public void attemptLogin() {
		String username = Communication.get(LOGIN_USERNAME);
		String password = Communication.get(LOGIN_PASSWORD);
		boolean validUsername = Database.checkUserExists(username);
		if(!validUsername) {
			errorReport("Invalid Username");
			return;
		}
		boolean validPassword = Database.checkValidPassword(username, password);
		if(!validPassword) {
			errorReport("Invalid Password");
			return;
		}
		user = new User(username, password);
		System.out.println("IN ATTEMPT LOGIN..." + username);
		Communication.set(CONTROL, CONTROL_TRIP_SELECT);
	}
	
	/**
	 * This method requests the information stored by Display in Communication's CREATE_USER_{USERNAME,
	 * PASSWORD, DOB, FIRSTNAME, LASTNAME} for use in creating a new database entry for a new user
	 * with that information.
	 * 
	 * Errors in data provision are reported to the user, otherwise the database checks if a user already
	 * exists by that name. If no such name is used, a User object is created with that information,
	 * its constructor communicating with the database for creating an entry.
	 * 
	 * The new User is then validated to ensure a username and password are present; if so, CONTROL is
	 * set to CONTROL_TRIP_SELECT.
	 * 
	 */
	
	public void createNewUser() {
		String username = Communication.get(CREATE_USER_USERNAME);
		String password = Communication.get(CREATE_USER_PASSWORD);
		String firstname = Communication.get(CREATE_USER_FIRSTNAME);
		String lastname = Communication.get(CREATE_USER_LASTNAME);
		
		if(username == null || username.equals("")) {
			System.out.println("Username invalid during User Account Creation: Intermediary > createNewUser");
			errorReport("Invalid Username");
			return;
		}
		if(password == null || password.equals("")) {
			System.out.println("Password invalid during User Account Creation: Intermediary > createNewUser");
			errorReport("Invalid Password");
			return;
		}
		if(firstname == null || firstname.equals("")) {
			System.out.println("Firstname invalid during User Account Creation: Intermediary > createNewUser");
			errorReport("Invalid Firstname");
			return;
		}
		if(lastname == null || lastname.equals("")) {
			System.out.println("Lastname invalid during User Account Creation: Intermediary > createNewUser");
			errorReport("Invalid Lastname");
			return;
		}
			
		boolean checkExists = Database.checkUserExists(username);
		if(!checkExists) {
			user = new User(firstname, lastname, username, password);
			if(user.validate()) {
				Communication.set(CONTROL, CONTROL_TRIP_SELECT);
			}
			else {
				errorReport("Failure to validate user");
			}
		}
		else {
			errorReport("Username already in use");
		}
	}
	
	/**
	 * This method requests the information stored by Display in Communication's CREATE_TRIP_{START, END}
	 * to create a new Trip object associated to the current user.
	 * 
	 */
	
	public void addTrip() {
		Date begin;
		Date end;
		
		String beginStr = Communication.get(CREATE_TRIP_START);
		String endStr = Communication.get(CREATE_TRIP_END);
		
		try{
			begin = new SimpleDateFormat("dd/MM/yyyy").parse(beginStr);
			end = new SimpleDateFormat("dd/MM/yyyy").parse(endStr);
			
			String dest = Communication.get(CREATE_TRIP_DEST);
			if(dest.equals("")){
				errorReport("Must have a destination");
			}
			else {
				//check if a trip with that name already exists with the user
				List<String[]> trips = Database.search(TableType.trips, user.getUsername(), Communication.get(CREATE_TRIP_TITLE), null, null, null);					//there where errors where if the user created an account and then a trip without signing out, the value of LOGIN_USERNAME was not being set
				if(trips.size() == 0) {
					Database.addEntry(TableType.trips, user.getUsername(), Communication.get(CREATE_TRIP_TITLE), dest, beginStr, endStr);//leave room for notes?
					//trips("username", "varchar(60)", "tripTitle", "varchar(60)", "destination", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)"),
				
					//after insertion, go back to trip select
					goToTripSelect();
				}else {
					errorReport("you have already created a trip with this title");
				}
			}
		} catch (Exception e) {
			errorReport("Invalid Dates");
		}
		//TODO: Set CONTROL to whatever we do next
	}
	
	/**
	 * Basically the method above but for reservations
	 */
	public void saveRes() {
		Date begin;
		Date end;
		
		String beginStr = Communication.get(CREATE_RES_START);
		String endStr = Communication.get(CREATE_RES_END);
		
		try{
			begin = new SimpleDateFormat("dd/MM/yyyy").parse(beginStr);
			end = new SimpleDateFormat("dd/MM/yyyy").parse(endStr);
		
			//check if a reservation with that name already exists with the user
			List<String[]> resList = Database.search(TableType.reservations, user.getUsername(), CURR_TRIP, Communication.get(CREATE_RES_TITLE), null, null, null);					//there where errors where if the user created an account and then a trip without signing out, the value of LOGIN_USERNAME was not being set
			if(resList.size() == 0)
			{
				Database.addEntry(TableType.reservations, user.getUsername(), CURR_TRIP, Communication.get(CREATE_RES_TITLE), beginStr, endStr, Communication.get(CREATE_RES_LOC));//leave room for notes?
				//reservations("username", "varchar(60)", "tripTitle", "varchar(60)", "name", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "address", "varchar(60)");
					//after insertion, go back to trip select
					goToRes();
			}else 
			{
				errorReport("you have already created a resercation with this title");
			}
		} catch (Exception e) {
			errorReport("Invalid Dates");
		}
		//TODO: Set CONTROL to whatever we do next
	}
	
	/**
	 * more repeated code, just trying to make it run tonight will definitely refactor hardcore before next milestone
	 */
	public void saveAccom() {
		Date begin;
		Date end;
		
		String beginStr = Communication.get(CREATE_ACCOM_START);
		String endStr = Communication.get(CREATE_ACCOM_END);
		System.out.println("begin str is " + beginStr);
		System.out.println("end str is " + endStr);
		
		try{
			begin = new SimpleDateFormat("dd/MM/yyyy").parse(beginStr);
			end = new SimpleDateFormat("dd/MM/yyyy").parse(endStr);
		
			//check if a reservation with that name already exists with the user
				Database.addEntry(TableType.accommodations, user.getUsername(), CURR_TRIP, null, Communication.get(CREATE_ACCOM_TITLE), beginStr, endStr, null, Communication.get(CREATE_ACCOM_LOC));//leave room for notes?
											//accommodations("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "checkIn", "varchar(60)", "checkOut", "varchar(60)", "paid", "boolean", "address", "varchar(60)"),
					goToAccom();
		} catch (Exception e) {
			errorReport("Invalid Dates");
		}
		//TODO: Set CONTROL to whatever we do next
	}
	
	public void showRes()
	{
		goToRes();
	}
	
	
	
//--- Getter Methods --------------------------------------------------------------------------

	/**
	 * Getter method to query the database for all entries associated to the current user.
	 * 
	 * @return - Returns a List<<r>String[]> object containing the Trips associated to the current User.
	 */
	
	public static List<String[]> getUsersTrips() {
		return Database.search(TableType.trips, user.getUsername() , null, null, null, null);
	}
	
	public static List<String[]> getTripsRes() 
	{
		return Database.search(TableType.reservations, user.getUsername() , CURR_TRIP, null, null, null, null);
		
		//reservations("username", "varchar(60)", "tripTitle", "varchar(60)", "name", "varchar(60)", "startDate", "varchar(60)", "endDate", "varchar(60)", "address", "varchar(60)");
	}
	
	public static List<String[]> getTripsAccom() 
	{
		return Database.search(TableType.accommodations, user.getUsername() , CURR_TRIP, null, null, null, null, null, null);
		
		//accommodations("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "checkIn", "varchar(60)", "checkOut", "varchar(60)", "paid", "boolean", "address", "varchar(60)"),
	}
	
//---  Navigation   ---------------------------------------------------------------------------
	
	/**
	 * This method navigates the Display to the logIn screen by hiding the current
	 * panels in the WindowFrame and calling display.logInScreen().
	 * 
	 */
	
	private void goToLogin() {
		display.resetView();
		display.logInScreen();
	}
	
	/**
	 * This method navigates the Display to the createAccount screen by hiding the current
	 * panels in the WindowFrame and calling display.createAccountScreen().
	 * 
	 */
	
	private void goToCreateAccount() {
		display.resetView();
		display.createAccountScreen();
	}
	
	/**
	 * This method navigates the Display to the tripSelect screen by hiding the current
	 * panels in the WindowFrame and calling display.tripSelectScreen().
	 * 
	 */

	private void goToTripSelect() {
		display.resetView();
		display.tripSelectScreen();
	}
	
	/**
	 * This method navigates the Display to the tripCreation screen by hiding the current
	 * panels in the WindowFrame and calling display.tripCreationScreen().
	 * 
	 */
	
	private void goToTripCreation() {
		display.resetView();
		display.tripCreationScreen();
	}
	/**
	 * This method navigates the Display to the tripCreation screen by hiding the current
	 * panels in the WindowFrame and calling display.reservationScreen().
	 */
	public void goToRes()
	{
		display.resetView();
		display.reservationScreen(Communication.get(CURR_TRIP));
	}
	
	public void goToMakeRes()
	{
		display.resetView();
		display.makeResScreen();
	}
	
	public void goToAccom()
	{
		display.resetView();
		display.accomScreen(Communication.get(CURR_TRIP));
	}
	
	public void goToNewAccom()
	{
		display.resetView();
		display.makeAccomScreen();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------	
	
	/**
	 * This method redirects the given String to the Display to create an error report box to 
	 * display an error to the user; the design of the box is to close on being clicked.
	 * 
	 * @param displayError - String object representing the error message to display to the User.
	 */

	private void errorReport(String displayError) {
		display.errorBox(displayError);
	}
	
}
