package model.trip.logistic;

/**
 * This interface defines the functionality of an Address-type object; that it,
 * in whatever format it may have, can be exported as a singular String object.
 * 
 * @author
 *
 */

public interface Address {
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * This method converts the information stored by this Address object to a pre-formatted
	 * String object that can be easily displayed.
	 * 
	 * @return - Returns a String object representing a formatted Address.
	 */
	
	public abstract String toString();

}
