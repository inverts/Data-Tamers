package io.analytics.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.analytics.domain.Account;
import io.analytics.repository.AccountRepository;
import io.analytics.repository.GoogleAccountRepository;
import io.analytics.repository.interfaces.IAccountRepository;
import io.analytics.repository.interfaces.IGoogleAccountRepository;
import io.analytics.service.interfaces.IAccountService;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private IAccountRepository accountRepository;
	@Autowired
	private IGoogleAccountRepository googleAccountRepository;
	
	/**
	 * Gets an Account object given its id.
	 * @param id
	 * @return
	 */
	public Account getAccountById(int id) {
		//TODO: Implement.
		return null;
	}
	
	/**
	 * Attempts to save an account to the database and returns a boolean indicating success.
	 * @param account The Account to save.
	 * @return
	 */
	public boolean saveAccount(Account account) {
		//TODO: Implement.
		return false;
	}
	
	@Override
	public List<Account> getAccountsOwnedByUser(int userId) {
		return accountRepository.getUserOwnedAccounts(userId);
	}
	
	@Override
	public Account createAndSaveAccount(int ownerId, int filterId) {
		Account acc = new Account(-1);
		acc.setOwnerId(ownerId);
		acc.setDefaultFilterId(filterId);
		acc.setCreationDate(Calendar.getInstance());
		
		return accountRepository.addNewAccount(acc);
		
	}

	@Override
	public boolean addAccountToGoogleAccount(int accountId, int googleAccountId) {
		return googleAccountRepository.addRelationshipToAccount(googleAccountId, accountId);
	}
}
