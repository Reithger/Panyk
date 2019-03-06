package model.trip.schedule;

import java.util.Date;

import model.trip.logistic.Contact;
import model.trip.logistic.ProtoAddress;

public class Transport 
{
	
	Appointment booking;

	//constructor
	public Transport(String called, Date cIn, Date cOut, Contact myContact, String description, String accomments, float price, ProtoAddress loc)
	{
		booking = new Appointment(called, cIn, cOut, myContact, description, accomments, price, loc);
	}
	
	
	//misc setters and getters
	public Contact getContact() 
	{
		return booking.getContact();
	}
	
	public void setContact(Contact newContact) 
	{
		booking.setContact(newContact);
	}
	
	public Date getStartDate() 
	{
		return booking.getStartDate();
	}
	
	public void setStartDate(Date givenCheckIn) 
	{
		booking.setStartDate(givenCheckIn);
	}
	
	public Date getEndDate() 
	{
		return booking.getEndDate();
	}
	
	public void setEndDate(Date givenCheckOut) 
	{
		booking.setEndDate(givenCheckOut);
	}

	public String getType() 
	{
		return booking.getType();
	}



	public String getComments() 
	{
		return booking.getComments();
	}




	public void setComments(String commentary) 
	{
		booking.setComments(commentary);
		
	}



	public String getTitle() 
	{
		return booking.getTitle();
	}




	public void setTitle(String name) 
	{
		booking.setTitle(name);
		
	}

	
}
