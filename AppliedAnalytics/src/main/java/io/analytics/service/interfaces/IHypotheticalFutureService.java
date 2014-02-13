package io.analytics.service.interfaces;

import io.analytics.domain.HypotheticalFutureData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface IHypotheticalFutureService {

	HypotheticalFutureData getHypotheticalFutureData(Credential credential, String profileID, String metric, Date startDate, Date endDate, int maxResults);
}
