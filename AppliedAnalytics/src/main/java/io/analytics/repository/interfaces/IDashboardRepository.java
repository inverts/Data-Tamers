package io.analytics.repository.interfaces;

import java.util.List;

import io.analytics.domain.Dashboard;

public interface IDashboardRepository {

	/**
	 * Adds a new Dashboard to the database, returning the id of the newly added Dashboard.
	 * @param accountId
	 * @param defaultFilterId
	 * @param dashboardName
	 * @return The id of the new Dashboard.
	 */
	public int addNewDashboard(int accountId, Integer defaultFilterId, String dashboardName);

	/**
	 * Adds a new Dashboard to the database, returning the id of the newly added Dashboard.
	 * @param accountId
	 * @param defaultFilterId
	 * @param dashboardName
	 * @return The id of the new Dashboard.
	 */
	public int addNewDashboard(Dashboard d);
	
	/**
	 * Removes a Dashboard from the database.
	 * @param dashboardId
	 */
	public void deleteDashboard(int dashboardId);

	/**
	 * Gets a Dashboard given an id.
	 * @param dashboardId
	 * @return A Dashboard with the matching id, or null if no Dashboards could be found or if there was an error.
	 */
	public Dashboard getDashboard(int dashboardId);
	
	/**
	 * Retrieves all the Dashboards that are listed under a particular account ID.
	 * @param accountId
	 * @return
	 */
	public List<Dashboard> getAccountDashboards(int accountId);
	
}
