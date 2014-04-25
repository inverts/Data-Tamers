package io.analytics.service.interfaces;

import io.analytics.domain.OverviewData;
import io.analytics.domain.VisitorDeviceData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface IVisitorDeviceService {
	public VisitorDeviceData getVisitorDeviceData(Credential credential, String profileID, Date startDate, Date endDate); 
}
