package io.analytics.service.interfaces;

import io.analytics.domain.Dashboard;
import io.analytics.domain.User;

public interface IDashboardService {

	void addNewDashboard(User user, int accountId, String dashboardName);
	
	void deleteDashboard(User user, int dashboardId);
	
	void updateDashboardWidgetLayout(User user, int dashboardId);
	
	public Dashboard getDashboard(int dashboardId);
	
}
