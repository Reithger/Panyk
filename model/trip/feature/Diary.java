package model.trip.feature;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements the Feature interface for consistency in functionality
 * with other Feature-implementing classes used in a Trip object to model a Diary;
 * that is to say, a means by which a User can record text-entries associated to
 * certain days of their Trip.
 * 
 * @author 
 *
 */

public class Diary implements Feature{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	private ArrayList<Entry> pages = new ArrayList<Entry>();
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param entries
	 */
	
	public Diary(ArrayList<Entry> entries){
		pages = entries;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * 
	 * @param e
	 */
	
	public void addEntry(Entry e){
		pages.add(e);
	}
	
	/**
	 * 
	 * 
	 * @param e
	 */
	
	public void deleteEntry(Entry e){
		pages.remove(e);
	}
	
	@Override
	public HashMap<String, String> exportDisplay() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void exportMemory() {
		// TODO Auto-generated method stub
		
	}

//---  Getter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @return
	 */
	
	public ArrayList<Entry> getEntries(){
		return pages;
	}

//---  Setter Methods   -----------------------------------------------------------------------
	
	/**
	 * 
	 * @param ale
	 */
	
	public void setEntries(ArrayList<Entry> ale){
		pages = ale;
	}


	
}
