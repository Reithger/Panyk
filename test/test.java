package test;

import java.awt.GraphicsEnvironment;

import intermediary.Intermediary;

/**
 * Just run this to get stuff going, or create an Intermediary object from anywhere else.
 * 
 * @author Mac Clevinger
 *
 */

public class test {

	public static void main(String[] args) {
		String fonts[] = 
			      GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();		
		Intermediary inter = new Intermediary();
	}
	
}
