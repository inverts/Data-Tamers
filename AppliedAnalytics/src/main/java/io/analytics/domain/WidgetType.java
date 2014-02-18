package io.analytics.domain;

/**
 * Represents a WidgetType entity in the database.
 * A WidgetType, as the name suggests, is a particular type of Widget,
 * but not an actual Widget. A widget type just consists of an identifier,
 * a name, and an optional description to be shown to the user. A WidgetType
 * is what tells the application what kind of model and view to use for a 
 * particular Widget.
 * 
 * @author Dave Wong
 *
 */
public class WidgetType {
	private int id;
	private String name;
	private String description;
	
	public WidgetType() {
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
