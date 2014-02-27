package io.analytics.service.interfaces;

import io.analytics.domain.Widget;

import java.util.List;

public interface IWidgetService {

	/**
	 * Adds a new Widget to the database, returning the id of the newly added Widget.
	 * @param accountId
	 * @param defaultFilterId
	 * @param dashboardName
	 * @return The id of the new Dashboard.
	 */
	public int addNewWidget(int defaultFilterId, int widgetTypeId, int dashboardId, int priority);

	/**
	 * Adds a new Widget to the database, returning the id of the newly added Widget.
	 * @param accountId
	 * @param defaultFilterId
	 * @param dashboardName
	 * @return The id of the new Dashboard.
	 */
	public int addNewWidget(Widget w);
	
	/**
	 * Removes a Widget from the database.
	 * @param dashboardId
	 */
	public void deleteWidget(int widgetId);

	/**
	 * Retrieves all the Widgets that are listed under a particular Dashboard ID.
	 * @param dashboardId The id of the dashboard we want widgets for.
	 * @return a List of all widgets beloning to the dashboard ID. Returns null if the dashboard ID is 0 or less, 
	 * or if there was a problem accessing the data.
	 */
	public List<Widget> getDashboardWidgets(int dashboardId);
	
}
