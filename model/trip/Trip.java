package model.trip;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model.trip.feature.Feature;
import model.trip.schedule.Schedulable;

public class Trip {
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private String title;
	/** */
	private Date start;
	/** */
	private Date end;
	/** */
	private String description;
	/** */
	private HashMap<String, Feature> features;
	/** */
	private HashMap<String, Schedulable> scheduled;
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param inTitle
	 */
	
	public Trip(String inTitle) {
		title = inTitle;
		start = new Date();
		end = new Date();
		description = "Describe your trip here!";
		features = new HashMap<String, Feature>();
		scheduled = new HashMap<String, Schedulable>();
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void setTitle(String in) {
		title = in;
	}
	
	public void setStartDate(Date in) {
		start = in;
	}
	
	public void setEndDate(Date in) {
		end = in;
	}
	
	public void setDescription(String in) {
		description = in;
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	public String getTitle() {
		return title;
	}
	
	public Date getStartDate() {
		return start;
	}
	
	public Date getEndDate() {
		return end;
	}
	
	public String getDescription() {
		return description;
	}
	
//---  Adder Methods   ------------------------------------------------------------------------
	
	public void addFeature(String name, Feature feat) { 
		features.put(name, feat);
	}
	
	public void addScheduledItem(String name, Schedulable sched) {
		scheduled.put(name, sched);
	}
	
//---  Remover Methods   ----------------------------------------------------------------------
	
	public void removeFeature(String name) {
		features.remove(name);
	}
	
	public void removedScheduledItem(String name) {
		scheduled.remove(name);
	}
	
//---  Edit Methods   -------------------------------------------------------------------------
	
	
	
}
