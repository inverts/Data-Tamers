package io.analytics.domain;

/**
 * Represents a single Google Account with Google Analytics permissions.
 * This should not be confused with a Google Analytics Account. For example,
 * one Google Account may have many Google Analytics Accounts. A Google Account
 * allows us to keep a small amount of information about our user to personalize
 * the application, and is the key-holder to that user's Google Analytics accounts.
 * 
 * @author Dave
 *
 */
public class GoogleAccount {

	private int id;
	private int ownerAccountId;
	private String activeRefreshToken;
	
	public GoogleAccount(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public int getOwnerAccountId() {
		return ownerAccountId;
	}
	public void setOwnerAccountId(int ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}
	public String getActiveRefreshToken() {
		return activeRefreshToken;
	}
	public void setActiveRefreshToken(String activeRefreshToken) {
		this.activeRefreshToken = activeRefreshToken;
	}
	
	
}
