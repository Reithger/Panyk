package model.trip.schedule;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class serves to simplify the process of using the data in a Schedulable Object
 * when seeking information that can be retrieved by searching for the data's title.
 * 
 * It also provides information regarding the Schedulable Type of the data being observed
 * 
 * Could probably just add a method to Schedulables for searching for a term, but eh.
 * 
 * @author Mac Clevinger
 *
 */

public class DisplayData {

//---  Instance Variables   -------------------------------------------------------------------
	
	/** SchedulableType object representing the type of Schedulable that the information corresponds to*/
	SchedulableType scheduleType;
	/** ArrayList<<r>String> object containing the titles of all the data stored in this DisplayData object (corresponding by index)*/
	ArrayList<String> titles;
	/** ArrayList<<r>String> object containing the data stored in this DisplayData object, corresponding to titles by index*/
	ArrayList<String> datum;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * Constructor for objects of the DisplayData type that assigns the provided SchedulableType
	 * object and initializes the storage of titles and data.
	 * 
	 * @param type - SchedulableType object to which this DisplayData is assigned the type thereof
	 */
	
	public DisplayData(SchedulableType type) {
		if(type == null) {
			throw new IllegalArgumentException("DisplayData given null value");
		}
		scheduleType = type;
		titles = new ArrayList<String>();
		datum = new ArrayList<String>();
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	/**
	 * This method appends the given title and data values to this DisplayData object's storage of
	 * those values, creating a new pairing that can be searched for.
	 * 
	 * @param title - String object representing the title of the data being stored subsequently
	 * @param data - String object representing the data being stored as described by the previously mentioned title
	 */
	
	public void addData(String title, String data) {
		
		title = title.replaceAll(" ", "_");
		
		titles.add(title);
		datum.add(data);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method that searches the stored information for the given String title (processed
	 * slightly) and returns the data that it finds.
	 * 
	 * Throws IllegalArgumentException if the searched-for title is non-existent.
	 * 
	 * @param title - String object representing the title of the data being requested
	 * @return - Returns the data requested as described by the given title
	 */
	
	public String getData(String title) {
		title = title.replaceAll(" ", "_");
		if(titles.indexOf(title) == -1) {
			throw new IllegalArgumentException("Invalid Request for Display Data of type: " + title + "\nViable Types: " + Arrays.toString(titles.toArray()));
		}
		return datum.get(titles.indexOf(title));
	}
	
	/**
	 * Getter method to retrieve the SchedulableType object associated to this DisplayData object
	 * 
	 * @return - Returns a SchedulableType object for which this DisplayData object corresponds.
	 */
	
	public SchedulableType getScheduleType() {
		return scheduleType;
	}
	
}
