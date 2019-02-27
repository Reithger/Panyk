package model.trip.feature;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.trip.logistic.Contact;

public class Phonebook implements Feature
{
	ArrayList<Contact> contacts = new ArrayList<Contact>();
	public Phonebook(ArrayList<Contact> tripPeople)//constructor for Phonebook class
	{
		contacts = tripPeople;
	}
	
	public void add(Contact c)
	{
		if(contacts.contains(c))
		{
			JOptionPane.showMessageDialog(null, "You already have that contact, so cannot add it again.", "Duplicate Contact", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			contacts.add(c);
		}
		
	}
	
	public void delete(Contact c)
	{
		contacts.remove(c);
	}
	
	public ArrayList<Contact> getContacts()
	{
		return contacts;
	}
	
}
