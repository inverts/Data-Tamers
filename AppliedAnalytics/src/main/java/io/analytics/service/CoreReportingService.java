package io.analytics.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;

import io.analytics.domain.CoreReportingData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.CoreReportingRepository.CredentialException;

public class CoreReportingService implements ICoreReportingService {
	private final CoreReportingRepository REPOSITORY;
	private final SimpleDateFormat dateFormat;
	public CoreReportingService(Credential credential, String profileId) throws CredentialException {
		this.REPOSITORY = new CoreReportingRepository(credential, profileId);
		//The date format required for Analytics.Data.Ga.Get.get
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	}
	
	public CoreReportingData getMetricByDay(String metric, Date startDate, Date endDate, int maxResult )  {
		return REPOSITORY.getMetricByDay(metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResult);
	}
	
	public CoreReportingData getMetricByDayOfWeek(String metric, Date startDate, Date endDate, int maxResults) {
		return REPOSITORY.getMetricByDayOfWeek(metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
	
	
	public CoreReportingData getTopTrafficSources(String metric, Date startDate, Date endDate, int maxResults) throws IOException {
		return REPOSITORY.getTopTrafficSources(metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
	
	
	public CoreReportingData getPagePerformance(Date startDate, Date endDate, int maxResults) throws IOException {
		return REPOSITORY.getPagePerformance(dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
}
