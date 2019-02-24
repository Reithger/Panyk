package view;

import java.awt.Color;
import java.awt.Font;

import input.Communication;
import visual.frame.WindowFrame;
import visual.panel.ElementPanel;

public class Display {
	
	private final static Font FONT_ONE = new Font("Arial Bold", Font.BOLD, 18);
	private final static Color COLOR_ONE = new Color(30, 80, 175);
	private final static Color COLOR_TWO = new Color(90, 80, 175);
	private final static int EVENT_LOGIN = 1;

	private WindowFrame display;
	private int width;
	private int height;
	
	public Display(int inWidth, int inHeight) {
		width = inWidth;
		height = inHeight;
		display = new WindowFrame(width, height);
		initialScreen();
	}
	
	private void initialScreen() {
		ElementPanel titlePanel = new ElementPanel(0, 0, display.getWidth(), display.getHeight()) {
			public void clickBehaviour(int event) {
				if(event == EVENT_LOGIN) {
					Communication.set("next");
				}
			}
			
		};
		titlePanel.addRectangle("rect1", 0, 0, 0, titlePanel.getWidth(), titlePanel.getHeight(), COLOR_ONE, false);
		titlePanel.addText("text1", 2, width/2, height/2, width/5, height/5, "Welcome", FONT_ONE, true);
		titlePanel.addButton("but1", 5, width/2, height/2, width/5, height/10, COLOR_TWO, EVENT_LOGIN, true);
		titlePanel.addText("text2", 10, width/2, height/2, width/5, height/10, "Log In", FONT_ONE, true);
		display.addPanel("Title", titlePanel);
	}

	public void logInScreen() {
		display.hidePanel("Title");
	}
	
}
