package model.trip.feature;

import java.util.HashMap;

/**
 * This interface defines the functionality of specialty structures in a Trip:
 * aggregates of provided information (Appointments/Schedulable and direct input)
 * to display in novel ways, such as in Calendars or PhotoAlbums.
 * 
 * Core functionality shared across all is being able to be exported to memory and
 * some display class (has to pack and label its information to be displayed.)
 * 
 * @author 
 *
 */

public interface Feature {

	/**
	 * This abstract method defines the functionality of a Feature implementing object
	 * being able to export its stored data into a format that can be easily interpreted
	 * and used by classes that convert the data into a visual form.
	 * 
	 * The HashMap is expected to work as its KeySet representing labels for terms stored
	 * in the adjoining Values.
	 * 
	 * @return - Returns a HashMap<<r>String, String> object representing <<r>Label, Value> storage format.
	 */
	
	public abstract HashMap<String, String> exportDisplay();
	
	/**
	 * This abstract method defines the functionality of a Feature implementing object
	 * being able to export its stored data into a format that can be easily interpreted
	 * and used by some method of long-term storage (SQLite database for example).
	 * 
	 * TODO: Return type is not void; change this once the correct format is known.
	 * 
	 */
	
	public abstract void exportMemory();
	
}
