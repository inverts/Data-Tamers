package io.analytics.service.interfaces;
import io.analytics.domain.OverviewData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface IOverviewService {
	
	public OverviewData getOverviewData(Credential credential, String profileID, Date startDate, Date endDate); 
}
