package io.analytics.domain;

import java.util.ArrayList;

/**
 * PagePerformanceData: Domain class for the Page Performance widget.
 * 	 Contains all the Google Analytics data for the Page Performance 
 *   Model computations and for the display of the widget.
 *   
 * @author gak
 *
 */
public class PagePerformanceData {
	private ArrayList<String> pagePath;
	private ArrayList<Integer> visits;
	private ArrayList<Double> visitsBounceRate;
	private ArrayList<Double> exitRate;
	private int visitsTotal;
	
	public PagePerformanceData(){
		pagePath = new ArrayList<String>();
		visits = new ArrayList<Integer>();
		visitsBounceRate = new ArrayList<Double>();
		exitRate = new ArrayList<Double>();
		visitsTotal = -1;
	}
	
	public void setPagePathData(ArrayList<String> data) {
		this.pagePath.clear();
		this.pagePath.addAll(data);
	}
	
	public ArrayList<String> getPagePathData() {
		ArrayList<String> data = new ArrayList<String>(this.pagePath);
		return data;
	}
	
	public void setVisitsData(ArrayList<Integer> data){
		this.visits.clear();
		this.visits.addAll(data);
	}
	
	public ArrayList<Integer> getVisitsData() {
		ArrayList<Integer> data = new ArrayList<Integer>(this.visits);
		return data;
	} 
	
	public void setVisitsBounceRateData(ArrayList<Double> data){
		this.visitsBounceRate.clear();
		this.visitsBounceRate.addAll(data);
	}
	
	public ArrayList<Double> getVisitsBounceRateData() {
		ArrayList<Double> data = new ArrayList<Double>(this.visitsBounceRate);
		return data;
	}
	
	public void setExitRateData(ArrayList<Double> data) {
		this.exitRate.clear();
		this.exitRate.addAll(data);
	}
	
	public ArrayList<Double> getExitRateData() {
		ArrayList<Double> data = new ArrayList<Double>(this.exitRate);
		return data;
	}
	
	public void setVisitsTotal(int datum) {
		this.visitsTotal = datum;
	}
	
	public int getVisitsTotal() {
		return this.visitsTotal;
	}
	

	
}
