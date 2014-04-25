
package io.analytics.service.interfaces;

import io.analytics.domain.GoogleUserData;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;

public interface IManagementService {

	public GoogleUserData getGoogleUserData(Credential credential);

	public Accounts getAccounts(Credential credential);
	
	public Webproperties getWebproperties(Account a, Credential credential);
	
	public Profiles getProfiles(Account a, Webproperty w, Credential credential);
	
	public Webproperties getWebproperties(String accountId, Credential credential);
	
	public Profiles getProfiles(String accountId, String webpropertyId, Credential credential);
}
