package io.analytics.site.models;

import io.analytics.domain.GoogleUserData;
import io.analytics.service.ManagementService;

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
	
	
	private ManagementService management;
	
	//The current profile used for queries
	private String accountId;
	private String propertyId;
	private String profileId;
	
	//The selected view of the account, web property, and profile on the settings page
	private String accountIdSelection;
	private String propertyIdSelection;
	private String profileIdSelection;
	
	
	public SettingsModel(ManagementService management) {
		this.management = management;
	}
	
	public GoogleUserData getUserData() {
		return management.getGoogleUserData();
	}

	public String getAccountIdSelection() {
		return accountIdSelection;
	}

	public void setAccountIdSelection(String accountIdSelection) {
		this.accountIdSelection = accountIdSelection;
	}

	public String getPropertyIdSelection() {
		return propertyIdSelection;
	}

	public void setPropertyIdSelection(String propertyIdSelection) {
		this.propertyIdSelection = propertyIdSelection;
	}

	public String getProfileIdSelection() {
		return profileIdSelection;
	}

	public void setProfileIdSelection(String profileIdSelection) {
		this.profileIdSelection = profileIdSelection;
	}
}
