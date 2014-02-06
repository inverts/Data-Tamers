package io.analytics.repository.interfaces;

import java.util.List;

import io.analytics.domain.GoogleAccount;

public interface IGoogleAccountRepository {

	/**
	 * Adds a new Google Account into the database. The GoogleAccount must have a refresh token 
	 * and an owner id set in order to add it to the database.
	 * 
	 * @param account The GoogleAccount to add to the database.
	 * @return Returns true on success, false otherwise.
	 */
	public boolean addGoogleAccount(GoogleAccount account);

	/**
	 * Retrieves a list of Google accounts owned by one particular user.
	 * Note that this is different from a list of Google Accounts associated with
	 * one particular Account.
	 * @param ownerId The User id that you are trying to find Google Accounts for.
	 * @return A List of GoogleAccounts, which may be empty if none are found.
	 */
	public List<GoogleAccount> getGoogleAccountsForOwner(int ownerId);
	
}
