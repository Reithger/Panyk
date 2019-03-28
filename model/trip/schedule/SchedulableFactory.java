package model.trip.schedule;

import java.util.Arrays;

public class SchedulableFactory {

	public static Schedulable getScheduleComponent(String[] type, String[] components, Object[] datum) {
		switch(type[0]){
			case "sString": return new SchedulableShortString(Arrays.copyOfRange(type, 1, type.length), Arrays.copyOfRange(components,  1,  components.length), Arrays.copyOfRange(datum, 1, datum.length));
			case "lString": return new SchedulableLongString(Arrays.copyOfRange(type, 1, type.length), Arrays.copyOfRange(components,  1,  components.length), Arrays.copyOfRange(datum, 1, datum.length));
			case "Date": return new SchedulableDate(Arrays.copyOfRange(type, 1, type.length), Arrays.copyOfRange(components,  1,  components.length), Arrays.copyOfRange(datum, 1, datum.length));
			default: System.out.println("Invalid Input for SchedulableFactory"); break;
		}
		return null;
	}
	
}
