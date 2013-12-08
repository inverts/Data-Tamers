package io.analytics.site.models;

/**
 * 
 * 
 * @author Dave Wong
 *
 */
public abstract class WidgetModel {

	// Note: A Widgets relation to its frame should be defined by the frame. (No positional data here)
		
	/**
	 * Returns some String that informs the View what AJAX call it needs to make to update the model.
	 * TODO: Figure out the syntax of this. We will need to begin developing the API to do this.
	 * 
	 * @return
	 */
	public abstract String getUpdateApiCall();
	
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

	/**
	 * A name to describe the widget to the user.
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * A short description of the widget.
	 * @return
	 */
	public abstract String getDescription();

	//No setters; these should not change over the lifetime of the Widget.
	
}
