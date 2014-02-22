package io.analytics.repository.interfaces;

import java.util.List;

import io.analytics.domain.Widget;

public interface IWidgetRepository {

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
	 * @param accountId
	 * @return
	 */
	public List<Widget> getDashboardWidgets(int dashboardId);
	
}
