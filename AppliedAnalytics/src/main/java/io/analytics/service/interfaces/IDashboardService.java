package io.analytics.service.interfaces;

import java.util.List;

import io.analytics.domain.Dashboard;
import io.analytics.domain.User;

public interface IDashboardService {

	public int addNewDashboard(int accountId, Integer defaultFilterId, String dashboardName);
	
	public void deleteDashboard(User user, int dashboardId);
	
	public void updateDashboardWidgetLayout(User user, int dashboardId);
	
	public Dashboard getDashboard(int dashboardId);
	
	public List<Dashboard> getAccountDashboards(int accountId);
	
}
