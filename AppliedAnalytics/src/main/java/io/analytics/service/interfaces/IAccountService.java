package io.analytics.service.interfaces;

import java.util.List;

import io.analytics.domain.Account;

public interface IAccountService {
	
	public Account getAccountById(int id);
	
	public boolean saveAccount(Account account);
	
	public List<Account> getAccountsOwnedByUser(int userId);
	
	public Account createAndSaveAccount(int ownerId, int filterId);
	
	public boolean addAccountToGoogleAccount(int accountId, int googleAccountId);
	
}
