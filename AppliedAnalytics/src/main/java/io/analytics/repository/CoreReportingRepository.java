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
			// TODO: Can we do this with only the access token?
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
		public CoreReportingData getMetric2D(String metric, String startDate, String endDate, int maxResults) throws IOException {
			GaData data = null;
			System.out.println("profile id = "+PROFILE_ID);
			try {
				data = CORE_REPORTING.get("ga:"+PROFILE_ID, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						"ga:"+metric) // Metrics.
						.setDimensions("ga:day")
						.setSort("ga:day")
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
		public CoreReportingData getTopTrafficSources(String metric, String startDate, String endDate, int maxResults) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get("ga:"+PROFILE_ID, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						"ga:"+metric) // Metrics.
						.setDimensions("ga:sourceMedium")
						.setSort("-ga:"+metric)
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
		public CoreReportingData getPagePerformance(String startDate, String endDate, int maxResults) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get("ga:"+PROFILE_ID, // profile id (table id).
						startDate, // Start date.
						endDate, // End date.
						"ga:visits,ga:visitBounceRate,ga:exitRate") // Metrics.
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
			return coreReportingMapper(data);
		}		


		
		private CoreReportingData coreReportingMapper(GaData data){
			CoreReportingData dataObject = new CoreReportingData(data);
			return dataObject;
		}
		
		
		private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
			System.err.println("There was a CoreReporting service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		}
}
