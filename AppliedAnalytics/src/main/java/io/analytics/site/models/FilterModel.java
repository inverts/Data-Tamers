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
		activeEndDate = dateFormat.format(now);
	}
	
	
}
