package intermediary;

import java.util.Timer;
import input.Communication;
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
 * @author Mac Clevinger
 *
 */

public class Intermediary {
	
//---  Constant Values   ----------------------------------------------------------------------
	
	/** Constant value representing the speed at which the program responds to user input (call rate of clock())*/
	private final static int REFRESH_RATE = 1000/60;

//---  Instance Variables   -------------------------------------------------------------------
	
	/** The Timer object periodically calls the clock() method of this Intermediary object*/
	private Timer timer;
	/** The Display object is the contact point this Intermediary object has to the View for Input/Output*/
	private Display display;
	/** The User object is the contact point this Intermediary object has to the Model for data access/manipulation*/
	private User user;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 */
	
	public Intermediary() {
		display = new Display(1000, 600);
		timer = new Timer();
		timer.schedule(new TimerRepeat(this), 0, REFRESH_RATE);
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * 
	 */
	
	public void clock() {
		String happen = Communication.get("Control");
		if(happen == null)
			return;
		if(happen.equals("next")) {
			display.logInScreen();
		}
	}
	
}
