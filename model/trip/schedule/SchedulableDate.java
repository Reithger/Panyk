package model.trip.schedule;

import java.util.Date;
import java.util.HashMap;

public class SchedulableDate implements Schedulable{

	private final static String TYPE = "Date";
	private final static String[] MONTHS = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
	private Schedulable next;
	private String title;
	private Date data;
	
	public SchedulableDate(String[] type, String[] components, Object[] datum) {
		title = components[0];
		data = (Date)datum[0];
		next = SchedulableFactory.getScheduleComponent(type, components, datum);
	}

	@Override
	public HashMap<String, String> getDisplayData(HashMap<String, String> fill) {
		fill.put(title, simplifyDate(data));
		if(next == null)
			return fill;
		return next.getDisplayData(fill);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public Object getData() {
		return data;
	}	
	
	public String getType() {
		return TYPE;
	}
	
	@Override
	public void setData(String provTitle, Object in) {
		if(getTitle().equals(provTitle))
			data = (Date)in;
		else
			next.setData(provTitle, in);
	}
	
	public String simplifyDate(Date in) {
		String[] hold = in.toString().split(" ");
		return hold[2] + "/" + indexOf(MONTHS, hold[1]) + "/" + hold[5];
	}
	
	public int indexOf(String[] arr, String key) {
		for(int i = 0; i < arr.length; i++)
			if(arr[i].equals(key))
				return i;
		return -1;
	}

	
	@Override
	public int count() {
		if(next != null)
			return 1 + next.count();
		return 1;
	}


	@Override
	public String[] generateDataType(String[] append, int plc) {
		append[plc] = title;
		if(next == null)
			return append;
		return next.generateDataType(append, plc + 1);
	}

	@Override
	public String[] generateDataEntry(String[] append, int plc) {
		append[plc] = simplifyDate(data);
		if(next == null)
			return append;
		return next.generateDataEntry(append, plc + 1);
	}
	
	
}
