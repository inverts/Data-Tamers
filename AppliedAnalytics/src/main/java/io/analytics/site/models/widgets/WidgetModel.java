package io.analytics.site.models.widgets;

import io.analytics.site.models.JSONSerializable;

/**
 * 
 * 
 * @author Dave Wong
 *
 */
public abstract class WidgetModel implements JSONSerializable {


	protected int viewCount = 1;
	
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
	
	
	/**
	 * Default number of views is 1. If more, change in widget level constructor.
	 * @return - the number of views this widget contains.
	 */
	public int getViewCount() {
		return this.viewCount;
	}
	
	//No setters; these should not change over the lifetime of the Widget.
	
}
