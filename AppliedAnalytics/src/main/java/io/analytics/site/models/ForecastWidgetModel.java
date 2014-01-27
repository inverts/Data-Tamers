package io.analytics.site.models;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.service.CoreReportingService;
import io.analytics.service.ICoreReportingService;
import io.analytics.service.ISessionService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.google.api.services.analytics.model.GaData;

/**
 * <Description Here>
 * Things that can be displayed/configured:
 * -Date range, time span of range in milliseconds
 * -Extent of forecast
 * -Historical data points
 * -Historical data points, smoothed
 * -Forecasted data points, smoothed
 * -Forecasted data points, with seasonality
 * -(Planned) Forecasted data points, with noise
 * 
 * @author Dave Wong
 *
 */
public class ForecastWidgetModel extends LineGraphWidgetModel {

	private Integer futureStartX; //Remove (redundant)
	private SimpleEntry<Double, Double> xRange; //Remove
	private ArrayList<Double> xValues; //Remove
	private ArrayList<Double> xValuesForecast; //Remove
	
	
	private SimpleEntry<Double, Double> yRange;
	private ArrayList<Double> yValues; //Rename to originalData
	private ArrayList<Double> yValuesForecast; //Rename to dataForecast
	//The exponential smoothing trendline slope at the last data point.
	private double finalTrend = 0.0;
	private ArrayList<Double> yValuesNormalized; //Rename to dataNormalized
	private ArrayList<Double> dataSmoothNormalized;
	private ArrayList<Double> yValuesForecastNormalized; //Rename to dataForecastNormalized

	private Date startDate;
	private Date endDate;
	private Date futureEndDate;
	private Calendar startDateC;
	private long timeSpan; //Time between startDate and endDate in milliseconds
	
	private String metric;
	private ISessionService sessionService;
	private ICoreReportingService reportingService;

	DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	private static final int MS_IN_DAY = 86400000;
	
	public ForecastWidgetModel(ISessionService sessionService, ICoreReportingService reportingService) {
		this.sessionService = sessionService;
		this.reportingService = reportingService;
		//Defaults:
		
		//TODO: Create an ENUM with metrics.
		metric = CoreReportingRepository.VISITS_METRIC;
		endDate = new Date();
		startDate = new Date(endDate.getTime() - (MS_IN_DAY * 30L)); //30 days advance
		startDateC = Calendar.getInstance();
		startDateC.setTime(startDate);
		
		updateFutureEndDate();
		updateData();
	}
	
	public static long getExcessDataPointsNeeded(long daysRequested) {
		final int NUM_WEEKS_DESIRED = 16;
		return Math.max((NUM_WEEKS_DESIRED * 7) - (int) daysRequested, 7 - ((int)daysRequested % 7));
	}
	
	public long getDataPointsRequested() {
		return (timeSpan / MS_IN_DAY) + 1;
	}
	
	/**
	 * TODO: REFACTOR!!! This is a huge mess.
	 * TODO: Replace Date with Calendar
	 * 
	 * @return
	 */
	public boolean updateData() {
		
		
		long days = getDataPointsRequested();
		// 5.8+ million year timespan necessary. Who knows, maybe some people want to know how their website performed when dinosaurs used it.
		if (days > Integer.MAX_VALUE)
			days = Integer.MAX_VALUE;
		
		//Get at least the sample size, and ensure an equal weighting of days
		long excessDaysNeeded = getExcessDataPointsNeeded(days);
		Date adjustedStartDate = new Date(startDate.getTime() - excessDaysNeeded * MS_IN_DAY);
		Calendar adjustedStartDateC = Calendar.getInstance();
		adjustedStartDateC.setTime(adjustedStartDate);
		
		/* Getting data from Google Analytics */
		CoreReportingData data = null;
		data = reportingService.getMetricByDay(sessionService.getCredentials(), sessionService.getUserSettings().getActiveProfile().getId(), metric, adjustedStartDate, futureEndDate, (int) (days + excessDaysNeeded));
		if (data == null)
			return false;
		
		/* Formatting data. */
		List<GaData.ColumnHeaders> columns = data.getData().getColumnHeaders();
		List<String> stringColumns = new ArrayList<String>();
		for (GaData.ColumnHeaders columnHeader : columns) {
			stringColumns.add(columnHeader.getName());
		}
		//Assert that the data MUST be sorted by day!
		List<List<String>> dataRows = data.getData().getRows();
		if (dataRows.isEmpty())
			return false;
		int metricColumn = stringColumns.indexOf(metric);
		int dayColumn = stringColumns.indexOf(CoreReportingRepository.NDAY_DIMENSION);
		//List<GaData.ColumnHeaders> columnHeaders = data.getData().getColumnHeaders();
		
		ArrayList<Double> xValuesTemp = new ArrayList<Double>();
		ArrayList<Double> yValuesTemp = new ArrayList<Double>();
		
		/* Start iterating through the data points */
		Double xMin = Double.parseDouble(dataRows.get(0).get(dayColumn));
		Double xMax = xMin;
		Double yMin = Double.parseDouble(dataRows.get(0).get(metricColumn));
		Double yMax = yMin;

		SummaryStatistics totalStats = new SummaryStatistics();
		int startingDayOfWeek = (adjustedStartDateC.get(Calendar.DAY_OF_WEEK) - 1);
		int currentDayOfWeek = startingDayOfWeek;
		ArrayList<double[]> dayOfWeekTotals = new ArrayList<double[]>();
		double[] week = new double[7];
		try {
			for (int i=0; i < dataRows.size(); i++) {
				Double x = Double.parseDouble(dataRows.get(i).get(dayColumn));
				Double y = Double.parseDouble(dataRows.get(i).get(metricColumn));
				
				/* Prepare for determining day-of-week seasonality */
				week[currentDayOfWeek] = y;
				if (currentDayOfWeek == (startingDayOfWeek + 6) % 7) {
					dayOfWeekTotals.add(week);
					week = new double[7];
				}
				currentDayOfWeek = (currentDayOfWeek + 1) % 7;

				/* Collect statistics */
				totalStats.addValue(y);
				
				/* Operations performed on only visible data */
				if (i >= excessDaysNeeded) {
					xValuesTemp.add(x);
					yValuesTemp.add(y);

					/* Determine minimum and maximum values */
					if (x > xMax)
						xMax = x;
					else if (x < xMin)
						xMin = x;
					if (y > yMax)
						yMax = y;
					else if (y < yMin)
						yMin = y;
				}
	
			}
		} catch (NumberFormatException e) {
			//The metric we are retrieving is not numeric. Bail out, malformed data.
			System.err.println("Encountered an error with non-numeric data from Google Analytics. Data update aborted.");
			return false;
		}
		double[] adjustment = new double[7];
		for (double[] adjustments : dayOfWeekTotals) 
			for(int i=0; i < adjustments.length; i++)
				adjustment[i] += adjustments[i];

		for(int i=0; i < adjustment.length; i++) 
			adjustment[i] = adjustment[i] / (double) (dayOfWeekTotals.size() * totalStats.getMean());
		

		System.out.println("Std Deviation: " + totalStats.getStandardDeviation());
		System.out.println("Mean: " + totalStats.getMean());
		
		//Once we're sure the set is complete, update the model.
		xValues = new ArrayList<Double>(xValuesTemp);
		yValues = new ArrayList<Double>(yValuesTemp);
		
		// Update graph ranges.
		xRange = new SimpleEntry<Double, Double>(xMin, xMax);
		yRange = new SimpleEntry<Double, Double>(yMin, yMax);

		/*
		double square = 0.0;
		Double forecastedMetric = yValues.get(historicalLength-1);
		double slope;
		double ysum = 0, xysum = 0, n = 0, xsum = 0, xsqsum = 0;
		for(int i=0; i<daysToPredict; i++) {
			ysum += yValuesNormalized.get(historicalLength - i - 1);
			xysum += xValues.get(historicalLength - i - 1) * yValuesNormalized.get(historicalLength - i - 1);
			n += 1;
			xsum += xValues.get(historicalLength - i - 1);
			xsqsum += xValues.get(historicalLength - i - 1) * xValues.get(historicalLength - i - 1);
			if (i==0)
				continue;
			
			**
			 * (Dave)
			 * This algorithm & equation for iteratively updating the slope of a linear regression is 
			 * derived by breaking down the ordinary least squares method of linear regression
			 * and turning the matrix operations into scalar operations, then simplifying.
			 * The algorithm aims to base future predictions on an increasing proportion of the
			 * corpus of the historical data, given an increasingly distant future.
			 * An improvement to this algorithm would be to ignore outliers. It also appears to be
			 * overly sensitive to the first 1-3 historical data points.
			 *
			slope = (ysum * -xsum + xysum * n) / (n * xsqsum - xsum * xsum);
			xValuesForecast.add(startOfFuture + i);
			// Y = (Current Nth Day * Slope + Intercept) * Adjuster Percentage
			//forecastedMetric = ((i+startOfFuture) * regressionModel.getSlope() + regressionModel.getIntercept()) * adjuster[(i+endDate.getDay()) % 7];
			
			forecastedMetric = forecastedMetric + slope; //Assumes x step size is 1
			double normalizedMetric = forecastedMetric * adjustment[(i+endDate.getDay()) % 7];
			if (normalizedMetric < 0.0)
				normalizedMetric = 0.0;
			yValuesForecastNormalized.add(normalizedMetric);
			yValuesForecast.add(forecastedMetric);
		}
		*/

		//How many days are we looking into the future?
		long daysToPredict = ((futureEndDate.getTime() - endDate.getTime()) / MS_IN_DAY) + 1;
		yValuesForecast = new ArrayList<Double>();
		yValuesNormalized = new ArrayList<Double>();

		//Adjust for seasonality
		for (int i=0; i < yValues.size(); i++) 
			yValuesNormalized.add(yValues.get(i) / adjustment[(startDateC.get(Calendar.DAY_OF_WEEK) + i - 1) % 7]);

		dataSmoothNormalized = updateSmoothedData(yValuesNormalized, 0.45, 0.3);
		yValuesForecastNormalized = updateForecast(yValuesNormalized, (int) daysToPredict);
		
		double forecastMax = 0.0;
		double forecastMin = 0.0;
		for (int i=0; i < yValuesForecastNormalized.size(); i++) {
			yValuesForecast.add(yValuesForecastNormalized.get(i) * adjustment[(startDateC.get(Calendar.DAY_OF_WEEK) + i) % 7]);
			if (yValuesForecast.get(i) > forecastMax)
				forecastMax = yValuesForecast.get(i);
			else if (yValuesForecast.get(i) < forecastMin)
				forecastMin = yValuesForecast.get(i);
		
		}
		
		//Update the y range to fit the forecast.
		yRange = new SimpleEntry<Double, Double>(Math.min(yMin, forecastMin), Math.max(yMax, forecastMax));
		

		//yValues = yValuesNormalized;
		//yValuesForecast = yValuesForecastNormalized;
		
		return true;
	}
	
	/**
	 * Uses a two-pass double exponential smoothing algorithm to smooth out
	 * a set of data. The first pass may introduce lag into the smoothing,
	 * so the second pass goes in the reverse direction, reversing the lag.
	 * Since two passes are made, choices for alpha and beta values may need 
	 * to be higher than they would for a single pass.
	 * This also updates a private member variable that stores the slope
	 * of the trend line generated at the end of the smoothing process.
	 * 
	 * @param data
	 * @param alpha
	 * @param beta
	 * @return
	 */
	private ArrayList<Double> updateSmoothedData(ArrayList<Double> data, double alpha, double beta) {
		int N = data.size();
		if (N < 2)
			return data;
		Double[] result = new Double[N];
		
		/* First do a backward pass. */
		double smoothedLast;
		double slope = data.get(N-2) - data.get(N-1);
		double smoothedCurrent = data.get(N-2);

		result[N-1] = data.get(N-1);
		result[N-2] = smoothedCurrent;
		
		for (int i=N-3; i > -1; i--) {
			smoothedLast = smoothedCurrent;
			smoothedCurrent = alpha * data.get(i) + (1 - alpha) * (smoothedLast + slope);
			slope = beta * (smoothedCurrent - smoothedLast) + (1 - beta) * slope;
			result[i] = Math.max(0, smoothedCurrent);
		}
		
		slope = result[1] - result[0];
		smoothedCurrent = result[1];
		
		for (int i=2; i < data.size(); i++) {
			smoothedLast = smoothedCurrent;
			smoothedCurrent = alpha * result[i] + (1 - alpha) * (smoothedLast + slope);
			slope = beta * (smoothedCurrent - smoothedLast) + (1 - beta) * slope;
			result[i] = Math.max(0, smoothedCurrent);
		}
		
		finalTrend = slope;
		dataSmoothNormalized = new ArrayList<Double>(Arrays.asList(result));
		
		return dataSmoothNormalized;
	}
	
	/**
	 * Updates the forecast we have saved, given a set of data.
	 * The option to present a particular set of data, instead of just
	 * the original data set member of this object, allows for creating
	 * a forecast from varying sets of data - perhaps even hypothetical.
	 * 
	 * @param data
	 * @param length
	 * @return
	 */
	private ArrayList<Double> updateForecast(ArrayList<Double> data, int length) {
		
		yValuesForecastNormalized = new ArrayList<Double>(length);
		
		int N = data.size();
		double slope = 0.0;
		double ysum = 0, xysum = 0, n = 0, xsum = 0, xsqsum = 0;
		SimpleRegression reg = new SimpleRegression();

		/**
		 * (Dave)
		 * This equation for iteratively updating the slope of a linear regression is 
		 * derived by breaking down the ordinary least squares method of linear regression
		 * and turning the matrix operations into scalar operations, then simplifying.
		 */
		for(int i=0; i < N; i++) {
			reg.addData(i, data.get(i));
			ysum += data.get(N - i - 1);
			xysum += (N - i - 1) * data.get(N - i - 1);
			n += 1;
			xsum += N - i - 1;
			xsqsum += (N - i - 1) * (N - i - 1);
			if (i==0)
				continue;
			slope = (ysum * -xsum + xysum * n) / (n * xsqsum - xsum * xsum);
			
		}
		double minMSE = 0.0, error, lastMSE, currentMSE = 0.0, breakpoint = 0;
		for (int i=1; i < length; i++) {
			error = Math.pow(data.get(N-1-i) - (data.get(N-1) - (i * finalTrend)), 2);
			lastMSE = currentMSE;
			currentMSE = lastMSE * (i/(i+1)) + (error / (i+1));
			if (currentMSE < minMSE || i <= 3) //Record first MSE 3 data points in
				minMSE = currentMSE;
			else if (currentMSE > 3 * minMSE) {
				breakpoint = i;
				break;
			}
		}
		System.out.println("Slope: " + slope + "\n REG Slope: " + reg.getSlope());
		slope = reg.getSlope();
		Double lastPoint = data.get(N-1);
		System.out.println("slope: " + slope);
		System.out.println("finalTrend: " + finalTrend);
		double percentComplete;
		for(double i=0; i < length; i++) {
			//percentComplete = i / (double) length;
			//output.add(lastPoint + (i+1) * finalTrend);
			//lastPoint = Math.max(0, lastPoint + (percentComplete * slope + (1 - percentComplete) * finalTrend));
			if(i < breakpoint)
				percentComplete = i / (2 * breakpoint);
			else
				percentComplete = 0.5 + i / (2 * (length - breakpoint));
			lastPoint = Math.max(0, lastPoint + (percentComplete * slope + (1 - percentComplete) * finalTrend));
			yValuesForecastNormalized.add(lastPoint);
		}
		
		return yValuesForecastNormalized;
		
	}
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
		this.startDateC.setTime(startDate);
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
	
	public boolean setFutureEndDate(String futureEndDate) {
		try {
			this.futureEndDate = presentationFormat.parse(futureEndDate);
			return true;
		} catch (java.text.ParseException e) {
			//Invalid Date format.
			return false;
		}
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
	
	public ArrayList<Double> getyValuesNormalized() {
		return yValuesNormalized;
	}

	public ArrayList<Double> getyValuesForecastNormalized() {
		return yValuesForecastNormalized;
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
	 * Gives a 20% padding on the range.
	 * @return
	 */
	public Double getYRangePadding() {
		return (yRange.getValue() - yRange.getKey()) / 20.0;
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
