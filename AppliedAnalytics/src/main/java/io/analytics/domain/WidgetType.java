package io.analytics.domain;

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
