package io.analytics.domain;

import java.util.ArrayList;

public class HypotheticalFutureData {

	private ArrayList<String> trafficSources;
	
	public HypotheticalFutureData(){
		trafficSources = new  ArrayList<String>();
	}
	
	public void setTrafficSources(ArrayList<String> data){
		this.trafficSources.clear();
		this.trafficSources.addAll(data);
	}
	
	public ArrayList<String> getTrafficSources() {
		ArrayList<String> data = new ArrayList<String>(this.trafficSources);
		return data;
	}
}
