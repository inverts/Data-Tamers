package io.analytics.service;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;

import java.io.IOException;
import java.util.Date;

public interface ICoreReportingService {
	public CoreReportingData getMetricByDay(String metric, Date startDate, Date endDate, int maxResult ) throws IOException;
	
	public CoreReportingTypedData getTopTrafficSources(String metric, Date startDate, Date endDate, int maxResults) throws IOException;
	
	public CoreReportingTypedData getPagePerformance(Date startDate, Date endDate, int maxResults) throws IOException;

}
