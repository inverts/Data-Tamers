package io.analytics.site.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.analytics.domain.Account;
import io.analytics.domain.Dashboard;
import io.analytics.domain.Widget;
import io.analytics.domain.WidgetLibrary;
import io.analytics.domain.WidgetLibraryType;
import io.analytics.service.interfaces.IAccountService;
import io.analytics.service.interfaces.IDashboardService;
import io.analytics.service.interfaces.ISessionService;
import io.analytics.service.interfaces.IWidgetLibrariesService;
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
	private IWidgetLibrariesService WidgetLibrariesService;
	private SessionModel sessionModel;
	private JSONArray dashboardsJSON;
	private List<WidgetLibraryData> widgetLibraryData;
	private List<WidgetLibrary> widgetLibraries;

	
	public SidePanelModel(IDashboardService dashboardService, IWidgetLibrariesService widgetLibrariesService, SessionModel sessionModel)
	{
		if (dashboardService == null || sessionModel == null)
			throw new IllegalArgumentException();
		
		this.DashboardService = dashboardService;
		this.WidgetLibrariesService = widgetLibrariesService;
		this.sessionModel = sessionModel;
		dashboardsJSON = new JSONArray();
		this.widgetLibraries = this.WidgetLibrariesService.getWidgetLibraries();
		this.widgetLibraryData = new ArrayList<WidgetLibraryData>();
		
		setWidgetLibraryData();
	}

	public JSONArray getDashboards() {
		return dashboardsJSON;
	}
	
	private void setWidgetLibraryData() {

		for(WidgetLibrary lib : this.widgetLibraries) {
			
			WidgetLibraryData data = new WidgetLibraryData();
			Map<String, Integer> info = new HashMap<String, Integer>();
			data.setLibraryTitle(lib.getName());
			
			List<WidgetLibraryType> widgetTypes = this.WidgetLibrariesService.getTypesInLibrary(lib.getId());
			
			for(WidgetLibraryType widgetType : widgetTypes) {
				info.put(widgetType.getName(), new Integer(widgetType.getId()));
			}
			data.setWidgetData(info);
			
			this.widgetLibraryData.add(data);	
		}
	}
	
	
	public List<WidgetLibraryData> getWidgetLibraryData() {
		return this.widgetLibraryData;
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
	
	
	public class WidgetLibraryData {
		
		private String libraryTitle;
		private Map<String, Integer> widgetData;
		
		public void setLibraryTitle(String name) {
			this.libraryTitle = name;
		}
		
		public String getLibraryTitle() {
			return this.libraryTitle;
		}
		
		public void setWidgetData(Map<String, Integer> data) {
			this.widgetData = data;
		}
		
		public Map<String, Integer> getWidgetData() {
			return this.widgetData;
		}
		
	}
	
	
	
}
