package io.analytics.repository.interfaces;

public interface IDashboardRepository {

	void addNewDashboard(int accountId, int userId, String dashboardName);
	
	void deleteDashboard(int dashboardId);
	
	void updateDashboardWidgetLayout(int dashboardId);
	
}
