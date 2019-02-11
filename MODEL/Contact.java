package model;

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
