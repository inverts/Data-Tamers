package io.analytics.repository;
import io.analytics.domain.CoreReportingData;
import java.io.IOException;

public interface ICoreReportingRepository {
	public CoreReportingData getMetric2D(String metric, String startDate, String endDate, int maxResults) throws IOException; 

	public CoreReportingData getTopTrafficSources(String metric, String startDate, String endDate, int maxResults) throws IOException;

	public CoreReportingData getPagePerformance(String startDate, String endDate, int maxResults) throws IOException;
	}
