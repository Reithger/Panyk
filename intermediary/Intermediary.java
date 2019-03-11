package intermediary;

import java.util.Calendar;
import java.util.Timer;

import database.Database;
import input.Communication;
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
	public final static String CONTROL_ATTEMPT_LOGIN = "login_fn";
	public final static String CONTROL_ATTEMPT_USER_CREATE = "user_create_fn";
	
	//-- Value Storage  ---------------------------------------
	
	public final static String LOGIN_USERNAME = "username";
	public final static String LOGIN_PASSWORD = "password";
	public final static String CREATE_USER_USERNAME = "create_username";
	public final static String CREATE_USER_PASSWORD = "create_password";
	public final static String CREATE_USER_DOB = "create_dob";
	public final static String CREATE_USER_FIRSTNAME = "create_firstname";
	public final static String CREATE_USER_LASTNAME = "create_lastname";

//---  Instance Variables   -------------------------------------------------------------------
	
	/** The Timer object periodically calls the clock() method of this Intermediary object*/
	private Timer timer;
	/** The Display object is the contact point this Intermediary object has to the View for Input/Output*/
	private Display display;
	/** The User object is the contact point this Intermediary object has to the Model for data access/manipulation*/
	private User user;
	/** Database object containing user data regarding their username/account and Trip info*/
	private Database database;
	
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
		database = new Database();
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
		boolean validUsername = database.checkUserExists(username);
		if(!validUsername) {
			errorReport("Invalid Username");
			return;
		}
		boolean validPassword = database.checkValidPassword(username, password);
		if(!validPassword) {
			errorReport("Invalid Password");
			return;
		}
		user = new User(username, password);
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
		String dob = Communication.get(CREATE_USER_DOB);
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
		if(dob == null || dob.equals("") || !validDOB(dob)) {
			System.out.println("D.O.B. invalid during User Account Creation: Intermediary > createNewUser");
			errorReport("Invalid Date of Birth");
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
			
		boolean checkExists = database.checkUserExists(username);
		if(!checkExists) {
			user = new User(firstname, lastname, username, password, dob);
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
	
//---  Mechanics   ----------------------------------------------------------------------------	
	
	/** 
	 * This method determines if a string (dob) is in the format YYYY-MM-DD
	 * 
	 * TODO: It doesn't check if the first String is 4 characters long; is that desired? 
	 * 
	 * @param dob
	 * @return
	 */
	
	private boolean validDOB(String dob) {
		String[] split = dob.split("-");
		if(split.length != 3) {
			return false;
		}
		try {
			int year = Integer.parseInt(split[0]);
			int month = Integer.parseInt(split[1]);
			int day = Integer.parseInt(split[2]);
			if(month > 12 || day > 31 || year > Calendar.getInstance().get(Calendar.YEAR)) {		//if the year is greater than the current year
				return false;
			}
		}
		catch(Exception e) {
			return false;
		}
		return true;
	}
	
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