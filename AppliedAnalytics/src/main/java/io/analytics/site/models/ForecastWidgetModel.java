package io.analytics.site.models;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.service.CoreReportingService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.google.api.services.analytics.model.GaData;

public class ForecastWidgetModel extends LineGraphWidgetModel {

	private Integer futureStartX;
	private SimpleEntry<Double, Double> xRange;
	private SimpleEntry<Double, Double> yRange;
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

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	//Milliseconds in a day
	private static final int MS_IN_DAY = 86400000;
	/*
	 * Standards:
	 * The future end date is equal to (1/3)(start - end) + end
	 */
	
	public ForecastWidgetModel(CoreReportingService reportingService) {
		this.reportingService = reportingService;
		//Defaults:
		
		metric = CoreReportingRepository.VISITS_METRIC;
		endDate = new Date();
		startDate = new Date(endDate.getTime() - (MS_IN_DAY * 30L)); //30 days advance
		updateFutureEndDate();
		boolean success = updateData();
	}
	
	/**
	 * TODO: REFACTOR!!! This is a huge mess.
	 * TODO: Replace Date with Calendar
	 * 
	 * @return
	 */
	public boolean updateData() {
		//TODO: Add a check so that this doesn't overflow
		//I've done the math, even a 1000-year timespan is acceptable.
		int days = (int) (timeSpan / MS_IN_DAY) + 1;
		CoreReportingData data = null;
		data = reportingService.getMetricByDay(metric, startDate, futureEndDate, days);
		if (data == null)
			return false;
		List<GaData.ColumnHeaders> columns = data.getData().getColumnHeaders();
		List<String> stringColumns = new ArrayList<String>();
		for (GaData.ColumnHeaders columnHeader : columns) {
			stringColumns.add(columnHeader.getName());
		}
		//The data MUST be sorted by day!
		List<List<String>> dataRows = data.getData().getRows();
		if (dataRows.isEmpty())
			return false;
		int metricColumn = stringColumns.indexOf(metric);
		int dayColumn = stringColumns.indexOf(CoreReportingRepository.NDAY_DIMENSION);
		List<GaData.ColumnHeaders> columnHeaders = data.getData().getColumnHeaders();
		//We can use the below to check metric types. But it still isn't very helpful.
		//columnHeaders.get(metricColumn).getDataType()
		xValues = new ArrayList<Double>();
		yValues = new ArrayList<Double>();

		//This is going to do the least-squares calculations for us later.
		SimpleRegression regressionModel = new SimpleRegression();
		Double xMin = Double.parseDouble(dataRows.get(0).get(dayColumn));
		Double xMax = Double.parseDouble(dataRows.get(0).get(dayColumn));
		Double yMin = Double.parseDouble(dataRows.get(0).get(metricColumn));
		Double yMax = Double.parseDouble(dataRows.get(0).get(metricColumn));
		
		try {
			for(List<String> row : dataRows) {
				Double x = Double.parseDouble(row.get(dayColumn));
				Double y = Double.parseDouble(row.get(metricColumn));
				xValues.add(x);
				yValues.add(y);
				if (y == null)
					break;
				if (x > xMax)
					xMax = x;
				else if (x < xMin)
					xMin = x;
				if (y > yMax)
					yMax = y;
				else if (y < yMin)
					yMin = y;
	
				//Add the data to our model.
				regressionModel.addData(x, y);
			}
		} catch (NumberFormatException e) {
			//The metric we are retrieving is not numeric.
			return false;
		}
		
		// Update graph ranges.
		xRange = new SimpleEntry<Double, Double>(xMin, xMax);
		yRange = new SimpleEntry<Double, Double>(yMin, yMax);

		/* Go back in time to get day of week data, and adjust to make sure 
		 * we get an equal sampling of each day.
		 */
		long pastTimeframe = (endDate.getTime() - startDate.getTime()) / MS_IN_DAY;
		pastTimeframe = pastTimeframe + (7 - (pastTimeframe % 7)); 
		Date backInTime = new Date(startDate.getTime() - pastTimeframe * MS_IN_DAY);
		
		data = reportingService.getMetricByDayOfWeek(metric, backInTime, endDate, days * 2);
		if (data == null)
			return false;
		List<GaData.ColumnHeaders> columns2 = data.getData().getColumnHeaders();
		List<String> stringColumns2 = new ArrayList<String>();
		for (GaData.ColumnHeaders columnHeader : columns2) {
			stringColumns2.add(columnHeader.getName());
		}
		List<List<String>> dataRows2 = data.getData().getRows();
		int metricColumn2 = stringColumns2.indexOf(metric);
		int dayColumn2 = stringColumns2.indexOf(CoreReportingRepository.DAYOFWEEK_DIMENSION);
		
		/*
		 * TODO: IMPORTANT! This algorithm is erroneous.
		 * Querying a past set of data needs to be checked to make sure we get an equal
		 * amount of data for each day of the week. For example, querying 8 days in the past
		 * from a Sunday will give us double the visits (or other metric) for Saturday, and 
		 * Saturday will therefore be weighted higher than all the other days.
		 * 
		 * 
		 */
		Double[] adjuster = new Double[7];
		
		//TODO: Remove these asserts later and replace with better code.
		assert(dataRows2.size() == 7);
		Double sum = 0.0;
		for (int i=0; i < 7; i++) {
			assert(Integer.parseInt(dataRows2.get(i).get(dayColumn2)) == i);
			adjuster[i] = Double.parseDouble(dataRows2.get(i).get(metricColumn2));
			sum += adjuster[i];
		}
		Double average = sum / 7.0;

		//Calculate percentage of average.
		if (average != 0) {
			for (int i=0; i < 7; i++) {
				adjuster[i] = adjuster[i] / average;
			}
		} //Default will be all 0 array
		

		/**
		 * Generate future data via a weighted regression line in one swoop.
		 */
		
		Double startOfFuture = xValues.get(xValues.size()-1) + 1;

		xValuesForecast = new ArrayList<Double>();
		yValuesForecast = new ArrayList<Double>();
		//How many days are we looking into the future?
		long daysToPredict = ((futureEndDate.getTime() - endDate.getTime()) / MS_IN_DAY) + 1;
		double square = 0.0;
		int historicalLength = yValues.size();
		Double forecastedMetric = yValues.get(historicalLength-1);
		double slope;
		double ysum = 0, xysum = 0, n = 0, xsum = 0, xsqsum = 0;
		for(int i=0; i<daysToPredict; i++) {
			ysum += yValues.get(historicalLength - i - 1) / adjuster[(i+endDate.getDay()) % 7];
			xysum += xValues.get(historicalLength - i - 1) * yValues.get(historicalLength - i - 1) * (1.0 / adjuster[(i+endDate.getDay()) % 7]);
			n += 1;
			xsum += xValues.get(historicalLength - i - 1);
			xsqsum += xValues.get(historicalLength - i - 1) * xValues.get(historicalLength - i - 1);
			if (i==0)
				continue;
			
			/**
			 * (Dave)
			 * This algorithm & equation for iteratively updating the slope of a linear regression is 
			 * derived by breaking down the ordinary least squares method of linear regression
			 * and turning the matrix operations into scalar operations, then simplifying.
			 * The algorithm aims to base future predictions on an increasing proportion of the
			 * corpus of the historical data, given an increasingly distant future.
			 * An improvement to this algorithm would be to ignore outliers. It also appears to be
			 * overly sensitive to the first 1-3 historical data points.
			 */
			slope = (ysum * -xsum + xysum * n) / (n * xsqsum - xsum * xsum);
			xValuesForecast.add(startOfFuture + i);
			// Y = (Current Nth Day * Slope + Intercept) * Adjuster Percentage
			//forecastedMetric = ((i+startOfFuture) * regressionModel.getSlope() + regressionModel.getIntercept()) * adjuster[(i+endDate.getDay()) % 7];
			
			forecastedMetric = forecastedMetric + slope; //Assumes x step size is 1
			System.out.println("slope: " + slope);
			yValuesForecast.add(forecastedMetric * adjuster[(i+endDate.getDay()) % 7]);
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

	public String getFutureEndDate() {
		return presentationFormat.format(this.futureEndDate);
	}
	private void updateFutureEndDate() {
		timeSpan = endDate.getTime()-startDate.getTime();
		futureEndDate = new Date(endDate.getTime() + (timeSpan / 3));
		
	}
	public Integer getFutureStartX() {
		return this.futureStartX;
	}
	
	public ArrayList<Double> getXValuesForecast() {
		return xValuesForecast;
	}
	
	public ArrayList<Double> getYValuesForecast() {
		return yValuesForecast;
	}

	@Override
	public SimpleEntry<Double, Double> getXRange() {
		return xRange;
	}

	@Override
	public SimpleEntry<Double, Double> getYRange() {
		return yRange;
	}

	/**
	 * Gives a 10% padding on the range.
	 * @return
	 */
	public Double getYRangePadding() {
		return (yRange.getValue() - yRange.getKey()) / 10.0;
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
