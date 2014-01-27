
package io.analytics.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.CoreReportingTypedData;
import io.analytics.repository.CoreReportingRepository;
import io.analytics.repository.CoreReportingRepository.CredentialException;
import io.analytics.repository.interfaces.ICoreReportingRepository;
import io.analytics.service.interfaces.ICoreReportingService;
import io.analytics.service.interfaces.ISessionService;

@Service
public class CoreReportingService implements ICoreReportingService {
	
	@Autowired private ICoreReportingRepository REPOSITORY;
	@Autowired private ISessionService sessionService;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;
	
	//private final String profile;
	
	/*public CoreReportingService(Credential credential, String profileId) throws CredentialException {
		this.REPOSITORY = new CoreReportingRepository(credential, profileId);
		//The date format required for Analytics.Data.Ga.Get.get
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		this.profile = profileId;
	}*/
	
	
	public String getProfile() {
		//return this.profile;
		return sessionService.getUserSettings().getActiveProfile().getId();
	}
	
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResult )  {
		return REPOSITORY.getMetricByDay(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResult);
	}
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) {
		return REPOSITORY.getMetricByDayOfWeek(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}

	public CoreReportingTypedData getTopTrafficSources(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) throws IOException {
		return REPOSITORY.getTopTrafficSources(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
	
	public CoreReportingTypedData getPagePerformance(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) {
		return REPOSITORY.getPagePerformance(credential, profileID, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
}
