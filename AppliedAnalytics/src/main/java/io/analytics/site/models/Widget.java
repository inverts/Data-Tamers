package io.analytics.site.models;

/**
 * 
 * 
 * @author Dave Wong
 *
 */
public abstract class Widget {

	// A Widgets relation to its frame should be defined by the frame. (No positional data here)
		
	//TODO: Possibly add a method for getting the widget's contents?
	
	//TODO: Add something requiring that the widget have information about how to
	//		update its data - or possibly create an UpdatableWidget model.
	//		Maybe some reference to what API call it needs to make to the system?

	/**
	 * Obtains the height, in units (not pixels) of the Widget.
	 * 
	 * @return The height of the Widget in units.
	 */
	public abstract int getUnitHeight();

	/**
	 * Obtains the width, in units (not pixels) of the Widget.
	 * 
	 * @return The width of the Widget in units.
	 */
	public abstract int getUnitWidth();

	public abstract String getName();
	
	public abstract String getDescription();

	//No setters for - these should not change over the lifetime of the Widget.
	
}
