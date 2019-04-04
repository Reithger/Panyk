package model.trip.schedule;

import java.util.Arrays;


public class SchedulableShortString implements Schedulable{

	private final static String TYPE = "sString";
	
	private Schedulable next;
	private String title;
	private String data;
	
	public SchedulableShortString(String[] type, String[] components, Object[] datum) {
		title = components[0];
		data = (String)datum[0];
		next = SchedulableFactory.getScheduleComponent(Arrays.copyOfRange(type, 1, type.length),Arrays.copyOfRange(components, 1, components.length),Arrays.copyOfRange(datum, 1, datum.length));
	}

	@Override
	public DisplayData getDisplayData(DisplayData fill) {
		fill.addData(title, data);
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
