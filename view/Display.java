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
	
	private final static Font FONT_ONE = new Font("Arial Bold", Font.BOLD, 18);
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	private final static Color COLOR_TWO = new Color(90, 80, 175);
	private final static Color COLOR_WHITE = new Color(255, 255, 255);
	private final static Color COLOR_BLACK = new Color(0, 0, 0);
	private final static int EVENT_LOGIN = 1;

	private WindowFrame display;
	private int width;
	private int height;
	private int elementCount;
	
	public Display(int inWidth, int inHeight) {
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width, height);
		elementCount = 0;
		initialScreen();
	}
	
	private void initialScreen() {
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					Communication.set("Control", "next");
				}
			}
			
		};
		
		titlePanel.addRectangle("rect1", 0, 0, 0, titlePanel.getWidth(), titlePanel.getHeight(), COLOR_ONE, false);
		/*
		titlePanel.addText("text1", 2, width/2, height/2, width/5, height/5, "Welcome", FONT_ONE, true);
		titlePanel.addButton("but1", 5, width/2, height/2, width/5, height/10, COLOR_TWO, EVENT_LOGIN, true);
		titlePanel.addText("text2", 10, width/2, height/2, width/5, height/10, "Log In", FONT_ONE, true);
		*/
		int wid = titlePanel.getWidth();
		int hei = titlePanel.getHeight();
		designTextField(titlePanel, wid/2, hei/2, wid/10, hei/15, 5, 2, true);
		display.addPanel("Title", titlePanel);
	}
	
	/**
	 * Priority intervals of 10
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
		pan.addTextEntry("tex_in_" + elementCount++, priority * 10 + 1, x, y, panWid, panHei, code, FONT_ONE, centered);
		
	}

	public void logInScreen() {
		display.hidePanel("Title");
	}
	
}
