package model.trip.schedule;

/**
 * This interface describes the behavior of any class that implements it, requiring that
 * they behave in a way which facilitates their usage as a Wrapper Structure for having
 * Schedulable objects be dynamic in what information they store.
 * 
 * Each class that implements this defines a data type that it stores, such as a Short String
 * or Date. As a Wrapper, the core ScheduledItem class stores Schedulable-extending classes
 * as an instance variable which possesses its own such instance variable as well. Each of
 * these can be any sub-class of Schedulable, created through a Factory and each type defined
 * from the initial input to the ScheduledItem object.
 *  
 * @author Mac Clevinger
 *
 */

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
	 * @param fill - DisplayData object that is progressively filled with the data from each Schedulable object
	 * @return - Returns a DisplayData object containing the information regarding what kinds of data are stored
	 */
	
	public DisplayData getDisplayData(DisplayData fill);
	
	/**
	 * Getter method to access the type of Schedulable object this is; its expected data format, that is.
	 * 
	 * @return - Returns a String object representing the data type of this Schedulable (the data format; sString, Date, etc.)
	 */
	
	public String getType();
	
	/**
	 * Getter method to access the title of the data stored by this Schedulable object (its label, effectively;
	 * such as 'Name' or 'Start Date')
	 * 
	 * @return - Returns a String object representing the title of the data stored by this Schedulable object
	 */
	
	public String getTitle();
	
	/**
	 * Getter method to access the data stored by this Schedulable object (which corresponds to the Title and
	 * is of a data format Type)
	 * 
	 * @return - Returns an Object that is implicitly cast by the implementing classes to return the desired data.
	 */
	
	public Object getData();
	
	/**
	 * This method produces a String array containing the data formats for each of 
	 * 
	 * @param append
	 * @param plc
	 * @return
	 */
	
	//InsertionSQL String
	public String[] generateDataType(String[] append, int plc);
	
	//
	public String[] generateDataEntry(String[] append, int plc);

	public int count();
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setData(String title, Object in);
	
}