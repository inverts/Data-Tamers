package io.analytics.repository;
import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.ISessionService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
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
import com.google.api.services.analytics.model.GaData.Query;

@Repository
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
		//private final String ACCESS_TOKEN;
		//private Credential CREDENTIAL;
		//private String profileID;
		private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
		private static final JsonFactory JSON_FACTORY = new JacksonFactory();

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

		/*public CoreReportingRepository(Credential credential, String profileId) throws CredentialException {
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
		}*/


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
			GaData data = null;
			try {

				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				data = reporting.get("ga:" + profileID, startDate, endDate, metric) // Metrics.
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
		public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, String startDate, String endDate, int n) {
			GaData gaData = null;
			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				gaData = reporting.get("ga:"+ profileID, // Table Id.
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

				gaData = reporting.get("ga:" + profileID, sDate, eDate, metric) // Metrics.
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

			return gaData;
		}
		
			
		 //  *********** Final version Hypothetical Future repository 	
		/**
		 * Get top n traffic sources relative to the passed metric
		 *    Widgets where used:
		 *       - Hypothetical Future
		 * @return
		 */
		public GaData getTopTrafficSources1(Credential credential, String profileID, String metric, Date startDate, Date endDate, int n) {
			GaData gaData = null;
			String sDate = dateFormat.format(startDate);
			String eDate = dateFormat.format(endDate);
			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
				gaData = reporting.get("ga:"+ profileID, // Table Id.
						sDate, // Start date.
						eDate, // End date.
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
			return gaData;
		}		
		

		/**
		 * Get page performance data for Web Performance widget
		 * 
		 * @return
		 */

		public GaData getPagePerformance(Credential credential, String profileID, Date sDate, Date eDate, int maxResults) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();

				gaData = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visitBounceRate,ga:visits,ga:exitRate") // Metrics.
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
			//return pagePerformanceMapper(gaData);
			return gaData;
		}	

		/**
		 *  Get the total metric for the website, e.g. visits, a single integer.
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

				gaData = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						metric)  // Metric
						.execute();
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
		 * Get keywords
		 * 
		 * @return
		 */

		public GaData getKeywords(Credential credential, String profileID, Date sDate, Date eDate, int maxResults, String medium, String substring) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);
			String searchKeyword = "";
			if (!substring.isEmpty()) {
				searchKeyword = ";ga:keyword=@"+substring;
			}

			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				gaData = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits,ga:visitBounceRate") // Metrics.
						.setDimensions("ga:medium,ga:keyword")
						.setFilters("ga:medium=="+medium+";ga:keyword!=(not provided)"+ searchKeyword)
						.setSort("-ga:visits")
						.setMaxResults(maxResults)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return gaData;
		}	
		
		/*
		 *  getMediumVisitsTotal:
		 *  	Get the total number of visits for the GA dimension medium.
		 *  Special Inputs: 
		 *  	String <medium> expecting "organic" or "cpc"
		 *      boolean isPrivate - True for return of "organic private search" visits total
		 *      				  - False for return of private and public total "organic search" visits 
		 *      				  - Don't care for medium = cpc
		 */
		public GaData getMediumVisitsTotal(Credential credential, String profileID, Date sDate, Date eDate, String medium, boolean isPrivate) {
			GaData gaData = null;
			String startDate = dateFormat.format(sDate);
			String endDate = dateFormat.format(eDate);

			String addFilter = "";
			// Keywords do not show up for SSL searches. Get visit counts for SSL searches
			if (isPrivate && medium.contentEquals(new StringBuffer("organic"))){
				addFilter = ";ga:keyword==(not provided)";
			}
			
			try {
				Ga reporting = new Analytics.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(APPLICATION_NAME).build().data().ga();
 
				gaData = reporting.get("ga:"+ profileID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits") // Metrics.
						.setDimensions("ga:medium")
						.setFilters("ga:medium=="+medium+addFilter)
						.execute();
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
		}

}