package io.analytics.site.models;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class ForecastWidgetModel extends LineGraphWidgetModel {

	Integer futureStartX;
	SimpleEntry<Integer, Integer> xRange;
	SimpleEntry<Integer, Integer> yRange;
	ArrayList<Integer> xValues;
	ArrayList<Integer> yValues;
	String metric;
	
	public ForecastWidgetModel() {
		
	}
	
	public Integer getFutureStartX() {
		return this.futureStartX;
	}
	
	@Override
	public SimpleEntry<Integer, Integer> getXRange() {
		return xRange;
	}

	@Override
	public SimpleEntry<Integer, Integer> getYRange() {
		return yRange;
	}

	@Override
	public ArrayList<Integer> getXValues() {
		return xValues;
	}

	@Override
	public ArrayList<Integer> getYValues() {
		return yValues;
	}

	@Override
	public String getName() {
		return "Forecast";
	}

	@Override
	public String getDescription() {
		return "View a forecast for your data.";
	}

	@Override
	public void setMetric(String metric) {
		this.metric = metric;
	}

	@Override
	public String getMetric() {
		return this.metric;
	}

	@Override
	public String getUpdateApiCall() {
		// TODO Auto-generated method stub
		return null;
	}


}
