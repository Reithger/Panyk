package view;

import java.awt.Color;
import java.awt.Font;

import input.Communication;
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
	private final static Font FONT_ENTRY = new Font("Arial Bold", Font.BOLD, 12);
	/** */
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	/** */
	private final static Color COLOR_TWO = new Color(90, 80, 175);
	/** */
	private final static Color COLOR_WHITE = new Color(255, 255, 255);
	/** */
	private final static Color COLOR_BLACK = new Color(0, 0, 0);
	/** */
	private final static int EVENT_LOGIN = 1;

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private WindowFrame display;
	/** */
	private int width;
	/** */
	private int height;
	/** */
	private int elementCount;
	
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
		elementCount = 0;
		initialScreen();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method instructs the Display object to present the initial screen as designed
	 * within this method; effectively a title screen for branding purposes and possible
	 * settings allocation. Just click the continue button for log-in.
	 * 
	 */
	
	private void initialScreen() {
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					Communication.set("Control", "next");
				}
			}
			
		};
		
		titlePanel.addRectangle("rect1", 0, 0, 0, titlePanel.getWidth(), titlePanel.getHeight(), COLOR_ONE, false);
		titlePanel.addRectangle("rect2", 5, width/20, height/12, width*18/20, 9*height/12, new Color(200,150,170), false);
		titlePanel.addText("tex1", 15, width/2, height / 3, width/4, height/5, "Plein Air", FONT_TWO, true);
		titlePanel.addRectangle("rect3", 14, width/2, 3*height/5, width/10, height/20, COLOR_WHITE, true);
		titlePanel.addButton("but1", 15, width/2, 3*height/5, width/20, height/20, EVENT_LOGIN, true);
		/*
		titlePanel.addText("text1", 2, width/2, height/2, width/5, height/5, "Welcome", FONT_ONE, true);
		titlePanel.addButton("but1", 5, width/2, height/2, width/5, height/10, COLOR_TWO, EVENT_LOGIN, true);
		titlePanel.addText("text2", 10, width/2, height/2, width/5, height/10, "Log In", FONT_ONE, true);
		*/
		display.addPanel("Title", titlePanel);
	}

	/**
	 * This method instructs the Display object to present the log-in screen as designed
	 * within this method; takes in Username and Password information to attempt to log the
	 * user in. If succesful, moves onward to a Trip select screen.
	 */
	
	public void logInScreen() {
		display.hidePanel("Title");
		ElementPanel login = new ElementPanel(0, 0, width, height);
		login.addRectangle("rect1", 0, 0, 0, width, height, COLOR_ONE, false);
		login.addRectangle("rect2", 3, width/2, height/3, width/2, height/3, COLOR_WHITE, COLOR_BLACK, true);
		login.addText("tex1", 5, width/2, height/3, width/2, height/3, "Log In", FONT_TWO, true);
		designTextField(login, width/2, height/2 + height/6, width/6, height/12, 10, 1, true);
		designTextField(login, width/2, height/2 + height/3, width/6, height/12, 10, 2, true);
		display.addPanel("Login", login);
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
	
	private void designTextField(ElementPanel pan, int x, int y, int panWid, int panHei, int priority, int code, boolean centered) {
		pan.addRectangle("rec_" + elementCount++, priority * 10, x, y, panWid + 10, panHei, COLOR_WHITE, COLOR_BLACK, centered);
		pan.addTextEntry("tex_in_" + elementCount++, priority * 10 + 1, x, y, panWid, panHei, code, FONT_ENTRY, centered);
		
	}

}
