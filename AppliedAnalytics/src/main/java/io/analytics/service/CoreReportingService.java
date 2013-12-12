package io.analytics.service;

import java.io.IOException;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.CoreReportingRepository.CredentialException;

public class CoreReportingService implements ICoreReportingService {
	private final CoreReportingRepository REPOSITORY;

	public CoreReportingService(Credential credential, String profileId) throws CredentialException{
		this.REPOSITORY = new CoreReportingRepository(credential, profileId);
	}
	
	public CoreReportingData getMetric2D(String metric, String startDate, String endDate, int maxResult ) throws IOException {
		return REPOSITORY.getMetric2D(metric, startDate, endDate, maxResult);
	}
	
	public CoreReportingData getTopTrafficSources(String metric, String startDate, String endDate, int maxResults) throws IOException {
		return REPOSITORY.getTopTrafficSources(metric, startDate, endDate, maxResults);
	}
	
	public CoreReportingData getPagePerformance(String startDate, String endDate) throws IOException {
		return REPOSITORY.getPagePeformance(startDate, endDate);
	}
}
