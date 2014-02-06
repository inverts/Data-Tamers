package io.analytics.service.interfaces;

import java.util.List;

import io.analytics.domain.GoogleAccount;

public interface IGoogleAccountService {
	
	public GoogleAccount addNewGoogleAccount(GoogleAccount ga);
	
	public List<GoogleAccount> getGoogleAccountsForOwner(int ownerId);
	
	public boolean addGoogleAccountToAccount(int googleAccountId, int accountId);
	
}
