package io.analytics.domain;

import java.util.Calendar;

/**
 * This represents a Filter in our application.
 * A Filter is what governs/guides the data that is shown in Widgets. It helps a
 * Widget customize its display to be more relevant to the user who chose a particular
 * Filter. A Filter has a single parent Account that is its owner, but can be used by 
 * many Dashboards and Widgets.
 * 
 * @author Dave Wong
 *
 */
public class Filter {

	private int id;
	private int parentAccountId;
	private Calendar startDate;
	private Calendar endDate;
	private String interestMetric;
	private String googleProfileId;
	
	public Filter (int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public int getParentAccountId() {
		return parentAccountId;
	}
	public void setParentAccountId(int parentAccountId) {
		this.parentAccountId = parentAccountId;
	}
	public Calendar getStartDate() {
		return startDate;
	}
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	public Calendar getEndDate() {
		return endDate;
	}
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	public String getInterestMetric() {
		return interestMetric;
	}
	public void setInterestMetric(String interestMetric) {
		this.interestMetric = interestMetric;
	}
	public String getGoogleProfileId() {
		return googleProfileId;
	}
	public void setGoogleProfileId(String googleProfileId) {
		this.googleProfileId = googleProfileId;
	}
	
	
}
