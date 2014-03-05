package io.analytics.site.models.widgits;

import io.analytics.site.models.JSONSerializable;

/**
 * 
 * 
 * @author Dave Wong
 *
 */
public abstract class WidgetModel implements JSONSerializable {


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
	
	/**
	 * 
	 * @return
	 */
	public abstract int getPositionPriority();

	/*@Override
	public String getJSONSerialization() {
		return "{error: \"unimplemented\"}";
	}*/
	
	/**
	 * 
	 * @return - class name for the Widget
	 */
	public abstract String getHTMLClass();
	
	/**
	 * 
	 * @return - string.properties path to the widget title.
	 */
	public abstract String getTitle();
	
	//No setters; these should not change over the lifetime of the Widget.
	
}
