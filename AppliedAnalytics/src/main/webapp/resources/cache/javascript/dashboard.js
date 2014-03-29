/**
 *  dashboard.js
 */

/**
 * Clears the current dashboard and loads the provided dashboardId, if available.
 * If the provided dashboardId does not exist, the dashboard will not be updated.
 * @author - Andrew Riley
 * @param dashboardId
 * @returns true if the dashboard successfully loaded, false otherwise.
 */


function loadDashboard(dashboardId) {
	$.post(applicationRoot + "application/dashboards/" + dashboardId, 
			{ },
			function(result) {
				var $content = newPage("dashboard", dashboardId);
				var dashboard = $.parseJSON(result);
				//var widgetIdArray = dashboard.widgetIds;
				//var widgetTypeIdArray = dashboard.widgetTypeIds;
				var widgets = dashboard.widgets;
				widgets = widgets.sort(compareWidgetPriority);
				
				// Setup widget remove functionality
				$trash = $(".trash").droppable({
					revert: false,
					tolerance: "touch",
					// highlight trash bin when the widget touches it.
					over: function(e, ui) {
						var $target = $(e.target);
						if ($target.hasClass("trash"))
							$(".trash span").css("color", "#00aeef");
						},
					// revert trash bin to unhighlighted state on out event.
					out: function(e) { $(".trash span").css("color", "#000"); },
					// triggers the remove on drop. Immediately hide the widget since removeWidget does not
					// remove it right away and the time the widget remains creates for some weird display
					// issues. If we this in vein, show the widget again.
					drop: function(e, ui) {
						e.preventDefault();
						ui.draggable.hide();
						$(e.target).hasClass("trash") ? removeWidget(ui.draggable.attr("id"))
											 		  : ui.draggable.show();
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
				
				// Setup dashboard sortable functionality that allows widgets to be dragged and moved around
				// as well as handle widgets being added.
				$content.sortable({ 
					// only do revert to start position animation when we try to click the widget
					// somewhere it cannot go.
					revert: "invalid",
					forcePlaceholderSize: true, // YOLO property (not sure if its needed).
					// we need the widgets to be up front when they drag otherwise they do not move
					// the other widgets around properly due to priority restraints with the html elements.
					zIndex: 100,
					// element that serves as the widget area when moving a new widget
					// from the drag n drop so the user can see where the widget
					// will be placed. Since we use a helper item to drag the widget, it
					// does not use its height and width attributes in conjunction with the
					// sortable list and therefore will not force the widgets in said list to
					// move.
					placeholder: "widget-placeholder",
					// update widgets after dragging.
					stop: updateWidgetPosition,
					// do not enable dragging while in these elements...
					cancel: "div.widget-content, header.w-header .w-text",
					// add a widget to the page functionality
					receive: function(e, ui) {
						if (addedWidget.hasClass("widget-select")) {
							// the widget itself is encapsulated into a container called .widget-select
							// and needs to be extracted out of it.
							var $w = addedWidget.children(":first");

							// did the user get the visualization?
							if ($w.children().length) {
								// set the currentItem html to be that of the widget.
								$(this).data().uiSortable.currentItem.html($w);
								// remove the encapsulating parent container (currentItem) and leave
								// the raw widget in its stead.
								$w.unwrap();
								
								// save widget to database
								addWidgetByList(parseInt(ui.item.attr("id")), $w);

							}
							else
								$(this).data().uiSortable.currentItem.remove();

							// zero out the widget for the next drag n drop
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




