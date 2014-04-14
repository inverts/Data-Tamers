package io.analytics.repository.interfaces;
import io.analytics.domain.CoreReportingData;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;

import java.util.Date;
import java.util.List;



public interface ICoreReportingRepository {
	//TODO: Eventually we will want this to be 10x more flexible, so this will all probably be rewritten
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResult); 
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults);

	public CoreReportingData getDimensionsByDay(Credential credential, String profileID, String metric, String dimension, List<String> dimensions, String startDate, String endDate, int maxResults);

	public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults);

	public GaData getPagePerformance(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) ;

	public GaData getPageTitle(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) ;

	public GaData getTotalMetric(Credential credential, String profileID, String metric, Date startDate, Date endDate);

	public GaData getMetricForGaDimension(Credential credential, String profileID, String metric, String gaDimension, Date startDate, Date endDate, int maxResults);
	
	public GaData getTopTrafficSources1(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);
	
	public GaData getKeywords(Credential credential, String profileID, Date startDate, Date endDate, int maxResults, String filter);

	public GaData getDenseVisitorInfo(Credential credential, String profileID, String startDate, String endDate); 
	
	public GaData getOverview(Credential credential, String profileID, Date sDate, Date eDate);
	
	public GaData getOverviewDim(Credential credential, String profileID, Date sDate, Date eDate, int maxResults, String dimension);
	
	public GaData getMobileOS(Credential credential, String profileID, Date sDate, Date eDate, int maxResults);
	
	public GaData getDesktopBrowser(Credential credential, String profileID, Date sDate, Date eDate, int maxResults);
}




