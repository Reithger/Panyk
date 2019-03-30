package model.trip.schedule;

import java.util.ArrayList;
import java.util.Arrays;

public class DisplayData {

	SchedulableType scheduleType;
	ArrayList<String> titles;
	ArrayList<String> datum;
	
	public DisplayData(SchedulableType type) {
		if(type == null) {
			throw new IllegalArgumentException("DisplayData given null value");
		}
		scheduleType = type;
		titles = new ArrayList<String>();
		datum = new ArrayList<String>();
	}
	
	public void addData(String title, String data) {
		titles.add(title);
		datum.add(data);
	}
	
	public String getData(String title) {
		title = title.replaceAll(" ", "_");
		if(titles.indexOf(title) == -1) {
			throw new IllegalArgumentException("Invalid Request for Display Data of type: " + title + "\nViable Types: " + Arrays.toString(titles.toArray()));
		}
		return datum.get(titles.indexOf(title));
	}
	
	public SchedulableType getScheduleType() {
		return scheduleType;
	}
	
}
