package model.trip.schedule;

import java.util.Date;
import model.trip.logistic.ProtoAddress;

/**
 * A class to replace Schedulable. In coding schedulable's implementing classes weren't different enough
 * to need different classes, and their enumerations were likewise of little use
 * 
 * @author nrcuthbertson
 *
 */

public class Appointment {

//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String title;
	/** */
	private Date start;
	/** */
	private Date end;
	/** */
	private String type;
	/** */
	private String comments;
	/** */
	private float price;
	/** */
	private ProtoAddress location;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Appointment(String called, Date begin, Date cease, String description, String userComments, float cost, ProtoAddress pa){
		if(called.equals(null))
			called = "untitled";
		else
			title = called;
		start = begin;
		end = cease;
		type = description;
		comments = userComments;
		price = cost;
		location = pa;
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	public Date getStartDate(){
		return start;
	}

	public Date getEndDate(){
		return end;
	}

	public String getType(){
		return type;
	}
	
	public String getComments(){
		return comments;
	}

	public String getTitle(){
		return title;
	}
	
	public float getPrice(){
		return price;
	}

	public ProtoAddress getLocation(){
		return location;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setStartDate(Date in){
		start = in;
		
	}

	public void setEndDate(Date d){
		end = d;
		
	}

	public void setComments(String commentary){
		comments = commentary;
		
	}

	public void setTitle(String name){
		title = name;
	}
	
	public void setPrice(float f){
		price = f;
	}
	
	public void setLocation(ProtoAddress newLoc){
		location = newLoc;
	}
	
}