package model.trip.schedule;

import java.util.Date;
import java.util.HashMap;

import model.trip.logistic.ProtoAddress;

/**
 * A class to replace Schedulable. In coding schedulable's implementing classes weren't different enough
 * to need different classes, and their enumerations were likewise of little use
 * 
 * @author nrcuthbertson
 *
 */
public class Appointment {

	private String title;
	private Date start;
	private Date end;
	private String type;
	private String comments;
	private float price;
	private ProtoAddress location;
	
	public Appointment(String called, Date begin, Date cease, String description, String userComments, float cost, ProtoAddress pa)
	{
		if(called.equals(null))
		{
			called="untitled";
		}
		title=called;
		start=begin;
		end=cease;
		type=description;
		comments=userComments;
		price=cost;
		location=pa;
	}
	
	public Date getStartDate() 
	{
		return start;
	}

	public Date getEndDate() 
	{
		return end;
	}

	public String getType() 
	{
		return type;
	}

	public void setStartDate(Date in) 
	{
		start=in;
		
	}

	public void setEndDate(Date d) 
	{
		end=d;
		
	}
	
	public String getComments() 
	{
		return comments;
	}

	public void setComments(String commentary) {
		comments=commentary;
		
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String name) 
	{
		title=name;
		
	}
	
	public float getPrice()
	{
		return price;
	}
	
	public void setPrice(float f)
	{
		price=f;
	}
	
	public ProtoAddress getLocation()
	{
		return location;
	}
	
	public void setLocation(ProtoAddress newLoc)
	{
		location=newLoc;
	}
	
}
