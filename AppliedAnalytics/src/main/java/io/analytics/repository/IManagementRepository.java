package io.analytics.repository;

import io.analytics.domain.GoogleUserData;

import com.google.api.services.analytics.model.Account;
import com.google.api.services.analytics.model.Accounts;
import com.google.api.services.analytics.model.Profiles;
import com.google.api.services.analytics.model.Webproperties;
import com.google.api.services.analytics.model.Webproperty;

public interface IManagementRepository {

	public GoogleUserData getGoogleUserData();

	public Accounts getAccounts();

	public Webproperties getWebproperties(Account a);

	public Profiles getProfiles(Account a, Webproperty w);
}