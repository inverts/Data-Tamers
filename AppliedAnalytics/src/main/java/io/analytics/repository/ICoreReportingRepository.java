package io.analytics.repository;
import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;

import java.io.IOException;

import com.google.api.client.auth.oauth2.Credential;

public interface ICoreReportingRepository {
	//TODO: Eventually we will want this to be 10x more flexible, so this will all probably be rewritten
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResult); 
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults);

	public CoreReportingTypedData getTopTrafficSources(Credential credential, String profileID, String metric, String startDate, String endDate, int maxResults) throws IOException;

	public CoreReportingTypedData getPagePerformance(Credential credential, String profileID, String startDate, String endDate, int maxResults) ;

	}

