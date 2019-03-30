package model.trip.schedule;

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
	
	public DisplayData getDisplayData(DisplayData fill);
	
	/**
	 * Getter method to access the type of Schedulable object this is. Typically undesirable in
	 * Object Oriented Design, but given the specificity of visual design, this becomes necessary.
	 * 
	 * @return
	 */
	
	public String getType();
	
	/**
	 * 
	 * @return
	 */
	
	public String getTitle();
	
	/**
	 * 
	 * @return
	 */
	
	public Object getData();
	
	//InsertionSQL String
	public String[] generateDataType(String[] append, int plc);
	
	//
	public String[] generateDataEntry(String[] append, int plc);

	public int count();
	
//---  Setter Methods   -----------------------------------------------------------------------

	public void setData(String title, Object in);
	
}