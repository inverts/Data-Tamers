package io.analytics.site.models;

import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SidePanelModel {

	private IAccountService AccountService;
	//private IDashboardService DashboardService;
	private int accountId;

	
	public SidePanelModel(IAccountService accountService, int accountId)
	{
		if (accountService == null) throw new NullPointerException("Account Service is null for SidePanelModel.");
		this.AccountService = accountService;
		
		this.accountId = accountId;
	}
	
	/* Returns a JSON Array of dashboard Ids */
	public JSONArray getDashboardLinks() {
		
		JSONArray result = new JSONArray();
		
		Account current = AccountService.getAccountById(this.accountId);
		Dashboard[] dashboardList = current.getDashboardList();
		
		for(Dashboard dashboard : dashboardList) {
			
			JSONObject content = new JSONObject();
			String url = "/application/" + dashboard.getId();
			
			try {
				content.put("url", url);
				content.put("id", dashboard.getAccountId());
				content.put("name", dashboard.getName());
			} catch (Exception e) {
				//TODO: handle this
			}
			
			result.put(content);
		}
		
		return result;
		
	}
	
	/* Dummy Data; Delete when live data is available */
	public JSONArray getDashboardFakes() {
		
		JSONArray result = new JSONArray();
		
		Dashboard[] dashboardList = { new Dashboard(), new Dashboard(), new Dashboard(), new Dashboard(), new Dashboard() } ;
		
		dashboardList[0].setId(123456);
		dashboardList[0].setName("123456");
		
		dashboardList[1].setId(057102);
		dashboardList[1].setName("057102");
		
		dashboardList[2].setId(185395);
		dashboardList[2].setName("185395");

		dashboardList[3].setId(732059);
		dashboardList[3].setName("732059");
		
		dashboardList[4].setId(873620);
		dashboardList[4].setName("This dashboard has a long name");

		for(Dashboard dashboard : dashboardList) {
			
			JSONObject content = new JSONObject();
			
			String url = "application/" + dashboard.getId();
			try {
				content.put("url", url);
				content.put("id", dashboard.getId());
				content.put("name", dashboard.getName());
			} catch (Exception e) {
				//TODO: handle this
			}
			
			result.put(content);
		}
		
		return result;
	}
	
	
}
