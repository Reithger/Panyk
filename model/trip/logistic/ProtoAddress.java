package model.trip.logistic;

public class ProtoAddress implements Address{

	private int civicNumber;
	private String streetName;
	private String city;
	private String state; //or whatever subdivision the area has (can make it clear to incl provinces, territories, prefectures etc in the GUI)
	private String postalCode;//ZIP etc
	
	public ProtoAddress() {
		
	}
	
	public int getCivicNum() {
		return civicNumber;
	}
	
	public void setCivicNum(int civNum) {
		civicNumber=civNum;
	}
	
	public String getStreetName() {
		return streetName;
	}
	
	public void setStreetName(String newStreetName){
		streetName=newStreetName;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String newCity) {
		city=newCity;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String newState) {
		state=newState;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(String newPostalCode) {
		postalCode=newPostalCode;
	}
}
