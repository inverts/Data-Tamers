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

	public Webproperties getWebproperties(Account a, Credential credential) {
		return REPOSITORY.getWebproperties(a, credential);
	}

	public Profiles getProfiles(Account a, Webproperty w, Credential credential) {
		return REPOSITORY.getProfiles(a, w, credential);
	}
	
	public Webproperties getWebproperties(String accountId, Credential credential) {
		return REPOSITORY.getWebproperties(accountId, credential);
	}
	
	public Profiles getProfiles(String accountId, String webpropertyId, Credential credential) {
		return REPOSITORY.getProfiles(accountId, webpropertyId, credential);
	}
}
