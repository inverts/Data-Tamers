package io.analytics.service.interfaces;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.ForecastData;

import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;


public interface ICoreReportingService {
	
	public String getProfile();
	
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResult);
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);

	public CoreReportingData getDimensionsByDay(Credential credential, String profileID, String metric, String dimension, List<String> dimensions, 
			Date startDate, Date endDate, int maxResults);

	/**
	 * Obtains a list of traffic sources (ga:source) and a metric of choice, sorted in descending order onthe metric of choice.
	 * 
	 * @param credential
	 * @param profileID The Google Analytics profile we want the data for.
	 * @param metric The metric we are interested in.
	 * @param startDate The start date.
	 * @param endDate The end date.
	 * @param maxResults
	 * @return Returns a CoreReportingData object with the results.
	 */
	public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);
	
	public ForecastData getForecastData(Credential credential, String profileID);

}

