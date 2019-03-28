package model.trip.schedule;

import java.util.HashMap;

public class SchedulableLongString implements Schedulable{

	private final static String TYPE = "lString";
	
	private Schedulable next;
	private String title;
	private String data;
	
	public SchedulableLongString(String[] type, String[] components, Object[] datum) {
		title = components[0];
		data = (String)datum[0];
		next = SchedulableFactory.getScheduleComponent(type, components, datum);
	}

	@Override
	public HashMap<String, String> getDisplayData(HashMap<String, String> fill) {
		fill.put(title, data);
		if(next == null)
			return fill;
		return next.getDisplayData(fill);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getData() {
		return data;
	}

	public String getType() {
		return TYPE;
	}
	
	@Override
	public void setData(String provTitle, Object in) {
		if(getTitle().equals(provTitle))
			data = (String)in;
		else
			next.setData(provTitle, in);
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
		append[plc] = data;
		if(next == null)
			return append;
		return next.generateDataEntry(append, plc + 1);
	}
	

	@Override
	public int count() {
		if(next == null)
			return 1;
		return 1 + next.count();
	}
	
}
