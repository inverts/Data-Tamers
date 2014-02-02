package io.analytics.domain;

import java.util.Calendar;

/**
 * Represents an Account with our application.
 * @author Dave
 *
 */
public class Account {

	//TODO: Ensure that the database stores an int32
	private int id;
	private int ownerId;
	private int defaultFilterId;
	private Calendar creationDate;
	
	public Account() {
	}
	public int getId() {
		return id;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getDefaultFilterId() {
		return defaultFilterId;
	}
	public void setDefaultFilterId(int defaultFilterId) {
		this.defaultFilterId = defaultFilterId;
	}
	public Calendar getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}
	
}
