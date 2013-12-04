package io.analytics.site.models;

import java.util.Map;
import java.util.TreeMap;

public class HypotheticalFutureModel {
	
	private Map<String, String> changePct;
	private Map<String, String> trafficSrc;
	
	String[] adjustments = { "05", "10", "20", "30" }; // temp, should defined by an enum in a service;
	
	
	public HypotheticalFutureModel(String change, String source) {	
		this.changePct = setDropDownOptions(change, adjustments);
	
	}
	
	
	public Map<String, String> getChangePercentOptions() {
		return this.changePct;
	}
	
	
	
	
	// Helper method for establishing the dropdown options which uses a map to set a boolean
	// for the selected item.
	private Map<String, String> setDropDownOptions (String selected, String[] values) {
		
		Map<String, String> result = new TreeMap<String, String>();
		
		for (String value : values) {
				result.put(value, value.equals(selected) ? "selected" : "");
		}
		
		return result;
		
	}
	

}
