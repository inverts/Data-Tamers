package io.analytics.service.interfaces;

import io.analytics.domain.ForecastData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface IForecastService {
	public ForecastData getForecastData (Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);
}
