package io.analytics.domain;

import java.util.ArrayList;
import java.util.List;

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
	private List<String> channels;
	private List<Integer> channelNewVisits;
	private List<Double> channelPercentNewVisits;
	private List<Integer> channelVisits;
	private List<Double> channelVisitBounceRate;
	private List<Double> channelPageviewsPerVisit;
	private List<Double> channelAvgTimePerVisit;  // aka  ga:avgTimeOnSite
	
	public OverviewData(){
		this.newVisits = -1;
		this.percentNewVisits = -1.0;
		this.visits = -1;
		this.visitBounceRate = -1.0;
		this.pageviewsPerVisit = -1;
		this.avgTimePerVisit = -1.0;
		this.channels = new ArrayList<String>();
		this.channelNewVisits = new ArrayList<Integer>();
		this.channelPercentNewVisits = new ArrayList<Double>();
		this.channelVisits = new ArrayList<Integer>();
		this.channelVisitBounceRate = new ArrayList<Double>();
		this.channelPageviewsPerVisit = new ArrayList<Double>();
		this.channelAvgTimePerVisit = new ArrayList<Double>();
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
	
	public void setChannels(ArrayList<String> data) {
		this.channels.clear();
		this.channels.addAll(data);
	}
	
	public ArrayList<String> getChannels() {
		ArrayList<String> data = new ArrayList<String>(this.channels);
		return data;
	}
	
	public void setChannelNewVisits(ArrayList<Integer> data) {
		this.channelNewVisits.clear();
		this.channelNewVisits.addAll(data);
	}
	
	public ArrayList<Integer> getChannelNewVisits() {
		ArrayList<Integer> data = new ArrayList<Integer>(this.channelNewVisits);
		return data;
	}
	
	public void setChannelPercentNewVisits(ArrayList<Double> data) {
		this.channelPercentNewVisits.clear();
		this.channelPercentNewVisits.addAll(data);
	}
	
	public ArrayList<Double> getChannelPercentNewVisits() {
		ArrayList<Double> data = new ArrayList<Double>(this.channelPercentNewVisits);
		return data;
	}
	
	public void setChannelVisits(ArrayList<Integer> data) {
		this.channelVisits.clear();
		this.channelVisits.addAll(data);
	}
	
	public ArrayList<Integer> getChannelVisits() {
		ArrayList<Integer> data = new ArrayList<Integer>(this.channelVisits);
		return data;
	}
	
	public void setChannelVisitBounceRate(ArrayList<Double> data) {
		this.channelVisitBounceRate.clear();
		this.channelVisitBounceRate.addAll(data);
	}
	
	public ArrayList<Double> getChannelVisitBounceRate() {
		ArrayList<Double> data = new ArrayList<Double>(this.channelVisitBounceRate);
		return data;
	}
	
	public void setChannelPageviewsPerVisit(ArrayList<Double> data) {
		this.channelPageviewsPerVisit.clear();
		this.channelPageviewsPerVisit.addAll(data);
	}
	
	public ArrayList<Double> getChannelPageviewsPerVisit() {
		ArrayList<Double> data = new ArrayList<Double>(this.channelPageviewsPerVisit);
		return data;
	}
	
	public void setChannelAvgTimePerVisit(ArrayList<Double> data) {
		this.channelAvgTimePerVisit.clear();
		this.channelAvgTimePerVisit.addAll(data);
	}
	
	public ArrayList<Double> getChannelAvgTimePerVisit() {
		ArrayList<Double> data = new ArrayList<Double>(this.channelAvgTimePerVisit);
		return data;
	}
}
