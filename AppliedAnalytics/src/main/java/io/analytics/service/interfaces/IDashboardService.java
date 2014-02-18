package io.analytics.service.interfaces;

import io.analytics.domain.Dashboard;
import io.analytics.domain.User;

public interface IDashboardService {

	public int addNewDashboard(User user, int accountId, String dashboardName);
	
	public void deleteDashboard(User user, int dashboardId);
	
	public void updateDashboardWidgetLayout(User user, int dashboardId);
	
	public Dashboard getDashboard(int dashboardId);
	
}
