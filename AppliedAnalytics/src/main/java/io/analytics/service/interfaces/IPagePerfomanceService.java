package io.analytics.service.interfaces;

import io.analytics.domain.PagePerformanceData;

import java.util.Date;

import com.google.api.client.auth.oauth2.Credential;

/**
 * Interface for the Page Performance widget service class.
 * 
 * @author gak
 *
 */
public interface IPagePerfomanceService {
	public PagePerformanceData getPagePerformanceData(Credential credential, String profileID, Date startDate, Date endDate, int maxResults);

	public String getProfile();
}
