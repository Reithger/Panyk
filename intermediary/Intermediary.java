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
	/** */
	private Database database;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
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
	 * 
	 * 
	 */
	
	public void clock() {
		String happen = Communication.get(CONTROL);
		Communication.set(CONTROL, CONTROL_NULL);
		if(happen == null)
			return;
		switch(happen) {
			case CONTROL_LOGIN_SCREEN: goToLogin(); break;
			case CONTROL_ATTEMPT_LOGIN: attemptLogin(); break;
			case CONTROL_USER_CREATE: goToCreateAccount(); break;
			case CONTROL_TRIP_SELECT: goToTripSelect(); break;
			case CONTROL_ATTEMPT_USER_CREATE: createNewUser(); break;
			default: break;
		}
	}
	
	/**
	 * 
	 * 
	 * @param username
	 * @param password
	 */
	
	public void attemptLogin() {
		String username = Communication.get(LOGIN_USERNAME);
		String password = Communication.get(LOGIN_PASSWORD);
		boolean validUsername = database.checkUserExists(username);
		if(!validUsername) {
			//error one
			return;
		}
		boolean validPassword = database.checkValidPassword(username, password);
		if(!validPassword) {
			//error two
			return;
		}
		user = new User(username, password);
	}
	
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
		}
	}
	
//---  Navigation   ---------------------------------------------------------------------------
	
	private void goToLogin() {
		display.resetView();
		display.logInScreen();
	}
	
	private void goToCreateAccount() {
		display.resetView();
		display.createAccountScreen();
	}

	private void goToTripSelect() {
		display.resetView();
		display.createAccountScreen();
	}
	
//---  Mechanics   ----------------------------------------------------------------------------	
	
	/** 
	 * This method determines if a string (dob) is in the format YYYY-MM-DD
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

	private void errorReport(String displayError) {
		display.errorBox(displayError);
	}
	
}