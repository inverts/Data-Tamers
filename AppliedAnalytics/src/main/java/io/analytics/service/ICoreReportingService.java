package io.analytics.service;

import io.analytics.domain.CoreReportingData;

import java.io.IOException;
import java.util.Date;

public interface ICoreReportingService {
	public CoreReportingData getMetricByDay(String metric, Date startDate, Date endDate, int maxResult ) throws IOException;
	
	public CoreReportingData getTopTrafficSources(String metric, Date startDate, Date endDate, int maxResults) throws IOException;
	
	public CoreReportingData getPagePerformance(Date startDate, Date endDate) throws IOException;

}
