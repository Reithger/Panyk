package model.trip.schedule;

import java.util.Arrays;
import java.util.HashMap;

public class ScheduledItem implements Schedulable{

	private final static String TYPE = "lString";
	
	private Schedulable next;
	private String title;
	private SchedulableType type;
	private int sizeDatum;
	private int buffer;
	
	/**
	 * 
	 * @param compType
	 * @param buf
	 * @param types - Strings representing what the next Schedulable should be
	 * @param components - String representing what the title of the next Schedulable is
	 * @param datum - String representing what data is stored by the next Schedulable
	 */
	
	public ScheduledItem(SchedulableType typeIn, Object[] datum, int buf) {
		title = "Composite Type";
		type = typeIn;
		next = SchedulableFactory.getScheduleComponent(typeIn.getDataTypes(), typeIn.getTitles(), datum);
		sizeDatum = next.count();
		buffer = buf;
	}
	
	@Override
	public DisplayData getDisplayData(DisplayData fill) {
		if(fill == null)
			fill = new DisplayData(type);
		if(next == null)
			return fill;
		return next.getDisplayData(fill);
	}

	@Override
	public SchedulableType getData() {
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
			type = (SchedulableType)in;
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
		append[plc] = type.getType();
		return next.generateDataType(append, plc);
	}
	
	@Override
	public String[] generateDataEntry(String[] append, int plc) {
		append = new String[sizeDatum + buffer];
		plc = buffer;
		return next.generateDataEntry(append, plc);
	}
}
