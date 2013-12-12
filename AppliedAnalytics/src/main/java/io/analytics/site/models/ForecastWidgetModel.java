package io.analytics.site.models;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.service.CoreReportingService;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.google.api.services.analytics.model.GaData;

public class ForecastWidgetModel extends LineGraphWidgetModel {

	private Integer futureStartX;
	private SimpleEntry<Integer, Integer> xRange;
	private SimpleEntry<Integer, Integer> yRange;
	private ArrayList<Double> xValues;
	private ArrayList<Double> yValues;
	private ArrayList<Double> xValuesForecast;
	private ArrayList<Double> yValuesForecast;

	//TODO: Add a real start date for sampling data.
	private Date startDate;
	private Date endDate;
	private Date futureEndDate;
	private long timeSpan; //Time between startDate and endDate in milliseconds
	
	private String metric;
	private CoreReportingService reportingService;
	
	//Milliseconds in a day
	private static final int MS_IN_DAY = 86400000;
	/*
	 * Standards:
	 * The future end date is equal to (1/3)(start - end) + end
	 */
	
	public ForecastWidgetModel(CoreReportingService reportingService) {
		this.reportingService = reportingService;
		
	}
	
	/**
	 * TODO: REFACTOR!!!
	 * 
	 * @return
	 */
	public boolean updateData() {
		//TODO: Add a check so that this doesn't overflow
		//I've done the math, even a 1000-year timespan is acceptable.
		int days = (int) (timeSpan / MS_IN_DAY) + 1;
		CoreReportingData data = null;
		try {
			data = reportingService.getMetricByDay(metric, startDate, futureEndDate, days);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		List<GaData.ColumnHeaders> columns = data.getData().getColumnHeaders();
		//The data MUST be sorted by day!
		List<List<String>> dataRows = data.getData().getRows();
		int metricColumn = columns.indexOf(metric);
		int dayColumn = columns.indexOf(CoreReportingRepository.NDAY_DIMENSION);
		List<GaData.ColumnHeaders> columnHeaders = data.getData().getColumnHeaders();
		//We can use the below to check metric types. But it still isn't very helpful.
		//columnHeaders.get(metricColumn).getDataType()
		xValues = new ArrayList<Double>();
		yValues = new ArrayList<Double>();

		//This is going to do the least-squares calculations for us later.
		SimpleRegression regressionModel = new SimpleRegression();
		
		try {
		for(List<String> row : dataRows) {
			Double x = Double.parseDouble(row.get(dayColumn));
			Double y = Double.parseDouble(row.get(metricColumn));
			xValues.add(x);
			yValues.add(y);

			//Add the data to our model.
			regressionModel.addData(x, y);
		}
		} catch (NumberFormatException e) {
			//The metric we are retrieving is not numeric.
			return false;
		}
		/**
		 * Part 2: 
		 * 1. We need to get a sum of day-of-week visits for a similar date range.
		 * 2. We find the average of these sums, and then calculate 7 percentage scales from the average. (one per day)
		 */
		Double startOfFuture = xValues.get(xValues.size()-1);
		//How many days are we looking into the future?
		long daysToPredict = ((futureEndDate.getTime() - endDate.getTime()) / MS_IN_DAY) + 1;

		for(int i=0; i<daysToPredict; i++) {
			//xValuesForecast.add(x);
			//TODO: In progress
			// Y = (Current Nth Day * Slope + Intercept) * Adjuster Percentage
			// Y = ((i+startOfFuture) * regressionModel.getSlope + regressionModel.getIntercept) * adjuster[(i+endDate.dayOfWeek) % 7]
			//yValuesForecast.add(Y);
		}
		
		
		/**
		 * At the end of this function, we should have updated the x and y values with appropriate historical and future data.
		 */

		return true;
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		updateFutureEndDate();
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
		updateFutureEndDate();
	}

	private void updateFutureEndDate() {
		timeSpan = endDate.getTime()-startDate.getTime();
		futureEndDate = new Date(endDate.getTime() + (timeSpan / 3));
		
	}
	public Integer getFutureStartX() {
		return this.futureStartX;
	}
	
	public ArrayList<Double> getxValuesForecast() {
		return xValuesForecast;
	}
	
	public ArrayList<Double> getyValuesForecast() {
		return yValuesForecast;
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
	public ArrayList<Double> getXValues() {
		return xValues;
	}

	@Override
	public ArrayList<Double> getYValues() {
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
