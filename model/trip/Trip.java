package model.trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import database.Database;
import database.TableType;
import model.trip.feature.Feature;
import model.trip.schedule.Schedulable;
import model.trip.schedule.SchedulableType;
import model.trip.schedule.ScheduledItem;

/**
 * This class models a Trip that the User has designed for themselves; a Trip consists
 * of meta-information (Title, Start/End Date, Description, etc.), Schedulables (scheduled
 * activities/events for certain days of the Trip), and Features (aggregates of Schedulable
 * information for user inspection/enjoyment.)
 * 
 * Trips can also be exported to memory in a format that can be read back in to generate
 * a Trip associated to a User. Functionalities need to exist that permit a User object to
 * query the Trip for data that encompasses the properties of an Schedulable or Feature so
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
	
//---  Constant Values   ----------------------------------------------------------------------
	
	private final static String[] MONTHS = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
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
	/** Schedulable Type -> Set of <Item name, Item object>*/
	private HashMap<String, HashMap<String, Schedulable>> schedulables;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * 
	 * @param inTitle
	 * @param inStart
	 * @param inEnd
	 * @param inDescribe
	 */
	
	public Trip(String inTitle, String inDestination, String inDescription, String inStart, String inEnd){
		setTitle(inTitle);
		setDestination(inDestination);
		setDescription(inDescription);
		features = new HashMap<String, Feature>();
		schedulables = new HashMap<String, HashMap<String, Schedulable>>();
		try {
			setStartDate(new SimpleDateFormat("dd/MM/yyyy").parse(inStart));
			setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(inEnd));
		}
		catch(Exception e) {
			setTitle(null);
			e.printStackTrace();
		}
	}

//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * 
	 * @param username
	 */
	
	public boolean saveToDatabase(String username) {
		boolean success = true;
		for(String title : schedulables.keySet()) {
			for(Schedulable s : schedulables.get(title).values()) {
				String[] types = s.generateDataType(null, 0);
				String[] data = s.generateDataEntry(null, 0);
				types[0] = "username"; types[1] = "tripTitle";
				data[0] = username; data[1] = getTitle();
				success = Database.addEntry(s.getData().toString(), types, data);
				if(!success) {
					System.out.println("Failed Database Entry for " + username + " in trip " + getTitle() + ".");
					return false;
				}
			}
		}
		return Database.addEntry(TableType.trips, username, getTitle(), getDestination(), simplifyDate(getStartDate()), simplifyDate(getEndDate()), getDescription());
	}
	
	public void pullFromDatabase(String username, SchedulableType scheduleType) {
		List<String[]> scheds = Database.search(scheduleType.getType(), scheduleType.getDataTypes(), new String[] {username, getTitle()});
		if(scheds == null || scheds.size() == 0)
			return;
		if(schedulables.get(scheduleType.getType()) == null) {
			schedulables.put(scheduleType.getType(), new HashMap<String, Schedulable>());
		}
		for(String[] sc : scheds) {
			ScheduledItem schedIt = new ScheduledItem(scheduleType, sc, 2);
			schedulables.get(scheduleType.getType()).put(schedIt.getDisplayData(null).getData("Name"), schedIt);
		}
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param in
	 */
	
	public void setTitle(String in) {
		title = in == null ? "" : in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setStartDate(Date in) {
		start = in == null ? new Date() : in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setEndDate(Date in) {
		end = in == null ? new Date() : in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setDescription(String in) {
		description = in == null ? "" : in;
	}
	
	/**
	 * 
	 * @param in
	 */
	
	public void setDestination(String in) {
		destination = in == null ? "" : in;
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
	 * 
	 * @return
	 */
	
	public String getDisplayStartDate() {
		return simplifyDate(getStartDate());
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
	 * @return
	 */
	
	public String getDisplayEndDate() {
		return simplifyDate(getEndDate());
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getDestination() {
		return destination;
	}
	
	public ArrayList<Schedulable> getSchedulables(String schedType){
		ArrayList<Schedulable> out = new ArrayList<Schedulable>();
		if(schedulables.get(schedType) == null) {
			return out;
		}
		out.addAll(schedulables.get(schedType).values());
		return out;
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
	
	public void addScheduledItem(String name, String type, Schedulable a) {
		if(schedulables.get(type) == null) {
			schedulables.put(type, new HashMap<String, Schedulable>());
		}
		System.out.println("H:" + type + " " + schedulables.get(type).toString());
		schedulables.get(type).put(name, a);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void deleteTrip() {
		//TODO
	}
	
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
	
	public void removedScheduledItem(String schedType, String name) {
		schedulables.get(schedType).remove(name);
	}
	
//---  Edit Methods   -------------------------------------------------------------------------
	
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	public String simplifyDate(Date in) {
		String[] hold = in.toString().split(" ");
		return hold[2] + "/" + indexOf(MONTHS, hold[1]) + "/" + hold[5];
	}
	
	public int indexOf(String[] arr, String key) {
		for(int i = 0; i < arr.length; i++)
			if(arr[i].equals(key))
				return i;
		return -1;
	}
	
}
