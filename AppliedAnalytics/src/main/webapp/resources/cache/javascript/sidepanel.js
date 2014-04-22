/**
 * Sidepanel.js
 * NOT LOCALIZED
 */

// Needed to preserve 'helper' so it can be inserted into the sortable list.
var addedWidget = {widget: null, events: null, data: null }; // variable used in dashboard.js to contain the widget being added. 
var currentPage; // contains which page the user is currently on.

$(function() {
	
	$(".sidepanel-background").css("width", $(".sidepanel").width());

	// Navigation gray hover effect
	$(".nav-cell").hover(function(){
		$(this).animate({ backgroundColor: "gray" }, 100, "swing");
	}, function() {
		$(this).animate({ backgroundColor: "transparent" }, 0, "swing");
	});
	
	// dashboard list open on click.
	$("#dashboard").click(function() { 
		$(".dashlist").slideToggle("fast", function() {
			tooltipReposition($(".dashlist"));
		}); 
	});
	
	
	// Opens trends library.
	$("#trends").click(function() { 
		$("#trends-list").slideToggle("fast", function() {
			tooltipReposition($(this));
		});
	});
	
	// Setup widget drag and drop for trends library.
	widgetDragNDrop("trends-list");

	
	// Opens forecast library.
	$("#forecast").click(function() { 
		$("#forecast-list").slideToggle("fast", function() {
			tooltipReposition($(this));
		}); 
	});
	
	
	
	// Setup widget drag and drop for forecast library.
	widgetDragNDrop("forecast-list");
	
	// Opens add dashboard pane.
	$("#addDashboard").click(addDashboardPage);
	
	// mark the page the user is currently on.
	 currentPage = $("#dashboard .nav-txt").css("color", "#00aeef");

});

/**
 * DEPRECATED: Used to invoke the dashboard list on hover.
 * @author - Andrew Riley
 */
function dashLinkHover() {
//	$(".dashlist").slideDown('fast');
//	$('.sidepanel-content').hover(null, function() {
//							$(".dashlist").slideUp('fast');
//						});
}

/**
 * Setups up a list to have the widget drag and drop ability.
 * @author - Andrew Riley
 * @param listClass - name of the list class that contains the draggable elements
 */
function widgetDragNDrop(listClass) {
	
	$("." + listClass + " .widgetLink").draggable({
		connectToSortable: ".dashboard-content",
		// fetches the widget via loadWidget function in widget.js
		helper: function(e, ui) { 
			var helper = $("<div>").addClass("widget widget-select").html("<img src=\"/appliedanalytics/cache/images/spinner.gif\" width = \"30\" height = \"30\" />");
			return helper;
		},
		start: function(e, ui) {
			if(ui.helper.length) {
				var widgetTypeId = parseInt(e.currentTarget.id);
				var $div = $("<div>").addClass("w_container")
									 .data("widgetTypeId", widgetTypeId)
									 .appendTo(ui.helper);
				
				loadWidget($div, widgetTypeId);
				
				$("iframe.tutorial").remove();
				$(".tutorialToggle").hide(function() {
					if ($(this).data("hasTooltip"))
						$("#tipsy" + $(this).data("tooltip-n")).remove();
				});
				
				removeTooltips($(".sidepanel"));
			}

		},
		// before inserting the widget, we need to fetch a clone of the widget to preserve the events
		// during this process. Also terminate the helper at this point since we no longer need it.
		stop: function(e, ui){ 
			var temp = ui.helper.clone(true, true).children(".w_container");
			addedWidget.widget = ui.helper; // need to set it as the actual object to preserve d3 events.
			addedWidget.events = $._data(temp.get(0), "events"); // copy events of the cloned object to be used on the receive event.
			addedWidget.data = {"hasData": temp.data("hasData"), "widgetTypeId": temp.data("widgetTypeId")};
			ui.helper.remove(); 
		},
		revert: false
	});
	
	// not 100% sure if this is needed or not. Was in a lot of the examples.
	$("." + listClass).disableSelection();
}

/**
 * Converts a list of dashboard Ids into the dashboard list 
 * displayed in the side panel.
 * @author - Andrew Riley
 * @param list - list of dashboard Ids
 */
function getDashboardList(list) {
	
	// TODO: Handle case of no dashboards. Should do something besides throw a console error.
	if (!list.length) {
		/*Modal.alert({
			"title": "No dashboards available!",
			"content": "It looks like you don't have any dashboards - click the Add New Dashboard link to get started!",
			"size": 'modal-default'
		});*/
		$(".dash-list").tooltip({
			"content": "It looks like you don't have any dashboards - click the Add New Dashboard link to get started!",
			"open": "load",
			"close": {"element": "", "event": "click", 
				"callback": function() {
				$(".dash-list").off("load.oTooltip").off("click.cTooltip");
			}}
		});
	}
	
	// Create a dashboard link in the sidepanel for each dashboard Id.
	for(var i = 0; i < list.length; i++) {	
		createDashboardLink(list[i]);
	}
}

/**
 * Fetches the html to populate the right pane when the page is invoked.
 * @author - Andrew Riley
 */
function addDashboardPage() {
	$.ajax({
		type: 'POST',
		url: applicationRoot + "addDashboard",
		success: function(data) {
			Modal.call({
				"content": data,
				"title": "Create Dashboard",
				"submit": true,
				"buttons" : {
					 			"size": "default", 
					 			"confirm": {"name": "Create", "action": function(e) {
			                		e.preventDefault();
			                		
			                		var name = $("#dashboardName").val();
			                		
			                		
			                		Modal.call({
			                			"title": "Create Dashboard",
			                			"content": "Create new dashboard \"" + name + "\"?",
			                			"action": function(e) { 
			                				e.preventDefault();
			                				$.ajax({
			                					type: 'POST',
			                					url: applicationRoot + "application/createDashboard",
			                					data: { name: name },
			                					success: function(result) {
			                						createDashboardLink($.parseJSON(result));
			                						Modal.alert({
			                							"title": "Success!",
			                							"content": name + " has been successfully created!",
			                						});
			                					},
			                					error: handleAjaxError
			                				}); // end ajax
			                			} // end action
			                		}); // end modal
			                	  } // end action
					 			}, // end confirm
					 			"cancel": "Cancel"
					 		} // end buttons 
				});
		},
		error: handleAjaxError
	});
}

/**
 * Deletes a dashboard.
 * @author - Andrew Riley
 * @param dashboardId - the id of the dashboard to be deleted.
 */
function removeDashboard(dashboardId) {
	$.ajax({
		type: 'POST',
		url: applicationRoot + "application/deleteDashboard",
		data: {dashboardId: dashboardId},
		success: function(result) {
			result = $.parseJSON(result);
			var $dash = $("#dashlist #" + result.dashboardId);
			var name = $dash.children("a").html();
			if (name != "Default Dashboard" && name != "Second Dashboard")
				$dash.remove();
			
			Modal.alert({
				"title": "Remove Dashboard",
				"content": "Dashboard \"" + name + "\" has been successfully removed!",
			});
		},
		error: handleAjaxError
	});
}

/**
 * Creates a new dashboard page.
 * @author - Andrew Riley
 * @param dashboard - dashboard object containing various properties like id and name.
 */
function createDashboardLink(dashboard) {
	var $dashlist = $("#dashlist");
	
	// dashboard link list item.
	var $li = $("<li>").attr("id", dashboard.id).appendTo($dashlist);

	// dashboard link click events
	var $link = $("<a>").prop("href", "#dashboards/" + dashboard.id).click(function() {
							loadDashboard(dashboard.id);
							window.dashboardId = dashboard.id;
							currentPage.css("color", "#fff");
							currentPage = $("#dashboard .nav-txt").css("color", "#00aeef");
				})
				.html((dashboard.name)? dashboard.name : dashboard.id).appendTo($li);
	
	// droppable functionality to allow drag and drop widgets into this dashboard from elsewhere.
	$link.droppable({
		accept: ".w_container",
		tolerance: "touch",
		over: function(event) {
			$dashlist.children().removeClass("hover");
			$li.addClass("hover");
		},
		out: function(event) {
			$li.removeClass("hover");
		},
		drop: function(e, ui) {
			if ($li.hasClass("hover")) {
				addWidget(ui.draggable, $(e.target).parent());
			}
			
			$li.removeClass("hover");
		}
	});
	
	// sets up the delete icon.
	var $delete = $("<span>").addClass("deleteDash glyphicon glyphicon-remove-circle")
							 .attr("title", "remove " + $link.html())
							 .appendTo($li);
	
	// adds remove event to delete icon.
	$delete.click(function(e){
		e.preventDefault();
		var name = $(this).siblings("a").html();
		var id = $(this).parent().attr("id");
		Modal.call({
			"title": "Remove Dashboard",
			"content": "Delete dashboard \"" + name + "\"?",
			"action": function(e) { e.preventDefault(); removeDashboard(id); }
		});
		
	});
	
	// display delete icon on hover only
	$("#dashlist #" + dashboard.id).hover(function() { $delete.toggle(); });
	
}

/* Opens up right pane */
function showAddDashboardPanel(width, showPage) {        
    if ($(".addDashboard").length) {
    	addDashboardPage();
    	$(".addDashboard")
    		.show()
    		.animate({width: width}, 500, "swing", function() {
                    $(".addDashboard-content").show();                        
                    $(window).on("click.dashboard", function() {
                    	hideAddDashboardPanel();
                    });        
                    
                    /* Create dashboard submission */
                	$("#dash-submit").on("click", function(e) {
                		e.preventDefault();
                		
                		var name = $("#dashboardName").val();
                		
                		
                		Modal.call({
                			"title": "Create Dashboard",
                			"content": "Create new dashboard \"" + name + "\"?",
                			"action": function(e) { 
                				e.preventDefault();
                				$.ajax({
                					type: 'POST',
                					url: applicationRoot + "application/createDashboard",
                					data: { name: name },
                					success: function(result) {
                						createDashboardLink($.parseJSON(result));
                						Modal.alert({
                							"title": "Success!",
                							"content": name + " has been successfully created!",
                						});
                						hideAddDashboardPanel();
                					},
                					error: handleAjaxError
                				});
                			}
                		
                		});
                		
                	});
                    
                    
            }).click(function(e){ e.stopPropagation(); });
    }
}

/**
 * Hides the add dashboard pane on click via animation.
 * @author - Andrew Riley
 */
function hideAddDashboardPanel() {
	$(".addDashboard").animate({width: 0}, 500, "swing", function() {
			$(".right-pane")
				.removeClass("addDashboard")
				.hide();
            $(window).off("click.dashboard");
            $(".addDashboard-content").hide(); 
    });
}



////TODO: I will probably combine these two functions into one when we hook
//// up data on the back end. For now keep them separate.
//function loadTrendsWidgets(trendsId) {
//	/*$.post(applicationRoot + "application/trends/" + trendsId, 
//			{ },
//			function(result) {*/
//				var $content = newPage("trends");
//				//var library = $.parseJSON(result);
//				//var widgetIdArray = library.widgetIds;
//				//var widgetTypeIdArray = library.widgetTypeIds;
//				//var widgets = library.widgets;
//				trendsWidgetLibraryId = 1;
//				$.post(applicationRoot + "widgetLibraries/" + trendsWidgetLibraryId + "/widgetTypes", function(data) {
//					
//					widgetLibraryTypes = $.parseJSON(data);
//					widgets = new Array();
//					for (index in widgetLibraryTypes) {
//						widgets.push({"widgetTypeId": widgetLibraryTypes[index].id, "id": 0})
//					}
//					/*
//					var widgets = [
//					               {"widgetTypeId": 2, "id": 235},
//					               {"widgetTypeId": 4, "id": 237},
//					               {"widgetTypeId": 5, "id": 239},
//					               {"widgetTypeId": 7, "id": 241}, 
//					              ];
//					*/
//					
//					loadWidgets($content, widgets, function() { });
//					
//					$content.sortable({
//						helper: "clone",
//						revert: true, 
//						tolerance: "pointer", 
//						zIndex: 100,
//						cancel: "div.widget-content",
//						over: function(event, ui) { 
//							if (event.target == $("#dashboard").get(0)) 
//								$("#dashboard").hover();
//							
//						}
//					});
//				
//
//				});
//				
//			//});
//}
//
//function loadForecastWidgets(forecastId) {
//	
//	/*$.post(applicationRoot + "application/forecast/" + forecastId, 
//	{ },
//	function(result) {*/
//		var $content = newPage("forecast");
//		//var library = $.parseJSON(result);
//		//var widgetIdArray = library.widgetIds;
//		//var widgetTypeIdArray = library.widgetTypeIds;
//		//var widgets = library.widgets;
//		
//		trendsWidgetLibraryId = 2;
//		$.post(applicationRoot + "widgetLibraries/" + trendsWidgetLibraryId + "/widgetTypes", function(data) {
//			
//			widgetLibraryTypes = $.parseJSON(data);
//			widgets = new Array();
//			for (index in widgetLibraryTypes) {
//				widgets.push({"widgetTypeId": widgetLibraryTypes[index].id, "id": 0})
//			}
//			/*
//			var widgets = [
//			               {"widgetTypeId": 1, "id": 234},
//			               {"widgetTypeId": 6, "id": 236}, 
//			              ];
//			*/
//			loadWidgets($content, widgets, function() { });
//			
//			$content.sortable({
//				helper: "clone",
//				revert: true, 
//				tolerance: "pointer", 
//				zIndex: 100,
//				cancel: "div.widget-content"
//			});
//		});
//
//	//});
//	
//}


