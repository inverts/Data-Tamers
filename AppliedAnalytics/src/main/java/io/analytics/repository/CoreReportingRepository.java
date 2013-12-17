package io.analytics.repository;
import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.Analytics.Data.Ga;
import com.google.api.services.analytics.model.*;
import com.google.api.services.analytics.model.GaData.ColumnHeaders;
import com.google.api.services.analytics.model.GaData.Query;

public class CoreReportingRepository implements ICoreReportingRepository {
	/**
	 * CoreReportingRepository class queries Google Analytics reports using 
	 *   dimensions, metrics, start and end dates, and table id.
	 * 
	 * TODO: Make sure we are respecting the rate limit (10 queries per second)
	 * 
	 * @author Gwen Knight
	 */
		private final String APPLICATION_NAME = "datatamers-appliedanalytics-0.1";
		private final String ACCESS_TOKEN;
		private final Credential CREDENTIAL;
		private final String PROFILE_ID;
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		private static final JsonFactory JSON_FACTORY = new JacksonFactory();

		private final Ga CORE_REPORTING;

		//Some metrics and dimensions defined for convenience.
		//TODO: Get an authoritative list of metrics and dimensions from the Metadata API.
		public static final String VISITS_METRIC = "ga:visits";
		public static final String BOUNCERATE_METRIC = "ga:entranceBounceRate";
		public static final String REVENUE_METRIC = "ga:transactionRevenue";
		public static final String GOALS_METRIC = "ga:goalCompletionsAll";
		public static final String PAGEVIEWS_METRIC = "ga:pageviewsPerVisit";

		public static final String SOURCE_DIMENSION = "ga:source";
		public static final String KEYWORD_DIMENSION = "ga:keyword";
		public static final String VISITLENGTH_DIMENSION = "ga:visitLength";
		public static final String DAYOFWEEK_DIMENSION = "ga:dayOfWeek";
		public static final String DAYOFMONTH_DIMENSION = "ga:day";
		public static final String MONTHOFYEAR_DIMENSION = "ga:month";
		public static final String NDAY_DIMENSION = "ga:nthDay";

		public static final boolean SORT_ASCENDING = true;
		public static final boolean SORT_DESCENDING = false;
		
		public static class CredentialException extends Throwable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public CredentialException() {
				super();
			}

			public CredentialException(String s) {
				super(s);
			}
		}

		public CoreReportingRepository(Credential credential, String profileId) throws CredentialException {
			if (credential == null)
				throw new CredentialException("Null credential object passed.");
			this.ACCESS_TOKEN = credential.getAccessToken();
			this.CREDENTIAL = credential;
			this.PROFILE_ID = profileId;
			this.CORE_REPORTING = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIAL)
					.setApplicationName(APPLICATION_NAME).build().data().ga();

			// Refresh the access token if it is about to expire.
			if (CREDENTIAL.getExpiresInSeconds() < 10) {
				boolean success = false;
				try {
					success = CREDENTIAL.refreshToken();
				} catch (IOException e) {
					throw new CredentialException("4xx error occured while refreshing token.");
				}
				if (!success)
					throw new CredentialException("Token refresh failed. There may not be a refresh token.");
			}
		}

		
		/**
		 * Get metric data vs. days (2 dimensional data) 
		 * 
		 * @return
		 */
		public CoreReportingData getMetricByDay(String metric, String startDate, String endDate, int maxResults) {
			return getMetricForSingleDimension(metric, NDAY_DIMENSION, startDate, endDate, maxResults);
		}
		
		
		/**
		 * Get metric data by day of week
		 * 
		 * @return
		 */
		public CoreReportingData getMetricByDayOfWeek(String metric, String startDate, String endDate, int maxResults) {
			return getMetricForSingleDimension(metric, DAYOFWEEK_DIMENSION, startDate, endDate, maxResults);
		}
		
		
		/**
		 * Obtains data for a metric over a single dimension.
		 * 
		 * @param metric
		 * @param dimension
		 * @param startDate
		 * @param endDate
		 * @param maxResults
		 * @return
		 */
		public CoreReportingData getMetricForSingleDimension(String metric, String dimension, String startDate, String endDate, int maxResults) {
			GaData data = null;
			try {
				data = CORE_REPORTING.get("ga:" + PROFILE_ID, startDate, endDate, metric) // Metrics.
						.setDimensions(dimension)
						.setSort(dimension)
						.setMaxResults(maxResults)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			
			return coreReportingMapper(data);
		}
		
		
		/**
		 * Get top n traffic sources relative to the passed metric
		 * 
		 * @return
		 */
		public CoreReportingTypedData getTopTrafficSources(String metric, String startDate, String endDate, int n) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get("ga:"+ PROFILE_ID, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						metric) // Metrics.
						.setDimensions("ga:source")
						.setSort("-" + metric)
						.setMaxResults(n)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return topTrafficSourcesMapper(data);
		}
		
		/**
		 * Get page performance data for Web Performance widget
		 * 
		 * @return
		 */

		@Override
		public CoreReportingTypedData getPagePerformance(String startDate, String endDate, int maxResults) {
			GaData gaData = null;
			System.out.println("start date = "+startDate+", "+"end date = "+endDate);
		
			try {
				gaData = CORE_REPORTING.get("ga:"+PROFILE_ID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:entranceBounceRate,ga:visits,ga:exitRate") // Metrics.
						.setDimensions("ga:pagePath")
						.setSort("-ga:visits")
						.setMaxResults(maxResults)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return pagePerformanceMapper(gaData);
		}		
		
		private CoreReportingData coreReportingMapper(GaData data){
			if (data == null)
				return null;
			CoreReportingData dataObject = new CoreReportingData(data);
			return dataObject;
		}
		
		/*
		 *  CoreReportingTypedData: Contains an aggregate array list of array lists 
		 *  	of Java numerical/string typed data. 
		 *  
		 *  Return domain object contains an array list of lists:
		 *      index: type: identification
		 *  	0: String: ordered list identifying the data in each remaining list.
		 *  	1: String: list of top traffic sources.
		 *  	2: Integers: list of the metric count.
		 */
		private CoreReportingTypedData topTrafficSourcesMapper(GaData gaData){
			
			// aggregate data list for the domain object
			ArrayList<Object> list = new ArrayList<Object>();
			
			// REVISIT: Add GaData type checking
			ArrayList<String> dataTypes = new ArrayList<String>();
						
			int sourceColumn = -1; 
			int metricColumn = -1; 
			
			int column = -1;
			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:source"))
					sourceColumn = column;
			    if (name.equals("ga:visits"))
					metricColumn = column;
				
				dataTypes.add(header.getDataType());
			}

			// Useful for debugging
			List<GaData.ColumnHeaders> columnHeaders = gaData.getColumnHeaders();
			
			ArrayList<String> source= new ArrayList<String>();
			ArrayList<Integer> metric= new ArrayList<Integer>();
			
			List<List<String>> dataRows = gaData.getRows();
			try {
			for(List<String> row : dataRows) {
				String s = row.get(sourceColumn);
				int m = Integer.parseInt(row.get(metricColumn));
				source.add(s);
				metric.add(m);
			}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}
			
			// List the identifiers for the remaining arrays
			ArrayList<String>types = new ArrayList<String>();
			types.add("source"); 
			types.add("metric");
			
			// populate aggregate list of data
			list.add(types);
			list.add(source);
			list.add(metric);
			
			// create the domain dataObject with the data
			CoreReportingTypedData dataObject = new CoreReportingTypedData(list);
			return dataObject;
		}
		
		/*
		 *  CoreReportingTypedData: Contains an aggregate array list of array lists 
		 *  	of Java numerical/string typed data. 
		 *  
		 *  Return domain object contains an array list of lists:
		 *      index: type: identification
		 *  	0: Type String: ordered list identifying the data in each remaining list
		 *  	1: Type String: list of top page paths.
		 *  	2: Type Integers: list of the visits count. 
		 *      3: Type Double: entrance Bounce rate in percentage
		 *      4: Type Double: exit rate in percentage.
		 */
		private CoreReportingTypedData pagePerformanceMapper(GaData gaData){
			
			// aggregate data list for the domain object
			ArrayList<Object> list = new ArrayList<Object>();

			//printColumnHeaders(gaData);
			//printDataTable(gaData);
			
			// REVISIT: Add GaData type checking
			ArrayList<String> dataTypes = new ArrayList<String>();
		
			int pagePathColumn = -1; 
			int visitsColumn = -1; 
			int entranceBounceRateColumn = -1; 
			int exitRateColumn = -1; 
			int column = -1;
			for (ColumnHeaders header : gaData.getColumnHeaders()) {
				column++;
				String name = header.getName();
				if (name.equals("ga:pagePath"))
					pagePathColumn = column;
			    if (name.equals("ga:visits"))
					visitsColumn = column;
				if (name.equals("ga:entranceBounceRate"))
					entranceBounceRateColumn = column;
				if (name.equals("ga:exitRate"))
					exitRateColumn = column;
					
				dataTypes.add(header.getDataType());
			}
			
			ArrayList<String> pagePath = new ArrayList<String>();
			ArrayList<Integer> visits= new ArrayList<Integer>();
			ArrayList<Double> entranceBounceRate = new ArrayList<Double>();
			ArrayList<Double> exitRate = new ArrayList<Double>();
			
			List<List<String>> dataRows = gaData.getRows();
			try {
			for(List<String> row : dataRows) {
				String one = row.get(pagePathColumn);
				int two = Integer.parseInt(row.get(visitsColumn));
				double three = Double.parseDouble(row.get(entranceBounceRateColumn));
				double four = Double.parseDouble(row.get(exitRateColumn));
				pagePath.add(one);
				visits.add(two);
				entranceBounceRate.add(three);
				exitRate.add(four);
			}
			} catch (NumberFormatException e) {
				//The metric we are retrieving is not numeric.
				return null;
			}
			
			// List the identifiers for the remaining arrays
			ArrayList<String>types = new ArrayList<String>();
			types.add("pagePath"); 
			types.add("visits");
			types.add("entranceBounceRate");
			types.add("exitRate");
			
			// populate aggregate list of data
			list.add(types);
			list.add(pagePath);
			list.add(visits);
			list.add(entranceBounceRate);
			list.add(exitRate);
			
			// create the domain dataObject with the data
			CoreReportingTypedData dataObject = new CoreReportingTypedData(list);
			return dataObject;
		}
		
		private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
			System.err.println("There was a CoreReporting service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		}
		
		private void printColumnHeaders(GaData gaData) {
			 System.out.println("Column Headers:");

			 for (ColumnHeaders header : gaData.getColumnHeaders()) {
			   System.out.println("Column Name: " + header.getName());
			   System.out.println("Column Type: " + header.getColumnType());
			   System.out.println("Column Data Type: " + header.getDataType());
			 }
			}
		
		private void printDataTable(GaData gaData) {
			 if (gaData.getTotalResults() > 0) {
			   System.out.println("Data Table:");

			   // Print the column names.
			   for (ColumnHeaders header : gaData.getColumnHeaders()) {
			     System.out.format("%-32s", header.getName() + '(' + header.getDataType() + ')');
			   }
			   System.out.println();

			   // Print the rows of data.
			   for (List<String> rowValues : gaData.getRows()) {
			     for (String value : rowValues) {
			       System.out.format("%-32s", value);
			     }
			     System.out.println();
			   }
			 } else {
			   System.out.println("No Results Found");
			 }
		}
		
}
