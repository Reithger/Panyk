package view;

import java.awt.Color;
import java.awt.Font;
import java.util.List;
import input.Communication;
import intermediary.Intermediary;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

/**
 * This class is the core of the View; Intermediary (the Controller) communicates with this
 * class to receive user input and display the Model. This class will be supported by helper
 * classes which automate certain kinds of display patterns (i.e, certain configurations
 * such as the calendar; designed visualizations of stored data.)
 * 
 * Communication between this and Intermediary via user input can be done by updating the
 * static Communication object in the clickBehaviour and keyBehaviour methods of Panel objects.
 * Intermediary is on a constant clock cycle to detect updates to Communication to respond to
 * user input this way.
 * 
 * @author Mac Clevinger
 *
 */

public class Display {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** */
	private final static Font FONT_ONE = new Font("Arial Bold", Font.BOLD, 18);
	/** */
	private final static Font FONT_TWO = new Font("SansSerif", Font.BOLD, 40);
	/** */
	private final static Font FONT_ENTRY = new Font("Arial Bold", Font.BOLD, 14);
	/** */
	private final static Font FONT_TAB = new Font("Arial Bold", Font.BOLD, 12);
	/** */
	private final static Font FONT_HEADER = new Font("Arial Bold", Font.BOLD, 36);
	/** */
	private final static Font FONT_TITLE = new Font("Baskerville Old Face", Font.BOLD, 80);	//Vivaldi
	
	//------------------------------------------
	/** */
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	/** */
	private final static Color COLOR_LOGIN = new Color(102, 255, 102);
	/** */
	private final static Color COLOR_TWO = new Color(110, 100, 175);
	/** */
	private final static Color COLOR_THREE = new Color(200,150,170);
	/** */
	private final static Color COLOR_WHITE = new Color(255, 255, 255);
	/** */
	private final static Color COLOR_BLACK = new Color(0, 0, 0);
	/** */
	private final static Color COLOR_ERR = new Color(255, 80, 80);
	/** */
	private final static Color COLOR_SEPARATOR = new Color(255, 153, 0);
	/** */
	private final static Color COLOR_CREATE_ACC_BOX = new Color(204, 0, 102);
	
	//---------------------------
	/** */
	private final static int EVENT_GO_TO_LOGIN = 1;
	/** */
	private final static int EVENT_LOGIN = 2;
	/** */
	private final static int EVENT_CREATE_ACC_BTN = 3;
	/** */
	private final static int EVENT_CREATE_ACC_FINALIZE = 4;
	/** */
	private final static int EVENT_BACK_TO_LOGIN = 5;
	/** */
	private final static int EVENT_GO_TO_TRIP_CREATION = 6;
	/** */
	private final static int EVENT_GO_TO_TRIP = 20;//I think this has to be the maximum value, so arranging accordingly
	/** */
	private final static int EVENT_TRIP_SELECTION = 7;
	/** */
	private static final int EVENT_TRIP_CREATED = 8;
	/** */
	private static final int EVENT_GO_TO_RES_CREATION = 9;
	/** */
	private static final int EVENT_SAVE_RESERVATION = 10;
	/** */
	private static final int EVENT_SAVE_ACCOMODATION = 20;
	/** */
	private static final int MAX_COMPOSITE_ELEMENTS = 10;
	/** */
	private static final int EVENT_RES_LIST = 11;
	/** */
	private static final int EVENT_ACCOM_LIST = 12;
	/** */
	private static final int EVENT_GO_TO_ACCOM_CREATION = 13;
	/** */
	private static final int EVENT_TRANSPORT_LIST = 14;
	/** */
	private static final int EVENT_GO_TO_TRANSP_CREATION = 15;
	/** */
	private static final int EVENT_SAVE_TRANSP=16;
	/** */
	private static final int EVENT_CONTACT_LIST=17;
	/** */
	private static final int EVENT_GO_TO_CONTACT_CREATION=18;
	/** */
	private static final int EVENT_SAVE_CONTACT = 19;
	
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** WindowFrame object that is used to visually communicate information to the user and receive their input*/
	private WindowFrame display;
	/** int value representing the width of the WindowFrame object created for this program*/
	private int width;
	/** int value representing the height of the WindowFrame object created for this program*/
	private int height;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Display type that assigns a provided width and height to the wholly
	 * encompassing WindowFrame object and instructs it to display the program's initial screen (log-in).
	 * 
	 * @param inWidth - int value representing the width of the WindowFrame that displays the program.
	 * @param inHeight - int value representing the height of the WindowFrame that displays the program.
	 */
	
	public Display(int inWidth, int inHeight){
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width + 14, height + 37);	//offset because java windows aren't quite accurate
		Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_INITIAL_SCREEN);
		
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method instructs the Display object to present the initial screen as designed
	 * within this method; effectively a title screen for branding purposes and possible
	 * settings allocation. Just click the continue button to go to the log-in screen.
	 * 
	 */
	
	public void initialScreen(){
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_LOGIN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_LOGIN_SCREEN);
				}
			}	
		};
		
		designTwoColorBorder(titlePanel, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		designBackedLabel(titlePanel, "title", COLOR_TWO, COLOR_BLACK, FONT_TITLE, "Plein Air", width/2, height/3, width/2, height/6, 1, true);
		designReactiveButton(titlePanel, "start", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Start", width/2, 7*height/10, width/10, height/20, 5, EVENT_GO_TO_LOGIN, true);

		display.addPanel("Title", titlePanel);
	}

	/**
	 * This method instructs the Display object to present the log-in screen as designed
	 * within this method; takes in Username and Password information to attempt to log the
	 * user in. If successful, moves onward to a Trip select screen. Can also travel to
	 * an account creation screen.
	 */
	
	public void logInScreen() {
		ElementPanel login = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					String uname = getElementStoredText("username_text");
					String pass = getElementStoredText("password_text");
					Communication.set(Intermediary.LOGIN_USERNAME, uname);
					Communication.set(Intermediary.LOGIN_PASSWORD, pass);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_LOGIN);
				}
				else if(event == EVENT_CREATE_ACC_BTN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_USER_CREATE);
				}
			}
		};

		designTwoColorBorder(login, "background", COLOR_ONE, COLOR_THREE, 0, 0, width*2/3, height, 30, 30, 0, false);
		designBackedLabel(login, "header", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Log In", width/3, height/4, width/3, height/5, 1, true);

		String[] elementName = new String[] {"username", "password"};
		String[] displayName = new String[] {"Username", "Password"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(login, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*2/15, height/2 + i * height/9, width/6, height/20, 3, true);
			designTextField(login, elementName[i], width/3, height/2 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
				
		designReactiveButton(login, "log_in", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Log In", width/3, height * 5 / 6, width/9, height/20, 1, EVENT_LOGIN, true);
		designReactiveButton(login,"account_create", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create Account", 5*width/6, height * 5 / 6, width/10, height/16, 2, EVENT_CREATE_ACC_BTN, true);		
		designTwoColorBorder(login, "background_2", COLOR_ONE, COLOR_THREE, width*2/3, 0, width/3, height, 30, 30, 0, false);
		
		designBackedLabel(login, "text_display", COLOR_CREATE_ACC_BOX, COLOR_BLACK, FONT_ENTRY, "Don't have an account?", 5*width/6, height * 2 / 3, width/6, height/12, 1, true);
		
		display.addPanel("Login", login);
	}
	
	/**
	 * This method designs the screen that allows the user to create a new account
	 * in this program with which trips can be created, edited, stored, and retrieved.
	 * 
	 * The user provides data for account creation and submits that information which
	 * can be rejected, or returns to the log in screen.
	 */
	
	public void createAccountScreen() {
		ElementPanel createAcc = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_CREATE_ACC_FINALIZE) {
					String fn    = getElementStoredText("fn_text");
					String ln    = getElementStoredText("ln_text");
					String uname = getElementStoredText("uname_text");
					String pass  = getElementStoredText("pass_text");
						
					Communication.set(Intermediary.CREATE_USER_FIRSTNAME, fn);
					Communication.set(Intermediary.CREATE_USER_LASTNAME, ln);
					Communication.set(Intermediary.CREATE_USER_USERNAME, uname);
					Communication.set(Intermediary.CREATE_USER_PASSWORD, pass);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_USER_CREATE);
				}
				else if(event == EVENT_BACK_TO_LOGIN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_LOGIN_SCREEN);
				}
			}
		};
		designTwoColorBorder(createAcc, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		designBackedLabel(createAcc, "header", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Create your account", width/2, height/6, width/2, height/8, 1, true);
		
		String[] elementName = new String[] {"fn", "ln", "uname", "pass"};
		String[] displayName = new String[] {"First Name", "Last Name", "Username", "Password"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(createAcc, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*3/10, height/3 + i * height/9, width/6, height/20, 3, true);
			designTextField(createAcc, elementName[i], width/2, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		designReactiveButton(createAcc, "create_account", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create my account", width/2, height*5/6, width/8, height/15, 2, EVENT_CREATE_ACC_FINALIZE, true);
		designReactiveButton(createAcc, "back", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width/12, height * 11 / 12, width / 12, height / 20, 2, EVENT_BACK_TO_LOGIN, true);

		display.addPanel("Create Account", createAcc);
	}

	/**
	 * This method designs the screen to allow the user to choose from pre-existing trips
	 * for their manipulation or to create a new trip by accessing a new screen.
	 */
	
	public void tripSelectScreen() {		
		List<String[]> trips = Intermediary.getUsersTrips();
		
		ElementPanel tripSelect = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_BACK_TO_LOGIN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_LOGIN_SCREEN);
				}
				else if(event == EVENT_GO_TO_TRIP_CREATION) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_CREATION);
				}
				else if(event >= EVENT_GO_TO_TRIP) {
					//TODO: change this so it switches to the calendar screen (yet to be created)
					int tripNum = event - EVENT_GO_TO_TRIP;
					Communication.set(Intermediary.CURR_TRIP, trips.get(tripNum)[1]);
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
				}
			}
		};

		designTwoColorBorder(tripSelect, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		designBackedLabel(tripSelect, "trip_select_title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Select Trip", width/2, height/8, width/3, height/10, 1, true);
		
		designReactiveButton(tripSelect, "logout", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Logout", width*11/12, height*5/6, width/12, height/14, 2, EVENT_BACK_TO_LOGIN, true);
		designReactiveButton(tripSelect, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a Trip", width*5/6, height*2/15, width/10, height/12, 2, EVENT_GO_TO_TRIP_CREATION, true);
		designTwoColorBorder(tripSelect, "background_backdrop", COLOR_WHITE, COLOR_BLACK, width/6, height*2/9, width*2/3, height*5/8, 30, 20, 1, false);

		//Display list of trips
		for(int i = 0; i < (trips == null ? 0 : trips.size()); i++) {
			designReactiveButton(tripSelect, "trip_"+i, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, trips.get(i)[1], width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_TRIP+i, true);
			tripSelect.addText("trip_description_"+i, 35, width/2 + width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(i)[2], FONT_ENTRY, true);
			tripSelect.addText("trip_date_"+i, 35, width/2 - width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(i)[3] + " - " + trips.get(i)[4], FONT_ENTRY, true);
		}

		display.addPanel("Trip Select", tripSelect);
	}

	/**
	 * This method designs the screen that lets the user create a new Trip
	 * that is associated to their account; this process can fail if bad
	 * input is provided.
	 * 
	 * TODO: Describe what makes bad input
	 * 
	 * The user can also return to the trip select screen, and upon successful
	 * creation of a trip the user will also be taken to the trip select screen.
	 */

	public void makeTripScreen() {
		ElementPanel tripCreate = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRIP_SELECTION){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_TRIP_CREATED){
					String title = getElementStoredText("tripTitle_text");
					String date1 = getElementStoredText("tripStart_text");
					String date2 = getElementStoredText("tripEnd_text");
					String dest = getElementStoredText("tripDest_text");
					
					Communication.set(Intermediary.CREATE_TRIP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRIP_START, date1);
					Communication.set(Intermediary.CREATE_TRIP_END, date2);
					Communication.set(Intermediary.CREATE_TRIP_DEST, dest);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_CREATE_TRIP);
				}
				
			}
		};
		
		designTwoColorBorder(tripCreate, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(tripCreate, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Trip Details", width/2, height/8, width/2, height/10, 1, true);
		designReactiveButton(tripCreate, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Exit", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		designReactiveButton(tripCreate, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_TRIP_CREATED, true);
		
		String[] elementName = new String[] {"tripTitle", "tripDest", "tripStart", "tripEnd", "null"};
		String[] displayName = new String[] {"Trip Name:", "Destination:", "Start Date", "End Date", "(dd/MM/yyyy)"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(tripCreate, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*3/10, height/3 + i * height/9, width/6, height/20, 3, true);
			if(!elementName[i].equals("null"))
				designTextField(tripCreate, elementName[i], width/2, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		display.addPanel("Trip Creation", tripCreate);
	}
	
	/**
	 * This method designs the screen that displays the stored reservations of this trip,
	 * and permits the user to create new reservations as well as TODO edit existing reservations.
	 * 
	 * User can return to generic display of trips, or traverse via the header to other types of
	 * scheduled items in this trip (accommodations, transports, reservations).
	 */

	public void reservationDisplayScreen(String tripName) {
		ElementPanel rS = new ElementPanel(0, 0, width, height){
			public void clickBehaviour(int event) {
				if(!interpretHeader(event)) {
					
				}
			}
		};
		
		List<String[]> res = Intermediary.getTripsRes();
		designTwoColorBorder(rS, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		
		addHeaderTabs(rS);
		designBackedLabel(rS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Reservations:", width/2, height/6, width/3, height/10, 1, true);
		designReactiveButton(rS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		designReactiveButton(rS, "create_reservation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Reservation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_RES_CREATION, true);
		displayItemList(rS, res, 2, 3, 4, 5);		//Display list of stored reservations
		
		display.addPanel("Reservations", rS);
	}

	/**
	 * This method designs the screen that permits the user to supply the information
	 * necessary to create a reservation and submit that for creation (can fail if
	 * improperly formatted.)
	 * 
	 * Upon successful submission, will return to reservation display screen; can also
	 * exit at any time to return to the reservation display screen.
	 */

	public void makeReservationScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_RES_LIST){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
				}
				else if(event == EVENT_SAVE_RESERVATION){
					String title = getElementStoredText("resTitle_text");
					String date1 = getElementStoredText("resStart_text");
					String date2 = getElementStoredText("resEnd_text");
					String loc = getElementStoredText("resLoc_text");
					
					Communication.set(Intermediary.CREATE_RES_TITLE, title);
					Communication.set(Intermediary.CREATE_RES_START, date1);
					Communication.set(Intermediary.CREATE_RES_END, date2);
					Communication.set(Intermediary.CREATE_RES_LOC, loc);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_RES);
				}
				
			}
		};
		
		designTwoColorBorder(mR, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Reservation Details", width/2, height/8, width*2/3, height/10, 1, true);
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_RES_LIST, true);
		designReactiveButton(mR, "create_reservation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*13/16, width/8, height/15, 2, EVENT_SAVE_RESERVATION, true);
				
		String[][] elementName = new String[][] {{"resTitle", "resLoc"},{"resStart", "resEnd"}};
		String[][] displayName = new String[][] {{"Reservation Name", "Address"}, {"Start Date", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i - height/10, width/6, height/14, 3, true);
			}
		}
		
		designBackedLabel(mR, "label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "dd/MM/yyyy", width/3 + width/6, height*4/9 + height/4 - height/10, width/7, height/16, 3, true);
		display.addPanel("Res Creation", mR);
	}

	/**
	 * This method designs the screen that displays the stored accommodations of this trip,
	 * and permits the user to create new accommodations as well as TODO edit existing reservations.
	 * 
	 * User can return to generic display of trips, or traverse via the header to other types of
	 * scheduled items in this trip (accommodations, transports, reservations).
	 */
	
	public void accomodationDisplayScreen() {
		List<String[]> accom = Intermediary.getTripsAccom();
		
		ElementPanel aS = new ElementPanel(0, 0, width, height){			
			public void clickBehaviour(int event) {
				if(!interpretHeader(event)) {
					
				}
			}
		};
		addHeaderTabs(aS);
		
		designTwoColorBorder(aS, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		addHeaderTabs(aS);
		designBackedLabel(aS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Accomodations:", width/2, height/6, width*4/9, height/10, 1, true);
		designReactiveButton(aS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		designReactiveButton(aS, "create_accomodation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Accomodation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_ACCOM_CREATION, true);
		displayItemList(aS, accom, 3, 4, 5, 7);		//Display list of stored accommodations

		display.addPanel("Reservations", aS);
	}
	
	/**
	 * This method designs the screen that permits the user to supply the information
	 * necessary to create a accommodation and submit that for creation (can fail if
	 * improperly formatted.)
	 * 
	 * Upon successful submission, will return to accommodation display screen; can also
	 * exit at any time to return to the accommodation display screen.
	 */
	
	public void makeAccomodationScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_ACCOM_LIST){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_LIST);
				}
				else if(event == EVENT_SAVE_ACCOMODATION){
					String title = this.getElementStoredText("accomTitle_text");
					String date1 = this.getElementStoredText("accomStart_text");
					String date2 = this.getElementStoredText("accomEnd_text");
					String loc = this.getElementStoredText("accomLoc_text");
					
					Communication.set(Intermediary.CREATE_ACCOM_TITLE, title);
					Communication.set(Intermediary.CREATE_ACCOM_START, date1);
					Communication.set(Intermediary.CREATE_ACCOM_END, date2);
					Communication.set(Intermediary.CREATE_ACCOM_LOC, loc);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_ACCOM);
				}
				
			}
		};
		designTwoColorBorder(mR, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Accomodation Details", width/2, height/8, width*2/3, height/10, 1, true);
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_ACCOM_LIST, true);
		designReactiveButton(mR, "create_accomodation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*13/16, width/8, height/15, 2, EVENT_SAVE_ACCOMODATION, true);
				
		String[][] elementName = new String[][] {{"accomTitle", "accomLoc"},{"accomStart", "accomEnd"}};
		String[][] displayName = new String[][] {{"Reservation Name", "Address"}, {"Start Date", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i - height/10, width/6, height/14, 3, true);			}
		}
		designBackedLabel(mR, "label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "dd/MM/yyyy", width/3 + width/6, height*4/9 + height/4 - height/10, width/7, height/16, 3, true);
		
		display.addPanel("Res Creation", mR);
	}

	/**
	 * This method designs the screen that displays the stored transportations of this trip,
	 * and permits the user to create new transportations as well as TODO edit existing transportations.
	 * 
	 * User can return to generic display of trips, or traverse via the header to other types of
	 * scheduled items in this trip (accommodations, transports, reservations).
	 */
	
	public void transportDisplayScreen() {
		List<String[]> transp = Intermediary.getTripsTransp();
		
		ElementPanel tS = new ElementPanel(0, 0, width, height)	{
			public void clickBehaviour(int event) {
				if(!interpretHeader(event)) {
					
				}
			}
		};
		designTwoColorBorder(tS, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		addHeaderTabs(tS);
		designBackedLabel(tS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Transportation:", width/2, height/6, width/3, height/10, 1, true);
		designReactiveButton(tS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		designReactiveButton(tS, "create_transport", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Transportation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_TRANSP_CREATION, true);
		displayItemList(tS, transp, 2, 3, 4, 5);		//Display list of stored transports
	
		display.addPanel("Reservations", tS);
	}
	
	/**
	 * This method designs the screen that permits the user to supply the information
	 * necessary to create a transportation and submit that for creation (can fail if
	 * improperly formatted.)
	 * 
	 * Upon successful submission, will return to transportation display screen; can also
	 * exit at any time to return to the transportation display screen.
	 */
	
	public void makeTransportScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRANSPORT_LIST){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
				}
				else if(event == EVENT_SAVE_TRANSP){
					String title = getElementStoredText("transpTitle_text");
					String date1 = getElementStoredText("transpStart_text");
					String date2 = getElementStoredText("transpEnd_text");
					String mode = getElementStoredText("transpMode_text");
					
					Communication.set(Intermediary.CREATE_TRANSP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRANSP_START, date1);
					Communication.set(Intermediary.CREATE_TRANSP_END, date2);
					Communication.set(Intermediary.CREATE_TRANSP_MODE, mode);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_TRANSP);
				}
				
			}
		};
		designTwoColorBorder(mR, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Transportation Details", width/2, height/8, width*2/3, height/10, 1, true);
		designReactiveButton(mR, "back", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRANSPORT_LIST, true);
		designReactiveButton(mR, "create_transport", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*13/16, width/8, height/15, 2, EVENT_SAVE_TRANSP, true);

		String[][] elementName = new String[][] {{"transpTitle", "transpMode"},{"transpStart", "transpEnd"}};
		String[][] displayName = new String[][] {{"Title", "Mode of Transportation"}, {"Start Date", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height*4/9 + height/4 * i - height/10, width/6, height/14, 3, true);			}
		}
		
		designBackedLabel(mR, "label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "dd/MM/yyyy", width/3 + width/6, height*4/9 + height/4 - height/10, width/7, height/16, 3, true);
		
		display.addPanel("transp Creation", mR);
	}
			
	/**
	 * This method adds a header to the provided ElementPanel providing links to other screens via clickable
	 * buttons at the top border of the screen.
	 * 
	 * TODO: Make this dynamic so we can open more tabs; make it prettier (buttons should provide feedback when
	 * clicked)
	 * 
	 * @param e - ElementPanel object representing the visual panel to which these header tabs are being added.
	 */
	
	public void addHeaderTabs(ElementPanel e){
		designReactiveButton(e, "home_tab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Home", width/8, 15, width/4, 30, 18, 0, true);
		designReactiveButton(e, "reservation_tab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Reservations", 3*width/8, 15, width/4, 30, 19, EVENT_RES_LIST, true);
		designReactiveButton(e, "accommodation_tab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Accomodations", 5*width/8, 15, width/4, 30, 20, EVENT_ACCOM_LIST, true);
		designReactiveButton(e, "transport_tab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Transportation", 7*width/8, 15, width/4, 30, 21, EVENT_TRANSPORT_LIST, true);
		//designReactiveButton(e, "contact_tab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Contacts", 9*width/12, 15, width/6, 30, 22, EVENT_CONTACT_LIST, true); editted out because Java hates our contacts
	}
	
	/**
	 * This method creates a vertical list of items (reservations, accommodations, transportations) that
	 * are displayed to the user; the information provided is specified by a provided list object and an
	 * ordered set of indexes to direct the retrieval of data from.
	 * 
	 * The consistent design is a rectangular region with the title of the item displayed centrally, with
	 * the range of dates to the lower left and its description to the lower right.
	 * 
	 * TODO: when number of items overloads the given space, need slider for further display (or page switch)
	 * 
	 * @param e - ElementPanel object representing the visual panel to which these composite elements are being added. 
	 * @param sl - List<<r>String[]> object containing the data that is being selectively chosen from 
	 * @param titlePos - int value representing the index in sl that corresponds to the title data
	 * @param startPos - int value representing the index in sl that corresponds to the start date data
	 * @param endPos - int value representing the index in sl that corresponds to the end date data
	 * @param otherPos - int value representing the index in sl that corresponds to a variable piece of data
	 */
	
	public void displayItemList(ElementPanel e, List<String[]> sl, int titlePos, int startPos, int endPos, int otherPos){
		designTwoColorBorder(e, "border_in", COLOR_WHITE, COLOR_BLACK, width/6, height /4, width*2/3, height/2, 50, 30, 1, false);
		
		for(int i = 0; i < (sl == null ? 0 : sl.size()); i++) {
			designReactiveButton(e, "trip_"+i, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, sl.get(i)[titlePos], width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_TRIP+i, true);
			e.addText("trip_desc_"+i, 35, width/2 + width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, sl.get(i)[otherPos], FONT_ENTRY, true);
			e.addText("trip_date_"+i, 35, width/2 - width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, sl.get(i)[startPos] + " - " + sl.get(i)[endPos], FONT_ENTRY, true);
		}	
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	/**
	 * This method resets the displayed Panel of the WindowFrame object stored by
	 * this Display object so that a new Panel may be displayed without overlap
	 * of priority in the displayed elements.
	 */
	
	public void resetView() {
		display.hidePanels();
		
	}

	/**
	 * This method handles the repetitive task of querying the user input for its
	 * interaction with the header tabs available on some displayed Panels.
	 * 
	 * To mesh with other input-interpretations by specific screens, this method
	 * returns a boolean value informing the calling method whether or not a Control
	 * value was assigned here.
	 * 
	 * TODO If Header tabs are made dynamic, make this respect that
	 * 
	 * @param event - int value representing the user's mouse input as a coded value to be interpreted
	 * @return - Returns a boolean value representing whether the event triggered a Control value to be assigned or not.
	 */
	
	public boolean interpretHeader(int event) {
		if(event == EVENT_TRIP_SELECTION){
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
			return true;
		}
		else if(event == EVENT_GO_TO_RES_CREATION) {
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RES_CREATION);
			return true;
		}
		else if(event == EVENT_ACCOM_LIST){
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_LIST);
			return true;
		}
		else if(event==EVENT_TRANSPORT_LIST){
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
			return true;
		}
		/*else if(event == EVENT_CONTACT_LIST){
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_LIST);
			return true;
		}*/
		
		return false;
	}
	
//---  Composite Methods   --------------------------------------------------------------------

	/**
	 * This method automates the composite of ElementPanel basic elements to create a text-input
	 * box that corresponds to the provided code for receiving user input and using it for functions.
	 * 
	 * Priority intervals are of 10 so that each Composite method can have its own organization of
	 * basic elements as well as be placed relative to other Composite structures.
	 * 
	 * @param pan - ElementPanel object to which the composite structure will be added.
	 * @param name - String object representing the name that these elements should be given to differentiate them to the ElementPanel.
	 * @param x - int object representing the x coordinate at which this composite structure should be placed
	 * @param y - int object representing the y coordinate at which this composite structure should be placed
	 * @param panWid - int value representing the width of this composite structure
	 * @param panHei - int value representing the height of this composite structure
	 * @param priority - int value representing the priority level that this composite structure should be placed at (in units of 10)
	 * @param code - int value representing the internal user input code that should be associated to elements in this Composite Structure
	 * @param centered - boolean value representing whether or not this composite structure should be drawn with x, y at the center or top-left corner
	 */
	
	private void designTextField(ElementPanel pan, String name, int x, int y, int panWid, int panHei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry(name + "_text", priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, panWid, panHei, code, FONT_ENTRY, centered);	
	}
	
	/**
	 * This method automates the composition of ElementPanel basic elements to create a reactive
	 * button that displays text and marks its location on the Panel with defined colors.
	 * 
	 * @param pan - ElementPanel object to which the composite structure will be added.
	 * @param name - String object representing the name that these elements should be given to differentiate them to the ElementPanel.
	 * @param fillCol - Color object representing the fill color of the rectangle marking its location on the Panel
	 * @param backColor - Color object representing the border color of the rectangle marking its location on the Panel
	 * @param font - Font object describing in what font to write the provided String to label this button on the Panel
	 * @param message - String object representing the phrase used to label this button on the Panel
	 * @param x - int object representing the x coordinate at which this composite structure should be placed
	 * @param y - int object representing the y coordinate at which this composite structure should be placed
	 * @param wid - int value representing the width of this composite structure
	 * @param hei - int value representing the height of this composite structure
	 * @param priority - int value representing the priority level that this composite structure should be placed at (in units of 10)
	 * @param code - int value representing the internal user input code that should be associated to elements in this Composite Structure
	 * @param centered - boolean value representing whether or not this composite structure should be drawn with x, y at the center or top-left corner
	 */
	
	private void designReactiveButton(ElementPanel pan, String name, Color fillCol, Color backColor, Font font, String message, int x, int y, int wid, int hei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, fillCol, backColor, centered);
		pan.addButton(name + "_but",     priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, wid, hei, code, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 2, x, y,  wid, hei, message, font, centered);
	}
	
	/**
	 * This method automates the composition of ElementPanel basic elements to create a label on
	 * the screen that is backed with defined colors to accentuate its placement.
	 * 
	 * @param pan - ElementPanel object to which the composite structure will be added.
	 * @param name - String object representing the name that these elements should be given to differentiate them to the ElementPanel.
	 * @param colFill - Color object representing the fill color of the rectangle marking its location on the Panel
	 * @param colBorder - Color object representing the border color of the rectangle marking its location on the Panel
	 * @param font - Font object describing in what font to write the provided String to label this button on the Panel
	 * @param message - String object representing the phrase used to label this button on the Panel
	 * @param x - int object representing the x coordinate at which this composite structure should be placed
	 * @param y - int object representing the y coordinate at which this composite structure should be placed
	 * @param wid - int value representing the width of this composite structure
	 * @param hei - int value representing the height of this composite structure
	 * @param priority - int value representing the priority level that this composite structure should be placed at (in units of 10)
	 * @param centered - boolean value representing whether or not this composite structure should be drawn with x, y at the center or top-left corner
	 */
	
	private void designBackedLabel(ElementPanel pan, String name, Color colFill, Color colBorder, Font font, String message, int x, int y, int wid, int hei, int priority, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, colFill, colBorder, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 1, x, y,  wid, hei, message, font, centered);
	}

	/**
	 * 
	 * @param pan - ElementPanel object to which the composite structure will be added.
	 * @param name - String object representing the name that these elements should be given to differentiate them to the ElementPanel.
	 * @param colFill - Color object representing the fill color of the rectangle marking its location on the Panel
	 * @param colBorder - Color object representing the border color of the rectangle marking its location on the Panel
	 * @param font - Font object describing in what font to write the provided String to label this button on the Panel
	 * @param message - String object representing the phrase used to label this button on the Panel
	 * @param x - int object representing the x coordinate at which this composite structure should be placed
	 * @param y - int object representing the y coordinate at which this composite structure should be placed
	 * @param wid - int value representing the width of this composite structure
	 * @param hei - int value representing the height of this composite structure
	 * @param xRatio - int value representing what margin of the screen's width should be given to the border color region
	 * @param yRatio - int value representing what margine of the screen's height should be given to the border color region
	 * @param priority - int value representing the priority level that this composite structure should be placed at (in units of 10)
	 * @param centered - boolean value representing whether or not this composite structure should be drawn with x, y at the center or top-left corner
	 */
	
	private void designTwoColorBorder(ElementPanel pan, String name, Color colFill, Color colBorder, int x, int y, int wid, int hei, int xRatio, int yRatio, int priority, boolean centered) {
		pan.addRectangle(name + "_rect1", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, colBorder, centered);
		pan.addRectangle(name + "_rect2", priority * MAX_COMPOSITE_ELEMENTS + 1, x + wid/xRatio, y + hei/yRatio, wid - 2 * wid / xRatio, hei - 2 * hei/yRatio, colFill, centered);
	}
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	/**
	 * This method generates a small window frame that alerts the user to an error in their
	 * interaction with the program; is easily dismissed.
	 * 
	 * @param in - String object representing the message to be displayed to the user regarding the error.
	 */
	
	public void errorBox(String in) {
		int wid = 300;
		int hei = 250;
		WindowFrame error = new WindowFrame(wid, hei);
		error.setExitOnClose(false);
		ElementPanel pan = new ElementPanel(0, 0, wid, hei) {
			public void clickBehaviour(int event) {
				if(event == 1) {
					getParentFrame().remove();
				}
			}
		};
		pan.addText("tex_1", 5, wid/2, hei*2/5, wid*9/10, hei*2/5, in, FONT_ONE, true);
		pan.addText("tex_2", 5, wid/2, hei*4/5, wid, hei/5, "Click Anywhere to Remove", FONT_ONE, true);
		pan.addButton("but1", 10, 0, 0, pan.getWidth(), pan.getHeight(), 1, false);
		error.add(pan);
	}
	
}
