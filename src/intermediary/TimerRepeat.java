package intermediary;

import java.util.TimerTask;

import visual.frame.Frame;

public class TimerRepeat extends TimerTask{

//---  Instance Variables   -------------------------------------------------------------------

	/** Intermediary object designating the object that this TimerRepeat is associated to; will call clock() on this object*/
	private Intermediary parent;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the TimerRepeat type that assigns the provided
	 * Intermediary object as the object to perform the clock() method.
	 * 
	 * @param par - Intermediary object that this TimerRefresh object will tell to call clock().
	 */
	
	public TimerRepeat(Intermediary par){
		super();
		parent = par;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	@Override
	public void run(){
		parent.clock();
	}

	
}
