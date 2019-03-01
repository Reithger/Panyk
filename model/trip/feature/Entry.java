package model.trip.feature;

import java.util.Date;

public class Entry{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private Date written = new Date();
	/** */
	private String text = new String();
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param journal
	 */
	
	public Entry(String journal){
		text = journal;
		long currentTime = System.currentTimeMillis();
		written = new Date(currentTime);
	}
	
//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public String getText(){
		return text;
	}

	/**
	 * 
	 * @return
	 */
	
	public Date getDate(){
		return written;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param s
	 */
	
	public void setText(String s){
		text = s;
	}
	
	/**
	 * 
	 * 
	 * @param d
	 */
	
	public void setDate (Date d){
		written = d;
	}
		
}