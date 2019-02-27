package model.trip.feature;

import java.util.ArrayList;

public class Diary implements Feature
{
	ArrayList<Entry> pages = new ArrayList<Entry>();
	
	public Diary(ArrayList<Entry> entries)
	{
		pages = entries;
	}
	
	public void add(Entry e)
	{
		pages.add(e);
	}
	
	public void delete(Entry e)
	{
		pages.remove(e);
	}
	
	public ArrayList<Entry> getEntries()
	{
		return pages;
	}
	
	public void setEntries(ArrayList<Entry> ale)
	{
		pages = ale;
	}
	
}
