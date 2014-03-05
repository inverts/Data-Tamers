package io.analytics.site.models.widgits;

import io.analytics.site.models.IMetricDependent;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;


public abstract class LineGraphWidgetModel extends WidgetModel implements IMetricDependent {
	

	public abstract SimpleEntry<Double, Double> getXRange();
	
	public abstract SimpleEntry<Double, Double> getYRange();

	// These may need to be further refined once we figure out more about the visualizations.
	// Note: Future tests should ensure the # of X values is equal to the # of Y values.
	//	Also check that the data labels is either null or long enough.
	public abstract ArrayList<Double> getXValues();

	public abstract ArrayList<Double> getYValues();
	
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