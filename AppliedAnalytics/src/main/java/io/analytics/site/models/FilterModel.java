package io.analytics.site.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class FilterModel {

	private String activeMetric;
	private Date activeStartDate;
	private Date activeEndDate;

	//Milliseconds in a day
	private static final long MS_IN_DAY = 86400000;
	
	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	public FilterModel() {
		activeMetric = "ga:visits";
		Date now = new Date();
		activeEndDate = now;
		activeStartDate = new Date(now.getTime() - (MS_IN_DAY * 30L));
		
		//activeStartDate = dateFormat.format(now);
		//activeEndDate = dateFormat.format(now);
	}

	public String getActiveMetric() {
		return activeMetric;
	}

	public void setActiveMetric(String activeMetric) {
		this.activeMetric = activeMetric;
	}

	public Date getActiveStartDate() {
		return activeStartDate;
	}
	
	public String getActiveStartDateString() {
		return presentationFormat.format(activeStartDate);
	}

	public void setActiveStartDate(Date activeStartDate) {
		this.activeStartDate = activeStartDate;
	}

	public Date getActiveEndDate() {
		return activeEndDate;
	}
	
	public String getActiveEndDateString() {
		return presentationFormat.format(activeEndDate);
	}

	public void setActiveEndDate(Date activeEndDate) {
		this.activeEndDate = activeEndDate;
	}
	
	
}
