package io.analytics.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.GoogleAccount;
import io.analytics.repository.interfaces.IGoogleAccountRepository;
import io.analytics.service.interfaces.IGoogleAccountService;

@Service
public class GoogleAccountService implements IGoogleAccountService {

	@Autowired
	IGoogleAccountRepository GoogleAccountRepository; 
	
	@Override
	public List<GoogleAccount> getGoogleAccountsForOwnerAccount(int accountId) {
		return GoogleAccountRepository.getGoogleAccountsForAccount(accountId);
	}

	@Override
	public GoogleAccount addNewGoogleAccount(GoogleAccount ga) {
		return GoogleAccountRepository.addGoogleAccount(ga);
	}

	@Override
	public boolean addGoogleAccountToAccount(int googleAccountId, int accountId) {
		return GoogleAccountRepository.addRelationshipToAccount(googleAccountId, accountId);
	}

	@Override
	public List<GoogleAccount> getGoogleAccountsForAccount(int accountId) {
		return GoogleAccountRepository.getGoogleAccountsForAccount(accountId);
	}

	@Override
	public boolean updateGoogleAccount(GoogleAccount ga) {
		return GoogleAccountRepository.updateGoogleAccount(ga);
	}
}
