package io.analytics.service;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;

import java.io.IOException;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface ICoreReportingService {
	
	public String getProfile();
	
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResult);
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) ;
	
	public CoreReportingTypedData getTopTrafficSources(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) throws IOException;
	
	public CoreReportingTypedData getPagePerformance(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) ;

}

