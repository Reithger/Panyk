package view;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import database.Database;
import input.Communication;
import intermediary.Intermediary;
import model.trip.Trip;
import model.trip.schedule.DisplayData;
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
	private final static int EVENT_GO_TO_LOGIN = 51;
	/** */
	private final static int EVENT_ATTEMPT_LOGIN = 52;
	/** */
	private final static int EVENT_GO_TO_CREATE_ACCOUNT = 53;
	/** */
	private final static int EVENT_ATTEMPT_CREATE_ACCOUNT = 54;
	/** */
	private final static int EVENT_GO_TO_SELECT_TRIP = 55;
	/** */
	private final static int EVENT_GO_TO_CREATE_TRIP = 56;
	/** */
	private static final int EVENT_ATTEMPT_CREATE_TRIP = 57;
	/** */
	private static final int EVENT_GO_TO_SELECT_SCHEDULABLE = 58;
	
	private final static int EVENT_GO_TO_CREATE_SCHEDULABLE = 59;
	
	private final static int EVENT_ATTEMPT_CREATE_SCHEDULABLE = 60;
	
	private static final int EVENT_GO_TO_MAIN = 61;
	
	private static final int EVENT_GO_TO_CREATE_SCHEDULABLE_ARCHETYPE = 62;
	
	private static final int EVENT_ATTEMPT_CREATE_NEW_SCHED_ARC_TYPE = 69;
	
	private static final int EVENT_ADD_FIELD_TO_NEW_SCHED_ARC_TYPE = 70;
	/** */
	private final static int EVENT_GO_TO_ITEM = 10000;		//this has to be the biggest by far to allow for the check done in listing the trips
	
	//TODO: This a very duct tape and bubble gum solution, should make robust 
	
	private final static int EVENT_NEXT_PAGE = 63;
	
	private final static int EVENT_PREV_PAGE = 64;
	
	private final static int EVENT_ATTEMPT_DELETE_SCHEDULABLE = 65;
	
	private final static int EVENT_CONFIRM_DELETE = 66;
	
	private final static int EVENT_CONFIRM = 67;
	
	private final static int EVENT_DENY = 68;

	/** This is not a limitation of how many elements can be in a composite, increase if you need more space; handles priority room*/
	private static final int MAX_COMPOSITE_ELEMENTS = 10;
	
	private static final int MAX_VIS_LIST_ITEMS=3;
	
	private static final int MAX_VIS_TRIPS=4;
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** WindowFrame object that is used to visually communicate information to the user and receive their input*/
	private WindowFrame display;
	/** */
	private Intermediary intermediary;
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
	
	public Display(int inWidth, int inHeight, Intermediary relation){
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width + 14, height + 37);	//offset because java windows aren't quite accurate
		intermediary = relation;		
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
				if(event == EVENT_ATTEMPT_LOGIN) {
					String uname = getElementStoredText("username_text");
					String pass = getElementStoredText("password_text");
					Communication.set(Intermediary.LOGIN_USERNAME, uname);
					Communication.set(Intermediary.LOGIN_PASSWORD, pass);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_LOGIN);
				}
				else if(event == EVENT_GO_TO_CREATE_ACCOUNT) {
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
			designTextField(login, elementName[i], "", width/3, height/2 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
				
		designReactiveButton(login, "log_in", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Log In", width/3, height * 5 / 6, width/9, height/20, 1, EVENT_ATTEMPT_LOGIN, true);
		designReactiveButton(login,"account_create", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create Account", 5*width/6, height * 5 / 6, width/10, height/16, 2, EVENT_GO_TO_CREATE_ACCOUNT, true);		
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
				if(event == EVENT_ATTEMPT_CREATE_ACCOUNT) {
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
				else if(event == EVENT_GO_TO_LOGIN) {
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
			designTextField(createAcc, elementName[i], "", width/2, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		designReactiveButton(createAcc, "create_account", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create my account", width/2, height*5/6, width/8, height/15, 2, EVENT_ATTEMPT_CREATE_ACCOUNT, true);
		designReactiveButton(createAcc, "back", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width/12, height * 11 / 12, width / 12, height / 20, 2, EVENT_GO_TO_LOGIN, true);

		display.addPanel("Create Account", createAcc);
	}

	/**
	 * This method designs the screen to allow the user to choose from pre-existing trips
	 * for their manipulation or to create a new trip by accessing a new screen.
	 * Updated for next/prev buttons
	 */
	
	public void tripSelectScreen(int pagenum) {		
		
		
		ArrayList<Trip> trips = intermediary.getUsersTrips();
	
		
		ElementPanel tripSelect = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_LOGIN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_LOGIN_SCREEN);
				}
				else if(event == EVENT_GO_TO_CREATE_TRIP) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_CREATION);
				}
				else if(event >= EVENT_GO_TO_ITEM) {
					int tripNum = event - EVENT_GO_TO_ITEM;
					Communication.set(Intermediary.CURR_TRIP, trips.get(tripNum).getTitle());
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_MAIN_SCREEN);
				}
				else if(event == EVENT_NEXT_PAGE)
				{
					resetView();
					tripSelectScreen(pagenum+1);
				}
				else if(event == EVENT_PREV_PAGE)
				{
					resetView();
					tripSelectScreen(pagenum-1);
				}
			}
		};

		designTwoColorBorder(tripSelect, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		designBackedLabel(tripSelect, "trip_select_title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Select Trip", width/2, height/8, width/3, height/10, 1, true);
		
		designReactiveButton(tripSelect, "logout", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Logout", width*11/12, height*5/6, width/12, height/14, 2, EVENT_GO_TO_LOGIN, true);
		designReactiveButton(tripSelect, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a Trip", width*5/6, height*2/15, width/10, height/12, 2, EVENT_GO_TO_CREATE_TRIP, true);
		designTwoColorBorder(tripSelect, "background_backdrop", COLOR_WHITE, COLOR_BLACK, width/6, height*2/9, width*2/3, height*5/8, 30, 20, 1, false);
		
		int scheds;
		if((trips.size() - pagenum*MAX_VIS_TRIPS) > MAX_VIS_TRIPS)//if there are too many of this thing to fit on the page
		{
			scheds = MAX_VIS_TRIPS;//present the maximum number we're able to show with the current window style (currently 3)
		}
		else//otherwise
		{
			scheds = trips.size() - (pagenum*MAX_VIS_TRIPS);//just show all the schedulables here
		}
		
		int itemnum;//variable to keep track of which schedulable in the chronological order this one is
		if(trips.size()!=0)// so long as there actually are schedulables to print
		{
			
			for(int i = 0; i < scheds; i++)//for(int i = 0; i < (trips == null ? 0 : trips.size()); i++) 
			{
				itemnum = (MAX_VIS_TRIPS*pagenum) + i;
				
				designReactiveButton(tripSelect, "trip_"+itemnum, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, trips.get(itemnum).getTitle(), width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_ITEM+itemnum, true);
				tripSelect.addText("trip_description_"+itemnum, 35, width/2 + width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(itemnum).getDestination(), FONT_ENTRY, true);
				tripSelect.addText("trip_date_"+itemnum, 35, width/2 - width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(itemnum).getDisplayStartDate() + " - " + trips.get(itemnum).getDisplayEndDate(), FONT_ENTRY, true);
			}
			
		}
		
		
		if(pagenum!=0)
		{
			designReactiveButton(tripSelect, "prev", COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, "Previous", 2*width/5, height * 11 / 12, width/12, height/20, 4, EVENT_PREV_PAGE, true);
		}
		
		if(trips.size() > scheds + pagenum*MAX_VIS_TRIPS)
		{
			designReactiveButton(tripSelect, "next", COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, "Next", 3*width/5, height * 11 / 12, width/12, height/20, 3, EVENT_NEXT_PAGE, true);
			
		}
		
		//Display list of trips
		

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
				if(event == EVENT_GO_TO_SELECT_TRIP){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_ATTEMPT_CREATE_TRIP){
					String title = getElementStoredText("tripTitle_text");
					String date1 = getElementStoredText("tripStart_text");
					String date2 = getElementStoredText("tripEnd_text");
					String dest = getElementStoredText("tripDest_text");
					String descr = getElementStoredText("tripDescrip_text");
					
					Communication.set(Intermediary.CREATE_TRIP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRIP_START, date1);
					Communication.set(Intermediary.CREATE_TRIP_END, date2);
					Communication.set(Intermediary.CREATE_TRIP_DESTINATION, dest);
					Communication.set(Intermediary.CREATE_TRIP_DESCRIPTION, descr);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_CREATE_TRIP);
				}
				
			}
		};
		
		designTwoColorBorder(tripCreate, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(tripCreate, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Trip Details", width/2, height/8, width/2, height/10, 1, true);
		designReactiveButton(tripCreate, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Exit", width*5/6, height*5/6, width/12, height/15, 2, EVENT_GO_TO_SELECT_TRIP, true);
		designReactiveButton(tripCreate, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_ATTEMPT_CREATE_TRIP, true);
		
		String[] elementName = new String[] {"tripTitle", "tripDest", "tripStart", "tripEnd", "null"};
		String[] displayName = new String[] {"Trip Name:", "Destination:", "Start Date", "End Date", "(dd/MM/yyyy)"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(tripCreate, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*2/15, height/3 + i * height/9, width/6, height/20, 3, true);
			if(!elementName[i].equals("null"))
				designTextField(tripCreate, elementName[i], "", width/3, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		designBackedLabel(tripCreate, "tripDescrip_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "Description", width*2/3, height/3 , width/6, height/20, 3, true);
		designTextField(tripCreate, "tripDescrip", "", width / 2, height * 7 / 18, width/3, height * 2/ 9, 3, 1000 + displayName.length, false);
		
		display.addPanel("Trip Creation", tripCreate);
	}
	
	/**
	 * This method designs the screen that displays to the user what Schedulable objects can be generated,
	 * and acts as a general 'hub' for accessing/using a Trip object.
	 * 
	 * Has access to the Header tab that allows transitioning between different screens via Schedulable Types
	 * and potentially Features.
	 * 
	 * @param scheduleTypes - ArrayList<<r>String> object containing the viable Schedulable Type objects
	 */

	public void makeMainScreen(ArrayList<String> scheduleTypes) {
		
		ElementPanel mainScreen = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(!interpretHeader(event)) {
					if(event == EVENT_GO_TO_SELECT_TRIP) {
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
					}
					else if(event == EVENT_GO_TO_CREATE_SCHEDULABLE_ARCHETYPE) {
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCREEN_SHEDULABLE_ARC_CREATE);
					}
					else if(event == EVENT_CONFIRM_DELETE) {
						confirmBox("Are you sure you want to delete this trip?", Intermediary.CONTROL_DELETE_TRIP, Intermediary.CONTROL_MAIN_SCREEN);
					}
					else if(event != -1 && event != EVENT_GO_TO_MAIN) {
						Communication.set(Intermediary.CURR_SCHEDULABLE_TYPE, scheduleTypes.get(event - EVENT_GO_TO_ITEM));
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHEDULABLE_SELECT);
					}
				}
			}
		};

		addHeaderTabs(mainScreen);
		
		designTwoColorBorder(mainScreen, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		designBackedLabel(mainScreen, "main_menu_title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Main Menu", width/2, height/8, width/3, height/10, 1, true);
		
		designReactiveButton(mainScreen, "back", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*11/12, height*5/6, width/12, height/14, 2, EVENT_GO_TO_SELECT_TRIP, true);
		designReactiveButton(mainScreen, "create_schedule_type", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a Schedulable", width*5/6, height*2/15, width/10, height/12, 2, EVENT_GO_TO_CREATE_SCHEDULABLE_ARCHETYPE, true);
		designTwoColorBorder(mainScreen, "background_backdrop", COLOR_WHITE, COLOR_BLACK, width/6, height*2/9, width*2/3, height*5/8, 30, 20, 1, false);
		designReactiveButton(mainScreen, "delete_trip", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Delete Trip", width*1/12, height*5/6, width/12, height/14, 2, EVENT_CONFIRM_DELETE, true);
		
		//Display list of schedulables
		for(int i = 0; i < scheduleTypes.size(); i++) {
			designReactiveButton(mainScreen, "shed_"+i, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, scheduleTypes.get(i), width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_ITEM+i, true);
		}

		display.addPanel("Schedulable Select", mainScreen);
	}
	
	/**
	 * This method designs the screen that displays to the user what Schedulable objects are extant for
	 * the current SchedulableType for this program. It only displays a subset of the total number of
	 * Schedulable objects, the location of that subset in the full set of Schedulable objects being
	 * defined by the provided int pagenum.
	 * 
	 * Can also create a new Schedulable object of the current type from this screen.
	 * 
	 * Has access to the Header tab that allows transitioning between different screens via Schedulable Types
	 * and potentially Features.
	 * 
	 * @param data
	 * @param pagenum
	 */
	
	public void schedulableSelectScreen(HashMap<String, DisplayData> data, int pagenum) 
	{
		
		String scheduleType = Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE);
		ArrayList<String> key = new ArrayList<String>(data.keySet());
		
		ElementPanel rS = new ElementPanel(0, 0, width, height){
			public void clickBehaviour(int event) {
				if(!interpretHeader(event)) {
					if(event == EVENT_GO_TO_CREATE_SCHEDULABLE) 
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHEDULABLE_CREATION);
					else if(event == EVENT_GO_TO_SELECT_TRIP) {
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
					}
					else if(event == EVENT_NEXT_PAGE) {
						
						resetView();
						schedulableSelectScreen(data, pagenum+1);//this is ugly, but pagenum++ throws a strange error
					}
					else if(event == EVENT_PREV_PAGE) {
						resetView();
						schedulableSelectScreen(data, pagenum-1);
					}else if(event >= EVENT_GO_TO_ITEM) {
							
						int schedNum = event - EVENT_GO_TO_ITEM;
						//DisplayData localMap = data.get(key.get(schedNum));
						//Intermediary.goToSchedScreen(scheduleType, data, schedNum);
						
						Communication.set(Intermediary.CURR_SCHED, Integer.toString(schedNum)); //TODO: edit communication to handle types other than String to avoid type conversions
						Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHED_SCREEN);
					}
					
				}
			}
		};
					
		designTwoColorBorder(rS, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		
		addHeaderTabs(rS);
		
		designBackedLabel(rS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, scheduleType + ":", width/2, height/6, width*8/21, height/10, 1, true);
		designReactiveButton(rS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back to Trip Select", width*5/6, height*5/6, width/9, height/15, 2, EVENT_GO_TO_SELECT_TRIP, true);
		designReactiveButton(rS, "create_schedulable", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New " + scheduleType + "!", width*4/30, height*3/20, width/7, height/12, 2, EVENT_GO_TO_CREATE_SCHEDULABLE, true);
		
		//displayItemList(rS, scheduleType, data, 0);		//Display list of stored reservations - start from page 0
		display.addPanel("Reservations", rS);
		/**
		 * below used to be a helper method, but needed to be called recursively to go forward or back to more schedulables
		 * So now it's one really big method and the behaviour can be set appropriately and pagecount can be accessed easily
		 */
		designTwoColorBorder(rS, "border_in", COLOR_WHITE, COLOR_BLACK, width/6, height /4, width*2/3, height/2, 50, 30, 1, false);
		int scheds;// the number of schedulables that will be displayed
		if((data.keySet().size() - pagenum*MAX_VIS_LIST_ITEMS) > MAX_VIS_LIST_ITEMS)//if there are too many of this thing to fit on the page
		{
			scheds = MAX_VIS_LIST_ITEMS;//present the maximum number we're able to show with the current window style (currently 3)
		}
		else//otherwise
		{
			scheds = data.keySet().size() - (pagenum*MAX_VIS_LIST_ITEMS);//just show all the schedulables here
		}
		
		int itemnum;//variable to keep track of which schedulable in the chronological order this one is
		
		if(data.keySet().size()!=0)// so long as there actually are schedulables to print
		{
			for(int i = 0; i < scheds; i++) //show all the schedulables for the page with correct placement and info
			{
				itemnum = (MAX_VIS_LIST_ITEMS*pagenum) + i;
				
				DisplayData map = data.get(key.get(itemnum));
				designReactiveButton(rS, "trip_"+itemnum, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, map.getData("Name"), width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_ITEM+itemnum, true);
				rS.addText("trip_desc_"+itemnum, 35, width/2 + width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, map.getData("Description"), FONT_ENTRY, true);
				rS.addText("trip_date_"+itemnum, 35, width/2 - width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, map.getData("Start Date") + " - " + map.getData("End Date"), FONT_ENTRY, true);
			}
		}
		
		
		if(pagenum!=0)
		{
			designReactiveButton(rS, "prev", COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, "Previous", 2*width/5, height * 10 / 12, width/12, height/20, 4, EVENT_PREV_PAGE, true);
		}
		if((MAX_VIS_LIST_ITEMS*pagenum + scheds)<data.keySet().size())
		{
			designReactiveButton(rS, "next", COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, "Next", 3*width/5, height * 10 / 12, width/12, height/20, 3, EVENT_NEXT_PAGE, true);
			
		}
		
	}
	
	
	/**
	 * A pretty ugly method to let us edit existing schedulables - still needs work
	 * problems attempting to write to text boxes, going to go back and try to refamiliarize with the
	 * GUI assets to make it work
	 * 
	 * @param typeData - a hashmap holding the specifics type of schedulable on display
	 * @param specifics - a hashmap with the specifics of schedulables of that type
	 * @param schedNum - which schedulable in that list is to be displayed
	 */
	public void schedScreen(HashMap<String, String> typeData, HashMap<String, DisplayData> specifics, int schedNum) {
		ArrayList<String> key = new ArrayList<String>(specifics.keySet());
		
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_SELECT_SCHEDULABLE){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHEDULABLE_SELECT);
				}
				else if(event == EVENT_ATTEMPT_CREATE_SCHEDULABLE){
					//insert deletion code here
					Communication.set(Intermediary.CURR_DELETE_SCHED, key.get(0));
					String header = Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE);
						
					String[] titles = Communication.get(Intermediary.CURR_SCHEDULABLE_TITLES).split("   ");
					
					for(int i = 0; i < titles.length; i++) {
						Communication.set(header + "_" + titles[i], getElementStoredText(header + "_" + titles[i] + "_text"));
					}
					Communication.set(Intermediary.CURR_DELETE_SCHED, key.get(0));
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_REPLACE_SCHED);
					//Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_SCHEDULABLE_CREATE);
				}
				else if(event == EVENT_ATTEMPT_DELETE_SCHEDULABLE)
				{
					specifics.remove(schedNum);
					
					Communication.set(Intermediary.CURR_DELETE_SCHED, key.get(0));
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_DELETE_SCHED);
				}
				else if(event==EVENT_GO_TO_ITEM)
				{
					System.out.println("here");
				}
				
			}
		};
		
		
		designTwoColorBorder(mR, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter " + Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE) + " Details", width/2, height/8, width*2/3, height/10, 1, true);
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_GO_TO_SELECT_SCHEDULABLE, true);
		designReactiveButton(mR, "edit_schedulable", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*13/16, width/8, height/15, 2, EVENT_ATTEMPT_CREATE_SCHEDULABLE, true);
		designReactiveButton(mR, "delete_schedulable", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Delete", width/3, height*13/16, width/8, height/15, 2, EVENT_ATTEMPT_DELETE_SCHEDULABLE, true);
		
		ArrayList<String> titles = new ArrayList<String>(typeData.keySet());
		String header = Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE);
		int singleSize = 0, doubleSize = 0;
		boolean date = false;
		
		for(String s : titles) {
			switch(typeData.get(s)) {
				case "sString": singleSize++; break;
				case "lString": doubleSize++; break;
				case "Date": singleSize++; date = true; break;
				default: break;
			}
		}
		
		int columns = (int)(Math.sqrt(singleSize + doubleSize*2) + .5);
		int count = 0;
		top:
		for(int i = 0; i < columns+2; i++) {
			for(int j = 0; j < columns; j++) {
				if(count == titles.size()) {
					break top;
				}
				String s = titles.get(count);
				int mult = 1;
				if(typeData.get(s).equals("lString")) {
					if(j + 1 >= columns + 1) {
						i++;
						j = -1;
					}
					mult++;
				}
				
				DisplayData detailMap = specifics.get(key.get(schedNum));
				
				int across = (int)((double)(i + 1) / (double)(columns + 2) * width);
				int down = height * 11 / 30 + height * j / (columns + 2);
				int wid = width/(columns + 3);
				int hei = height/14 * mult;
				int heiLabel = height/14;
				designTextField(mR, header+"_"+s, detailMap.getData(s), across, down, wid, hei, 2, 1000 + i * columns + j, true);
				//designReactiveButton(mR, "existing_"+typeData.get(s), COLOR_WHITE, COLOR_BLACK, FONT_ENTRY, detailMap.getData(s), across, down, wid, hei, 2, EVENT_GO_TO_ITEM, true);
				//
				//header+"_"+s.addText(header + "_" + detailMap.getData(header) + "_text");
				
				designBackedLabel(mR, s + "_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, s, across, down - height/10, wid, (int)(heiLabel*.9), 3, true);
				count++;
				if(typeData.get(s).equals("lString"))
					j++;
			}
		}
		
		if(date) {
			designBackedLabel(mR, "label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "dd/MM/yyyy", width/6, height*13/16, width/8, height/15, 3, true);
		}
		
		display.addPanel("Res Creation", mR);
		
		
	}
	
	/**
	 * This method designs the screen that facilitates the creation of a new Schedulable object by
	 * providing the user with a series of text fields to fill, each attributed to a label for the
	 * kind of data they should be providing.
	 * 
	 * Can return to Schedulable Select screen from here.
	 * 
	 * @param data - HashMap<<r>String, String> object containing the data fields and their types for making the current Schedulable Object
	 */

	public void makeSchedulableScreen(HashMap<String, String> data) {
		
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_SELECT_SCHEDULABLE){
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHEDULABLE_SELECT);
				}
				else if(event == EVENT_ATTEMPT_CREATE_SCHEDULABLE){
					String header = Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE);

					String[] titles = Communication.get(Intermediary.CURR_SCHEDULABLE_TITLES).split("   ");
					
//					System.out.println(Arrays.toString(titles));
					
					for(int i = 0; i < titles.length; i++) {
			
						Communication.set(header + "_" + titles[i], getElementStoredText(header + "_" + titles[i] + "_text"));
					}
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_SCHEDULABLE_CREATE);
				}
				
			}
		};
		
		designTwoColorBorder(mR, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter " + Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE) + " Details", width/2, height/8, width*2/3, height/10, 1, true);
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_GO_TO_SELECT_SCHEDULABLE, true);
		designReactiveButton(mR, "create_schedulable", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*13/16, width/8, height/15, 2, EVENT_ATTEMPT_CREATE_SCHEDULABLE, true);
				
		ArrayList<String> titles = new ArrayList<String>(data.keySet());
		String header = Communication.get(Intermediary.CURR_SCHEDULABLE_TYPE);
		
		int singleSize = 0, doubleSize = 0;
		boolean date = false;
		
		for(String s : titles) {
			switch(data.get(s)) {
				case "sString": singleSize++; break;
				case "lString": doubleSize++; break;
				case "Date": singleSize++; date = true; break;
				default: break;
			}
		}
		
		int columns = (int)(Math.sqrt(singleSize + doubleSize*2) + .5);
		int count = 0;
		top:
		for(int i = 0; i < columns+2; i++) {
			for(int j = 0; j < columns; j++) {
				if(count == titles.size()) {
					break top;
				}
				String s = titles.get(count);
				int mult = 1;
				if(data.get(s).equals("lString")) {
					if(j + 1 >= columns + 1) {
						i++;
						j = -1;
					}
					mult++;
				}
				int across = (int)((double)(i + 1) / (double)(columns + 2) * width);
				int down = height * 11 / 30 + height * j / (columns + 2);
				int wid = width/(columns + 3);
				int hei = height/14 * mult;
				int heiLabel = height/14;
	
				designTextField(mR, header+"_"+s, "", across, down, wid, hei, 2, 1000 + i * columns + j, true);
				designBackedLabel(mR, s + "_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, s, across, down - height/10, wid, (int)(heiLabel*.9), 3, true);
				count++;
				if(data.get(s).equals("lString"))
					j++;
			}
		}
		
		if(date) {
			designBackedLabel(mR, "label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "dd/MM/yyyy", width/6, height*13/16, width/8, height/15, 3, true);
		}
		
		display.addPanel("Res Creation", mR);
	}

	/**
	 * 	Screen for schedulable archetype creation
	 */
	public void scheduleArcTypeCreateScreen() {
		
		ElementPanel screen = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_MAIN) { //when going back
					Intermediary.NEW_SCHED_ARC_HEADER = "";
					Intermediary.NEW_SCHED_ARC_FIELDS = Intermediary.DEFAULT_NEW_SCHED_ARC_FIELDS;
					Intermediary.NEW_SCHED_ARC_TYPES = Intermediary.DEFAULT_NEW_SCHED_ARC_TYPES;;
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_MAIN_SCREEN);
				}else if (event == EVENT_ATTEMPT_CREATE_NEW_SCHED_ARC_TYPE) {
					
					Intermediary.NEW_SCHED_ARC_HEADER = getElementStoredText("name_entry_text");
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_SHEDULABLE_ARC_CREATE);
					
				}else if (event == EVENT_ADD_FIELD_TO_NEW_SCHED_ARC_TYPE) {
					JFrame jf = new JFrame();
					jf.setAlwaysOnTop(true);
					String new_field_name = JOptionPane.showInputDialog(jf, "Enter new Field Name: ");
					if(new_field_name != null) { //only continue if there wasnt a cancel
						String test = new_field_name;
						if(test.replaceAll(" ", "").length() > 0) {		//if not all spaces
							String new_field_type_ind_str = JOptionPane.showInputDialog(jf, "Enter Number of desired Field type: \n \t 1) String \n \t 2) Date \n");
							test = new_field_type_ind_str;
							if(new_field_type_ind_str != null && test.replaceAll(" ", "").length() > 0) {		//if the number isnt null or all spaces
								int field_index = 0;
								try {
									field_index = Integer.parseInt(new_field_type_ind_str);
								}catch(Exception e) {
									errorBox("Invalid field type selection");
								}
								if(field_index != 1 && field_index != 2) {
									errorBox("Invalid field type selection");
								}else { //if a valid selection
									
									String[] curr_field_names =  Intermediary.NEW_SCHED_ARC_FIELDS;
									String[] curr_field_types =  Intermediary.NEW_SCHED_ARC_TYPES;
									
									String new_field_type = field_index == 1? "sString" : "Date";
									String[] newFields = new String[curr_field_names.length + 1];
									String[] newFieldTypes = new String[curr_field_names.length + 1];
									for(int i = 0; i < curr_field_names.length; i++) {
										newFields[i] = curr_field_names[i];
										newFieldTypes[i] = curr_field_types[i];
									}
									newFields[newFields.length - 1] = new_field_name;
									newFieldTypes[newFieldTypes.length - 1] = new_field_type;
									String curr_header = getElementStoredText("name_entry_text");
								
									Intermediary.NEW_SCHED_ARC_HEADER = curr_header.replaceAll(" ", "");
									Intermediary.NEW_SCHED_ARC_FIELDS = newFields;
									Intermediary.NEW_SCHED_ARC_TYPES = newFieldTypes;
									
									Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCREEN_SHEDULABLE_ARC_CREATE);
								}
							}else {
								errorBox("Invalid field type selection");
							}
						}
					}
				}
			}	
		};
		int name_code = 500;
		int box_w = 300;
		int box_h = 200;
		//background
		designTwoColorBorder(screen, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		//title
		designBackedLabel(screen, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Create a Schedulable", width/2, height/8, width/2, height/10, 1, true);
		//back button
		designReactiveButton(screen, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_GO_TO_MAIN, true);
		//name label
		designBackedLabel(screen, "name_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "Name", 200, 175, 100, 40, 3, true);
		//name text entry
		designTextField(screen, "name_entry", Intermediary.NEW_SCHED_ARC_HEADER, 285, 155, width/2, 40, 4, name_code, false);
		//fields label
		designBackedLabel(screen, "fields_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, "Fields", width/2, height/2-50, 100, 40, 5, true);
		//fields empty box
		screen.addRectangle("fields_rect", 6, width/2, height/2 + box_h/2, box_w, box_h, COLOR_WHITE, COLOR_BLACK, true);
		//add field button
		screen.addRectangle("fields_btn_rect", 7, width/2 + 80, height/2-50, 40, 40, COLOR_LOGIN, COLOR_BLACK, true);
		screen.addRectangle("fields_btn_rect_1", 8, width/2 + 80, height/2-50, 5, 20, COLOR_WHITE, COLOR_WHITE, true);
		screen.addRectangle("fields_btn_rect_2", 9, width/2 + 80, height/2-50, 20, 5, COLOR_WHITE, COLOR_WHITE, true);
		screen.addButton("fields_btn", 10, width/2 + 80, height/2-50, 40, 40, EVENT_ADD_FIELD_TO_NEW_SCHED_ARC_TYPE, true);
		//filling in fields separators
		screen.addText("field_name", 11, width/2 - box_w/4, height/2 + 15, 100, 100, "Field Name", FONT_TAB, true);
		screen.addText("field_type", 12, width/2 + box_w/4, height/2 + 15, 100, 100, "Field Type", FONT_TAB, true);
		screen.addRectangle("fields_sep_vert", 13, width/2, height/2 + box_h/2, 3, box_h, COLOR_BLACK, COLOR_BLACK, true);
		screen.addRectangle("fields_sep_hor", 14, width/2, height/2 + 27 , box_w, 3, COLOR_BLACK, COLOR_BLACK, true);
		//fill in field box values
		
		String[] curr_field_names =  Intermediary.NEW_SCHED_ARC_FIELDS;
		String[] curr_field_types =  Intermediary.NEW_SCHED_ARC_TYPES;
		
		if(curr_field_names != null && curr_field_types != null) {
			int start_prior = 15;
			int start_y = height/2 + 45;
			int gap = 25;
			int x_left = width/2 - box_w/4;
			int x_right = width/2 + box_w/4;
			for(int i  = 0; i < curr_field_names.length; i++) {
				screen.addText("field_name_"+i, ++start_prior, x_left, start_y+gap*i, 100, 100, curr_field_names[i], FONT_TAB, true);
				screen.addText("field_type_"+i, ++start_prior, x_right, start_y+gap*i, 100, 100, curr_field_types[i], FONT_TAB, true);
			}
		}
		//submit button
		designReactiveButton(screen, "create_schedulable_arc_type", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", 130 , height-100, width/12, height/16, 1000, EVENT_ATTEMPT_CREATE_NEW_SCHED_ARC_TYPE, true);
		//add to display
		display.addPanel("Sched Arc Create Screen", screen);
	}
	
	/**
	 * This method handles the inclusion of a header-strip at the top of the screen which allows the
	 * user to transition to a new screen as described by that portion of the header.
	 * 
	 * It's a tab system for navigation.
	 * 
	 * @param e - ElementPanel object to which this header will be added on to
	 */
	
	public void addHeaderTabs(ElementPanel e){
		ArrayList<String> headers = new ArrayList<String>();
		headers.add(Intermediary.CONTROL_MAIN_SCREEN);
		headers.addAll(intermediary.getSchedulableTypeHeaders());
		
		for(int i = 0; i < headers.size(); i++) {
			int eve = i == 0 ? EVENT_GO_TO_MAIN : i;
			designReactiveButton(e, headers.get(i), COLOR_TWO, COLOR_BLACK, FONT_TAB, headers.get(i), i * width/headers.size() + width/headers.size()/2, height/34, width/headers.size() + width % headers.size(), height/17, 18, eve, true);
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
	 * @param event - int value representing the user's mouse input as a coded value to be interpreted
	 * @return - Returns a boolean value representing whether the event triggered a Control value to be assigned or not.
	 */
	
	public boolean interpretHeader(int event) {
		if(event == EVENT_GO_TO_MAIN){
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_MAIN_SCREEN);
			return true;
		}
		else if(event != -1 && event <= intermediary.getSchedulableTypeHeaders().size()){
			Communication.set(Intermediary.CURR_SCHEDULABLE_TYPE, intermediary.getSchedulableTypeHeaders().get(event-1));
			Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SCHEDULABLE_SELECT);
			return true;
		}
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
	
	private void designTextField(ElementPanel pan, String name, String defaultText, int x, int y, int panWid, int panHei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry(name + "_text", priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, panWid - 30, panHei - 5, code, FONT_ENTRY, defaultText, centered);	
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
	 * This method automates the composition of ElementPanel basic elements to create a two-colored
	 * rectangular region in which one color functions as a margin around the edge of the other.
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
	
	/**
	 * This method generates a small window frame that confirms the users intent
	 * 
	 * @param in - String object representing the message to be displayed to the user regarding the user's intent
	 * @param commandConfirm - the command you would like to send to intermediary if the user confirms
	 * @param commandConfirm - the command you would like to send to intermediary if the user does not confirm
	 */
	
	public void confirmBox(String in, String commandConfirm, String commandDeny) {
		int wid = 300;
		int hei = 500;
		WindowFrame confirm = new WindowFrame(wid, hei);
		confirm.setExitOnClose(false);
		ElementPanel pan = new ElementPanel(0, 0, wid, hei) {
			public void clickBehaviour(int event) {
				if(event == EVENT_CONFIRM) {
					System.out.println("confirm");
					Communication.set(Intermediary.CONTROL, commandConfirm);
					getParentFrame().remove();
				}
				else if (event == EVENT_DENY)
				{
					System.out.println("deny");
					Communication.set(Intermediary.CONTROL, commandDeny);
					getParentFrame().remove();
				}
			}
		};
		pan.addText("tex", 5, wid/2, hei/5, wid*9/10, hei*2/5, in, FONT_ONE, true);
		designReactiveButton(pan, "say_no", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "No", wid/2, hei/2, wid/2, hei/6, 2, EVENT_DENY, true);
		designReactiveButton(pan, "say_yes", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Yes", wid/2, hei*3/4, wid/2, hei/6, 2, EVENT_CONFIRM, true);
		confirm.add(pan);
	}
	
}
