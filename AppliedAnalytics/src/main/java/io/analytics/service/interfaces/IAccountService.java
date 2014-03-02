package io.analytics.service.interfaces;

import java.util.List;

import io.analytics.domain.Account;

public interface IAccountService {
	
	public Account getAccountById(int id);
	
	public boolean saveAccount(Account account);

	/**
	 * Gets a list of accounts owned by a particular user.
	 * 
	 * @param userId
	 * @return a list of accounts owned by a particular user.
	 */
	public List<Account> getAccountsOwnedByUser(int userId);

	/**
	 * Creates a new Account. In order for an Account to exist, it must have an owner,
	 * and it must have a default filter.
	 * The remaining details (id and creation date) are filled in by the method.
	 * WARNING: Currently does not return an Account with a valid Account ID.
	 * 
	 * @param ownerId
	 * @param filterId
	 * @return The new Account that was created and saved to the database. <code>null</code>
	 * if it could not be saved.
	 */
	public Account createAndSaveAccount(int ownerId, int filterId);
	
	public boolean addAccountToGoogleAccount(int accountId, int googleAccountId);
	
}
