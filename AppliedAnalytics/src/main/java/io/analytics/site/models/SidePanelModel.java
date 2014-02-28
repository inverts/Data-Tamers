package io.analytics.site.models;

import java.util.List;

import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.site.models.SessionModel.CorruptedSessionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SidePanelModel {

	private IDashboardService DashboardService;
	private SessionModel sessionModel;
	private JSONArray dashboardsJSON;

	
	public SidePanelModel(IDashboardService dashboardService, SessionModel sessionModel)
	{
		if (dashboardService == null || sessionModel == null)
			throw new IllegalArgumentException();
		this.DashboardService = dashboardService;
		this.sessionModel = sessionModel;
		dashboardsJSON = new JSONArray();
		
	}

	public JSONArray getDashboards() {
		return dashboardsJSON;
	}
	
	/**
	 * 
	 * @return
	 * @throws CorruptedSessionException 
	 */
	public void generateDashboardInfo() throws CorruptedSessionException {
		
		Account account = sessionModel.getAccount();
		List<Dashboard> dashboards = DashboardService.getAccountDashboards(account.getId());
		
		for(Dashboard dashboard : dashboards) {
			
			try {
				dashboardsJSON.put(new JSONObject(dashboard.getJSONSerialization()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
}
