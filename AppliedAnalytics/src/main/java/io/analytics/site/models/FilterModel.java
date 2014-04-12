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
	
	/**
	 * Constructs a new FilterModel, automatically populating it with a default metric
	 * of ga:visits, a default start date of 30 days ago, and a default end date of today.
	 */
	public FilterModel() {
		activeMetric = "ga:visits";
		Date now = new Date();
		long nowTime = now.getTime() - MS_IN_DAY;
		long nowMidnight = nowTime - (nowTime % MS_IN_DAY);
		activeEndDate = new Date(nowMidnight); //Yesterday
		activeStartDate = new Date(activeEndDate.getTime() - (MS_IN_DAY * 30L)); //30 days from yesterday
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
