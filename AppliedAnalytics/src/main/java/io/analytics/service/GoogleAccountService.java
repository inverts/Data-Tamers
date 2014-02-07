package io.analytics.service;

import java.util.List;

import io.analytics.domain.GoogleAccount;
import io.analytics.service.interfaces.IGoogleAccountService;

public class GoogleAccountService implements IGoogleAccountService {

	@Override
	public List<GoogleAccount> getGoogleAccountsForOwner(int ownerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GoogleAccount addNewGoogleAccount(GoogleAccount ga) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addGoogleAccountToAccount(int googleAccountId, int accountId) {
		// TODO Auto-generated method stub
		return false;
	}
}
