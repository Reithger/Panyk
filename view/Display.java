package view;

import java.awt.Color;
import java.awt.Font;
import java.util.Calendar;

import input.Communication;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;
import visual.panel.element.*;

import controller.*;

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
	private final static Font FONT_ENTRY = new Font("Arial Bold", Font.BOLD, 12);
	
	//------------------------------------------
	/** */
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	/** */
	private final static Color COLOR_LOGIN = new Color(102, 255, 102);
	/** */
	private final static Color COLOR_TWO = new Color(90, 80, 175);
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
	

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private WindowFrame display;
	/** */
	private int width;
	/** */
	private int height;
	
	private boolean incorrectPassword;
	
	private boolean incorrectUsername;
	
	private boolean invalidFirstName;

	private boolean invalidLastName;
	
	private boolean invalidUsername;
	
	private boolean invalidPassword;
	
	private boolean invalidDOB;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the Display type that assigns a provided width and height to the wholly
	 * encompassing WindowFrame object and instructs it to display the program's initial screen (log-in).
	 * 
	 * @param inWidth - int value representing the width of the WindowFrame that displays the program.
	 * @param inHeight - int value representing the height of the WindowFrame that displays the program.
	 */
	
	public Display(int inWidth, int inHeight) {
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width, height);
		initialScreen();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method instructs the Display object to present the initial screen as designed
	 * within this method; effectively a title screen for branding purposes and possible
	 * settings allocation. Just click the continue button for log-in.
	 * 
	 */
	private void initialScreen() 
	{
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_GO_TO_LOGIN) {
					display.removePanel("Title");
					logInScreen();
				}
			}	
		};
		titlePanel.addRectangle("rect1", 0, 0, 0, titlePanel.getWidth(), titlePanel.getHeight(), COLOR_ONE, false);
		titlePanel.addRectangle("rect2", 5, width/20, height/12, width*18/20, 9*height/12, new Color(200,150,170), false);
		titlePanel.addText("tex1", 15, width/2, height / 3, width/4, height/5, "Plein Air", FONT_TWO, true);
		titlePanel.addRectangle("rect3", 14, width/2, 3*height/5, width/10, height/20, COLOR_WHITE, true);
		titlePanel.addButton("but1",     15, width/2, 3*height/5, width/20, height/20, EVENT_GO_TO_LOGIN, true);
		titlePanel.addText("text_but1",  16, width/2, 3*height/5 + 5,  width/20, height/20, "start", FONT_ENTRY, true);

		display.addPanel("Title", titlePanel);
	}

	/**
	 * This method instructs the Display object to present the log-in screen as designed
	 * within this method; takes in Username and Password information to attempt to log the
	 * user in. If succesful, moves onward to a Trip select screen.
	 */
	
	public void logInScreen() {
		ElementPanel login = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					DrawnTextArea area_username = (DrawnTextArea) this.getElement("text_username");
					DrawnTextArea area_password = (DrawnTextArea) this.getElement("text_password");
					
					String uname = area_username.getText();
					String pass = area_password.getText();
					
					uname = removeFrontSpace(uname);
					pass = removeFrontSpace(pass);
					
					//if user name and password are in DB
					if(ViewLoginQuery.validUser(uname, pass)) {
						System.out.println("username and password are good");
						incorrectPassword = false;
						incorrectUsername = false;
					}
					//if only user name is in DB (ie: password is incorrect)
					else if(ViewLoginQuery.usernameExists(uname)) {
						System.out.println("username exists -> password is wrong");
						incorrectUsername = false;
						incorrectPassword = true;
						display.removePanel("Login");
						logInScreen();
					}
					//otherwise if user name and password do not exist
					else {
						System.out.println("user does not exist");
						incorrectPassword = false;
						incorrectUsername = true;
						display.removePanel("Login");
						logInScreen();
					}	
				}else if(event == EVENT_CREATE_ACC_BTN) {
					incorrectPassword = false;
					incorrectUsername = false;
					display.removePanel("Login");
					createAccountScreen();
				}
			}
		};
		login.addRectangle("rect1", 0, 0, 0, width, height, COLOR_ONE, false);
		login.addRectangle("rect2", 3, width/3, height/3, width/2, height/4, COLOR_WHITE, COLOR_BLACK, true);
		login.addText("tex1", 5, width/3, height/3 + 40, width/2, height/3, "Log In", FONT_TWO, true);
		//add username and password entries
		//username
		designTextField(login, "username", width/3, height/2 + 40, width/6, height/12, 10, 10001, true);
		login.addText("tex2", 78,          width/3, height/2 + 10, width/6, height/12, "Username", FONT_ONE, true);
		//password
		designTextField(login, "password", width/3, height/2 + 130, width/6, height/12, 10, 10000, true);
		login.addText("tex3", 79,          width/3, height/2 + 100, width/6, height/12, "Password", FONT_ONE, true);
		//add button to login
		login.addRectangle("but1_rect", 14, width/3, height - 100, width/8, height/20,  COLOR_LOGIN , true);
		login.addText("but1_text", 15, width/3, height - 100, width/9, height/20 - 10, "log in!", FONT_ENTRY, true);
		login.addButton("but1", 15, width/3, height - 100, width/9, height/20, EVENT_LOGIN, true);
		//add create a user on the side
		login.addRectangle("ver_bar", 24, 2*width/3, 0, 5, height, COLOR_SEPARATOR, false);
		login.addRectangle("no_acc_rect", 25, 2*width/3 + 65, 150, 200, 50, COLOR_CREATE_ACC_BOX, false);
		login.addText("no_acc_text", 26, 2*width/3 + 90, 165, 200, 50, "Don't have an account?", FONT_ENTRY, false);
		login.addRectangle("no_acc_create_rect", 25, 2*width/3 + 115, 250, 100, 30, COLOR_LOGIN, false);
		login.addText("no_acc_text_btn",         27, 2*width/3 + 130, 255, 100, 30, "create one!", FONT_ENTRY, false);
		login.addButton("create_acc_btn", 28, 2*width/3 + 130, 255, 100, 30, EVENT_CREATE_ACC_BTN, false);
		//check if password is incorrect
		if(this.incorrectPassword) {
			login.addRectangle("rect_incorrect_pass", 20, width/3 + 100, height/2 + 115, 90, 30,  COLOR_ERR, false);
			login.addText(	   "text_incorrect_pass", 21, width/3 + 120, height/2 + 112, 90, 30, "Incorrect password", FONT_ENTRY, false);
		}
		//check if username is valid
		if(this.incorrectUsername) {
			login.addRectangle("rect_incorrect_user", 22, width/3 + 100, height/2 + 25 , 90, 30,  COLOR_ERR, false);
			login.addText(	   "text_incorrect_user", 23, width/3 + 120, height/2 + 23 , 90, 30, "Invalid username", FONT_ENTRY, false);
		}
		display.addPanel("Login", login);
	}
	
//------------------------------------------------------------------------------------------------------------------------------
	public void createAccountScreen() {
		ElementPanel createAcc = new ElementPanel(0, 0, width, height) {
			public void clickBehaviour(int event) {
				if(event == EVENT_CREATE_ACC_FINALIZE) {
					//create the user based on passed in information
					DrawnTextArea area_firstname = (DrawnTextArea) this.getElement("text_firstname");
					DrawnTextArea area_lastname = (DrawnTextArea) this.getElement("text_lastname");
					DrawnTextArea area_username = (DrawnTextArea) this.getElement("text_username");
					DrawnTextArea area_password = (DrawnTextArea) this.getElement("text_password");
					DrawnTextArea area_dob = (DrawnTextArea) this.getElement("text_dob");
					
					String fn    = area_firstname.getText();
					String ln    = area_lastname.getText();
					String dob   = area_dob.getText();
					String uname = area_username.getText();
					String pass  = area_password.getText();
					
					fn = removeFrontSpace(fn);
					ln = removeFrontSpace(ln);
					dob = removeFrontSpace(dob);
					uname = removeFrontSpace(uname);
					pass = removeFrontSpace(pass);
					
					//check validity of first name
					if(fn.isEmpty()) {
						invalidFirstName = true;
					}else {
						invalidFirstName = false;
					}
					//check validity of last name
					if(ln.isEmpty()) {
						invalidLastName = true;
					}else {
						invalidLastName = false;
					}
					//check valid DOB
					if(dob.isEmpty() || !validDOB(dob)) {
						invalidDOB = true;
					}else {
						invalidDOB = false;
					}
					//check validity of username
					if(uname.isEmpty() || ViewLoginQuery.usernameExists(uname)) {
						invalidUsername = true;
					}else {
						invalidUsername = false;
					}
					//check validity of password
					if(pass.isEmpty()) {
						invalidPassword = true;
					}else {
						invalidPassword = false;
					}
					//remove old frame
					display.removePanel("Create Account");
					//finally, check if everything passed
					if(invalidFirstName || invalidLastName || invalidUsername || invalidPassword || invalidDOB) {
						createAccountScreen();
					}else {
						//create account now
						ObjectDBCreator.createUser(fn, ln, uname, pass, dob);
						logInScreen();
					}
				}else if(event == EVENT_BACK_TO_LOGIN) {
					display.removePanel("Create Account");
					invalidFirstName = false;
					invalidLastName = false;
					invalidUsername = false;
					invalidPassword = false;
					invalidDOB = false;
					logInScreen();
				}
			}
		};
		//backround
		createAcc.addRectangle("backround", 0, 0, 0, width, height, COLOR_ONE, false);
		//title
		createAcc.addText("create_acc_title", 1, width/2, 65, width, height/5, "Create your account", FONT_TWO, true);
		//first name
		createAcc.addText("fn", 2, width/2, 130, 200, 100, "First name", FONT_ONE, true);
		designTextField(createAcc, "firstname", width/2, 130, 200, 30, 3, 10002, true);
		if(invalidFirstName) {
			createAcc.addRectangle("rect_inv_fn",    13, width/2 + 115, 130 - 15, 100, 30, COLOR_ERR, false);
			createAcc.addText("text_inv_fn", 14, width/2 + 140, 130 - 15, 100, 30, "Invalid first name", FONT_ENTRY, false);
		}
		//last name
		createAcc.addText("ln", 4, width/2, 200, 200, 100, "Last name", FONT_ONE, true);
		designTextField(createAcc, "lastname", width/2, 200, 200, 30, 5, 10003, true);
		if(invalidLastName) {
			createAcc.addRectangle("rect_inv_ln", 15, width/2 + 115, 200 - 15, 100, 30, COLOR_ERR, false);
			createAcc.addText(     "text_inv_ln", 16, width/2 + 140, 200 - 15, 100, 30, "Invalid last name", FONT_ENTRY, false);
		}
		//DOB
		createAcc.addText("dob", 4, width/2, 270, 500, 100, "Date of birth (YYYY-MM-DD)", FONT_ONE, true);
		designTextField(createAcc, "dob", width/2, 270, 200, 30, 5, 10007, true);
		if(invalidDOB) {
			createAcc.addRectangle("rect_inv_dob", 22, width/2 + 115, 270 - 15, 100, 30, COLOR_ERR, false);
			createAcc.addText(     "text_inv_dob", 23, width/2 + 130, 270 - 10, 100, 30, "Invalid DOB", FONT_ENTRY, false);
		}
		//username
		createAcc.addText("uname", 6, width/2, 340, 200, 100, "Username", FONT_ONE, true);
		designTextField(createAcc, "username", width/2, 340, 200, 30, 7, 10004, true);
		if(invalidUsername) {
			createAcc.addRectangle("rect_inv_un", 17, width/2 + 115, 340 - 15, 100, 30, COLOR_ERR, false);
			createAcc.addText(     "text_inv_un", 18, width/2 + 140, 340 - 15, 100, 30, "Invalid username", FONT_ENTRY, false);
		}
		//password
		createAcc.addText("pass", 8, width/2, 410, 200, 100, "Password", FONT_ONE, true);
		designTextField(createAcc, "password", width/2, 410, 200, 30, 9, 10005, true);
		if(invalidPassword) {
			createAcc.addRectangle("rect_inv_pw", 19, width/2 + 115, 410 - 15, 100, 30, COLOR_ERR, false);
			createAcc.addText(     "text_inv_pw", 20, width/2 + 140, 410 - 15, 100, 30, "Invalid password", FONT_ENTRY, false);
		}
		//create account button
		createAcc.addRectangle("but_rect", 10, width/2, height - 110, width/8, height/20,  COLOR_LOGIN , true);
		createAcc.addText("but_text",      11, width/2, height - 110, width, height/20 - 10, "Create my account!", FONT_ENTRY, true);
		createAcc.addButton("create_but",  12, width/2, height - 110, width/9, height/20, EVENT_CREATE_ACC_FINALIZE, true);
		//create a back button
		createAcc.addRectangle("btn_back_rect", 26, 50, height - 100, 90, 30,  COLOR_ERR , false);
		createAcc.addText("but_back_text",      27, 80, height - 95,  90, 30, "back", FONT_ENTRY, false);
		createAcc.addButton("back_btn",         28, 50, height - 100, 90, 30, EVENT_BACK_TO_LOGIN, false);
		
		display.addPanel("Create Account", createAcc);
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
		pan.addRectangle("rect_" + name, priority * 10, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry("text_" + name , priority * 10 + 1, x, y, panWid, panHei, code, FONT_ENTRY, centered);	
	}
	
	//---------------------------------------------------------------------------------------------
	
	/** determines if a string (dob) is in the format YYYY-MM-DD
	 * 
	 * @param dob
	 * @return
	 */
	private static boolean validDOB(String dob) {
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
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	//------------------------------------------------------------------------------------------------
	//wierd issues with spaces in front of words
	public static String removeFrontSpace(String str) {
		if(str != null) {
			if(str.charAt(0) == ' ') {
				str = str.substring(1);
			}
		}
		return str;
	}

}

