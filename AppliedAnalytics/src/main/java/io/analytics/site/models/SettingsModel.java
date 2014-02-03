package io.analytics.site.models;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.api.services.analytics.model.*;

import io.analytics.domain.GoogleUserData;
import io.analytics.service.interfaces.IManagementService;
import io.analytics.service.interfaces.ISessionService;

@Component
@Scope("session")
public class SettingsModel {

	/*
	 * Settings information:
	 * 
	 * Name
	 * Email
	 * Picture
	 * 
	 * Current selected profile
	 * 
	 * Filter
	 * 	Date Range
	 * 	Metric
	 */
	
	private ISessionService sessionService;
	private IManagementService management;
	private GoogleUserData googleUserData;
	
	//The current profile used for queries
	private Profile activeProfile;
	
	//The selected view of the account, web property, and profile on the settings page
	private Account accountSelection;
	private Webproperty propertySelection;
	private Profile profileSelection;

	//The current lists of accounts, web properties, and profiles visible.
	private Accounts currentAccounts;
	private Webproperties currentWebproperties;
	private Profiles currentProfiles;
	
	private boolean ecommerceTrackingEnabled;
	
	
	public SettingsModel(ISessionService sessionService, IManagementService management) {
		this.sessionService = sessionService;
		this.management = management;
		currentAccounts = management.getAccounts(sessionService.getCredentials());
		googleUserData = management.getGoogleUserData(sessionService.getCredentials());
		//Select the first available account by default. 
		if (currentAccounts != null && currentAccounts.getItems() != null && !currentAccounts.getItems().isEmpty() ) {
			setAccountSelection(currentAccounts.getItems().get(0));
		}
		//If the above chain of selections resulted in a profile being selected, set it as the active one.
		setActiveProfile(profileSelection);
	}
	
	
	public SettingsModel(ISessionService sessionService, IManagementService management, Profile activeProfile) {
		this(sessionService, management); 
		this.setActiveProfile(activeProfile);
	}

	
	/**
	 * Finds an account by ID in a list of accounts. 
	 * Returns null if no account is found.
	 * 
	 * @param accounts
	 * @param accountId
	 * @return
	 */
	private Account findAccount(Accounts accounts, String accountId) {
		if (accounts != null && accounts.getItems() != null && !accounts.getItems().isEmpty()) {
			for (Account account : accounts.getItems())
				if (account.getId().equals(accountId))
					return account;
		}
		return null;
	}
	
	/**
	 * Finds a web property by ID in a list of web properties. 
	 * Returns null if no web property is found.
	 * 
	 * @param accounts
	 * @param accountId
	 * @return
	 */
	private Webproperty findWebproperty(Webproperties properties, String propertyId) {
		if (properties != null && properties.getItems() != null && !properties.getItems().isEmpty()) {
			for (Webproperty property : properties.getItems())
				if (property.getId().equals(propertyId))
					return property;
		}
		return null;
	}
	
	/**
	 * Finds a profile by ID in a list of profiles. 
	 * Returns null if no profile is found.
	 * 
	 * @param accounts
	 * @param accountId
	 * @return
	 */
	private Profile findProfile(Profiles profiles, String profileId) {
		if (profiles != null && profiles.getItems() != null && !profiles.getItems().isEmpty()) {
			for (Profile profile : profiles.getItems())
				if (profile.getId().equals(profileId))
					return profile;
		}
		return null;
	}
	
	
	public boolean hasECommerceTracking() {
		return activeProfile.getECommerceTracking();
	}


	public GoogleUserData getGoogleUserData() {
		return googleUserData;
	}


	public void setGoogleUserData(GoogleUserData googleUserData) {
		this.googleUserData = googleUserData;
	}


	public Profile getActiveProfile() {
		return activeProfile;
	}


	public boolean setActiveProfile(Profile activeProfile) {
		if (activeProfile != null) {
			this.activeProfile = activeProfile;
			
			//This may all be redundant:
			//TODO: Evaluate redundancy and determine if we can remove the lines below.
			currentWebproperties = management.getWebproperties(activeProfile.getAccountId());
			currentProfiles = management.getProfiles(activeProfile.getAccountId(), activeProfile.getWebPropertyId());
			accountSelection = findAccount(currentAccounts, activeProfile.getAccountId());
			propertySelection = findWebproperty(currentWebproperties, activeProfile.getWebPropertyId());
			profileSelection = activeProfile;
			
			return true;
		} 
		//Failure.
		return false;
	}
	
	public boolean setActiveProfile() {
		return setActiveProfile(this.profileSelection);
	}


	public Account getAccountSelection() {
		return accountSelection;
	}


	public void setAccountSelection(Account accountSelection) {
		if (accountSelection != null) {
			this.accountSelection = accountSelection;
			currentWebproperties = management.getWebproperties(accountSelection);
			currentProfiles = null;
			//Select the first available property by default. 
			if (currentWebproperties != null && currentWebproperties.getItems() != null && !currentWebproperties.getItems().isEmpty() ) {
				setPropertySelection(currentWebproperties.getItems().get(0));
			}
		}
	}

	public void setAccountSelection(String accountId) {
		setAccountSelection(findAccount(currentAccounts, accountId));
	}

	
	public Webproperty getPropertySelection() {
		return propertySelection;
	}
	

	
	public void setPropertySelection(Webproperty propertySelection) {
		if (propertySelection != null) {
			this.propertySelection = propertySelection;
			currentProfiles = management.getProfiles(this.accountSelection, this.propertySelection);
			//Select the first available profile by default. 
			if (currentProfiles != null && currentProfiles.getItems() != null && !currentProfiles.getItems().isEmpty() ) {
				setProfileSelection(currentProfiles.getItems().get(0));
			}
		}
	}
	
	
	
	public void setPropertySelection(String webpropertyId) {
		setPropertySelection(findWebproperty(currentWebproperties, webpropertyId));
	}

	
	public Profile getProfileSelection() {
		return profileSelection;
	}


	public void setProfileSelection(Profile profileSelection) {
		if (profileSelection != null) 
			this.profileSelection = profileSelection;
	}
	
	public void setProfileSelection(String profileId) {
		setProfileSelection(findProfile(currentProfiles, profileId));
	}


	public Accounts getCurrentAccounts() {
		return currentAccounts; 
	}


	public Webproperties getCurrentWebproperties() {
		return currentWebproperties;
	}


	public Profiles getCurrentProfiles() {
		return currentProfiles;
	}

	
}
