package io.analytics.site.models;

import io.analytics.service.CoreReportingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;




import org.json.*;


public class HypotheticalFutureModel extends ForecastWidgetModel {
	
	private JSONObject dataPoints;
	private Integer changePercentage;
	private String dimension;
	private ArrayList<Integer> changePercentageOptions; //Using ArrayList for future flexibility if necessary.

	/*
	 * For use in JSP file:
	 * 
	 * var historicalData = JSON.parse('${ hfModel.yValues() }');
	 * var forecastData = JSON.parse('${ hfModel.yValuesForecast() }');
	 * var hypotheticalData = JSON.parse('${ hfModel.???????? }');
	 * 
	 */
	public HypotheticalFutureModel(CoreReportingService reportingService) {	
		super(reportingService);
		
		this.changePercentage = 10;
		this.dataPoints = new JSONObject();
		Integer[] percentageOptions = { 5, 10, 15, 20, 25, 30 };
		changePercentageOptions = new ArrayList<Integer>(Arrays.asList(percentageOptions));
	}

	public ArrayList<Integer> getChangePercentageOptions() {
		return changePercentageOptions;
	}
	
	@Override
	public String getName() {
		return "Hypothetical Future";
	}

	@Override
	public String getDescription() {
		return "View a forecast of your data depending on hypothetical conditions.";
	}
	
	public Integer getChangePercentage() {
		return this.changePercentage;
	}

	public void setChangePercentage(Integer changePercentage) {
		this.changePercentage = changePercentage;
	}
	
	public boolean setChangePercentage(String changePercentage) {
		try {
			this.changePercentage = Integer.parseInt(changePercentage);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public String getDimension() {
		//TODO: Add a check for valid dimensions.
		return this.dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	
	
	
	/* test method to pass data to javascript */
	public JSONObject getDataPoints() {
		
		float[] pts = new float[] { 88,135,111,131,104,139,138,106,102,85,137,139,132,109,114,92,90,149,138,134,108,106,95,97,132,112,104,76,96,91 };
		
		 try {
			 
			 /* Feel free to create categories of data like I did for points. */
			 JSONObject points = new JSONObject();
			 points.put("values", Arrays.toString(pts));
			 
			 /* this.data is the parent object that will contain ALL of
			  * the data that will be processed by the visualization.
			  */
			 this.dataPoints.put("points", points);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return this.dataPoints;
	}
	
	
	
	/*
	// Helper method for establishing the dropdown options which uses a map to set a boolean
	// for the selected item.
	private Map<String, String> setDropDownOptions (String selected, String[] values) {
		
		Map<String, String> result = new TreeMap<String, String>();
		
		for (String value : values) {
				result.put(value, value.equals(selected) ? "selected" : "");
		}
		
		return result;
		
	}
	*/

}
