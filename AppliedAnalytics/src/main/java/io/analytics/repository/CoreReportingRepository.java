package io.analytics.repository;
import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.analytics.Analytics;
import com.google.api.services.analytics.Analytics.Data.Ga;
import com.google.api.services.analytics.model.*;

public class CoreReportingRepository {
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

		public CoreReportingRepository(Credential credential) throws CredentialException {
			if (credential == null)
				throw new CredentialException("Null credential object passed.");
			this.ACCESS_TOKEN = credential.getAccessToken();
			this.CREDENTIAL = credential;
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
		public GaData getMetric2D(String metric, String startDate, String endDate, int maxResult, String tableId) throws IOException {
			GaData data = null;
			try {
				data = CORE_REPORTING.get(tableId, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						"ga:"+metric) // Metrics.
						.setDimensions("ga:day")
						.setSort("ga:day")
						.setMaxResults(maxResult)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return data;
		}

		/**
		 * Get top n traffic sources relative to the passed metric
		 * 
		 * @return
		 */
		public GaData getTopTrafficSources(String metric, String startDate, String endDate, int n, String tableId) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get(tableId, // Table Id.
						startDate, // Start date.
						endDate, // End date.
						"ga:"+metric) // Metrics.
						.setDimensions("ga:sourceMedium")
						.setSort("-ga:"+metric)
						.setMaxResults(n)
						.execute();
			} catch (GoogleJsonResponseException e) {
				handleGoogleJsonResponseException(e);
			} catch (IOException e) {
				// Catch general parsing network errors.
				e.printStackTrace();
			}
			return data;
		}
		
		/**
		 * Get top n traffic sources relative to the passed metric
		 * 
		 * @return
		 */
		public GaData getWebsitePerfomanceData(String startDate, String endDate, String tableId) throws IOException{
			GaData data = null;
			try {
				data = CORE_REPORTING.get(tableId, // Table Id.
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
			return data;
		}		

		private void handleGoogleJsonResponseException(GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		}
}