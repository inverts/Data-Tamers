package io.analytics.site.models;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;


import org.json.*;


public class HypotheticalFutureModel extends ForecastWidgetModel {
	
	private JSONObject data;
	private Map<String, String> changePct;
	private Map<String, String> trafficSrc;
	
	String[] adjustments = { "05", "10", "20", "30" }; // temp, should defined by an enum in a service;


	//TODO: Require the CoreReportingService to create this
	public HypotheticalFutureModel(String change, String source) {	
		this.changePct = setDropDownOptions(change, adjustments);
		
		
		this.data = new JSONObject();
	}
	
	@Override
	public String getName() {
		return "Hypothetical Future";
	}

	@Override
	public String getDescription() {
		return "View a forecast of your data depending on hypothetical conditions.";
	}
	
	
	public Map<String, String> getChangePercentOptions() {
		return this.changePct;
	}
	
	
	
	/* test method to pass data to javascript */
	public JSONObject getVisualization() {
		
		float[] pts = new float[] { 88,135,111,131,104,139,138,106,102,85,137,139,132,109,114,92,90,149,138,134,108,106,95,97,132,112,104,76,96,91 };
		
		 try {
			 
			 /* Feel free to create categories of data like I did for points. */
			 JSONObject points = new JSONObject();
			 points.put("values", Arrays.toString(pts));
			 
			 /* this.data is the parent object that will contain ALL of
			  * the data that will be processed by the visualization.
			  */
			 this.data.put("points", points);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return this.data;
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
