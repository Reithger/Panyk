
package view;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Arrays;
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
 * Current contents are very tentative, design is not finalized, just getting the foot in the door.
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
	
	private final static Font FONT_TAB = new Font("Arial Bold", Font.BOLD, 12);
	
	private final static Font FONT_HEADER = new Font("Arial Bold", Font.BOLD, 28);
	
	private final static Font FONT_TITLE = new Font("Baskerville Old Face", Font.BOLD, 80);	//Vivaldi
	
	//------------------------------------------
	/** */
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	/** */
	private final static Color COLOR_LOGIN = new Color(102, 255, 102);
	/** */
	private final static Color COLOR_TWO = new Color(90, 80, 175);
	
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
	private final static int EVENT_GO_TO_TRIP = 17;//I think this has to be the maximum value, so arranging accordingly
	/** */
	private final static int EVENT_TRIP_SELECTION = 7;
	/** */
	private static final int EVENT_TRIP_CREATED = 8;
	/** */
	private static final int EVENT_GO_TO_RES_CREATION = 9;
	/** */
	private static final int EVENT_SAVE_RES = 10;
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
	

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private WindowFrame display;
	/** */
	private int width;
	/** */
	private int height;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Display type that assigns a provided width and height to the wholly
	 * encompassing WindowFrame object and instructs it to display the program's initial screen (log-in).
	 * 
	 * @param inWidth - int value representing the width of the WindowFrame that displays the program.
	 * @param inHeight - int value representing the height of the WindowFrame that displays the program.
	 */
	
	/*@Override
	public void start(Stage stage) throws Exception 
	{
	
	}*/
	
	public Display(int inWidth, int inHeight){
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width + 15, height + 38);	//offset because java windows aren't quite accurate
		initialScreen();
		
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method instructs the Display object to present the initial screen as designed
	 * within this method; effectively a title screen for branding purposes and possible
	 * settings allocation. Just click the continue button for log-in.
	 * 
	 */
	
	private void initialScreen(){
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_LOGIN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_LOGIN_SCREEN);
				}
			}	
		};
		
		//Make a composite for headers with consistent color usage; decide on a color scheme.
		designTwoColorBorder(titlePanel, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		
		designBackedLabel(titlePanel, "title", COLOR_TWO, COLOR_BLACK, FONT_TITLE, "Plein Air", width/2, height/3, width/2, height/6, 1, true);
		
		designReactiveButton(titlePanel, "cont", COLOR_WHITE, FONT_ENTRY, "Start", width/2, 7*height/10, width/10, height/20, 5, EVENT_GO_TO_LOGIN, true);

		display.addPanel("Title", titlePanel);
	}

	/**
	 * This method instructs the Display object to present the log-in screen as designed
	 * within this method; takes in Username and Password information to attempt to log the
	 * user in. If successful, moves onward to a Trip select screen.
	 */
	
	public void logInScreen() {
		ElementPanel login = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					String uname = this.getElementStoredText("text_username");
					String pass = this.getElementStoredText("text_password");
					Communication.set(Intermediary.LOGIN_USERNAME, uname);
					Communication.set(Intermediary.LOGIN_PASSWORD, pass);
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_LOGIN);
				}
				else if(event == EVENT_CREATE_ACC_BTN) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_USER_CREATE);
				}
			}
		};

		designTwoColorBorder(login, "border_1", COLOR_ONE, COLOR_THREE, 0, 0, width*2/3, height, 30, 30, 0, false);
		
		designBackedLabel(login, "login_", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Log In", width/3, height/4, width/2, height/4, 1, true);

		login.addText("tex2", 78,          width/6, height/2 + 40, width/6, height/12, "Username:", FONT_ONE, true);
		designTextField(login, "username", width/3, height/2 + 40, width/6, height/16, 10, 10001, true);	//username entry
		login.addText("tex3", 79,          width/6, height/2 + 130, width/6, height/12, "Password:", FONT_ONE, true);		
		designTextField(login, "password", width/3, height/2 + 130, width/6, height/16, 10, 10000, true);	//password entry
		//Log In Button
		designReactiveButton(login, "but1", COLOR_LOGIN, FONT_ENTRY, "Log In", width/3, height * 5 / 6, width/9, height/20, 1, EVENT_LOGIN, true);
		//Create Account Button
		designReactiveButton(login,"acc_create", COLOR_LOGIN, FONT_ENTRY, "Create Account", 5*width/6, height * 5 / 12, width/12, height/20, 2, EVENT_CREATE_ACC_BTN, true);		
		//add create a user on the side
		designTwoColorBorder(login, "border_2", COLOR_ONE, COLOR_THREE, width*2/3, 0, width/3, height, 30, 30, 0, false);
		
		designBackedLabel(login, "no_acc", COLOR_CREATE_ACC_BOX, COLOR_BLACK, FONT_ENTRY, "Don't have an account?", 5*width/6, height / 4, width/6, height/12, 1, true);
		
		display.addPanel("Login", login);
	}
	
	/**
	 * 
	 */
	
	public void createAccountScreen() {
		ElementPanel createAcc = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_CREATE_ACC_FINALIZE) {
					//create the user based on passed in information
					String fn    = this.getElementStoredText("text_firstname");
					String ln    = this.getElementStoredText("text_lastname");
					String uname = this.getElementStoredText("text_username");
					String pass  = this.getElementStoredText("text_password");
					
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
		//background
		
		designTwoColorBorder(createAcc, "background", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		//title
		designBackedLabel(createAcc, "acct_title", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Create your account", width/2, height/6, width/3, height/8, 1, true);
		//first name
		createAcc.addText("fn",              2, width/2, 150, 200, 100, "First name", FONT_ONE, true);
		designTextField(createAcc, "firstname", width/2, 180, 200, 30, 3, 10002, true);

		//last name
		createAcc.addText("ln",             4, width/2, 220, 200, 100, "Last name", FONT_ONE, true);
		designTextField(createAcc, "lastname", width/2, 250, 200, 30, 5, 10003, true);

		//username
		createAcc.addText("uname",          6, width/2, 290, 200, 100, "Username", FONT_ONE, true);
		designTextField(createAcc, "username", width/2, 320, 200, 30, 7, 10004, true);

		//password
		createAcc.addText("pass",           8, width/2, 360, 200, 100, "Password", FONT_ONE, true);
		designTextField(createAcc, "password", width/2, 390, 200, 30, 9, 10005, true);

		//create account button
		createAcc.addRectangle("but_rect", 10, width/2, height - 130, width/7, height/20,  COLOR_LOGIN , true);
		createAcc.addText("but_text",      11, width/2, height - 130, width, height/20 - 10, "Create my account!", FONT_ENTRY, true);
		createAcc.addButton("create_but",  12, width/2, height - 130, width/9, height/20, EVENT_CREATE_ACC_FINALIZE, true);
		//create a back button
		createAcc.addRectangle("btn_back_rect", 26, 50, height - 100, 90, 30,  COLOR_ERR , false);
		createAcc.addText("but_back_text",      27, 80, height - 95,  90, 30, "back", FONT_ENTRY, false);
		createAcc.addButton("back_btn",         28, 50, height - 100, 90, 30, EVENT_BACK_TO_LOGIN, false);
		
		display.addPanel("Create Account", createAcc);
	}

	/**
	 * 
	 */
	
	public void tripSelectScreen() {
		
		List<String[]> trips = Intermediary.getUsersTrips();
		
		ElementPanel tS = new ElementPanel(0, 0, width, height) {
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
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
					Communication.set(Intermediary.CURR_TRIP, trips.get(tripNum)[1]);
					
					
					System.out.println("go to trip " + tripNum);
				}
			}
		};
		//background
		tS.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
		//title of page
		tS.addRectangle("title_backround", 1, width/2, 60, width/3, 60, COLOR_WHITE, true);
		tS.addText("title",                100, width/2, 50, width, height/10, "Select Trip", FONT_TWO, true);
		//logout button
		tS.addRectangle("logout_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		tS.addText(     "logout_text", 3, width - 105, height - 95,  90, 30, "logout", FONT_ENTRY, false);
		tS.addButton(   "logout_btn",  4, width - 130, height - 100, 90, 30, EVENT_BACK_TO_LOGIN , false);
		//create trip button
		tS.addRectangle("create_trip_rect", 5, width - 150, 120, 130, 30,  		          COLOR_LOGIN , false);
		tS.addText(     "create_trip_text", 6, width - 135, 125, 120, 30, "Create a Trip!", FONT_ENTRY, false);
		tS.addButton(   "create_trip_btn",  7, width - 150, 120, 130, 30,   EVENT_GO_TO_TRIP_CREATION , false);
		int sideOffset = 200;
		int topOffset = height/3 - 30;
		int bottomOffset = 140;
		//background of main screen
		tS.addRectangle("backdrop_sel", 9, sideOffset, topOffset, width - 2*sideOffset, height - bottomOffset - topOffset, COLOR_WHITE, false);
		//adding trip buttons
		int tileInset = 10;
		int tileGap = 10;
		int tileHeight = 60;
		
		for(int i = 0; i < (trips == null ? 0 : trips.size()); i++) {
			//display the trips on screen
			tS.addRectangle("trip_rect_"+i, tileInset+i, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, COLOR_SEPARATOR, false);
			tS.addText("trip_title"+i,      tileInset+i+1, width/2, topOffset+tileInset+i*tileGap+i*tileHeight + 30, width/3, 50, trips.get(i)[1], FONT_ONE, true);
			tS.addText("trip_desc"+i, tileInset+i+2, width - sideOffset - tileInset - 50, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, trips.get(i)[2], FONT_ENTRY, false);
			tS.addText("trip_desc2_"+i, tileInset+i+4, sideOffset + tileInset + 30, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, trips.get(i)[3] + " - " + trips.get(i)[4], FONT_ENTRY, false);
			tS.addButton("go_to_trp_btn"+i, tileInset+i+3, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, EVENT_GO_TO_TRIP+i, false);
		}

		display.addPanel("Trip Select", tS);
	}
	/**
	 * create standard tabs in elementpanel e
	 * @param e
	 */
	public void doTabs(ElementPanel e)
	{
		//background
		e.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
				
		//tabs
		e.addRectangle("tabpane", 10, 0, 0, width, 30, COLOR_WHITE, false);
				
		e.addRectangle("hometab", 11, 0, 0, width/6, 30, COLOR_TWO, false);
		e.addText("home_label", 12, 10, 5,  90, 30, "Home", FONT_TAB, false);
				//add main tab link here when it exists
				
		e.addRectangle("restab", 13, width/6, 0, width/6, 30, COLOR_THREE, false);
		e.addText("res_label", 14, width/6 + 10, 5,  90, 30, "Reservations", FONT_TAB, false);
		e.addButton("res_btn",  15, width/6, 0, width/6, 30, EVENT_RES_LIST , false);
				
		e.addRectangle("accomtab", 16, 2*width/6, 0, width/6, 30, COLOR_TWO, false);
		e.addText("accom_label", 17, 2*width/6 + 10, 5,  120, 30, "Accommodations", FONT_TAB, false);
		e.addButton("accom_btn",  15, 2*width/6, 0, width/6, 30, EVENT_ACCOM_LIST, false);
		
		e.addRectangle("ttab", 16, 3*width/6, 0, width/6, 30, COLOR_THREE, false);
		e.addText("t_label", 17, 3*width/6 + 10, 5,  120, 30, "Transportation", FONT_TAB, false);
		e.addButton("t_btn",  15, 3*width/6, 0, width/6, 30, EVENT_TRANSPORT_LIST, false);
	}
	
	public void doList(ElementPanel e, List<String[]> sl, int titlePos, int startPos, int endPos, int otherPos)
	{
		int sideOffset = 200;
		int topOffset = height/3 - 30;
		int bottomOffset = 140;
		
		e.addRectangle("backdrop_sel", 9, sideOffset, topOffset, width - 2*sideOffset, height - bottomOffset - topOffset, COLOR_WHITE, false);
		
		int tileInset = 10;
		int tileGap = 10;
		int tileHeight = 60;
		
		for(int i = 0; i < (sl == null ? 0 : sl.size()); i++) {
			//display the items on screen
			e.addRectangle("ls_rect_"+i, tileInset+i, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, COLOR_SEPARATOR, false);
			e.addText("ls_title"+i,      tileInset+i+1, width/2, topOffset+tileInset+i*tileGap+i*tileHeight + 30, width/3, 50, sl.get(i)[titlePos], FONT_ONE, true);
			e.addText("ls_desc"+i, tileInset+i+2, width - sideOffset - tileInset - 150, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, sl.get(i)[otherPos], FONT_ENTRY, false);
			e.addText("ls_desc2_"+i, tileInset+i+4, sideOffset + tileInset + 30, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, sl.get(i)[startPos] + " - " + sl.get(i)[endPos], FONT_ENTRY, false);
			//e.addButton("go_to_trp_btn"+i, tileInset+i+3, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, EVENT_GO_TO_RES_CREATION, false);
		}
	}
	
	//----------------------------------------------------------------------------lists-----------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * A screen to display all reservations
	 */

	public void reservationScreen(String tripName) {
		
		List<String[]> res = Intermediary.getTripsRes();
		
		ElementPanel rS = new ElementPanel(0, 0, width, height) 
		{
			
			public void clickBehaviour(int event) {
				if(event == EVENT_TRIP_SELECTION) 
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_GO_TO_RES_CREATION) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RES_CREATION);
				}
				else if(event == EVENT_ACCOM_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_LIST);
				}
				else if(event==EVENT_TRANSPORT_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
				}
			}
		};
		
		
		doTabs(rS);
		
		
		//title of page
		rS.addRectangle("title_backround", 1, width/2, 90, width/3, 60, COLOR_WHITE, true);
		rS.addText("title",                100, width/2, 85, width, height/10, "Reservations:", FONT_TWO, true);
		//exit button
		rS.addRectangle("exit_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		rS.addText(     "exit_text", 3, width - 105, height - 95,  90, 30, "back", FONT_ENTRY, false);
		rS.addButton(   "exit_btn",  4, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
		//create res button
		rS.addRectangle("create_res_rect", 5, width - 150, 120, 120, 50,  		          COLOR_LOGIN , false);
		rS.addText(     "create_res_text", 6, width - 140, 125, 150, 50, "Create a new reservation!", FONT_ENTRY, false);
		rS.addButton(   "create_res_btn",  7, width - 150, 120, 120, 50,   EVENT_GO_TO_RES_CREATION , false);
		
		
		//adding res buttons
		
		
		
		doList(rS, res, 2, 3, 4, 5);
		

		display.addPanel("Reservations", rS);
		
	}
	
	/**
	 * 
	 * @param tripName
	 */
public void accomScreen(String tripName) {
		
		List<String[]> accom = Intermediary.getTripsAccom();
		
		ElementPanel aS = new ElementPanel(0, 0, width, height) 
		{
			
			public void clickBehaviour(int event) {
				if(event == EVENT_TRIP_SELECTION) 
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_RES_LIST) {
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);//fix this later-------------------------------------------------------------------------------------------------
				}
				else if(event == EVENT_GO_TO_ACCOM_CREATION)
				{
					//System.out.println("here");
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_CREATE);
				}
				else if(event==EVENT_TRANSPORT_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
				}
			}
		};
		
		
		doTabs(aS);
		
		
		//title of page
		aS.addRectangle("title_backround", 1, width/2, 90, width/3, 60, COLOR_WHITE, true);
		aS.addText("title",                100, width/2, 85, width, height/10, "Accomodations:", FONT_TWO, true);
		//exit button
		aS.addRectangle("exit_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		aS.addText(     "exit_text", 3, width - 105, height - 95,  90, 30, "back", FONT_ENTRY, false);
		aS.addButton(   "exit_btn",  4, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
		//create res button
		aS.addRectangle("create_res_rect", 5, width - 150, 120, 120, 50,  		          COLOR_LOGIN , false);
		aS.addText(     "create_res_text", 6, width - 140, 125, 150, 50, "new accomodation!", FONT_ENTRY, false);
		aS.addButton(   "create_res_btn",  7, width - 150, 120, 120, 50,   EVENT_GO_TO_ACCOM_CREATION , false);
		
		
		//adding res buttons
		
		
		
		doList(aS, accom, 3, 4, 5, 7);
		

		display.addPanel("Reservations", aS);
		
	}

public void transportScreen(String tripName) {
	
	List<String[]> transp = Intermediary.getTripsTransp();
	
	ElementPanel tS = new ElementPanel(0, 0, width, height) 
	{
		
		public void clickBehaviour(int event) {
			if(event == EVENT_TRIP_SELECTION) 
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
			}
			else if(event == EVENT_RES_LIST) {
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
			}
			else if(event == EVENT_ACCOM_LIST)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_LIST);
			}
			else if(event == EVENT_GO_TO_TRANSP_CREATION)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_CREATE);
			}
		}
	};
	
	
	doTabs(tS);
	
	
	//title of page
	tS.addRectangle("title_backround", 1, width/2, 90, width/3, 60, COLOR_WHITE, true);
	tS.addText("title",                100, width/2, 85, width, height/10, "Transportation:", FONT_TWO, true);
	//exit button
	tS.addRectangle("exit_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
	tS.addText(     "exit_text", 3, width - 105, height - 95,  90, 30, "back", FONT_ENTRY, false);
	tS.addButton(   "exit_btn",  4, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
	//create res button
	tS.addRectangle("create_res_rect", 5, width - 150, 120, 120, 50,  		          COLOR_LOGIN , false);
	tS.addText(     "create_res_text", 6, width - 140, 125, 150, 50, "new transportation!", FONT_ENTRY, false);
	tS.addButton(   "create_res_btn",  7, width - 150, 120, 120, 50,   EVENT_GO_TO_TRANSP_CREATION, false);
	
	
	//adding res buttons
	
	
	//transportation("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "startTime", "varchar(60)", "endTime", "varchar(60)")-----------------------confirm later
	doList(tS, transp, 2, 3, 4, 5);
	

	display.addPanel("Reservations", tS);
	
}

	/**
	 * 
	 */

	public void tripCreationScreen()
	{
		ElementPanel tC = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRIP_SELECTION) 
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_TRIP_CREATED) 
				{
					String title = this.getElementStoredText("text_tripTitle");//actual titles here have to be prefaced with text_ for some reason
					String date1 = this.getElementStoredText("text_tripStart");
					String date2 = this.getElementStoredText("text_tripEnd");
					String comments = this.getElementStoredText("text_tripNotes");
					String dest = this.getElementStoredText("text_tripDest");
					
					Communication.set(Intermediary.CREATE_TRIP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRIP_START, date1);
					Communication.set(Intermediary.CREATE_TRIP_END, date2);
					Communication.set(Intermediary.CREATE_TRIP_DEST, dest);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_CREATE_TRIP);
					
				}
				
			}
		};
		
		
		//background
		tC.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
		
		//title of page
		tC.addRectangle("title_backround", 1, width/2, 60, (2*width)/3, 60, COLOR_WHITE, true);
		tC.addText("title", 120, width/2, 50, width, height/10, "Enter Trip Details", FONT_TWO, true);
		
		//cancel button
		tC.addRectangle("cancel_rect", 3, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		tC.addText(     "cancel_text", 4, width - 105, height - 95,  90, 30, "Exit", FONT_ENTRY, false);
		tC.addButton(   "cancel_btn",  1, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
		
		//create trip button
		tC.addRectangle("create_trip_rect", 5, width - 150, 120, 120, 30,  		          COLOR_LOGIN , false);
		tC.addText(     "create_trip_text", 6, width - 135, 125, 120, 30, "Submit", FONT_ENTRY, false);
		tC.addButton(   "create_trip_btn",  2, width - 150, 120, 120, 30,   EVENT_TRIP_CREATED , false);
		int sideOffset = 200;
		int topOffset = height/3 - 30;
		int bottomOffset = 140;
		
		//adding trip info fields
		designTextField(tC, "tripTitle", width/4, height/4 + 40, width/6, height/12, 10, 10001, true);
		tC.addText("name", 78,          width/4, height/4 - 10, width/6, height/12, "Trip Name:", FONT_ONE, true);
		
		designTextField(tC, "tripDest", width/2+50, height/4 + 40, width/6, height/12, 10, 10000, true);
		tC.addText("Destination", 79,          width/2+50, height/4 - 10, width/6, height/12, "Destination:", FONT_ONE, true);
		
		designTextField(tC, "tripStart", width/4, height/2 + 40, width/6, height/12, 10, 10002, true);
		tC.addText("begin", 77,          width/4, height/2 - 10, width/3, height/12, "Start Date (dd/MM/yyyy):", FONT_ONE, true);
		
		designTextField(tC, "tripEnd", width/2+50, height/2 + 40, width/6, height/12, 10, 10003, true);
		tC.addText("cease", 76,          width/2+50, height/2 - 10, width/3, height/12, "End Date (dd/MM/yyyy):", FONT_ONE, true);
		

		display.addPanel("Trip Creation", tC);
		
		
	}
	
	public void makeAccomScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_RES_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
				}
				else if(event == EVENT_SAVE_RES) 
				{
					String title = this.getElementStoredText("text_accomTitle");
					String date1 = this.getElementStoredText("text_accomStart");
					String date2 = this.getElementStoredText("text_accomEnd");
					String loc = this.getElementStoredText("text_accomLoc");
					
					
					Communication.set(Intermediary.CREATE_ACCOM_TITLE, title);
					Communication.set(Intermediary.CREATE_ACCOM_START, date1);
					Communication.set(Intermediary.CREATE_ACCOM_END, date2);
					Communication.set(Intermediary.CREATE_ACCOM_LOC, loc);
					
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_ACCOM);
					
					
				}
				
			}
		};
		
		//background
		mR.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
		
		//title of page
		
		mR.addRectangle("title_backround", 1, width/2, 60, (2*width)/3, 60, COLOR_WHITE, true);
		mR.addText("title", 120, width/2, 50, width, height/10, "Enter Reservation Details", FONT_TWO, true);
		
		
		//cancel button
		mR.addRectangle("cancel_rect", 3, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		mR.addText(     "cancel_text", 4, width - 105, height - 95,  90, 30, "Exit", FONT_ENTRY, false);
		mR.addButton(   "cancel_btn",  1, width - 130, height - 100, 90, 30, EVENT_RES_LIST , false);
		
		
		//create trip button
		mR.addRectangle("create_trip_rect", 5, width - 150, 120, 120, 30,  		          COLOR_LOGIN , false);
		mR.addText(     "create_trip_text", 6, width - 135, 125, 120, 30, "Submit", FONT_ENTRY, false);
		mR.addButton(   "create_trip_btn",  2, width - 150, 120, 120, 30,   EVENT_SAVE_RES , false);
		
		
		//adding trip info fields
		designTextField(mR, "accomTitle", width/4, height/4 + 40, width/6, height/12, 10, 10001, true);
		mR.addText("name", 78,          width/4, height/4, width/4, height/12, "Reservation Name:", FONT_ONE, true);
		
		//here be glitch
		
		designTextField(mR, "accomLoc", width/2+50, height/4 + 40, width/6, height/12, 10, 10000, true);
		mR.addText("Location", 79,          width/2+50, height/4, width/6, height/12, "Address:", FONT_ONE, true);
		
		
		
		designTextField(mR, "accomStart", width/4, height/2 + 40, width/6, height/12, 10, 10002, true);
		mR.addText("begin", 77,          width/4, height/2, width/3, height/12, "Start Date (dd/MM/yyyy):", FONT_ONE, true);
		
		
		
		designTextField(mR, "accomEnd", width/2+50, height/2 + 40, width/6, height/12, 10, 10003, true);
		mR.addText("cease", 76,          width/2+50, height/2, width/3, height/12, "End Date (dd/MM/yyyy):", FONT_ONE, true);
		
		

		display.addPanel("Res Creation", mR);
		
		
	}
	
	//---------------------------------------------------------------------------------------------------------------------------------------edit here
	public void makeTransportScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRANSPORT_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
				}
				else if(event == EVENT_SAVE_TRANSP) 
				{
					String title = this.getElementStoredText("text_transpTitle");
					String date1 = this.getElementStoredText("text_transpStart");
					String date2 = this.getElementStoredText("text_transpEnd");
					String mode = this.getElementStoredText("text_transpMode");
					
					
					Communication.set(Intermediary.CREATE_TRANSP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRANSP_START, date1);
					Communication.set(Intermediary.CREATE_TRANSP_END, date2);
					Communication.set(Intermediary.CREATE_TRANSP_MODE, mode);
					
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_TRANSP);
					
					
				}
				
			}
		};
		
		//background
		mR.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
		
		//title of page
		
		mR.addRectangle("title_backround", 1, width/2, 60, (2*width)/3, 60, COLOR_WHITE, true);
		mR.addText("title", 120, width/2, 50, width, height/10, "Enter Transportation Details", FONT_TWO, true);
		
		
		//cancel button
		mR.addRectangle("cancel_rect", 3, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		mR.addText(     "cancel_text", 4, width - 105, height - 95,  90, 30, "Exit", FONT_ENTRY, false);
		mR.addButton(   "cancel_btn",  1, width - 130, height - 100, 90, 30, EVENT_TRANSPORT_LIST , false);
		
		
		//create trip button
		mR.addRectangle("create_transp_rect", 5, width - 150, 120, 120, 30,  		          COLOR_LOGIN , false);
		mR.addText(     "create_transp_text", 6, width - 135, 125, 120, 30, "Submit", FONT_ENTRY, false);
		mR.addButton(   "create_transp_btn",  2, width - 150, 120, 120, 30,   EVENT_SAVE_TRANSP , false);
		
		
		//adding trip info fields
		designTextField(mR, "transpTitle", width/4, height/4 + 40, width/6, height/12, 10, 10001, true);
		mR.addText("name", 78,          width/4, height/4, width/4, height/12, "Title:", FONT_ONE, true);
		
		//here be glitch
		
		designTextField(mR, "transpMode", width/2+50, height/4 + 40, width/6, height/12, 10, 10000, true);
		mR.addText("Location", 79,          width/2+50, height/4, width/6, height/12, "Mode of Transportation:", FONT_ONE, true);
		
		
		
		designTextField(mR, "transpStart", width/4, height/2 + 40, width/6, height/12, 10, 10002, true);
		mR.addText("begin", 77,          width/4, height/2, width/3, height/12, "Start Date (dd/MM/yyyy):", FONT_ONE, true);
		
		
		
		designTextField(mR, "transpEnd", width/2+50, height/2 + 40, width/6, height/12, 10, 10003, true);
		mR.addText("cease", 76,          width/2+50, height/2, width/3, height/12, "End Date (dd/MM/yyyy):", FONT_ONE, true);
		
		

		display.addPanel("transp Creation", mR);
		
		
	}
	

	public void makeResScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_RES_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
				}
				else if(event == EVENT_SAVE_RES) 
				{
					String title = this.getElementStoredText("text_resTitle");
					String date1 = this.getElementStoredText("text_resStart");
					String date2 = this.getElementStoredText("text_resEnd");
					String loc = this.getElementStoredText("text_resLoc");
					
					
					Communication.set(Intermediary.CREATE_RES_TITLE, title);
					Communication.set(Intermediary.CREATE_RES_START, date1);
					Communication.set(Intermediary.CREATE_RES_END, date2);
					Communication.set(Intermediary.CREATE_RES_LOC, loc);
					
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_RES);
					
					
				}
				
			}
		};
		
		//background
		mR.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
		
		//title of page
		
		mR.addRectangle("title_backround", 1, width/2, 60, (2*width)/3, 60, COLOR_WHITE, true);
		mR.addText("title", 120, width/2, 50, width, height/10, "Enter Reservation Details", FONT_TWO, true);
		
		
		//cancel button
		mR.addRectangle("cancel_rect", 3, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
		mR.addText(     "cancel_text", 4, width - 105, height - 95,  90, 30, "Exit", FONT_ENTRY, false);
		mR.addButton(   "cancel_btn",  1, width - 130, height - 100, 90, 30, EVENT_RES_LIST , false);
		
		
		//create trip button
		mR.addRectangle("create_trip_rect", 5, width - 150, 120, 120, 30,  		          COLOR_LOGIN , false);
		mR.addText(     "create_trip_text", 6, width - 135, 125, 120, 30, "Submit", FONT_ENTRY, false);
		mR.addButton(   "create_trip_btn",  2, width - 150, 120, 120, 30,   EVENT_SAVE_RES , false);
		
		
		//adding trip info fields
		designTextField(mR, "resTitle", width/4, height/4 + 40, width/6, height/12, 10, 10001, true);
		mR.addText("name", 78,          width/4, height/4, width/4, height/12, "Reservation Name:", FONT_ONE, true);
		
		//here be glitch
		
		designTextField(mR, "resLoc", width/2+50, height/4 + 40, width/6, height/12, 10, 10000, true);
		mR.addText("Location", 79,          width/2+50, height/4, width/6, height/12, "Address:", FONT_ONE, true);
		
		
		
		designTextField(mR, "resStart", width/4, height/2 + 40, width/6, height/12, 10, 10002, true);
		mR.addText("begin", 77,          width/4, height/2, width/3, height/12, "Start Date (dd/MM/yyyy):", FONT_ONE, true);
		
		
		
		designTextField(mR, "resEnd", width/2+50, height/2 + 40, width/6, height/12, 10, 10003, true);
		mR.addText("cease", 76,          width/2+50, height/2, width/3, height/12, "End Date (dd/MM/yyyy):", FONT_ONE, true);
		
		

		display.addPanel("Res Creation", mR);
		
		
	}
	
	/**
	 * 
	 */
	
	public void resetView() {
		display.hidePanels();
		
	}

//---  Composite Methods   --------------------------------------------------------------------
		
	/**
	 * This method automates the composite of ElementPanel basic elements to create a text-input
	 * box that corresponds to the provided code for receiving user input and using it for functions.
	 * 
	 * Priority intervals are of 10 so that each Composite method can have its own organization of
	 * basic elements as well as be placed relative to other Composite structures.
	 * 
	 * @param pan
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param priority
	 * @param code
	 */
	
	private void designTextField(ElementPanel pan,String name, int x, int y, int panWid, int panHei, int priority, int code, boolean centered) {
		pan.addRectangle("rect_" + name, priority * MAX_COMPOSITE_ELEMENTS, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry("text_" + name , priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, panWid, panHei, code, FONT_ENTRY, centered);	
	}
	
	private void designReactiveButton(ElementPanel pan, String name, Color col, Font font, String message, int x, int y, int wid, int hei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, col, centered);
		pan.addButton(name + "_but",     priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, wid, hei, code, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 2, x, y,  wid, hei, message, font, centered);
	}
	
	private void designBackedLabel(ElementPanel pan, String name, Color colFill, Color colBorder, Font font, String message, int x, int y, int wid, int hei, int priority, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, colFill, colBorder, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 1, x, y,  wid, hei, message, font, centered);
	}
	
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
