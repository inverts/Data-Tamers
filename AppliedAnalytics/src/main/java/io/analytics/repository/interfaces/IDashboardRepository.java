package io.analytics.repository.interfaces;

import io.analytics.domain.Dashboard;

public interface IDashboardRepository {

	public void addNewDashboard(int accountId, int userId, String dashboardName);
	
	public void deleteDashboard(int dashboardId);
	
	public void updateDashboardWidgetLayout(int dashboardId);
	
	public Dashboard getDashboard(int dashboardId);
	
}
