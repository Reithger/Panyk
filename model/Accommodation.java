package model;

import java.util.Date;

public interface Accommodation {
	
	public Contact getContact();
	public void setContact(Contact accommContact);
	
	public Date getCheckInDate();
	public void setCheckInDate(Date checkIn);
	
	public Date getCheckOutDate();
	public void setCheckOutDate(Date checkOut);

}
