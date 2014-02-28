package io.analytics.domain;


/**
 * This represents a Dashboard in our application.
 * A Dashboard is defined as a particular set of ordered widgets. Every Dashboard
 * belongs to a single Account, and has a name (not necessarily unique!).
 * 
 * @author Dave Wong
 *
 */
public class Dashboard {
	private int id;
	//For now, the Account ID serves as the parent/owner account. But in the distant future we may 
	//use dashboards in the same way that we use widgets.
	private int accountId;
	private int defaultFilterId;
	private String name;
	
	public Dashboard(int id) {
		this.id = id;
	}
	
	public Dashboard() {
		
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Do not use.
	 * 
	 * @deprecated When a Dashboard is created, its identity should not change from that point. 
	 * You can create a Dashboard and do things to it, but you can't create one and then turn it into another.
	 * A clone() method may be one appropriate addition to accommodate for this.
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
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
