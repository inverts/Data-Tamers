package io.analytics.service.interfaces;

import io.analytics.domain.KeywordInsightData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

public interface IKeywordInsightService {	
	KeywordInsightData getKeywordInsightData(Credential credential, String profileID, Date startDate, Date endDate, int maxResults);
}
