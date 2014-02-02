package io.analytics.service;

import java.util.List;

import io.analytics.domain.Account;

public class AccountService {

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
	
	/**
	 * Gets a list of accounts thst
	 * @param userId
	 * @return
	 */
	public List<Account> getAccountsOwnedByUser(int userId) {
		//TODO: Implement.
		return null;
	}
	
	/**
	 * Creates a new Account. In order for an Account to exist, it must have an owner,
	 * and it must have a default filter.
	 * @param ownerId
	 * @param filterId
	 * @return The new Account
	 */
	public Account createAccount(int ownerId, int filterId) {
		return null;
	}
}
