package io.analytics.site.models;

/**
 * 
 * 
 * @author Dave Wong
 *
 */
public abstract class WidgetModel implements JSONSerializable {

		
	/**
	 * Returns some String that informs the View what AJAX call it needs to make to update the model.
	 * TODO: Figure out the syntax of this. We will need to begin developing the API to do this.
	 * 
	 * @return
	 */
	public abstract String getUpdateApiCall();

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

	@Override
	public String getJSONSerialization() {
		return "{error: \"unimplemented\"}";
	}
	//No setters; these should not change over the lifetime of the Widget.
	
}
