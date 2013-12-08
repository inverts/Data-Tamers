package io.analytics.site.models;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

public class ForecastWidgetModel extends LineGraphWidgetModel {

	Integer futureStartX;
	
	public Integer getFutureStartX() {
		return this.futureStartX;
	}
	
	@Override
	public SimpleEntry<Integer, Integer> getXRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimpleEntry<Integer, Integer> getYRange() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> getXValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Integer> getYValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUpdateApiCall() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Forecast";
	}

	@Override
	public String getDescription() {
		return "View a forecast for your data.";
	}
	

}
