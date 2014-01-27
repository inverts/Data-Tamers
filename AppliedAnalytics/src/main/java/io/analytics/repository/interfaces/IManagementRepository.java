package io.analytics.repository.interfaces;

import io.analytics.domain.GoogleUserData;

import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;

public interface IManagementRepository {

	/**
	 * Gets information about a Google user.
	 * 
	 * @return
	 */
	public GoogleUserData getGoogleUserData();

	/**
	 * Gets a list of accounts and relevant data.
	 * 
	 * @return
	 */
	public Accounts getAccounts();
	
	public Webproperties getWebproperties(Account a);
	
	public Profiles getProfiles(Account a, Webproperty w);

	/**
	 * Gets a list of web properties for an account.
	 * 
	 * @param a
	 * @return
	 */
	public Webproperties getWebproperties(String a);
	
	public Profiles getProfiles(String a, String w);
}
