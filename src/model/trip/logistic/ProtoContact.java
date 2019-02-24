package model.trip.logistic;

public class ProtoContact implements Contact{

	private String fullName;
	private String company;
	private String jobTitle;
	private String email;
	private String displayAs;
	private String webpage;
	private PhoneNumber tele;
	private Address address;
	
	public ProtoContact() {
		
	}

	@Override
	public String getName() {
		return fullName;
	}

	@Override
	public void setName(String name) {
		fullName=name;
	}

	@Override
	public String getCompany() {
		return company;
	}

	@Override
	public void setCompany(String companyName) {
		company=companyName;
		
	}

	@Override
	public String getJobTitle() {
		return jobTitle;
	}

	@Override
	public void setJobTitle(String title) {
		jobTitle=title;
		
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String emailAddr) {
		email=emailAddr;
	}

	@Override
	public String getDisplayTitle() {
		return displayAs;
	}

	@Override
	public void setDisplayTitle(String displayName) {
		displayAs=displayName;
	}

	@Override
	public String getWebPage() {
		return webpage;
	}

	@Override
	public void setWebPage(String url) {
		webpage=url;
		
	}

	@Override
	public PhoneNumber getPhoneNumber() {
		return tele;
	}

	@Override
	public void setPhoneNumber(String number) {
		tele=new ProtoPhoneNum(number);
	}

	@Override
	public Address getAddress() {
		return address;
	}

	@Override
	public void setAddress(Address newAddress) {
		address=newAddress;
	}
	
}
