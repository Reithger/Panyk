package model.trip.logistic;

/**
 * This interface defines the functionality of a Contact-type object; that it, in
 * whatever format it may be, can have certain traits be stored and accessed relating
 * to Contact information for a person or business.
 * 
 * Note: May want to refactor to make a leaner version of contact; will need to go further
 * into the implementation to decide if that will create more problems or improve things. 
 * 
 * Also, yeah, this is way too all-encompassing and vague; we don't want objects that would
 * consistently have attributes be unused/in the way. Should break up/redesign.
 * 
 * @author 
 *
 */

public interface Contact {

//---  Getter Methods   -----------------------------------------------------------------------
	
	public abstract String getName();
	
	public abstract String getCompany();
	
	public abstract String getJobTitle();

	public abstract String getEmail();

	public abstract String getDisplayTitle();

	public abstract String getWebPage();

	public abstract PhoneNumber getPhoneNumber();

	public abstract Address getAddress();
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public abstract void setName(String name);
	
	public abstract void setCompany(String companyName);
	
	public abstract void setJobTitle(String title);
	
	public abstract void setEmail(String emailAddr);
	
	public abstract void setDisplayTitle(String displayAs);
	
	public abstract void setWebPage(String url);
	
	public abstract void setPhoneNumber(String number);
	
	public abstract void setAddress(Address newAddress);
	
}
