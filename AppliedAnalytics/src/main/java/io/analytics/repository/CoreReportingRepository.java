package io.analytics.repository;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.ISessionService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.stereotype.Repository;

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

@Repository
public class CoreReportingRepository implements ICoreReportingRepository {
	/**
	 * CoreReportingRepository class queries Google Analytics reports using 
	 *   dimensions, metrics, start and end dates, and table id.
	 * 
	 * TODO: Make sure we are respecting the rate limit (10 queries per second)
	 * 
	 * @author gak
	 */
		private final String APPLICATION_NAME = "datatamers-appliedanalytics-0.1";
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		private static final JsonFactory JSON_FACTORY = new JacksonFactory();
		private QueryDispatcher queryDispatcher = new QueryDispatcher();

		private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;
		private ISessionService _sessionService;
		private Ga CORE_REPORTING;

		//Some metrics and dimensions defined for convenience.
		//TODO: Get an authoritative list of metrics and dimensions from the Metadata API.
		public static final String VISITS_METRIC = "ga:visits";
		public static final String BOUNCERATE_METRIC = "ga:entranceBounceRate";
		public static final String REVENUE_METRIC = "ga:transactionRevenue";
		public static final String GOALS_METRIC = "ga:goalCompletionsAll";
		public static final String PAGEVIEWS_METRIC = "ga:pageviewsPerVisit";

		public static final String SOURCE_DIMENSION = "ga:source";
		public static final String KEYWORD_DIMENSION = "ga:keyword";
		public static final String MEDIUM_DIMENSION = "ga:medium";
		public static final String VISITLENGTH_DIMENSION = "ga:visitLength";
		public static final String DAYOFWEEK_DIMENSION = "ga:dayOfWeek";
		public static final String DAYOFMONTH_DIMENSION = "ga:day";
		public static final String MONTHOFYEAR_DIMENSION = "ga:month";
		public static final String NDAY_DIMENSION = "ga:nthDay";
		
		public static final String KEYWORD_NOT_PROVIDED_FILTER = "ga:keyword==(not provided)";
		public static final String KEYWORD_FILTER_NOT_PROVIDED_AND_NOT_SET = "ga:keyword!=(not provided);ga:keyword!=(not set)";
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


		/**
		 * Get metric data vs. days (2 dimensional data) 
		 * 
		 * @return
		 */
		public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults) {
			return getMetricForSingleDimension(credential, profileID, metric, NDAY_DIMENSION, startDate, endDate, maxResults);
		}


		/**
		 * Get metric data by day of week
		 * 
		 * @return
		 */
		public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults) {
			return getMetricForSingleDimension(credential, profileID, metric, DAYOFWEEK_DIMENSION, startDate, endDate, maxResults);
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
		public CoreReportingData getMetricForSingleDimension(Credential credential, String profileID, String metric, String dimension, String startDate, String endDate, int maxResults) {
			GaData gaData = null;
			try {

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				Ga.Get request = reporting.get("ga:" + profileID, startDate, endDate, metric) // Metrics.
						.setDimensions(dimension)
						.setSort(dimension)
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
			
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}

			return coreReportingMapper(gaData);
		}

		
		/**
		 * 
		 * @param credential
		 * @param profileID The profile ID of the account we're interested in.
		 * @param metric The metric we're interested in.
		 * @param dimension The dimension we're interested in.
		 * @param dimensions A list of values for <code>dimension</code> to limit the results to.
		 * @param startDate
		 * @param endDate
		 * @param maxResults
		 * @return
		 */
		public CoreReportingData getDimensionsByDay(Credential credential, String profileID, String metric, String dimension, List<String> dimensions, String startDate, String endDate, int maxResults) {
			GaData gaData = null;
			try {
				
				String filterString = "";
				for (String d : dimensions) {
					if ( !filterString.equals("") )
						filterString += ",";
					filterString += dimension + "==" + d;
				}

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				Ga.Get request = reporting.get("ga:" + profileID, startDate, endDate, metric) // Metrics.
						.setDimensions(dimension + ",ga:nthDay")
						.setFilters(filterString)
						.setSort(dimension + ",ga:nthDay") //Sort by dimension and then day
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
			
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}

			return coreReportingMapper(gaData);
		}

		
		/**
		 * Get top n traffic sources relative to the passed metric
		 * 
		 * @return
		 */
		public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, String startDate, String endDate, int n) {
			GaData gaData = null;
			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				Ga.Get request = reporting.get("ga:"+ profileID, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						metric) // Metrics.
						.setDimensions("ga:source")
						.setSort("-" + metric)
						.setMaxResults(n);
				gaData = queryDispatcher.execute(request);
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return coreReportingMapper(gaData);
			//return topTrafficSourcesMapper(data);
			//return gaData;
		}
		
	    //  *********** Final version Forecast repository 	

		/**
		 * Obtains data for a metric over a single GA dimension.
		 * 
		 * @param metric
		 * @param dimension
		 * @param startDate
		 * @param endDate
		 * @param maxResults
		 * @return
		 */
		public GaData getMetricForGaDimension(Credential credential, String profileID, String metric, String dimension, Date startDate, Date endDate, int maxResults) {
			GaData gaData = null;
			String sDate = dateFormat.format(startDate);
			String eDate = dateFormat.format(endDate);
			try {

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				Ga.Get request = reporting.get("ga:" + profileID, sDate, eDate, metric) // Metrics.
						.setDimensions(dimension)
						.setSort(dimension)
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}

			return gaData;
		}
		
			
		 //  *********** Final version Hypothetical Future repository 	
		/**
		 * Get top n traffic sources relative to the passed metric
		 *    Widgets where used:
		 *       - Hypothetical Future
		 *       	 * @param metric
		 * @param metric
		 * @param startDate
		 * @param endDate
		 * @param n
		 * @return
		 */
		public GaData getTopTrafficSources1(Credential credential, String profileID, String metric, Date startDate, Date endDate, int n) {
			GaData gaData = null;
			String sDate = dateFormat.format(startDate);
			String eDate = dateFormat.format(endDate);
			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				Ga.Get request = reporting.get("ga:"+ profileID, // Table Id.
						sDate, // Start date.
						eDate, // End date.
						metric) // Metrics.
						.setDimensions("ga:source")
						.setSort("-" + metric)
						.setMaxResults(n);
				
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}		
		

		/**
		 * Get page performance data for Web Performance widget
		 * 
		 * @param startDate
		 * @param endDate
		 * @param maxResults
		 * @return
		 */

		public GaData getPagePerformance(Credential credential, String profileID, Date sDate, Date eDate, int maxResults) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visitBounceRate,ga:visits,ga:exitRate") // Metrics.
						.setDimensions("ga:pagePath,ga:hostname,ga:pageTitle")
						.setSort("-ga:visits")
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			//return pagePerformanceMapper(gaData);
			return gaData;
		}	

		/**
		 * Get page title data for Web Performance widget
		 * 
		 * @param startDate
		 * @param endDate
		 * @param maxResults
		 * @return
		 */

		public GaData getPageTitle(Credential credential, String profileID, Date sDate, Date eDate, int maxResults) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits") // Metrics.
						.setDimensions("ga:pagePath,ga:pageTitle")
						.setFilters("ga:pageTitle!=(not set)")
						.setSort("-ga:visits")
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}

			return gaData;
		}	
		
	
		/**
		 *  Get the total metric for the website (e.g. visits)
		 *  Widgets where used:
		 *    - Page Performance
		 *  Inputs: 
		 *  	metric <String> must be in the GA command form
		 */

		public GaData getTotalMetric(Credential credential, String profileID, String metric, Date sDate, Date eDate){
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);

			//System.out.println("start date = "+startDate+", "+"end date = "+endDate);
			
			try {

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						metric);  // Metric
						
				gaData = queryDispatcher.execute(request);
						
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}
		

		/**
		 * Get the all keywords using filter
		 *   Special Inputs: 
		 *      String <filter>: set filter with this value;
		 *      
		 * @param sDate
		 * @param eDate
		 * @param maxResults
		 * @param medium
		 * @param filter
		 * @return
		 */

		public GaData getKeywords(Credential credential, String profileID, Date sDate, Date eDate, int maxResults, String filter) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits,ga:visitBounceRate") // Metrics.
						.setDimensions("ga:medium,ga:keyword,ga:hostname")
						.setFilters(filter)
						.setSort("-ga:visits")
						.setMaxResults(maxResults);
				
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}	
		
		/**
		 * Get aquisition overview data	
		 *      
		 * @param sDate
		 * @param eDate
		 * @return
		 */

		public GaData getOverview(Credential credential, String profileID, Date sDate, Date eDate) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:newVisits,ga:percentNewVisits,ga:visits,ga:visitBounceRate,ga:pageviewsPerVisit,ga:avgTimeOnSite") // Metrics.
						.setSort("-ga:visits")
						.setFilters("ga:medium!=(none)");
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}
		/**
		 * Get aquisition overview data	broken down by channel
		 *      
		 * @param sDate
		 * @param eDate
		 * @return
		 */

		public GaData getOverviewDim(Credential credential, String profileID, Date sDate, Date eDate, int maxResults, String dimension) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:newVisits,ga:percentNewVisits,ga:visits,ga:visitBounceRate,ga:pageviewsPerVisit,ga:avgTimeOnSite") // Metrics.
						.setDimensions(dimension)
						.setFilters("ga:medium!=(none)")
						.setSort("-ga:visits")
						.setMaxResults(maxResults);
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}
		
		/**
		 * Get vistor device category (not desktop), OS, and visits
		 *  
		 * @param credential
		 * @param sDate
		 * @param eDate
		 * @param maxResults
		 * @return
		 */
		public GaData getMobileOS(Credential credential, String profileID, Date sDate, Date eDate, int maxResults) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits") // Metrics.
						.setDimensions("ga:deviceCategory,ga:operatingSystem")
						.setSort("-ga:visits")
						.setFilters("ga:deviceCategory!=desktop")
						.setMaxResults(maxResults);
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}
		
		/**
		 * Get vistor desktop Browser, and visits
		 *  
		 * @param credential
		 * @param sDate
		 * @param eDate
		 * @param maxResults
		 * @return
		 */
		public GaData getDesktopBrowser(Credential credential, String profileID, Date sDate, Date eDate, int maxResults) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				Ga.Get request = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits") // Metrics.
						.setDimensions("ga:deviceCategory,ga:browser")
						.setSort("-ga:visits")
						.setFilters("ga:deviceCategory==desktop")
						.setMaxResults(maxResults);
				gaData = queryDispatcher.execute(request);
				
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}
		
		private CoreReportingData coreReportingMapper(GaData data){
			if (data == null)
				return null;
			CoreReportingData dataObject = new CoreReportingData(data);
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
			 QueryDispatcher q = new QueryDispatcher();
			 
		}

		private class QueryDispatcher {
			private int load;
			private static final int HEAVY_LOAD_WAIT_MS = 100;
			private static final int MAX_ATTEMPTS = 5;
			private static final int TIMEOUT_MS = 5000;
			
			private QueryDispatcher() {
				load = 0;
				System.out.println("A QueryDispatcher was created. (QD #" + this.hashCode() + ")");
			}
			
			/**
			 * Gets the approximate number of queries that have been made in the last second.
			 * @return
			 */
			private int getLoad() {
				return load;
			}
			
			private void increaseLoad() {
				this.load++;
				System.out.println("Load increased for QD #" + this.hashCode() + ". Current load: " + QueryDispatcher.this.getLoad());
			}
			
			/**
			 * Executes the specified Ga.Get request, taking into account the current load,
			 * and retrying the request upon failure up to a max amount of times.
			 * 
			 * @param request
			 * @return null if the request failed to execute. Otherwise, a GaData response.
			 */
			private GaData execute(Ga.Get request) {
				GaData result = null;
				int attempts = 0;
				long startTime = System.currentTimeMillis();
				while (attempts < MAX_ATTEMPTS) {
					
					//If our load is high, wait until it is low or until we time out
					while (this.load >= 10)
						if (System.currentTimeMillis() - startTime < TIMEOUT_MS) 
							try { Thread.sleep(HEAVY_LOAD_WAIT_MS); } catch (InterruptedException e) { e.printStackTrace(); }
						else 
							return null;
					
					//If the request succeeds, break out of the loop.
					try {
						//Increase the load, since we are making a query. Does Google count failed queries towards the QPS?
						increaseLoad();
						
						//Set a timer to decrease the load in one second.
						Timer t = new Timer();
						t.schedule(new LoadReducer(), 1000);
						
						result = request.execute();
						break;
						
					} catch (GoogleJsonResponseException e) {
						handleGoogleJsonResponseException(e);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					//Otherwise, increment the attempt counter.
					attempts++;
					System.out.println("Attempt #" + attempts + " failed.");
				}
				
				
				
				return result;
			}
			
			/**
			 * Reduces the load by 1.
			 */
			private class LoadReducer extends TimerTask {
				@Override
				public void run() {
					QueryDispatcher.this.load -= 1;
					System.out.println("Load decreased for QD #" + QueryDispatcher.this.hashCode() + ". Current load: " + QueryDispatcher.this.getLoad());
				}
			}
			
		}


		@Override
		public GaData getDenseVisitorInfo(Credential credential, String profileID, String startDate, String endDate, int maxResults) {
			GaData gaData = null;
			try {

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				
				String metrics = "ga:avgTimeOnSite,ga:pageviewsPerVisit,ga:visits";
				String dimensions = "ga:hour,ga:dayOfWeek,ga:longitude,ga:latitude,ga:visitorType,ga:screenResolution";
				int totalRows = maxResults;
				int receivedRows = 0;
				while (receivedRows < maxResults && receivedRows < totalRows) {
					Ga.Get request = reporting.get("ga:" + profileID, startDate, endDate, metrics) // Metrics.
							.setDimensions(dimensions)
							.setSort("-ga:visits")
							.setMaxResults(10000) //Get all data later.. change..
							.setStartIndex(receivedRows+1);
					
					GaData currentResults = queryDispatcher.execute(request);
					if (gaData != null)
						gaData.getRows().addAll(currentResults.getRows());
					else
						gaData = currentResults;
					totalRows = currentResults.getTotalResults();
					receivedRows = gaData.getRows().size();
					System.out.println("VisitorClustering:\tGathered " + receivedRows + " of " + Math.max(maxResults, totalRows) + " rows... (" + totalRows + " rows available)");
				}
			
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}

			return gaData;
		}

}
