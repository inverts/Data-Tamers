package io.analytics.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.*;

import io.analytics.domain.GoogleUserData;
import io.analytics.repository.ManagementRepository;
import io.analytics.repository.ManagementRepository.CredentialException;
import io.analytics.service.interfaces.IManagementService;

/**
 * A service for accessing the following: - Google Analytics Management API -
 * Google user information
 * 
 * @author Dave Wong
 * 
 */
public class ManagementService implements IManagementService {
	private final ManagementRepository REPOSITORY;

	public ManagementService(Credential credential) throws CredentialException {
		this.REPOSITORY = new ManagementRepository(credential);
	}

	/**
	 * Gets information about a Google user.
	 * 
	 * @return
	 */
	public GoogleUserData getGoogleUserData() {
		return REPOSITORY.getGoogleUserData();
	}

	public Accounts getAccounts() {
		return REPOSITORY.getAccounts();
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
