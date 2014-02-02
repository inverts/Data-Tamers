package io.analytics.domain;

import java.util.Calendar;

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
