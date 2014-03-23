package io.analytics.service;

import io.analytics.service.interfaces.ICoreReportingService;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.auth.oauth2.Credential;

public class TestOf_CoreReportingService {
	
	@Autowired
	ICoreReportingService CoreReportingService;

	public void testGetTopTrafficSources() {
		Credential credential = Mockito.mock(Credential.class);
	}
	
	public void testGetDimensionsByDay() {
	
	}
	
}
