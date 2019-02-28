package model.trip.feature;

import java.util.Date;

public class Entry 
{
	private Date written = new Date();
	private String text = new String();
	
	public Entry(String journal)
	{
		text = journal;
		long currentTime = System.currentTimeMillis();
		written = new Date(currentTime);
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setText(String s)
	{
		text = s;
	}
	
	public Date getDate()
	{
		return written;
	}
	
	public void setDate (Date d)
	{
		written = d;
	}
	
	
}
