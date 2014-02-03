package io.analytics.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.*;

import io.analytics.domain.GoogleUserData;
import io.analytics.repository.interfaces.IManagementRepository;
import io.analytics.service.interfaces.IManagementService;

/**
 * A service for accessing the following: - Google Analytics Management API -
 * Google user information
 * 
 * @author Dave Wong
 * 
 */
@Service
public class ManagementService implements IManagementService {
	@Autowired private IManagementRepository REPOSITORY;

	/**
	 * Gets information about a Google user.
	 * 
	 * @return
	 */
	public GoogleUserData getGoogleUserData(Credential credential) {
		return REPOSITORY.getGoogleUserData(credential);
	}

	public Accounts getAccounts(Credential credential) {
		return REPOSITORY.getAccounts(credential);
	}

	public Webproperties getWebproperties(Account a) {
		return REPOSITORY.getWebproperties(a);
	}

	public Profiles getProfiles(Account a, Webproperty w) {
		return REPOSITORY.getProfiles(a, w);
	}
	
	public Webproperties getWebproperties(String accountId) {
		return REPOSITORY.getWebproperties(accountId);
	}
	
	public Profiles getProfiles(String accountId, String webpropertyId) {
		return REPOSITORY.getProfiles(accountId, webpropertyId);
	}
}
