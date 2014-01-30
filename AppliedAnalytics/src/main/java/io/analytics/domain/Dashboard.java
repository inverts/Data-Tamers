package io.analytics.domain;

public class Dashboard {
	private int id;
	//For now, the Account ID serves as the parent/owner account. But in the distant future we may 
	//use dashboards in the same way that we use widgets.
	private int accountId;
	private int defaultFilterId;
	private String name;
	
	public Dashboard() {
	}
	public int getId() {
		return id;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public int getDefaultFilterId() {
		return defaultFilterId;
	}
	public void setDefaultFilterId(int defaultFilterId) {
		this.defaultFilterId = defaultFilterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
