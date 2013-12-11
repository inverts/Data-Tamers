package io.analytics.site.models;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;


public abstract class LineGraphWidgetModel extends WidgetModel {
	
	//TODO: Possibly add in data for a map of colors and legend labels.
	
	public int getUnitHeight() {
		return 2;
	}

	public int getUnitWidth() {
		return 4;
	}

	public abstract SimpleEntry<Integer, Integer> getXRange();
	
	public abstract SimpleEntry<Integer, Integer> getYRange();

	// These may need to be further refined once we figure out more about the visualizations.
	// Note: Future tests should ensure the # of X values is equal to the # of Y values.
	//	Also check that the data labels is either null or long enough.
	public abstract ArrayList<Integer> getXValues();

	public abstract ArrayList<Integer> getYValues();
	
	/**
	 * Retrieves a list of data labels aligned with the X and Y values lists
	 * that can be shown with each data point.
	 * 
	 * @return <code>null</code> if unimplemented/unavailable, a list of label strings otherwise.
	 */
	public ArrayList<String> getDataLabels() {
		return null; // Let child classes implement optionally.
	}
}