package model;
/*
 * May want to refactor to make a leaner version of contact
Will need to go further into the implementation to decide if 
that will create more problems or improve things 
 * 
 * */
public interface Contact {

	public String getName();
	public void setName(String name);
	
	public String getCompany();
	public void setCompany(String companyName);
	
	public String getJobTitle();
	public void setJobTitle(String title);
	
	public String getEmail();
	public void setEmail(String email);
	
	public String getDisplayTitle();
	public void setDisplayTitle(String displayAs);
	
	public String getWebPage();
	public void setWebPage(String url);
	
	public PhoneNumber getPhoneNumber();
	public void setPhoneNumber(String number);
	
	public Address getAddress();
	public void setAddress(Address address);
}
