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
 * Current contents are very tentative, design is not finalized, just getting the foot in the door.
 * 
 * @author Mac Clevinger
 *
 */



/*public void makeContactScreen(){
	System.out.println("why am I here");
	ElementPanel mC = new ElementPanel(0, 0, width, height) {
		public void clickBehaviour(int event) {
			if(event == EVENT_CONTACT_LIST)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_LIST);
			}
			else if(event == EVENT_SAVE_CONTACT) 
			{
				String name = this.getElementStoredText("con_name_text");
				String descrip = this.getElementStoredText("con_descrip_text");
				String address = this.getElementStoredText("con_address_text");
				String phonum = this.getElementStoredText("con_phone_text");
				
				Communication.set(Intermediary.CREATE_CONTACT_NAME, name);
				Communication.set(Intermediary.CREATE_CONTACT_DESCRIP, descrip);
				Communication.set(Intermediary.CREATE_CONTACT_PHONE, phonum);
				Communication.set(Intermediary.CREATE_CONTACT_ADDRESS, address);
				
				
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_CONTACT);
				
				
			}
			
		}
	};
	
	//background
	mC.addRectangle("background", 0, 0, 0, width, height, COLOR_ONE, false);
	
	//title of page
	
	mC.addRectangle("title_backround", 1, width/2, 60, (2*width)/3, 60, COLOR_WHITE, true);
	mC.addText("title", 120, width/2, 50, width, height/10, "Enter Contact Details", FONT_TWO, true);
	
	
	//cancel button
	mC.addRectangle("cancel_rect", 3, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
	mC.addText(     "cancel_text", 4, width - 105, height - 95,  90, 30, "Exit", FONT_ENTRY, false);
	mC.addButton(   "cancel_btn",  1, width - 130, height - 100, 90, 30, EVENT_CONTACT_LIST , false);
	
	
	//create trip button
	mC.addRectangle("create_trip_rect", 5, width - 150, 120, 120, 30,  		          COLOR_LOGIN , false);
	mC.addText(     "create_trip_text", 6, width - 135, 125, 120, 30, "Submit", FONT_ENTRY, false);
	mC.addButton(   "create_trip_btn",  2, width - 150, 120, 120, 30,   EVENT_SAVE_RESERVATION , false);
	
	
	//adding trip info fields
	designTextField(mC, "con_name", width/4, height/4 + 40, width/6, height/12, 10, 10001, true);
	mC.addText("name", 78,          width/4, height/4 , width/4, height/12, "First Name:", FONT_ONE, true);
	
	System.out.println("test");
	
	designTextField(mC, "con_descrip", width/2+50, height/4 + 40, width/6, height/12, 10, 10000, true);
	mC.addText("details", 79,          width/2+50, height/4 , width/6, height/12, "Description:", FONT_ONE, true);
	
	
	
	designTextField(mC, "con_address", width/4, height/2 + 40, width/6, height/12, 10, 10002, true);
	mC.addText("lives_at", 77,          width/4, height/2 , width/3, height/12, "Address:", FONT_ONE, true);
	
	
	
	designTextField(mC, "con_phone", width/2+50, height/2 + 40, width/6, height/12, 10, 10003, true);
	mC.addText("employer", 76,          width/2+50, height/2 , width/3, height/12, "Phone:", FONT_ONE, true);
	
	
	System.out.println("test2");
	
	display.addPanel("Contact Creation", mC);
	
	System.out.println("test3");
}

/**
 * 
 */

//contacts("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "description", "varchar(60)", "phoneNumber", "varchar(60)", "address", "varchar(60)"),


/*public void contactScreen() {
	
	List<String[]> contacts = Intermediary.getTripsContacts();
	
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
			else if(event == EVENT_GO_TO_CONTACT_CREATION)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_CREATE);
			}
			else if(event==EVENT_TRANSPORT_LIST)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
			}
		}
	};
	
	
	addHeaderTabs(tS);
	
	
	//title of page
	tS.addRectangle("title_backround", 1, width/2, 90, width/3, 60, COLOR_WHITE, true);
	tS.addText("title",                100, width/2, 85, width, height/10, "Contacts:", FONT_TWO, true);
	//exit button
	tS.addRectangle("exit_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
	tS.addText(     "exit_text", 3, width - 105, height - 95,  90, 30, "back", FONT_ENTRY, false);
	tS.addButton(   "exit_btn",  4, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
	//create res button
	tS.addRectangle("create_res_rect", 5, width - 150, 120, 120, 50,  		          COLOR_LOGIN , false);
	tS.addText(     "create_res_text", 6, width - 140, 125, 150, 50, "new contact!", FONT_ENTRY, false);
	tS.addButton(   "create_res_btn",  7, width - 150, 120, 120, 50,   EVENT_GO_TO_TRANSP_CREATION, false);
	
	
	//adding res buttons
	
	
	//contacts("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "name", "varchar(60)", "description", "varchar(60)", "phoneNumber", "varchar(60)", "address", "varchar(60)"),
	displayItemList(tS, contacts, 3, 5, 6, 4);
	

	display.addPanel("contacts", tS);
	
}

/*public void contactScreen(String tripName) {
	
	System.out.println("contact screen");
	List<String[]> phonebook = Intermediary.getTripsContacts();
	
	ElementPanel cS = new ElementPanel(0, 0, width, height) 
	{
		
		public void clickBehaviour(int event) {
			System.out.println("clicked");
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
			else if(event==EVENT_TRANSPORT_LIST)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
			}
			else if(event == EVENT_GO_TO_CONTACT_CREATION)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_CREATE);
			}
		}
	};
	
	
	addHeaderTabs(cS);
	
	
	//title of page
	cS.addRectangle("title_backround", 1, width/2, 90, width/3, 60, COLOR_WHITE, true);
	cS.addText("title",                100, width/2, 85, width, height/10, "Contacts:", FONT_TWO, true);
	//exit button
	cS.addRectangle("exit_rect", 2, width - 130, height - 100, 90, 30,  		   COLOR_ERR , false);
	cS.addText(     "exit_text", 3, width - 105, height - 95,  90, 30, "back", FONT_ENTRY, false);
	cS.addButton(   "exit_btn",  4, width - 130, height - 100, 90, 30, EVENT_TRIP_SELECTION , false);
	//create contact button
	cS.addRectangle("create_contact_rect", 5, width - 150, 120, 120, 50,  		          COLOR_LOGIN , false);
	cS.addText(     "create_contact_text", 6, width - 140, 125, 150, 50, "new contact!", FONT_ENTRY, false);
	cS.addButton(   "create_contact_btn",  7, width - 150, 120, 120, 50,   EVENT_GO_TO_CONTACT_CREATION, false);
	
	
	int sideOffset = 200;
	int topOffset = height/3 - 30;
	int bottomOffset = 140;
	
	cS.addRectangle("backdrop_sel", 9, sideOffset, topOffset, width - 2*sideOffset, height - bottomOffset - topOffset, COLOR_WHITE, false);
	
	int tileInset = 10;
	int tileGap = 10;
	int tileHeight = 60;
	
	
	System.out.println("phonebook.size() is "+ phonebook.size());
	for(int i = 0; i < phonebook.size(); i++) 
	{
		System.out.println("in the for");
		//display the items on screen
		cS.addRectangle("ls_rect_"+i, tileInset+i, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, COLOR_SEPARATOR, false);
		cS.addText("ls_title"+i,      tileInset+i+1, width/2, topOffset+tileInset+i*tileGap+i*tileHeight + 30, width/3, 50, phonebook.get(i)[3]+" "+phonebook.get(i)[4], FONT_ONE, true);
		//cS.addText("ls_desc"+i, tileInset+i+2, width - sideOffset - tileInset - 150, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, phonebook.get(i)[7], FONT_ENTRY, false);
		cS.addText("ls_desc2_"+i, tileInset+i+4, sideOffset + tileInset + 30, topOffset+tileInset+i*tileGap+i*tileHeight + 40, width/3, 20, phonebook.get(i)[6] +" at "+ phonebook.get(i)[7], FONT_ENTRY, false);
		//cS.addButton("go_to_trp_btn"+i, tileInset+i+3, sideOffset+tileInset, topOffset+tileInset+i*tileGap+i*tileHeight, width - 2*sideOffset - 2*tileInset, tileHeight, EVENT_GO_TO_RES_CREATION, false);
	}
	
	/** username, tripTitle, item, fname, lname, company, jobTitle, PhoneNumber, Address */	
	
	//System.out.println("passed the for");
	
	//display.addPanel("Contacts", cS);
	
	//System.out.println("passed display addPanel. Intermediary.CONTROL is "+ Intermediary.CONTROL);
	
//}

public class Display {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** */
	private final static Font FONT_ONE = new Font("Arial Bold", Font.BOLD, 18);
	/** */
	private final static Font FONT_TWO = new Font("SansSerif", Font.BOLD, 40);
	/** */
	private final static Font FONT_ENTRY = new Font("Arial Bold", Font.BOLD, 14);
	
	private final static Font FONT_TAB = new Font("Arial Bold", Font.BOLD, 12);
	
	private final static Font FONT_HEADER = new Font("Arial Bold", Font.BOLD, 36);
	
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
	private final static int EVENT_GO_TO_TRIP = 20;//I think this has to be the maximum value, so arranging accordingly
	/** */
	private final static int EVENT_TRIP_SELECTION = 7;
	/** */
	private static final int EVENT_TRIP_CREATED = 8;
	/** */
	private static final int EVENT_GO_TO_RES_CREATION = 9;
	/** */
	private static final int EVENT_SAVE_RESERVATION = 10;
	
	private static final int EVENT_SAVE_ACCOMODATION = 20;
	
	private static final int EVENT_SAVE_TRANSPORT = 21;
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
	
	public Display(int inWidth, int inHeight){
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width + 14, height + 37);	//offset because java windows aren't quite accurate
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
		
		designTwoColorBorder(titlePanel, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		
		designBackedLabel(titlePanel, "title", COLOR_TWO, COLOR_BLACK, FONT_TITLE, "Plein Air", width/2, height/3, width/2, height/6, 1, true);
		
		designReactiveButton(titlePanel, "cont", COLOR_WHITE, COLOR_BLACK, FONT_ENTRY, "Start", width/2, 7*height/10, width/10, height/20, 5, EVENT_GO_TO_LOGIN, true);

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
					String uname = this.getElementStoredText("username_text");
					String pass = this.getElementStoredText("password_text");
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
		
		designBackedLabel(login, "login_", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Log In", width/3, height/4, width/3, height/5, 1, true);

		String[] elementName = new String[] {"username", "password"};
		String[] displayName = new String[] {"Username", "Password"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(login, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*2/15, height/2 + i * height/9, width/6, height/20, 3, true);
			if(!elementName[i].equals("null"))
				designTextField(login, elementName[i], width/3, height/2 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
				
		designReactiveButton(login, "but1", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Log In", width/3, height * 5 / 6, width/9, height/20, 1, EVENT_LOGIN, true);
		//Create Account Button
		designReactiveButton(login,"acc_create", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create Account", 5*width/6, height * 5 / 6, width/10, height/16, 2, EVENT_CREATE_ACC_BTN, true);		
		//add create a user on the side
		designTwoColorBorder(login, "border_2", COLOR_ONE, COLOR_THREE, width*2/3, 0, width/3, height, 30, 30, 0, false);
		
		designBackedLabel(login, "no_acc", COLOR_CREATE_ACC_BOX, COLOR_BLACK, FONT_ENTRY, "Don't have an account?", 5*width/6, height * 2 / 3, width/6, height/12, 1, true);
		
		display.addPanel("Login", login);
	}
	
	/**
	 * This method
	 */
	
	public void createAccountScreen() {
		ElementPanel createAcc = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_CREATE_ACC_FINALIZE) {
					//create the user based on passed in information
					String fn    = this.getElementStoredText("fn_text");
					String ln    = this.getElementStoredText("ln_text");
					String uname = this.getElementStoredText("uname_text");
					String pass  = this.getElementStoredText("pass_text");
						
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
		designBackedLabel(createAcc, "acct_title", COLOR_WHITE, COLOR_BLACK, FONT_HEADER, "Create your account", width/2, height/6, width/2, height/8, 1, true);
		
		String[] elementName = new String[] {"fn", "ln", "uname", "pass"};
		String[] displayName = new String[] {"First Name", "Last Name", "Username", "Password"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(createAcc, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*3/10, height/3 + i * height/9, width/6, height/20, 3, true);
			if(!elementName[i].equals("null"))
				designTextField(createAcc, elementName[i], width/2, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		//create account button
		designReactiveButton(createAcc, "but", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create my account", width/2, height*5/6, width/8, height/15, 2, EVENT_CREATE_ACC_FINALIZE, true);
		//create a back button
		designReactiveButton(createAcc, "back", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width/12, height * 11 / 12, width / 12, height / 20, 2, EVENT_BACK_TO_LOGIN, true);

		display.addPanel("Create Account", createAcc);
	}

	/**
	 * This method
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
					
					
					//System.out.println("go to trip " + tripNum);
				}
			}
		};
		//background
		designTwoColorBorder(tS, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		//title
		designBackedLabel(tS, "acct_title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Select Trip", width/2, height/8, width/3, height/10, 1, true);
		
		//logout button
		designReactiveButton(tS, "logout", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Logout", width*11/12, height*5/6, width/12, height/14, 2, EVENT_BACK_TO_LOGIN, true);
		//create trip button
		designReactiveButton(tS, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a Trip", width*5/6, height*2/15, width/10, height/12, 2, EVENT_GO_TO_TRIP_CREATION, true);
		//background of main screen
		designTwoColorBorder(tS, "backdrop", COLOR_WHITE, COLOR_BLACK, width/6, height*2/9, width*2/3, height*5/8, 30, 20, 1, false);
		//adding trip buttons
		for(int i = 0; i < (trips == null ? 0 : trips.size()); i++) {
			//display the trips on screen
			designReactiveButton(tS, "trip_"+i, COLOR_SEPARATOR, COLOR_BLACK, FONT_ENTRY, trips.get(i)[1], width/2, height*2/9 + (i+1)*(height/8), width*7/12, height/10, 3, EVENT_GO_TO_TRIP+i, true);
			tS.addText("trip_desc_"+i, 35, width/2 + width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(i)[2], FONT_ENTRY, true);
			tS.addText("trip_date_"+i, 35, width/2 - width*7/48, height*2/9 + (i+1)*(height/8) + height/30, width*7/12, height/10, trips.get(i)[3] + " - " + trips.get(i)[4], FONT_ENTRY, true);
		}

		display.addPanel("Trip Select", tS);
	}

	/**
	 * This method
	 */

	public void makeTripScreen() {
		ElementPanel tC = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRIP_SELECTION) 
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRIP_SELECT);
				}
				else if(event == EVENT_TRIP_CREATED) 
				{
					String title = this.getElementStoredText("tripTitle_text");//actual titles here have to be prefaced with text_ for some reason
					String date1 = this.getElementStoredText("tripStart_text");
					String date2 = this.getElementStoredText("tripEnd_text");
					String comments = this.getElementStoredText("tripNotes_text");
					String dest = this.getElementStoredText("tripDest_text");
					
					Communication.set(Intermediary.CREATE_TRIP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRIP_START, date1);
					Communication.set(Intermediary.CREATE_TRIP_END, date2);
					Communication.set(Intermediary.CREATE_TRIP_DEST, dest);
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ATTEMPT_CREATE_TRIP);
					
				}
				
			}
		};
		
		//background
		designTwoColorBorder(tC, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		//title of page
		designBackedLabel(tC, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Trip Details", width/2, height/8, width/2, height/10, 1, true);
		
		//cancel button
		designReactiveButton(tC, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Exit", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		
		//create trip button
		designReactiveButton(tC, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_TRIP_CREATED, true);
		
		String[] elementName = new String[] {"tripTitle", "tripDest", "tripStart", "tripEnd", "null"};
		String[] displayName = new String[] {"Trip Name:", "Destination:", "Start Date", "End Date", "(dd/MM/yyyy)"};
		
		for(int i = 0; i < displayName.length; i++) {
			designBackedLabel(tC, elementName[i]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i], width*3/10, height/3 + i * height/9, width/6, height/20, 3, true);
			if(!elementName[i].equals("null"))
				designTextField(tC, elementName[i], width/2, height/3 + i * height / 9, width/5, height/20, 3, 1000 + i, true);
		}
		
		display.addPanel("Trip Creation", tC);
	}
	
	/**
	 * A screen to display all reservations
	 */

	public void reservationDisplayScreen(String tripName) {
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
				/*else if(event == EVENT_CONTACT_LIST)
				{
					System.out.println("on reservation screen");
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_LIST);
					System.out.println("done passing to intermediary");

				}*/
			}
		};
		
		List<String[]> res = Intermediary.getTripsRes();
		designTwoColorBorder(rS, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		addHeaderTabs(rS);
		//title of page
		designBackedLabel(rS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Reservations:", width/2, height/6, width/3, height/10, 1, true);
		//exit button
		designReactiveButton(rS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		//create res button
		designReactiveButton(rS, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Reservation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_RES_CREATION, true);
		//adding res buttons
		displayItemList(rS, res, 2, 3, 4, 5);
		
		display.addPanel("Reservations", rS);
	}

	/**
	 * This method
	 */

	public void makeReservationScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_RES_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_RESERVATIONS);
				}
				else if(event == EVENT_SAVE_RESERVATION) 
				{
					String title = this.getElementStoredText("resTitle_text");
					String date1 = this.getElementStoredText("resStart_text");
					String date2 = this.getElementStoredText("resEnd_text");
					String loc = this.getElementStoredText("resLoc_text");
					
					
					Communication.set(Intermediary.CREATE_RES_TITLE, title);
					Communication.set(Intermediary.CREATE_RES_START, date1);
					Communication.set(Intermediary.CREATE_RES_END, date2);
					Communication.set(Intermediary.CREATE_RES_LOC, loc);
					
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_RES);
					
					
				}
				
			}
		};
		
		//background
		designTwoColorBorder(mR, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		//title of page
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Reservation Details", width/2, height/10, width*2/3, height/10, 1, true);
		//cancel button
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_RES_LIST, true);
		//create trip button
		designReactiveButton(mR, "create_reservation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_SAVE_RESERVATION, true);
				
		String[][] elementName = new String[][] {{"resTitle", "resStart"},{"resMode", "resEnd"}};
		String[][] displayName = new String[][] {{"Reservation Name", "Start Date"}, {"Address", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height/3 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height/3 + height/4 * i - height/10, width/6, height/14, 3, true);
			}
		}
		display.addPanel("Res Creation", mR);
	}

	/**
	 * This method
	 * s
	 * @param tripName
	 */
	
	public void accomodationDisplayScreen(String tripName) {
		
		List<String[]> accom = Intermediary.getTripsAccom();
		
		ElementPanel aS = new ElementPanel(0, 0, width, height){			
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
				/*else if(event == EVENT_CONTACT_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_LIST);
				}*/
			}
		};
		
		addHeaderTabs(aS);
		
		designTwoColorBorder(aS, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
		addHeaderTabs(aS);
		//title of page
		designBackedLabel(aS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Accomodations:", width/2, height/6, width*4/9, height/10, 1, true);
		//exit button
		designReactiveButton(aS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
		//create res button
		designReactiveButton(aS, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Accomodation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_ACCOM_CREATION, true);
		//adding res buttons
		
		displayItemList(aS, accom, 3, 4, 5, 7);
		

		display.addPanel("Reservations", aS);
		
	}
	
	/**
	 * This method
	 */
	
	public void makeAccomodationScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_ACCOM_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_ACCOM_LIST);
				}
				else if(event == EVENT_SAVE_ACCOMODATION) 
				{
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
		
		//background
		designTwoColorBorder(mR, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		//title of page
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Accomodation Details", width/2, height/10, width*2/3, height/10, 1, true);
		//cancel button
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_ACCOM_LIST, true);
		//create trip button
		designReactiveButton(mR, "create_reservation", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_SAVE_ACCOMODATION, true);
				
		String[][] elementName = new String[][] {{"accomTitle", "accomStart"},{"accomLoc", "accomEnd"}};
		String[][] displayName = new String[][] {{"Reservation Name", "Start Date"}, {"Address", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height/3 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height/3 + height/4 * i - height/10, width/6, height/14, 3, true);			}
		}

		display.addPanel("Res Creation", mR);
		
		
	}

	/**
	 * This method
	 * 
	 * @param tripName
	 */
	
	public void transportDisplayScreen(String tripName) {
	
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
			/*else if(event == EVENT_CONTACT_LIST)
			{
				Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_CONTACT_LIST);
			}*/
		}
	};
	
	
	addHeaderTabs(tS);
	
	
	designTwoColorBorder(tS, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);
	addHeaderTabs(tS);
	//title of page
	designBackedLabel(tS, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Transportation:", width/2, height/6, width/3, height/10, 1, true);
	//exit button
	designReactiveButton(tS, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRIP_SELECTION, true);
	//create res button
	designReactiveButton(tS, "create_trip", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Create a New Transportation!", width/10, height/5, width/8, height/12, 2, EVENT_GO_TO_TRANSP_CREATION, true);
	//adding res buttons
	//transportation("username", "varchar(60)", "tripTitle", "varchar(60)", "item", "varchar(60)", "startTime", "varchar(60)", "endTime", "varchar(60)")-----------------------confirm later
	displayItemList(tS, transp, 2, 3, 4, 5);
	

	display.addPanel("Reservations", tS);
	
}
	
	/**
	 * This method
	 */
	
	public void makeTransportScreen(){
		ElementPanel mR = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_TRANSPORT_LIST)
				{
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_TRANSP_LIST);
				}
				else if(event == EVENT_SAVE_TRANSP) 
				{
					String title = this.getElementStoredText("transpTitle_text");
					String date1 = this.getElementStoredText("transpStart_text");
					String date2 = this.getElementStoredText("transpEnd_text");
					String mode = this.getElementStoredText("transpMode_text");
					
					
					Communication.set(Intermediary.CREATE_TRANSP_TITLE, title);
					Communication.set(Intermediary.CREATE_TRANSP_START, date1);
					Communication.set(Intermediary.CREATE_TRANSP_END, date2);
					Communication.set(Intermediary.CREATE_TRANSP_MODE, mode);
					
					
					Communication.set(Intermediary.CONTROL, Intermediary.CONTROL_SAVE_TRANSP);
					
					
				}
				
			}
		};
		
		//background
		designTwoColorBorder(mR, "border", COLOR_ONE, COLOR_THREE, 0, 0, width, height, 30, 20, 0, false);		
		//title of page
		designBackedLabel(mR, "title", COLOR_WHITE, COLOR_BLACK, FONT_TWO, "Enter Transportation Details", width/2, height/10, width*2/3, height/10, 1, true);
		//cancel button
		designReactiveButton(mR, "exit", COLOR_ERR, COLOR_BLACK, FONT_ENTRY, "Back", width*5/6, height*5/6, width/12, height/15, 2, EVENT_TRANSPORT_LIST, true);
		//create trip button
		designReactiveButton(mR, "create_transport", COLOR_LOGIN, COLOR_BLACK, FONT_ENTRY, "Submit", width/2, height*25/32, width/8, height/15, 2, EVENT_SAVE_TRANSP, true);

		String[][] elementName = new String[][] {{"transpTitle", "transpStart"},{"transpMode", "transpEnd"}};
		String[][] displayName = new String[][] {{"Title", "Start Date"}, {"Mode of Transportation", "End Date"}};
		
		for(int i = 0; i < elementName.length; i++) {
			for(int j = 0; j < elementName[i].length; j++) {
				designTextField(mR, elementName[i][j], width/3 + j * width/3, height/3 + height/4 * i, width/6, height/12, 2, 1000 + i * elementName.length + j, true);
				designBackedLabel(mR, elementName[i][j]+"_label", COLOR_SEPARATOR, COLOR_BLACK, FONT_ONE, displayName[i][j], width/3 + j * width/3, height/3 + height/4 * i - height/10, width/6, height/14, 3, true);			}
		}

		display.addPanel("transp Creation", mR);
	}
			
	/**
	 * This method adds a header to the provided ElementPanel providing links to other screens via tabs
	 * 
	 * @param e
	 */
	
	public void addHeaderTabs(ElementPanel e){
		//background
		designReactiveButton(e, "hometab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Home", width/8, 15, width/4, 30, 18, 0, true);
		designReactiveButton(e, "restab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Reservations", 3*width/8, 15, width/4, 30, 19, EVENT_RES_LIST, true);
		designReactiveButton(e, "accomtab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Accomodations", 5*width/8, 15, width/4, 30, 20, EVENT_ACCOM_LIST, true);
		designReactiveButton(e, "ttab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Transportation", 7*width/8, 15, width/4, 30, 21, EVENT_TRANSPORT_LIST, true);
		
		//designReactiveButton(e, "contactab", COLOR_TWO, COLOR_BLACK, FONT_TAB, "  Contacts", 9*width/12, 15, width/6, 30, 22, EVENT_CONTACT_LIST, true); editted out because Java hates our contacts
	}
	
	/**
	 * This method
	 * 
	 * @param e
	 * @param sl
	 * @param titlePos
	 * @param startPos
	 * @param endPos
	 * @param otherPos
	 */
	
	public void displayItemList(ElementPanel e, List<String[]> sl, int titlePos, int startPos, int endPos, int otherPos){
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

//---  Mechanics   ----------------------------------------------------------------------------
	
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
	 * @param name
	 * @param x
	 * @param y
	 * @param panWid
	 * @param panHei
	 * @param priority
	 * @param code
	 * @param centered
	 */
	
	private void designTextField(ElementPanel pan,String name, int x, int y, int panWid, int panHei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry(name + "_text", priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, panWid, panHei, code, FONT_ENTRY, centered);	
	}
	
	/**
	 * 
	 * @param pan
	 * @param name
	 * @param fillCol
	 * @param backColor
	 * @param font
	 * @param message
	 * @param x
	 * @param y
	 * @param wid
	 * @param hei
	 * @param priority
	 * @param code
	 * @param centered
	 */
	
	private void designReactiveButton(ElementPanel pan, String name, Color fillCol, Color backColor, Font font, String message, int x, int y, int wid, int hei, int priority, int code, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, fillCol, backColor, centered);
		pan.addButton(name + "_but",     priority * MAX_COMPOSITE_ELEMENTS + 1, x, y, wid, hei, code, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 2, x, y,  wid, hei, message, font, centered);
	}
	
	/**
	 * 
	 * @param pan
	 * @param name
	 * @param colFill
	 * @param colBorder
	 * @param font
	 * @param message
	 * @param x
	 * @param y
	 * @param wid
	 * @param hei
	 * @param priority
	 * @param centered
	 */
	
	private void designBackedLabel(ElementPanel pan, String name, Color colFill, Color colBorder, Font font, String message, int x, int y, int wid, int hei, int priority, boolean centered) {
		pan.addRectangle(name + "_rect", priority * MAX_COMPOSITE_ELEMENTS, x, y, wid, hei, colFill, colBorder, centered);
		pan.addText(name + "_text_but",  priority * MAX_COMPOSITE_ELEMENTS + 1, x, y,  wid, hei, message, font, centered);
	}

	/**
	 * 
	 * @param pan
	 * @param name
	 * @param colFill
	 * @param colBorder
	 * @param x
	 * @param y
	 * @param wid
	 * @param hei
	 * @param xRatio
	 * @param yRatio
	 * @param priority
	 * @param centered
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
