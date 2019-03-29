package intermediary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	public final static String CONTROL_INITIAL_SCREEN = "start_screen";
	public final static String CONTROL_LOGIN_SCREEN = "log_in";
	public final static String CONTROL_USER_CREATE = "create_user";
	public final static String CONTROL_TRIP_SELECT = "trip_select";
	public final static String CONTROL_TRIP_CREATION = "trip_creation";
	public final static String CONTROL_MAIN_SCREEN = "Main";
	public final static String CONTROL_ATTEMPT_LOGIN = "login_fn";
	public final static String CONTROL_ATTEMPT_USER_CREATE = "user_create_fn";
	public final static String CONTROL_ATTEMPT_CREATE_TRIP = "create_trip";
	public final static String CONTROL_SCHEDULABLE_SELECT = "select_Sched_nav";
	public final static String CONTROL_SCHEDULABLE_CREATION = "create_Sched_nav";
	public final static String CONTROL_ATTEMPT_SCHEDULABLE_CREATE = "create_Sched_act";
	
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
	public final static String CREATE_TRIP_DESTINATION = "trip_dest";
	public final static String CREATE_TRIP_START = "trip_start_date";
	public final static String CREATE_TRIP_END = "trip_end_date";
	public final static String CREATE_TRIP_DESCRIPTION = "trip_description";
			
	public final static String CURR_TRIP = "current_trip";
	public final static String CURR_SCHEDULABLE_TYPE = "current_sched";
	public final static String CURR_SCHEDULABLE_TITLES = "schedulable_titles";
	
	private final static String SCHEDULABLE_META_FIELD_LABEL = "metaField";
	private final static String[] SCHEDULABLE_META_FIELD_TYPES = new String[] {"sString", "sString", "sString", "sString", "sString", "sString", "sString", "sString", "sString"};
	private final static String[] SCHEDULABLE_META_FIELD_TITLES = new String[] {"fieldTitle","title1","title2","title3","title4","title5","title6","title7","title8"};

	private final static String[] DEFAULT_SCHEDULABLE_ACCOMMODATION_TITLES = new String[] {"Accommodation","username", "tripTitle", "sString_Name", "sString_Address", "Date_Start Date", "Date_End Date", "lString_Description"};
	private final static String[] DEFAULT_SCHEDULABLE_RESERVATION_TITLES = new String[] {"Reservation","username", "tripTitle", "sString_Name", "sString_Address", "Date_Start Date", "Date_End Date", "lString_Description"};
	private final static String[] DEFAULT_SCHEDULABLE_TRANSPORTATION_TITLES = new String[] {"Transportation","username", "tripTitle", "sString_Name", "sString_Mode", "Date_Start Date", "Date_End Date", "lString_Description"};
	
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
		display = new Display(1000, 600, this);
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
			case CONTROL_ATTEMPT_LOGIN:				//Attempts to log-in with the provided information 
				attemptLogin(); break;
			case CONTROL_ATTEMPT_USER_CREATE: 		//Attempts to create a new User with the provided information
				createNewUser(); break;
			case CONTROL_ATTEMPT_CREATE_TRIP:		//Attempts to create a new Trip with the provided information
				addTrip(); break;
			case CONTROL_ATTEMPT_SCHEDULABLE_CREATE:
				addSchedulable(); break;
			case CONTROL_INITIAL_SCREEN:			//Orders display to show the initial screen
				goToInitialScreen(); break;
			case CONTROL_LOGIN_SCREEN: 				//Orders display to show the log-in screen
				goToLogin(); break;
			case CONTROL_USER_CREATE: 				//Orders display to show the create account screen
				goToCreateAccount(); break;
			case CONTROL_TRIP_SELECT: 				//Orders display to show the trip select screen
				goToTripSelect(); break;
			case CONTROL_TRIP_CREATION:				//Orders display to show the trip creation screen
				goToTripCreation(); break;
			case CONTROL_SCHEDULABLE_SELECT:
				goToSchedulableSelect(); break;
			case CONTROL_SCHEDULABLE_CREATION:
				goToSchedulableCreation(); break;
			case CONTROL_MAIN_SCREEN:
				goToMainScreen(); break;
			default: break;
		}
	}
	
	public void initializeSchedulableTypes() {
		Database.includeTableType(SCHEDULABLE_META_FIELD_LABEL, SCHEDULABLE_META_FIELD_TITLES, SCHEDULABLE_META_FIELD_TYPES);
		Database.addEntry(SCHEDULABLE_META_FIELD_LABEL, SCHEDULABLE_META_FIELD_TITLES, DEFAULT_SCHEDULABLE_ACCOMMODATION_TITLES);
		Database.addEntry(SCHEDULABLE_META_FIELD_LABEL, SCHEDULABLE_META_FIELD_TITLES, DEFAULT_SCHEDULABLE_RESERVATION_TITLES);
		Database.addEntry(SCHEDULABLE_META_FIELD_LABEL, SCHEDULABLE_META_FIELD_TITLES, DEFAULT_SCHEDULABLE_TRANSPORTATION_TITLES);
		List<String[]> schedTypes = Database.search(SCHEDULABLE_META_FIELD_LABEL, new String[] {}, new String[] {});
		for(String[] s : schedTypes) {
			String head = s[0];
			String[] titles = new String[s.length];
			String[] types = new String[s.length];
			int count = 0;
			int index = 0;
			for(int i = 0; i < s.length; i++) {
				System.out.println("D: " + s[i]);
				if(s[i].indexOf("_") == -1) {
					count++;
				}
				else {
					types[index] = s[i].substring(0, s[i].indexOf("_"));
					titles[index] = s[i].substring(s[i].indexOf("_")+1);
					index++;
				}
			}
			System.out.println(Arrays.toString(titles) + "\n" + Arrays.toString(types));
			Database.includeTableType(head, Arrays.copyOfRange(titles, 0, titles.length - count), Arrays.copyOfRange(types, 0, types.length - count));
			user.addSchedulableType(head, Arrays.copyOfRange(titles, 0, titles.length - count), Arrays.copyOfRange(types, 0, types.length - count));
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
		initializeSchedulableTypes();
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
			initializeSchedulableTypes();
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
		String title = Communication.get(CREATE_TRIP_TITLE);
		String beginStr = Communication.get(CREATE_TRIP_START);
		String endStr = Communication.get(CREATE_TRIP_END);
		String dest = Communication.get(CREATE_TRIP_DESTINATION);
		String descr = Communication.get(CREATE_TRIP_DESCRIPTION);
		
		if(dest.equals("") || descr.equals("")){
			errorReport("Please provide data to each field.");
		}
		else {
			boolean result = user.makeTrip(title, dest, descr, beginStr, endStr);
			if(!result) {
				errorReport("Error during Trip creation; extant Trip name reused or Date format invalid");
			}
			else {
				Communication.set(CONTROL, CONTROL_TRIP_SELECT);
			}
		}
		
	}

	public void addSchedulable() {
		String header = Communication.get(CURR_SCHEDULABLE_TYPE);
		String[] titles = user.getSchedulableTypeTitles(header);
		String[] data = new String[titles.length];
		for(int i = 0; i < data.length; i++) {
			data[i] = Communication.get(CURR_SCHEDULABLE_TYPE + "_" + titles[i]);
		}
		user.addSchedulableItem(Communication.get(CURR_TRIP), header, data);
		Communication.set(CONTROL, CONTROL_SCHEDULABLE_SELECT);
	}
		
//--- Getter Methods --------------------------------------------------------------------------

	/**
	 * Getter method to query the database for all entries associated to the current user.
	 * 
	 * @return - Returns a List<<r>String[]> object containing the Trips associated to the current User.
	 */
	
	public ArrayList<Trip> getUsersTrips() {
		return user.getTrips();
	}
	
	public ArrayList<String> getSchedulableTypeHeaders(){
		ArrayList<String> out = user.getSchedulableTypes();
		ArrayList<String> pass = new ArrayList<String>();
		for(int i = 0; i < out.size(); i++) {
			if(user.getSchedulables(Communication.get(CURR_TRIP), out.get(i)).size() != 0) {
				pass.add(out.get(i));
			}
		}
		return pass;
	}
		
//---  Navigation   ---------------------------------------------------------------------------
	
	private void goToInitialScreen() {
		display.resetView();
		display.initialScreen();
	}
	
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
		display.makeTripScreen();
	}

	private void goToMainScreen() {
		display.resetView();
		display.makeMainScreen(user.getSchedulableTypes());
	}
	
	public void goToSchedulableSelect() {
		display.resetView();
		display.schedulableSelectScreen(Communication.get(CURR_SCHEDULABLE_TYPE), user.getDisplaySchedulablesData(Communication.get(CURR_TRIP), Communication.get(CURR_SCHEDULABLE_TYPE)));
	}
	
	public void goToSchedulableCreation() {
		display.resetView();
		String[] titles = user.getSchedulableTypeTitles(Communication.get(CURR_SCHEDULABLE_TYPE));
		String out = "";
		for(int i = 0; i < titles.length; i++)
			out += titles[i] + (i+1 < titles.length ? "   " : "");	//TODO Constant value for the splitter here
		Communication.set(CURR_SCHEDULABLE_TITLES, out);
		display.makeSchedulableScreen(user.getCreateSchedulablesData(Communication.get(CURR_SCHEDULABLE_TYPE)));
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
