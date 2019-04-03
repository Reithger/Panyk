package model.trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

package model.trip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	/** String[] constant containing the months of the year for converting Date formats from Text to Numerical*/
	private final static String[] MONTHS = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** String object representing the title of this Trip object*/
	private String title;
	/**String object holding dateformat*/
	private String dateformat="dd/MM/yyyy";
	/** Date object describing the start Date of this Trip object*/
	private Date start;
	/** Date object describing the end Date of this Trip object*/
	private Date end;
	/** String object containing a description of this Trip object*/
	private String description;
	/** String object containing the destination for this Trip object*/
	private String destination;
	/** HashMap<<r>String, Feature> object containing all attributed Feature objects to this Trip object, accessed via their name*/
	private HashMap<String, Feature> features;
	/** HashMap<<r>String, HashMap<<r>String, Schedulable>> object containing a HashMap of Schedulable objects for each Schedulable Type*/
	private HashMap<String, HashMap<String, Schedulable>> schedulables;
	
//---  Constructors   -------------------------------------------------------------------------

	/**
	 * Constructor for objects of the Trip type that receives information to initialize the Trip: a
	 * name, a destination, a description, and Start/End dates.
	 * 
	 * Ensures provided dates are in dd/MM/yyyy format.
	 * 
	 * @param inTitle - String object representing the title to be assigned to this Trip object
	 * @param inDestination - String object representing the destination to be assigned to this Trip object
	 * @param inDescription - String object representing the description to be assigned to this Trip object
	 * @param inStart - String object representing the Start Date to be assigned to this Trip object, interpreted as dd/MM/yyyy
	 * @param inEnd - String object representing the End Date to be assigned to this Trip object, interpreted as dd/MM/yyyy
	 */
	
	public Trip(String inTitle, String inDestination, String inDescription, String inStart, String inEnd){
		setTitle(inTitle);
		setDestination(inDestination);
		setDescription(inDescription);
		features = new HashMap<String, Feature>();
		schedulables = new HashMap<String, HashMap<String, Schedulable>>();
		try {
			setStartDate(new SimpleDateFormat(dateformat).parse(inStart));
			setEndDate(new SimpleDateFormat(dateformat).parse(inEnd));
		}
		catch(Exception e) {
			setTitle(null);
			e.printStackTrace();
		}
	}

//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method instructs this Trip object to process all of the Schedulable Objects stored
	 * by this Trip to a format that can be saved to the Database, and then store them all to
	 * the Database. Similarly, the Trip object itself is processed and saved as well.
	 * 
	 * @param username - String object informing us of the User under which this Trip object should save its Schedulables and itself
	 */
	
	public boolean saveToDatabase(String username) {
		for(String title : schedulables.keySet()) {	
			for(Schedulable s : schedulables.get(title).values()) {
				String[] types = s.generateDataType(null, 0);
				String[] data = s.generateDataEntry(null, 0);
				types[0] = "username"; types[1] = "tripTitle";
				data[0] = username; data[1] = getTitle();				
				if(!Database.addEntry(s.getData().toString(), types, data)) {
					List<String[]> currVal = Database.search(s.getData().toString(), types, Arrays.copyOfRange(data, 0, 3));
					boolean different = false;
					for(int i = 0; i < currVal.get(0).length; i++) {
						if(!currVal.get(0)[i].equals(data[i])) {
							different = true;
						}
					}
					if(different) {
						Database.deleteEntry(s.getData().toString(), types, data);
						Database.addEntry(s.getData().toString(), types, data);
					}
				}
			}
		}
		return Database.addEntry(TableType.trips, username, getTitle(), getDestination(), simplifyDate(getStartDate()), simplifyDate(getEndDate()), getDescription());
	}
	
	
	
	/**
	 * This method instructs the Trip to access the Database to receive specific Schedulable Object data
	 * corresponding to the provided username, the Trip's title, and being of the type specified by the
	 * provided SchedulableType object.
	 * 
	 * The received data is processed to generate Schedulable Objects that are stored by this Trip object.
	 * 
	 * SchedulableType objects do not store the User and Trip information, so this function also prepends those.
	 * 
	 * @param username - String object representing the User under which this Trip and the desired Schedulables exist.
	 * @param scheduleType - SchedulableType object informing this Trip as to what kind of Schedulables to search for.
	 */
	
	public void pullFromDatabase(String username, SchedulableType scheduleType) {
		String[] frstTit = scheduleType.getTitles();
		String[] titles = new String[frstTit.length+2];
		for(int i = 2; i < titles.length; i++) {
			titles[i] = frstTit[i-2];
		}
		titles[0] = "username";
		titles[1] = "tripTitle";
		List<String[]> scheds = Database.search(scheduleType.getType(), titles, new String[] {username, getTitle()});
		if(scheds == null || scheds.size() == 0)
			return;
		if(schedulables.get(scheduleType.getType()) == null) {
			schedulables.put(scheduleType.getType(), new HashMap<String, Schedulable>());
		}
		for(String[] sc : scheds) {
			ScheduledItem schedIt = new ScheduledItem(scheduleType, Arrays.copyOfRange(sc, 2, sc.length), 2);
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
	
	/**
	 * This method requests a list of Schedulable objects corresponding to the defined Schedulable Type
	 * that are stored by this Trip object.
	 * 
	 * @param schedType - String object representing the type of Schedulable objects to retrieve
	 * @return - Returns an ArrayList<<r>Schedulable> containing the Schedulable objects corresponding to the defined Schedulable Type
	 */
	
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
	 * This method takes the provided information to add a new Schedulable object to this Trip's
	 * storage, using the given name and type to find the correct location to store the provided
	 * Schedulable object.
	 * 
	 * @param name - String object representing the name of the new Schedulable object
	 * @param type - String object representing the Schedulable Type of the new Schedulable object
	 * @param a - Schedulable object that is being added to this Trip object for storage/later access
	 */
	
	public void addScheduledItem(String name, String type, Schedulable a) {
		if(schedulables.get(type) == null) {
			schedulables.put(type, new HashMap<String, Schedulable>());
		}
		schedulables.get(type).put(name, a);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void deleteTrip() {
		//TODO Has to be able to remove stuff from the database
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
	
	public void removedScheduledItem(String schedType, String name) 
	{
		schedulables.get(schedType).remove(name);
	}
	
//---  Edit Methods   -------------------------------------------------------------------------
	
	
//---  Mechanics   ----------------------------------------------------------------------------
	
	/**
	 * This method processes the large quantity of data given by a Date object to
	 * only use the relevant information in the desired format.
	 * 
	 * @param in - Date object whose information needs to be processed and simplified for display
	 * @return - Returns a String object representing the simplified information derived from the Date object
	 */
	
	public String simplifyDate(Date in) {
		String[] hold = in.toString().split(" ");
		return hold[2] + "/" + indexOf(MONTHS, hold[1]) + "/" + hold[5];
	}

	/**
	 * This method lets us do the array thing where we find where something is that for some reason
	 * isn't just a default functionality; so many projects need a method like this for convenience,
	 * make it official you cowards.
	 * 
	 * @param arr - String[] that is being searched through for the key value 
	 * @param key - String object representing the term we are looking for in the String[] arr
	 * @return - Returns an int value representing the location in arr that key is, or -1 if it is not present.
	 */
	
	public int indexOf(String[] arr, String key) {
		for(int i = 0; i < arr.length; i++)
			if(arr[i].equals(key))
				return i;
		return -1;
	}
	
}
