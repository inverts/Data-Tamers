package io.analytics.service;

import io.analytics.domain.CoreReportingData;

import java.io.IOException;

public interface ICoreReportingService {
	public CoreReportingData getMetric2D(String metric, String startDate, String endDate, int maxResult ) throws IOException;
	
	public CoreReportingData getTopTrafficSources(String metric, String startDate, String endDate, int maxResults) throws IOException;
	
	public CoreReportingData getPagePerformance(String startDate, String endDate)throws IOException;

}
