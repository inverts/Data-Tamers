package io.analytics.service.interfaces;

import io.analytics.domain.CoreReportingData;
import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;


public interface ICoreReportingService {
	
	public String getProfile();
	
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResult);
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) ;
	
	public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) ;

}

