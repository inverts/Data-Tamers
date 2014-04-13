package io.analytics.domain;

/**
 *  OverviewData: Domain class for
 *    Contains all the Google Analytics data for the Overview
 *    Model computations and for the display of the Overview 
 *    widget.
 * @author gak
 *
 */
public class OverviewData {
	private int newVisits;
	private double percentNewVisits;
	private int visits;
	private double visitBounceRate;
	private double pageviewsPerVisit;
	private double avgTimePerVisit;  // aka  ga:avgTimeOnSite
	
	public OverviewData(){
		newVisits = -1;
		this.percentNewVisits = -1.0;
		this.visits = -1;
		this.visitBounceRate = -1.0;
		this.pageviewsPerVisit = -1;
		this.avgTimePerVisit = -1.0;
	}
	
	public void setNewVisitsTotal(int datum) {
		this.newVisits = datum;
	}
	
	public int getNewVisitsTotal() {
		return this.newVisits;
	}
	
	public void setPercentNewVisitsTotal(double datum) {
		this.percentNewVisits = datum;
	}
	
	public double getPercentNewVisitsTotal() {
		return this.percentNewVisits;
	}
	
	public void setVisitsTotal(int datum) {
		this.visits = datum;
	}
	
	public int getVisitsTotal() {
		return this.visits;
	}
	
	public void setVisitBounceRateTotal(double datum){
		this.visitBounceRate = datum;
	}
	
	public double getVisitBounceRateTotal() {
		return this.visitBounceRate;
	}

	public void setPageviewsPerVisit(double datum) {
		this.pageviewsPerVisit = datum;
	}
	
	public double getPageviewsPerVisit() {
		return this.pageviewsPerVisit;
	}

	// aka ga:avgTimeOnSite
	public void setAvgTimePerVisit(double datum){
		this.avgTimePerVisit = datum;
	}

	// aka ga:avgTimeOnSite
	public double getAvgTimePerVisit() {
		return this.avgTimePerVisit;
	}
}
