/**
 *  dashboard.js
 */

/**
 * Clears the current dashboard and loads the provided dashboardId, if available.
 * If the provided dashboardId does not exist, the dashboard will not be updated.
 * 
 * @param dashboardId
 * @returns true if the dashboard successfully loaded, false otherwise.
 */
function loadDashboard(dashboardId) {
	$.post(applicationRoot + "application/dashboards/" + dashboardId, 
			{ },
			function(result) {
				var $content = newPage("dashboard");
				var dashboard = $.parseJSON(result);
				var widgetIdArray = dashboard.widgetIds;
				var widgetTypeIdArray = dashboard.widgetTypeIds;
				var widgets = dashboard.widgets;
				widgets = widgets.sort(compareWidgetPriority);
				
				// since the remove event is only specific to the dashboard, 
				// we create a callback method to set it up. This is so we
				// do not create the remove event for the widgets on the
				// Trends and Forecast pages.
				loadWidgets($content, widgets, function() {
													var $last = $content.children().last();
													var elementId = $last.attr("id");
													$last.on("click", ".dropdown-menu a.removeWidget", function() { // remove menu event
														Modal.call({
															"title" : "Remove Widget",
															"content": "Remove " + $("#" + elementId + " .widget_title").html() + " widget?",
															"action": function() { removeWidget(elementId); }
														});
													});
												});
				
				$content.sortable({ 
					revert: true, 
					tolerance: "pointer", 
					zIndex: 100,
					stop: updateWidgetPosition,
					cancel: "div.widget-content"
				});
				
	
			});
}

/**
 * Compares the priority of two Widget JSON objects. These objects should have 
 * an integer priority property such that high priority widgets are to be
 * placed prior to lower priority widgets.
 * @param w1
 * @param w2
 * @returns 0 if the priorities are equal, < 0 if w1 has higher priority than w2, > 0 if vice versa.
 */
function compareWidgetPriority(w1, w2) {
	return w1.priority - w2.priority;
}




