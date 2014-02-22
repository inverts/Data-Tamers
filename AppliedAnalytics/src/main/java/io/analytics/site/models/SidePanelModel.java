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
	private JSONArray dashboardLinks;

	
	public SidePanelModel(IDashboardService dashboardService, SessionModel sessionModel)
	{
		if (dashboardService == null || sessionModel == null)
			throw new IllegalArgumentException();
		this.DashboardService = dashboardService;
		this.sessionModel = sessionModel;
		dashboardLinks = new JSONArray();
		
	}

	public JSONArray getDashboardLinks() {
		return dashboardLinks;
	}
	
	/**
	 * 
	 * @return
	 * @throws CorruptedSessionException 
	 */
	public void generateDashboardLinks() throws CorruptedSessionException {
		
		Account account = sessionModel.getAccount();
		List<Dashboard> dashboards = DashboardService.getAccountDashboards(account.getId());
		
		for(Dashboard dashboard : dashboards) {
			
			JSONObject content = new JSONObject();
			String url = "/application/" + dashboard.getId();
			
			try {
				content.put("url", url);
				content.put("id", dashboard.getId());
				content.put("name", dashboard.getName());
			} catch (JSONException e) {
				//This only occurs if the key is null or the value being put is a non-finite number.
				e.printStackTrace();
			}
			
			dashboardLinks.put(content);
		}
		
		
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
