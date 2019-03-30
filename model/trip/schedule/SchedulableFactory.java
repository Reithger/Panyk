package model.trip.schedule;

import java.util.Arrays;

public class SchedulableFactory {

	public static Schedulable getScheduleComponent(String[] type, String[] components, Object[] datum) {
		if(type.length == 0)
			return null;
		switch(type[0]){
			case "sString": return new SchedulableShortString(type, components, datum);
			case "lString": return new SchedulableLongString(type, components, datum);
			case "Date": return new SchedulableDate(type, components, datum);
			default: System.out.println("Invalid Input for SchedulableFactory"); break;
		}
		return null;
	}
	
}
