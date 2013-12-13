package io.analytics.service;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;

import java.io.IOException;
import java.util.Date;

public interface ICoreReportingService {
	
	public String getProfile();
	
	public CoreReportingData getMetricByDay(String metric, Date startDate, Date endDate, int maxResult);
	
	public CoreReportingData getMetricByDayOfWeek(String metric, Date startDate, Date endDate, int maxResults) ;
	
	public CoreReportingTypedData getTopTrafficSources(String metric, Date startDate, Date endDate, int maxResults) throws IOException;
	
	public CoreReportingTypedData getPagePerformance(Date startDate, Date endDate, int maxResults) throws IOException;

}

