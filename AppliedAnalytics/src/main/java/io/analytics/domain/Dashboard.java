package io.analytics.domain;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import io.analytics.site.models.JSONSerializable;


/**
 * This represents a Dashboard in our application.
 * A Dashboard is defined as a particular set of ordered widgets. Every Dashboard
 * belongs to a single Account, and has a name (not necessarily unique!).
 * 
 * @author Dave Wong
 *
 */
public class Dashboard implements JSONSerializable {
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

	@Override
	public String getJSONSerialization() {
		
		Gson g = new Gson();
		return g.toJson(this);
		
	}
	
}
