package io.analytics.domain;

import com.google.gson.Gson;

import io.analytics.site.models.JSONSerializable;

public class WidgetLibraryType extends WidgetType implements JSONSerializable {
	
	private int priority;
	private int isFeatured;
	
	public WidgetLibraryType(int id) {
		super(id);
	}
	
	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	/**
	 * @return the isFeatured
	 */
	public int getIsFeatured() {
		return isFeatured;
	}
	/**
	 * @param isFeatured the isFeatured to set
	 */
	public void setIsFeatured(int isFeatured) {
		this.isFeatured = isFeatured;
	}

	@Override
	public String getJSONSerialization() {
		Gson g = new Gson();
		return g.toJson(this);
	}
	
}
