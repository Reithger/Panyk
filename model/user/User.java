package model.user;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import exceptions.BadTimeException;
import controller.Encryptor;
import database.*;
import model.trip.Trip;
import model.trip.schedule.DisplayData;
import model.trip.schedule.Schedulable;
import model.trip.schedule.SchedulableType;
import model.trip.schedule.ScheduledItem;

/**
 * This class is the contact point that the Intermediary can use to access the data in the
 * Model (only one User is logged in at a time, and their info is necessary to access the
 * information, so it seemed a good focal point.)
 * 
 * @author Mac Clevinger
 * @author Regan Lynch
 */

public class User {
//---------------------------------------------------------------------------------------------	
//									 INSTANCE VARIABLES
//---------------------------------------------------------------------------------------------	
	/** HashMap<<r>String, Trip> object containing the trips associated to this User object*/
	private HashMap<String, Trip> trips;
	/** String object representing the username for which data has been stored or will be stored for later access*/
	private String username;
	/** String object representing the provided password for decrypting data stored under the username heading*/
	private String password;
	/** HashMap<<r>String, SchedulableType> object containing the Schedulable Types this user has access to*/
	private HashMap<String, SchedulableType> scheduleTypes;
	
//---------------------------------------------------------------------------------------------	
// 										CONSTRUCTORS
//---------------------------------------------------------------------------------------------	
	/**	
	 * Constructor for objects of the User type that creates an entry in the database for this User using
	 * the provided input to the constructor; input is assumed to be perfect.
	 * 
	 * @param fname - String object representing the First Name of this new User
	 * @param lname - String object representing the Last Name of this new User
	 * @param usernameIn - String object representing the Username of this new User
	 * @param passwordIn - String object representing the Password of this new User
	 */
 	public User(String fname, String lname, String usernameIn, String passwordIn){
		username = usernameIn;
		password = passwordIn;
		String[] hash = Encryptor.createSaltedHash(password);
		
		boolean result = Database.addEntry(TableType.users, username, fname, lname, getCreationDate(), hash[0], hash[1]);
		trips = new HashMap<String, Trip>();
		scheduleTypes = new HashMap<String, SchedulableType>();
		if(!result) {
			username = null;
			password = null;
			trips = null;
			scheduleTypes = null;
		}
	}
 //-----------------------------------------------------------------	
	/**
	 * Constructor for objects of the User type that assumes an entry is extant for this user
	 * and initializes the User object to have the provided username and password.
	 * 
	 * @param usernameIn - String object representing the Username of this user
	 * @param passwordIn - String object representing the Password of this user
	 */
	public User(String usernameIn, String passwordIn) {
		username = usernameIn;
		password = passwordIn;
		trips = new HashMap<String, Trip>();
		scheduleTypes = new HashMap<String, SchedulableType>();
	}
//---------------------------------------------------------------------------------------------	
//							            OPERATIONS
//---------------------------------------------------------------------------------------------	
	/**
	 * This method accesses the database entries associated to this User and constructs Trip
	 * objects from that data for display/manipulation, storing that information as instance
	 * variables across the breadth of the Model.
	 */
	public void retrieveData() {
		List<String[]> tripsIn = Database.search(TableType.trips, getUsername(), null, null, null, null, null);
		ArrayList<Trip> out = new ArrayList<Trip>();
		for(String[] rawData : tripsIn) {
			out.add(new Trip(rawData[1], rawData[2], rawData[5], rawData[3], rawData[4]));
		}
		for(Trip t : out) {
			if(t != null)
				trips.put(t.getTitle(), t);
			for(SchedulableType sched : scheduleTypes.values()) {
				t.pullFromDatabase(username, sched);
			}
		}
	}
//-----------------------------------------------------------------			
	/**
	 * This method creates a new Trip object for the User to design/have access to, and saves
	 * it to the database.
	 * 
	 * @return - Returns a boolean value representing whether or not the Trip was added successfully
	 */
	public boolean makeTrip(String title, String destination, String description, String dateStart, String dateEnd) 
	{
		String dateformat="dd/MM/yyyy";
		if(trips.get(title) != null)  return false;
		boolean datesOK = true;
		Date d1=null,d2=null;
		try {
			d1 = new SimpleDateFormat(dateformat).parse(dateStart);//try to parse dates
			d2 = new SimpleDateFormat(dateformat).parse(dateEnd);
			DateFormat df=new SimpleDateFormat(dateformat);
			df.setLenient(false);
			df.parse(dateStart);
			df.parse(dateEnd);
		}catch(ParseException pe)
		{
			return false;
		}//try to parse dates
		
		if(d1.after(d2))  return false;//make sure the dates don't indicate time travel
		
		Trip t = new Trip(title, destination, description, dateStart, dateEnd);
		if((t.getTitle() != null) && datesOK) {
			trips.put(t.getTitle(), t);
			t.saveToDatabase(username);
			return true;
		}
		return false;
	}
//-----------------------------------------------------------------			
	/**
	 * This method should delete the defined Trip object from the list stored by this User.
	 * 
	 * TODO: Do this, make sure we have confirmation messages before deletion But confirmation is
	 * not handled by the User object, it is handled by the user interface.
	 */
	public boolean deleteTrip(String tripName) {
		if(trips.get(tripName) == null) return false;

		trips.get(tripName).deleteTrip();
		Database.deleteEntry(TableType.trips, username, tripName, null, null, null, null);
		trips.remove(tripName);
		return true;
	}
//-----------------------------------------------------------------		
	/**
	 * This method converts the Trip data stored by this User to a format that can be put
	 * into the Database.
	 * TODO: Do this
	 * @return - Returns a boolean value representing the success of this operation.
	 */
	private boolean saveData() {
		boolean result = true;
		for(Trip t : trips.values()) {
			result = t.saveToDatabase(username);
			if(!result) return false;
		}
		return true;
	}
//-----------------------------------------------------------------		
	/**
	 * This method adds a Schedulable Object, unless its dates are bad in which case it throws an error
	 * and the user has to fix things and resubmit
	 * 
	 * @param tripName
	 * @param type
	 * @param data
	 * @throws BadTimeException
	 */
	public void addSchedulableItem(String tripName, String type, String ... data) throws BadTimeException
	{	
		Trip theTrip = trips.get(tripName);
		boolean okToSave = true;
		Date d1=null;
		int d1pos = -1;
		int d2pos = -1;
		Date d2=null;
		int numDates=0;
		BadTimeException e = new BadTimeException();
		for(int i=0; i<data.length; i++)
		{
			try {
				Date myDate = new SimpleDateFormat("dd/MM/yyyy").parse((String)data[i]);//try to parse dates
				numDates++;//keep track of how many there are
				if(d1==null)//track starting date
				{
					d1=myDate;
					d1pos = i;
				}
				else//and ending date
				{
					d2 = myDate;
					d2pos = i;
				}
				if(myDate.before(theTrip.getStartDate()) || myDate.after(theTrip.getEndDate()))//make sure the dates are in the right range
				{
					okToSave=false;
					throw e;
				}
			}
			catch(ParseException pe) {
				//do nothing
			}
		}
		if((d2!=null && d1!=null && d2.before(d1)) || numDates!=2)//if you plan on traveling backwards in time our application does not currently support that
		{
			okToSave=false;
			throw e;	
		}
		try
		{
			DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
			df.setLenient(false);
			df.parse((String)data[d1pos]);
			df.parse((String)data[d2pos]);
		}
		catch(ParseException pe)
		{
			throw e;
		}
		if(okToSave)//if appropriate, save the item
		{
			theTrip.addScheduledItem(data[0], type, new ScheduledItem(scheduleTypes.get(type), data, 2));
			trips.get(tripName).saveToDatabase(username);
		}
	}
//-----------------------------------------------------------------		
	/**
	 * This method takes in descriptive input to create a new SchedulableType object for
	 * the User to have access to in prompting the program user when creating SchedulableType
	 * objects.
	 * 
	 * @param header - String object representing the name of the Schedulable Type
	 * @param titles - String[] containing the labels for each piece of data stored by this Schedulable Type
	 * @param types - String[] containing the data types for each piece of data stored by this Schedulable Type
	 */
	public void addSchedulableType(String header, String[] titles, String[] types) {
		scheduleTypes.put(header, new SchedulableType(header, titles, types));
	}
//-----------------------------------------------------------------	
	/**
	 * Does what it says on the tin - deletion galore!
	 * @param tripName - name of the trip with the to be deleted schedulable
	 * @param schedName - the name of that schedulable
	 * @param schedulableType - the type of that schedulable
	 */
	public void deleteSchedulable(String tripName, String schedName, String schedulableType){
		trips.get(tripName).removedScheduledItem(schedulableType, schedName);
	}
//---------------------------------------------------------------------------------------------	
//										GETTER METHODS	
//---------------------------------------------------------------------------------------------
	/**
	 * Getter method that queries whether or not the data stored by this User object
	 * is valid; i.e, that it is not null and has been assigned properly.
	 * 
	 * @return - Returns a boolean value describing the state of this User; true if 'valid', false otherwise.
	 */
	public boolean validate() {
		return username != null && password != null;
	}
//-----------------------------------------------------------------	
	/**
	 * Getter method that requests the user's username.
	 * 
	 * @return - Returns a String object representing the User object's username.
	 */
	public String getUsername() {
		return username;
	}
//-----------------------------------------------------------------	
	/**
	 * Getter method that requests the user's password.
	 * 
	 * @return - Returns a String object representing the User object's password.
	 */
	public String getPassword() {
		return password;
	}
//-----------------------------------------------------------------
	/**
	 * Getter method that requests a list of the Trip objects stored to this User object
	 * 
	 * @return - Returns an ArrayList<<r>Trip> object containing all extant Trip objects for this User object
	 */
	public ArrayList<Trip> getTrips(){
		return new ArrayList<Trip>(trips.values());
	}
//-----------------------------------------------------------------	
	/**
	 * Getter method to retrieve a list of all Schedulables associated to a defined Trip that
	 * come under the defined SchedulableType (i.e., all Accommodations, all Reservations, etc.)
	 * 
	 * @param tripName - String object representing the Trip of this User to request the Schedulable objects from
	 * @param schedulableType - String object representing which Schedulable objects are viable to be returned
	 * @return - Returns an ArrayList<<r>Schedulable> object containing all Schedulables of type schedulableType
	 */
	public ArrayList<Schedulable> getSchedulables(String tripName, String schedulableType){
		return trips.get(tripName).getSchedulables(schedulableType);
	}
//-----------------------------------------------------------------
	/**
	 * Getter method to retrieve a String[] of the labels for each piece of data stored by
	 * Schedulable Objects of a type defined by the input 'header'. These would be user-friendly
	 * descriptors of the data in the Schedulable Type described by the input, with terms such
	 * as 'Name' or 'Start Date'.
	 * 
	 * It functions by retrieving the SchedulableType object stored by the User object and having
	 * it provide this information directly.
	 * 
	 * @param header - String object representing the SchedulableType to retrieve the titles thereof.
	 * @return - Returns a String[] containing the titles of the data in the defined SchedulableType
	 */
	public String[] getSchedulableTypeTitles(String header) {
		return scheduleTypes.get(header).getTitles();
	}
//-----------------------------------------------------------------
	/**
	 * Getter method to request a HashMap<String, DisplayData> containing the title of specific
	 * Schedulable objects leading to a DisplayData object that can be conveniently used for
	 * accessing its data by specific data titles (such as 'Name' or 'Start Date').
	 * 
	 * It requests a list of all Schedulables in the defined Trip of the defined SchedulableType
	 * and has those objects generate their DisplayData object via a recursive Wrapper method
	 * used for Schedulable objects.
	 * 
	 * @param tripName - String object representing the Trip to retrieve Schedulables from
	 * @param schedulableType - String object representing the SchedulableType for which all retrieved Schedulables should be a type of
	 * @return - Returns a HashMap<<r>String, DisplayData> object containing entries for each viable Schedulable object in the defined Trip
	 */
	public HashMap<String, DisplayData> getDisplaySchedulablesData(String tripName, String schedulableType){
		HashMap<String, DisplayData> out = new HashMap<String, DisplayData>();
		ArrayList<Schedulable> sched = getSchedulables(tripName, schedulableType);
		
		for(Schedulable sc : sched) {
			DisplayData in = sc.getDisplayData(null);
			out.put(in.getData("Name"), in);
		}
		return out;
	}
//-----------------------------------------------------------------
	/**
	 * Getter method to access a pairing of each title for the data in a specified SchedulableType
	 * object to the type of data that represents it; i.e., for the title 'Name', the generated
	 * HashMap<<r>String, String> would lead to 'sString' to define it as a 'short String' type
	 * of stored data.
	 * 
	 * Used for specifying what data is desired for the creation of a Schedulable object; a title
	 * to label the kind of input and a dataType for its interpretation/implementation of a receiver.
	 * 
	 * @param schedulableType - String object representing the desired kind of SchedulableType to retrieve the titles and types of
	 * @return - Returns a HashMap<<r>String, String> object containing the titles of the stored data leading to their types
	 */
	public HashMap<String, String> getCreateSchedulablesData(String schedulableType){
		return scheduleTypes.get(schedulableType).getSchedulableFormatted();
	}
//-----------------------------------------------------------------
	/**
	 * Getter method to request a list of all SchedulableType objects associated to this User object.
	 * 
	 * @return - Returns an ArrayList<<r>String> object containing all the SchedulableType objects that this User object possesses.
	 */
	public ArrayList<String> getSchedulableTypes(){
		return new ArrayList<String>(scheduleTypes.keySet());
	}
//---------------------------------------------------------------------------------------------	
//    									HELPER METHODS	
//---------------------------------------------------------------------------------------------
	private String getCreationDate() 
	{
		String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH));
		String year = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
		if(month.length() == 1) {
			month = "0" + month;
		}
		if(day.length() == 1) {
			day = "0" + day;
		}
		return year + "-" + month + "-" + day;
	}
}
