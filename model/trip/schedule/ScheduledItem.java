package model.trip.schedule;

import java.util.HashMap;

public class ScheduledItem implements Schedulable{

	private final static String TYPE = "lString";
	
	private Schedulable next;
	private String title;
	private String type;
	private int sizeDatum;
	private int buffer;
	
	public ScheduledItem(String compType, int buf, String[] types, String[] components, Object[] datum) {
		title = "Composite Type";
		type = (String)compType;
		next = SchedulableFactory.getScheduleComponent(types, components, datum);
		sizeDatum = next.count();
		buffer = buf;
	}
	
	@Override
	public HashMap<String, String> getDisplayData(HashMap<String, String> fill) {
		fill.put(title, type);
		if(next == null)
			return fill;
		return next.getDisplayData(fill);
	}

	@Override
	public String getData() {
		return type;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	public String getType() {
		return TYPE;
	}
	
	@Override
	public void setData(String provTitle, Object in) {
		if(getTitle().equals(provTitle))
			type = (String)in;
		else
			next.setData(provTitle, in);
	}
	
	@Override
	public int count() {
		if(next != null)
			return 1 + next.count();
		return 1;
	}

	@Override
	public String[] generateDataType(String[] append, int plc) {
		append = new String[sizeDatum + buffer];
		plc = buffer;
		append[plc] = type;
		return next.generateDataType(append, plc);
	}
	
	@Override
	public String[] generateDataEntry(String[] append, int plc) {
		append = new String[sizeDatum + buffer];
		plc = buffer;
		return next.generateDataEntry(append, plc);
	}
}
