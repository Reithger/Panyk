package model.trip.schedule;

import java.util.Date;
import java.util.HashMap;

public interface Schedulable {

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * Getter method to request the data stored in this Schedulable object as a HashMap for
	 * which the category of entry is the Key and the matching data is the Value. (For example,
	 * you may have a pair such as 'Title', 'Schedulable_Name')
	 * 
	 * This permits a consistent naming strategy for reference by methods that need to process
	 * uncertain data, but can react to key terms when provided the data. (These should be CONSTANT
	 * VALUES somewhere for consistency in field titling.)
	 * 
	 * @return
	 */
	
	public HashMap<String, String> getDisplayData();
	
	/**
	 * Getter method to request the Date in which this Schedulable object exists (in a Trip, Scheduled
	 * Items exist on certain dates even if they extend over multiple days. This date would be the first
	 * day, with extended periods implementing a second date on their own/adding a second Schedulable item
	 * for the end of the period.)
	 * 
	 * Primarily intended for integration with a Calendar of some kind; all Schedulable items have a related
	 * Date, though. Exceptions may come up, but pretty sure we're good.
	 * 
	 * @return
	 */
	
	public Date getDate();
	
	/**
	 * Getter method to access the type of Schedulable object this is. Typically undesirable in
	 * Object Oriented Design, but given the specificity of visual design, this becomes necessary.
	 * 
	 * @return
	 */
	
	public String getType();
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setDate(Date in);
	
}
