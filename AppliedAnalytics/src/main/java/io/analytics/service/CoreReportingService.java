package io.analytics.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.GaData;

import io.analytics.domain.CoreReportingData;
import io.analytics.domain.ForecastData;
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
	
	
	public CoreReportingData getMetricByDay(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResult )  {
		return REPOSITORY.getMetricByDay(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResult);
	}
	
	public CoreReportingData getMetricByDayOfWeek(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults) {
		return REPOSITORY.getMetricByDayOfWeek(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
	
	public CoreReportingData getTopTrafficSources(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults)  {
		return REPOSITORY.getTopTrafficSources(credential, profileID, metric, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}

	@Override
	public ForecastData getForecastData(Credential credential, String profileID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CoreReportingData getDimensionsByDay(Credential credential, String profileID, String metric,
			String dimension, List<String> dimensions, Date startDate, Date endDate, int maxResults) {

		return REPOSITORY.getDimensionsByDay(credential, profileID, metric, dimension, dimensions, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);

	}

	@Override
	public GaData getDenseVisitorInfo(Credential credential, String profileID, Date startDate, Date endDate, int maxResults) {
		return REPOSITORY.getDenseVisitorInfo(credential, profileID, dateFormat.format(startDate), dateFormat.format(endDate), maxResults);
	}
	

	

}
