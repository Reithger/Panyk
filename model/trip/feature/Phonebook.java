package model.trip.feature;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import model.trip.logistic.Contact;

/**
 * This class implements the Feature interface for consistency in functionality
 * with other Feature-implementing classes used in a Trip object to model a
 * Phonebook; that is to say, a collection of Contacts associated to the Trip
 * as provided by the User.
 * 
 * @author 
 *
 */

public class Phonebook implements Feature{
	
//---  Instance Variables   -------------------------------------------------------------------
	
	/** */
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	
//---  Constructors   -------------------------------------------------------------------------
	
	/**
	 * 
	 * @param tripPeople
	 */
	
	public Phonebook(ArrayList<Contact> tripPeople){
		contacts = tripPeople;
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	/**
	 * 
	 * @param c
	 */
	
	public void add(Contact c){
		if(contacts.contains(c)){
			JOptionPane.showMessageDialog(null, "You already have that contact, so you cannot add it again.", "Duplicate Contact", JOptionPane.ERROR_MESSAGE);
		}
		else{
			contacts.add(c);
		}
	}
	
	/**
	 * 
	 * @param c
	 */
	
	public void delete(Contact c){
		contacts.remove(c);
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
	
	public ArrayList<Contact> getContacts(){
		return contacts;
	}

	
}