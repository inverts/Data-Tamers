package io.analytics.repository.interfaces;
import io.analytics.domain.CoreReportingData;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;

import java.util.Date;



public interface ICoreReportingRepository {
	//TODO: Eventually we will want this to be 10x more flexible, so this will all probably be rewritten
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResult); 
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults);

	public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults);

	public GaData getPagePerformance(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) ;

	public GaData getTotalMetric(Credential credential, String profileID, String metric, Date startDate, Date endDate);

	public GaData getMetricForGaDimension(Credential credential, String profileID, String metric, String gaDimension, Date startDate, Date endDate, int maxResults);
	
	public GaData getTopTrafficSources1(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);

}


