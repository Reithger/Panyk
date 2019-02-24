package intermediary;

import java.util.Timer;
import input.Communication;
import view.Display;

public class Intermediary {
	
	private final static int REFRESH_RATE = 1000/60;

	private Timer timer;
	private Display display;
	
	public Intermediary() {
		display = new Display(1000, 1000);
		timer = new Timer();
		timer.schedule(new TimerRepeat(this), 0, REFRESH_RATE);
	}
	
	public void clock() {
		String happen = Communication.get();
		if(happen == null)
			return;
		if(happen.equals("next")) {
			display.logInScreen();
		}
	}
	
}
