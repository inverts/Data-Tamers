package io.analytics.site.models;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Interacting with the ForecastWidgetModel:
 * Update properties of the model, like:
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
        private ArrayList<Double> dataOriginal;
        private ArrayList<Double> dataForecast; 
        
        //The exponential smoothing trendline slope at the last data point.
        private double finalTrend = 0.0;
        private ArrayList<Double> dataNormalized;
        private ArrayList<Double> dataSmoothNormalized;
        private ArrayList<Double> dataForecastNormalized;

        private Date startDate;
        private Date endDate;
        private Date futureEndDate;
        private Calendar startDateC;
        private Calendar endDateC;
        private long timeSpan; //Time between startDate and endDate in milliseconds
        
        //TODO: This should come from the filter:
        private String metric = CoreReportingRepository.VISITS_METRIC;
        private static final int NUM_WEEKS_NEEDED = 16;

		private ISessionService sessionService;
        private ICoreReportingService reportingService;

        DateFormat presentationFormat = new SimpleDateFormat("MM/dd/yyyy");
        
        private static final int MS_IN_DAY = 86400000;
        private static final double DEFAULT_ALPHA = 0.45;
        private static final double DEFAULT_BETA = 0.3;
        
        public ForecastWidgetModel() {
        	super();
        }
        /**
         * Creates a new ForecastWidgetModel.
         * 
         * @param sessionService
         * @param reportingService
         */
        public ForecastWidgetModel(ISessionService sessionService, ICoreReportingService reportingService) {
                this.sessionService = sessionService;
                this.reportingService = reportingService;
                
                endDate = new Date();
                endDateC = Calendar.getInstance();
                startDate = new Date(endDate.getTime() - (MS_IN_DAY * 30L)); //30 days advance
                startDateC = Calendar.getInstance();
                startDateC.setTime(startDate);
                
                updateFutureEndDate();
                updateData();
        }
        
        public static long getExcessDataPointsNeeded(long dataRequested) {
                return Math.max((NUM_WEEKS_NEEDED * 7) - (int) dataRequested, 7 - ((int)dataRequested % 7));
        }
        
        public long getDataPointsRequested() {
                return (timeSpan / MS_IN_DAY) + 1;
        }
        
        /**
         * TODO: REFACTOR!!! This is a huge mess.
         * TODO: Replace Date with Calendar
         * 
         * What are we doing here?
         * -Figuring out what data we actually need, given information in the model.
         * -Actually making a call to get the data.
         * -Fetching the actual pertinent pieces of info/data from the API return for processing.
         * -Collecting statistics and information about the data.
         * -Generate the forecast and smoothed data sets.
         * -Place all the data we have in variables to be accessed.
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
                Calendar adjustedStartDateC = Calendar.getInstance();
                adjustedStartDateC.setTime(new Date(startDate.getTime() - excessDaysNeeded * MS_IN_DAY));
                
                /* Getting data from Google Analytics */
                 CoreReportingData data = reportingService.getMetricByDay(sessionService.getCredentials(), 
							                		sessionService.getUserSettings().getActiveProfile().getId(), 
							                		metric, adjustedStartDateC.getTime(), 
							                		futureEndDate, 
							                		(int) (days + excessDaysNeeded));
                if (data == null) return false;
                
                /* Formatting data. */
                List<GaData.ColumnHeaders> columns = data.getData().getColumnHeaders();
                int metricColumn = -1;
                int dayColumn = -1;
                for (int i = 0; i < columns.size(); i++)
                	if (columns.get(i).getName().equals(metric))
                		metricColumn = i;
                	else if (columns.get(i).getName().equals(CoreReportingRepository.NDAY_DIMENSION))
                		dayColumn = i;
                
                //Assert that the data MUST be sorted by day!
                List<List<String>> dataRows = data.getData().getRows();
                if (dataRows.isEmpty())
                        return false;
                
               
                

                /* Start iterating through the data points */
                
                ArrayList<Double> xValuesTemp = new ArrayList<Double>();
                ArrayList<Double> yValuesTemp = new ArrayList<Double>();
                
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
                                        if (x > xMax)		xMax = x;
                                        else if (x < xMin)	xMin = x;
                                        if (y > yMax)		yMax = y;
                                        else if (y < yMin)	yMin = y;
                                }
        
                        }
                } catch (NumberFormatException e) {
                        //The metric we are retrieving is not numeric. Bail out, malformed data.
                        System.err.println("Encountered an error with non-numeric data from Google Analytics. Data update aborted.");
                        return false;
                }
                
                
                
                
                double[] adjustment = new double[7];
                //Sum up the metric for days of the week.
                for (double[] adjustments : dayOfWeekTotals) 
                        for(int i=0; i < adjustments.length; i++)
                                adjustment[i] += adjustments[i];

                //Create the adjustments from average
                double average = (double) (dayOfWeekTotals.size() * totalStats.getMean());
                
                for(int i=0; i < adjustment.length; i++) 
                	if (average == 0.0)
                		adjustment[i] = 1.0;
                	else
                        adjustment[i] = adjustment[i] / average;
                

                //System.out.println("Std Deviation: " + totalStats.getStandardDeviation());
                //System.out.println("Mean: " + totalStats.getMean());
                
                //Once we're sure the set is complete, update the model.
                xValues = new ArrayList<Double>(xValuesTemp);
                dataOriginal = new ArrayList<Double>(yValuesTemp);
                
                // Update graph ranges.
                xRange = new SimpleEntry<Double, Double>(xMin, xMax);
                yRange = new SimpleEntry<Double, Double>(yMin, yMax);

                //How many days are we looking into the future?
                long daysToPredict = ((futureEndDate.getTime() - endDate.getTime()) / MS_IN_DAY) + 1;
                dataForecast = new ArrayList<Double>();
                dataNormalized = new ArrayList<Double>();

                //Adjust for seasonality
                for (int i=0; i < dataOriginal.size(); i++) 
                	dataNormalized.add(dataOriginal.get(i) / adjustment[(startDateC.get(Calendar.DAY_OF_WEEK) + i - 1) % 7]);

                SimpleEntry<ArrayList<Double>, Double> dataAndSlope = smoothData(dataNormalized, this.DEFAULT_ALPHA, this.DEFAULT_BETA);
                if (dataAndSlope == null)
                	dataSmoothNormalized = new ArrayList<Double>();
                else 
                	dataSmoothNormalized = dataAndSlope.getKey();
                
                dataForecastNormalized = updateForecast(dataNormalized, (int) daysToPredict, dataAndSlope.getValue());
                if (dataForecastNormalized == null)
                	dataForecastNormalized = new ArrayList<Double>();
                
                double forecastMax = 0.0;
                double forecastMin = 0.0;
                for (int i=0; i < dataForecastNormalized.size(); i++) {
                	dataForecast.add(dataForecastNormalized.get(i) * adjustment[(startDateC.get(Calendar.DAY_OF_WEEK) + i) % 7]);
                        if (dataForecast.get(i) > forecastMax)
                                forecastMax = dataForecast.get(i);
                        else if (dataForecast.get(i) < forecastMin)
                                forecastMin = dataForecast.get(i);
                
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
         * See http://en.wikipedia.org/wiki/Exponential_smoothing#Double_exponential_smoothing for
         * more on double exponential smoothing.
         * 
         * @param data
         * @param alpha Must be a double between 0 and 1.
         * @param beta Must be a double between 0 and 1.
         * @return A SimpleEntry containing a smoothed data ArrayList<Double> as the key, and a double as the value
         * indicating the slope at the last data point from exponential smoothing (this is important for forecasting).
         */
        private SimpleEntry<ArrayList<Double>, Double> smoothData(ArrayList<Double> data, double alpha, double beta) {
                int N = data.size();
                if (N < 2)
                        return new SimpleEntry<ArrayList<Double>, Double>(data, 0.0);
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
                        //Special to our application - do not allow negative values.
                        result[i] = Math.max(0, smoothedCurrent); 
                }
                
                /* Now, do a forward pass. */
                slope = result[1] - result[0];
                smoothedCurrent = result[1];
                
                for (int i=2; i < data.size(); i++) {
                        smoothedLast = smoothedCurrent;
                        smoothedCurrent = alpha * result[i] + (1 - alpha) * (smoothedLast + slope);
                        slope = beta * (smoothedCurrent - smoothedLast) + (1 - beta) * slope;
                        result[i] = Math.max(0, smoothedCurrent);
                }
                
                SimpleEntry<ArrayList<Double>, Double> results = 
                		new SimpleEntry<ArrayList<Double>, Double>(new ArrayList<Double>(Arrays.asList(result)), slope);
                return results;
        }
        
        /**
         * Updates the forecast we have saved, given a set of data.
         * The option to present a particular set of data, instead of just
         * the original data set member of this object, allows for creating
         * a forecast from varying sets of data - perhaps even hypothetical.
         * 
         * This assumes that the data provided is already normalized.
         * 
         * @param data A normalized (free of seasonal trends) set of data to forecast.
         * @param length
         * @return
         */
        private ArrayList<Double> updateForecast(ArrayList<Double> data, int length, double lastSlope) {
                
        	dataForecastNormalized = new ArrayList<Double>(length);
                
                int N = data.size();
                double slope = 0.0;
                double ysum = 0, xysum = 0, n = 0, xsum = 0, xsqsum = 0;
                SimpleRegression reg = new SimpleRegression();

                /**
                 * (Dave)
                 * This equation for iteratively updating the slope of a linear regression is 
                 * derived by breaking down the ordinary least squares method of linear regression
                 * and turning the matrix operations into scalar operations, then simplifying.
                 * This is useful for a constantly redefined slope, but we can otherwise use 
                 * SimpleRegression for simple slope statistics.
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
                        error = Math.pow(data.get(N-1-i) - (data.get(N-1) - (i * lastSlope)), 2);
                        lastMSE = currentMSE;
                        currentMSE = lastMSE * (i/(i+1)) + (error / (i+1));
                        if (currentMSE < minMSE || i <= 3) //Record first MSE 3 data points in
                                minMSE = currentMSE;
                        else if (currentMSE > 3 * minMSE) {
                                breakpoint = i;
                                break;
                        }
                }
                //System.out.println("Slope: " + slope + "\n REG Slope: " + reg.getSlope());
                slope = reg.getSlope();
                Double lastPoint = data.get(N-1);
                //System.out.println("slope: " + slope);
                //System.out.println("finalTrend: " + lastSlope);
                if (Double.isNaN(slope))
                	return null;
                
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
                        dataForecastNormalized.add(lastPoint);
                }
                
                return dataForecastNormalized;
                
        }
        
        /**
         * Returns the starting date for the window of historical data.
         * @return
         */
        public Date getStartDate() {
                return startDate;
        }

        /**
         * Specifies the starting date for the window of historical data.
         * Effects do not take place until updateData() is called.
         * @param startDate
         */
        public void setStartDate(Date startDate) {
                this.startDate = startDate;
                this.startDateC.setTime(startDate);
                updateFutureEndDate();
        }
        
        /**
         * Returns the last date at which historical data is available in the model.
         * @return The last date at which historical data is available in the model.
         */
        public Date getEndDate() {
                return endDate;
        }

        /**
         * Specifies the last date at which historical data should be available in the model.
         * Effects do not take place until updateData() is called.
         * @param endDate
         * @return True on success, false if the String could not be parsed into a Date.
         */
        public void setEndDate(Date endDate) {
                this.endDate = endDate;
                this.endDateC.setTime(endDate);
                updateFutureEndDate();
        }

        /**
         * Returns the last date at which future data is available in the model
         * @return The last date at which future data is available in the model
         */
        public String getFutureEndDate() {
                return presentationFormat.format(this.futureEndDate);
        }
        
        /**
         * Specifies the last date at which future data should be available in the model.
         * Effects do not take place until updateData() is called.
         * @param futureEndDate
         * @return True on success, false if the String could not be parsed into a Date.
         */
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
                futureEndDate = new Date(endDate.getTime() + (timeSpan));
                
        }
        
        /**
         * @deprecated
         * @return
         */
        public Integer getFutureStartX() {
                return this.futureStartX;
        }
        
        /**
         * 
         * @return A list of x values.
         */
        //TODO: Rename method
        public ArrayList<Double> getXValuesForecast() {
                return xValuesForecast;
        }

        /**
         * Gives the forecasted values available.
         * @return
         */
        //TODO: Rename method
        public ArrayList<Double> getYValuesForecast() {
                return dataForecast;
        }

        /**
         * Generates a list of values that are normalized to exclude seasonal trends.
         * @return
         */
        //TODO: Rename method
        public ArrayList<Double> getyValuesNormalized() {
                return dataNormalized;
        }

        /**
         * Gives the forecasted values available, normalized to exclude seasonal trends.
         * @return
         */
        //TODO: Rename method
        public ArrayList<Double> getyValuesForecastNormalized() {
                return dataForecastNormalized;
        }
        
        /**
         * Gives the historical data - first normalized to exclude seasonal trends, then smoothed with exponential smoothing.
         * @return
         */
        public ArrayList<Double> getDataSmoothNormalized() {
			return dataSmoothNormalized;
		}

        /**
         * Gives the historical data smoothed using exponential smoothing, without normalizing for seasonal trends.
         * @return
         */
        public ArrayList<Double> getDataSmoothed() {
        	SimpleEntry<ArrayList<Double>, Double> result = smoothData(this.dataOriginal, this.DEFAULT_ALPHA, this.DEFAULT_BETA);
        	return result.getKey();
        }

        /**
         * Gives the forecast data smoothed using exponential smoothing, without normalizing for seasonal trends.
         * @return
         */
        public ArrayList<Double> getDataForecastSmoothed() {
        	SimpleEntry<ArrayList<Double>, Double> result = smoothData(this.dataForecast, this.DEFAULT_ALPHA, this.DEFAULT_BETA);
        	return result.getKey();
        }
        
        /**
         * Gives the forecast data smoothed using exponential smoothing, without normalizing for seasonal trends.
         * @return
         */
        public ArrayList<Double> getDataForecastNormalizedSmoothed() {
        	SimpleEntry<ArrayList<Double>, Double> result = smoothData(this.dataForecastNormalized, this.DEFAULT_ALPHA, this.DEFAULT_BETA);
        	return result.getKey();
        }

        /**
         * Formats the historical data points into an JSON array of objects with "date" and "value" properties.
         * 
         * Note: If both smooth and normalized booleans are set to true, the data will be normalized first,
         * and then smoothed (not the other way around).
         * 
         * @param smoothed A boolean indicating whether the data should be smoothed or not.
         * @param normalized A boolean indicating whether the data should be normalized or not.
         * @return A JSON array of objects with "date" and "value" properties.
         */
        public String getJSONPointsHistorical(boolean smoothed, boolean normalized) {
        	if (smoothed && normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.dataSmoothNormalized);
        	else if (!smoothed && normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.dataNormalized);
        	else if (smoothed && !normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.getDataSmoothed());
        	else
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.dataOriginal);
        }

        /**
         * Formats the forecast data points into an JSON array of objects with "date" and "value" properties.
         * @return A JSON array of objects with "date" and "value" properties.
         */
        public String getJSONPointsForecast(boolean smoothed, boolean normalized) {
        	if (smoothed && normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.getDataForecastNormalizedSmoothed());
        	else if (!smoothed && normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.dataForecastNormalized);
        	else if (smoothed && !normalized)
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.getDataForecastSmoothed());
        	else
        		return getJSONPoints(this.startDateC.getTime().getTime(), this.dataForecast);
        }
        
        /**
         * Formats a list of data points, given a start date, into an JSON array of objects with "date" and "value" properties.
         * @param startDateTime The starting date which coincides with the first value in the list of values.
         * @param values The list of values.
         * @return A JSON array of objects with "date" and "value" properties.
         */
        private String getJSONPoints(long startDateTime, ArrayList<Double> values) {
        	JSONArray points = new JSONArray();
        	for (Double value : values) {
        		try {
					JSONObject point = new JSONObject().put("jsonDate", startDateTime).put("jsonHitCount", value);
	        		points.put(point);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new JSONArray().toString();
				}
        		//Add one day.
        		startDateTime += 1000 * 60 * 60 * 24;
        	}
        	return points.toString();
        }
        
        /**
         * 
         * @param startDateTime
         * @return
         */
        public JSONArray getJSONPointsFormatted() {
        	long startDateTime = this.startDateC.getTime().getTime();
        	JSONArray points = new JSONArray();
        	int i;

        	for (i=0; i < this.dataOriginal.size(); i++) {
        		try {
					JSONObject point = new JSONObject().put("jsonDate", startDateTime)
							.put("jsonHitCount", this.dataOriginal.get(i))
							.put("smooth", this.dataSmoothNormalized.get(i))
							.put("normal", this.dataNormalized.get(i));
	        		points.put(point);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new JSONArray();
				}
        		//Add one day.
        		startDateTime += 1000 * 60 * 60 * 24;
        	}
        	
        	
        	for (i=0; i < this.dataForecast.size(); i++) {
        		ArrayList<Double> forecastNormalizedSmoothed = this.getDataForecastNormalizedSmoothed();
        		try {
					JSONObject point = new JSONObject().put("jsonDate", startDateTime)
							.put("jsonHitCount", this.dataForecast.get(i))
							.put("smooth", forecastNormalizedSmoothed.get(i))
							.put("normal", this.dataForecastNormalized.get(i));
	        		points.put(point);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return new JSONArray();
				}
        		//Add one day.
        		startDateTime += 1000 * 60 * 60 * 24;
        	}
        	return points;
        }
        
        
        /**
         * Gives a 20% padding for the range.
         * @return The amount of padding needed.
         */
        public Double getYRangePadding() {
                return (yRange.getValue() - yRange.getKey()) / 5.0;
        }
        
        /**
         * @deprecated
         */
        @Override
        public SimpleEntry<Double, Double> getXRange() {
                return xRange;
        }

        @Override
        public SimpleEntry<Double, Double> getYRange() {
                return yRange;
        }
        
        /**
         * @deprecated
         */
        @Override
        public ArrayList<Double> getXValues() {
                return xValues;
        }

        @Override
        public ArrayList<Double> getYValues() {
                return dataOriginal;
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
		public int getPositionPriority() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getJSONSerialization() {
			JSONObject result = new JSONObject();
			try {
				result.put("name", this.getName());
				result.put("description", this.getDescription());
				result.put("metric", this.getMetric());
				result.put("priority", this.getPositionPriority());
				result.put("data", this.getJSONPointsFormatted());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result.toString();
		}


}
