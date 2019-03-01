package model.trip;

import java.util.Date;
import java.util.HashMap;
import model.trip.feature.Feature;
import model.trip.schedule.Appointment;

/**
 * This class models a Trip that the User has designed for themselves; a Trip consists
 * of meta-information (Title, Start/End Date, Description, etc.), Appointments (scheduled
 * activities/events for certain days of the Trip), and Features (aggregates of Appointment
 * information for user inspection/enjoyment.)
 * 
 * Trips can also be exported to memory in a format that can be read back in to generate
 * a Trip associated to a User. Functionalities need to exist that permit a User object to
 * query the Trip for data that encompasses the properties of an Appointment or Feature so
 * that a disjoint entity for displaying this information can use/interpret it.
 * 
 * Two forms of export: to memory and to the view. The former can be done via a database
 * (SQLite if you like), the latter some data format that encompasses labeled Strings 
 * (a HashMap can do this; KeySet are labels, Values are display data).
 * 
 * @author Mac Clevinger
 *
 */

public class Trip {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String title;
	/** */
	private Date start;
	/** */
	private Date end;
	/** */
	private String description;
	/** */
	private String destination;		//Client specified that this is a custom descriptor from the user
	/** */
	private HashMap<String, Feature> features;
	/** */
	private HashMap<String, Appointment> appointments;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param inTitle
	 */
	
	public Trip(String inTitle){
		title = inTitle;
		start = new Date();
		end = new Date();
		description = "Describe your trip here!";
		features = new HashMap<String, Feature>();
		appointments = new HashMap<String, Appointment>();
	}

	/**
	 * 
	 * @param inTitle
	 * @param inStart
	 * @param inEnd
	 * @param inDescribe
	 */
	
	public Trip(String inTitle, Date inStart, Date inEnd, String inDescribe){
		title = inTitle;
		start = inStart;
		end = inEnd;
		description = inDescribe;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param in
	 */
	
	public void setTitle(String in) {
		title = in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setStartDate(Date in) {
		start = in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setEndDate(Date in) {
		end = in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setDescription(String in) {
		description = in;
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public String getTitle() {
		return title;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Date getStartDate() {
		return start;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public Date getEndDate() {
		return end;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	
	public String getDescription() {
		return description;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * 
	 * @param name
	 * @param feat
	 */
	
	public void addFeature(String name, Feature feat) { 
		features.put(name, feat);
	}
	
	/**
	 * 
	 * @param name
	 * @param a
	 */
	
	public void addScheduledItem(String name, Appointment a) {
		appointments.put(name, a);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	/**
	 * 
	 * @param name
	 */
	
	public void removeFeature(String name) {
		features.remove(name);
	}
	
	/**
	 * 
	 * @param name
	 */
	
	public void removedScheduledItem(String name) {
		appointments.remove(name);
	}
	
//---  Edit Methods   -------------------------------------------------------------------------
	
	
	
}

