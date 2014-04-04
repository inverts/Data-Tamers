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
				
				
				
				/************************
				 * 	  REMOVE WIDGET		*
				 ************************/
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
				
				
				/************************
				 *  	DROP WIDGET	    *
				 ************************/
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
						
						//e.preventDefault();
						
						if (addedWidget.widget.hasClass("widget-select")) {
							// the widget itself is encapsulated into a container called .widget-select
							// and needs to be extracted out of it.
							var $w = addedWidget.widget.children(".w_container");
			
							// set the currentItem html to be that of the widget.
							$(this).data().uiSortable.currentItem.html($w);
							// remove the encapsulating parent container (currentItem) and leave
							// the raw widget in its stead.
							$w.unwrap();
							
							for(var e in addedWidget.events) {
								for(var i = 0; i < addedWidget.events[e].length; i++)
									$w.on(addedWidget.events[e][i].type, addedWidget.events[e][i].selector, addedWidget.events[e][i].handler);
							}

							// VALIDATION							
							// widget did not load at all!
							var reload;
							if (!addedWidget.events) {
								reload = setTimeout(function() {
									if (!$w.children().length) // if no events, that means we did not get the widget loaded at all
										loadWidget($w.empty(), parseInt(ui.item.attr("id"))); // reload the widget.
									else
										clearTimeout(this);
								}, 5000);
							}
							
							
							// widget loaded but data did not!
							if ($w.children().length && !addedWidget.data.hasData)
								updateWidget($w.data("widgetTypeId"), $w.attr("id"));
							
							// save widget to database
							addWidgetByList(parseInt(ui.item.attr("id")), $w);

							// zero out the widget for the next drag n drop
							addedWidget = {widget: null, events: null, data: null };
							
							if ($w.children().length && reload)
								clearTimeout(reload);
						}
						else
							$(this).data().uiSortable.currentItem.remove();
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




