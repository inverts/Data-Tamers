package io.analytics.domain;

/**
 * Represents a single Google Account with Google Analytics permissions.
 * @author Dave
 *
 */
public class GoogleAccount {

	private int id;
	private int ownerAccountId;
	private String activeRefreshToken;
	
	public GoogleAccount() {
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
