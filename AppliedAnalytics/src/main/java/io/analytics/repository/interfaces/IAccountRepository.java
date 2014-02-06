package io.analytics.repository.interfaces;

import io.analytics.domain.Account;

import java.util.List;

public interface IAccountRepository {

	/**
	 * Gets a list of accounts that this user owns.
	 * 
	 * @param userId
	 * @return A (possibly empty) list of Accounts. Returns <code>null</code> if there was a problem.
	 */
	public List<Account> getUserOwnedAccounts(int ownerUserId);

	
	/**
	 * Adds a new account to the database.
	 * 
	 * @param a The new Account to add.
	 * @return <code>true</code> if the Account was added successfully. <code>false</code> otherwise.
	 */
	public boolean addNewAccount(Account a);
}
