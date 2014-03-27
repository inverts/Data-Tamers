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
				var $content = newPage("dashboard", dashboardId);
				var dashboard = $.parseJSON(result);
				var widgetIdArray = dashboard.widgetIds;
				var widgetTypeIdArray = dashboard.widgetTypeIds;
				var widgets = dashboard.widgets;
				widgets = widgets.sort(compareWidgetPriority);
				
				// Setup widget remove functionality
				$trash = $(".trash").droppable({
					revert: false,
					tolerance: "touch",
					over: function(e, ui) {
						var $target = $(e.target);
						if ($target.hasClass("trash"))
							$(".trash span").css("color", "#00aeef");
						},
					out: function(e) { $(".trash span").css("color", "#000"); },
					drop: function(e, ui) {
						ui.draggable.hide();
						$(e.target).hasClass("trash") ? removeWidget(ui.draggable.attr("id"))
											 		  : ui.draggable.show();
						e.stopPropagation();
					}
				});
					
					loadWidgets($content, widgets);

					/*
					 * This will not work as-is but may be a useful code snippet for loading in the list of dashboards.
				$.post(applicationRoot + "application/dashboards", function(data) {
						dashboards = $.parseJSON(data);
						dashboardList = $last.find('.dropdown-menu ul#dashboard-list');
						for (index in dashboards) {
							dashboardList.append('<li><a href="#">' + dashboards[index].name + '</a></li>');
						}
				});
				*/
				
				$content.sortable({ 
					revert: "invalid",
					forcePlaceholderSize: true,
					//tolerance: "touch", 
					zIndex: 100,
					placeholder: "widget-placeholder",
					stop: updateWidgetPosition,
					cancel: "div.widget-content, header.w-header .w-text",
					// add widget functionality
					receive: function(e, ui) {
						if (addedWidget.hasClass("widget-select")) {
							var $w = addedWidget.children(":first");
							$(this).data().uiSortable.currentItem.html($w);
							$w.unwrap();
							
							addWidgetByList(parseInt(ui.item.attr("id")), $w);
							
							addedWidget = null;
						}
					},
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




