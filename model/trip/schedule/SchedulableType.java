package model.trip.schedule;

import java.util.HashMap;

public class SchedulableType {

	private String type;
	private String[] titles;
	private String[] dataTypes;
	
	public SchedulableType(String inType, String[] headers, String[] typeData) {
		type = inType;
		titles = headers;
		dataTypes = typeData;
	}
	
	public String getType() {
		return type;
	}
	
	public String[] getTitles() {
		return titles;
	}
	
	public String[] getDataTypes() {
		return dataTypes;
	}
	
	public HashMap<String, String> getSchedulableFormatted(){
		HashMap<String, String> out = new HashMap<String, String>();
		for(int i = 0; i < titles.length; i++) {
			out.put(titles[i], dataTypes[i]);
		}
		return out;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
}
