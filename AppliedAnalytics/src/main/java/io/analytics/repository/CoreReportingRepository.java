package io.analytics.repository;
import io.analytics.domain.CoreReportingData;

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
		public static final String BOUNCERATE_METRIC = "ga:visitBounceRate";
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
		public CoreReportingData getTopTrafficSources(String metric, String startDate, String endDate, int n) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get(PROFILE_ID, // Table Id.
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
			return coreReportingMapper(data);
		}
		
		/**
		 * Get top n traffic sources relative to the passed metric
		 * 
		 * @return
		 */

		@Override
		public CoreReportingData getPagePerformance(String startDate, String endDate, int maxResults) throws IOException {
			GaData data = null;
			try {
				data = CORE_REPORTING.get(PROFILE_ID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:entranceBounceRate,ga:visits,ga:exitRate") // Metrics.
						.setDimensions("ga:pagePath")
						.setSort("-ga:visits")
						.setMaxResults(100)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return coreReportingMapper(data);
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

		
}
