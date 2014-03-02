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
	public GoogleAccount addGoogleAccount(GoogleAccount account);

	
	/**
	 * Retrieves a list of Google accounts owned by one particular Account.
	 * Note that this is different from a list of Google Accounts available to
	 * one particular Account.
	 * 
	 * @param accountId The Account id
	 * @return A List of GoogleAccounts, which may be empty if none are found, or null if there was a data access problem.
	 */
	public List<GoogleAccount> getGoogleAccountsForOwnerAccount(int accountId);

	
	/**
	 * Retrieves a list of GoogleAccounts available to
	 * one particular Account.
	 * 
	 * @param accountId The Account id
	 * @return A List of GoogleAccounts, which may be empty if none are found, or null if there was a data access problem.
	 */
	public List<GoogleAccount> getGoogleAccountsForAccount(int accountId);
	
	
	/**
	 * Adds an access relationship between a Google Account and an Account.
	 * The given Account will be given access to the provided Google Account.
	 * 
	 * @param googleAccountId The Google Account ID.
	 * @param accountId The Account ID.
	 * @return <code>true</code> if the operation succeeded, <code>false</code> otherwise.
	 */
	public boolean addRelationshipToAccount(int googleAccountId, int accountId);
	
}
