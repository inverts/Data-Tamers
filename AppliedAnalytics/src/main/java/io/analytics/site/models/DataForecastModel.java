package io.analytics.site.models;

import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.*;


public class DataForecastModel extends ForecastWidgetModel {
	
	private JSONObject dataPoints;
	private Integer changePercentage;
	private String dimension;
	private ArrayList<Integer> changePercentageOptions; //Using ArrayList for future flexibility if necessary.
	private String activeProfile;
	
	
	/*
	 * For use in JSP file:
	 * 
	 * var historicalData = JSON.parse('${ hfModel.yValues() }');
	 * var forecastData = JSON.parse('${ hfModel.yValuesForecast() }');
	 * var hypotheticalData = JSON.parse('${ hfModel.???????? }');
	 * 
	 */
	public DataForecastModel(ISessionService sessionService, ICoreReportingService reportingService) {	
		super(sessionService, reportingService);
		this.activeProfile = reportingService.getProfile();
		this.changePercentage = 10;
		this.dataPoints = new JSONObject();
		Integer[] percentageOptions = { 5, 10, 15, 20, 25, 30 };
		changePercentageOptions = new ArrayList<Integer>(Arrays.asList(percentageOptions));
	}

	public String getActiveProfile() {
		return this.activeProfile;
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
		//This does not work for some reason
		//Double[] data =  (Double[]) this.getYValues().toArray();
		Double[] data = new Double[this.getYValues().size() + this.getYValuesForecast().size()];
		int i;
		for (i=0; i<this.getYValues().size(); i++) {
			data[i] = this.getYValues().get(i);
		}
		int k = i;
		for (i=0; i<this.getYValuesForecast().size(); i++) {
			data[i + k] = this.getYValuesForecast().get(i);
		}
		//float[] pts = new float[] { 88,135,111,131,104,139,138,106,102,85,137,139,132,109,114,92,90,149,138,134,108,106,95,97,132,112,104,76,96,91 };
		
		 try {
			 
			 /* Feel free to create categories of data like I did for points. */
			 JSONObject points = new JSONObject();
			 points.put("values", Arrays.toString(data));
			 
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

}
