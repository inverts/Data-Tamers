package io.analytics.site.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilterModel {

	private String activeMetric;
	private String activeStartDate;
	private String activeEndDate;
	
	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public FilterModel() {
		activeMetric = "ga:visits";
		Date now = new Date();
		activeStartDate = dateFormat.format(now);
		activeEndDate = dateFormat.format(now);
	}

	public String getActiveMetric() {
		return activeMetric;
	}

	public void setActiveMetric(String activeMetric) {
		this.activeMetric = activeMetric;
	}

	public String getActiveStartDate() {
		return activeStartDate;
	}

	public void setActiveStartDate(String activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	public String getActiveEndDate() {
		return activeEndDate;
	}

	public void setActiveEndDate(String activeEndDate) {
		this.activeEndDate = activeEndDate;
	}
	
	
}
