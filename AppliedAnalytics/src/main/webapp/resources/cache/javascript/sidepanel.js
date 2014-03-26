/**
 * Sidepanel.js
 */

var active;
var addedWidget = { "events": null, "item": null };

$(function() {
	
	$(".sidepanel-background").css("width", $(".sidepanel").width());
	
	/*$(".sidepanel-nav").droppable({
		 tolerance: "touch",
		 greedy: false,
		 over: function(event, ui) { 
			 dashLinkHover();
		 },
		 out: function() { $(".dashlist").slideUp('fast'); }
	});*/

	/* Navigation hover effect */
	$(".nav-cell").hover(function(){
		$(this).animate({ backgroundColor: "gray" }, 100, "swing");
	}, function() {
		$(this).animate({ backgroundColor: "transparent" }, 0, "swing");
	});
	
	// dashboard list open on hover
	//$("#dashboard").hover(dashLinkHover);
	
	// dashboard list open on click
	$("#dashboard").click(function() { $(".dashlist").slideToggle("fast"); });
	
	
	// open trends widget list
	$("#trends").click(function() { $(".trends-list").slideToggle("fast"); });
	
	$(".trends-list").sortable({
		items: "li",
		connectWith: ".dashboard-content",
		helper: function() { 
			var helper = $("<div>").addClass("widget-select");
			return helper;  
		}
		
	});

	// open forecast widget list
	$("#forecast").click(function() { $(".forecast-list").slideToggle("fast"); });
	
	/*$(".forecast-list").sortable({
		items: "li",
		connectWith: ".dashboard-content",
		helper: function(e, ui) { 
			var helper = $("<div>").addClass("widget-select");
			loadWidget(helper, parseInt(ui.attr("id")), -1, $(".dashboard-content").data("n"));
			return helper;
		},
		beforeStop: function(e, ui) { 
			addedWidget.item = ui.helper; 
			addedWidget.events = $._data(ui.helper.children(":first").get(0), "events");
		}
	});*/
	
	$("li.dataForecastWidget").draggable({
		connectToSortable: ".dashboard-content",
		helper: function(e, ui) { 
			var helper = $("<div>").addClass("widget-select");
			loadWidget(helper, parseInt(e.currentTarget.id), -1, $(".dashboard-content").data("n"));
			return helper;
		},
		drag: function(e, ui) { 
			addedWidget.item = ui.helper; 
			addedWidget.events = $._data(ui.helper.children(":first").get(0), "events");
		},
		revert: "invalid"
	});
	
	$(".forecast-list").disableSelection();
	
	/* add new dashboard */
	$("#addDashboard").click(function() {
		$(".right-pane")
			.addClass("addDashboard");
			
		showAddDashboardPanel(550, addDashboardPage);
	});
	
	active = $("#dashboard .nav-txt").css("color", "#00aeef");

});


function dashLinkHover() {
	$(".dashlist").slideDown('fast');
	$('.sidepanel-content').hover(null, function() {
							$(".dashlist").slideUp('fast');
						});
}


function getDashboardList(list) {
	
	if (!list.length)
		console.error("No Dashboards");
	
	for(var i = 0; i < list.length; i++) {	
		createDashboardLink(list[i]);
	}
}

/* Populates the right pane with the new dashboard form */
function addDashboardPage() {
	$.post(applicationRoot + "addDashboard", {}, function( data ) {
		  $(".addDashboard").html( data );
	});
}

/* Removes a dashboard */
function removeDashboard(dashboardId) {
	
	$.post(applicationRoot + "application/deleteDashboard", {dashboardId: dashboardId}, 
			function(result) {
				result = $.parseJSON(result);
				var $dash = $("#dashlist #" + result.dashboardId);
				var name = $dash.children("a").html();
				if (name != "Default Dashboard" && name != "Second Dashboard")
					$dash.remove();
				
				Modal.alert({
					"title": "Remove Dashboard",
					"content": "Dashboard \"" + name + "\" has been successfully removed!",
				});
	});
}

/* Creates a dashboard link */
function createDashboardLink(dashboard) {
	var $dashlist = $("#dashlist");
	
	var $li = $("<li>").attr("id", dashboard.id)
					   .appendTo($dashlist);

	
	var $link = $("<a>").click(function() {
							loadDashboard(dashboard.id);
							active.css("color", "#fff");
							active = $("#dashboard .nav-txt").css("color", "#00aeef");
				})
				.html((dashboard.name)? dashboard.name : dashboard.id).appendTo($li);
	
	/* Creates the droppable action that will add a widget */
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
	
	var $delete = $("<span>").addClass("deleteDash glyphicon glyphicon-remove-circle")
							 .attr("title", "remove " + $link.html())
							 .appendTo($li);
	
	/* Set remove dashboard event */
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
	
	$("#dashlist #" + dashboard.id).hover(function() {
		$delete.toggle();
	});
	
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
                				$.post(applicationRoot + "application/createDashboard", { name: name },
                        				function(result) {
                        						createDashboardLink($.parseJSON(result));
                        						Modal.alert({
                        							"title": "Create Dashboard",
                        							"content": name + " has been successfully created!",
                        						});
                        						hideAddDashboardPanel();
                        				});
                			}
                		
                		});
                		
                	});
                    
                    
            }).click(function(e){ e.stopPropagation(); });
    }
}

/* Hides right pane */
function hideAddDashboardPanel() {
	$(".addDashboard").animate({width: 0}, 500, "swing", function() {
			$(".right-pane")
				.removeClass("addDashboard")
				.hide();
            $(window).off("click.dashboard");
            $(".addDashboard-content").hide(); 
    });
}

//TODO: I will probably combine these two functions into one when we hook
// up data on the back end. For now keep them separate.
function loadTrendsWidgets(trendsId) {
	/*$.post(applicationRoot + "application/trends/" + trendsId, 
			{ },
			function(result) {*/
				var $content = newPage("trends");
				//var library = $.parseJSON(result);
				//var widgetIdArray = library.widgetIds;
				//var widgetTypeIdArray = library.widgetTypeIds;
				//var widgets = library.widgets;
				trendsWidgetLibraryId = 1;
				$.post(applicationRoot + "widgetLibraries/" + trendsWidgetLibraryId + "/widgetTypes", function(data) {
					
					widgetLibraryTypes = $.parseJSON(data);
					widgets = new Array();
					for (index in widgetLibraryTypes) {
						widgets.push({"widgetTypeId": widgetLibraryTypes[index].id, "id": 0})
					}
					/*
					var widgets = [
					               {"widgetTypeId": 2, "id": 235},
					               {"widgetTypeId": 4, "id": 237},
					               {"widgetTypeId": 5, "id": 239},
					               {"widgetTypeId": 7, "id": 241}, 
					              ];
					*/
					
					loadWidgets($content, widgets, function() { });
					
					$content.sortable({
						helper: "clone",
						revert: true, 
						tolerance: "pointer", 
						zIndex: 100,
						cancel: "div.widget-content",
						over: function(event, ui) { 
							if (event.target == $("#dashboard").get(0)) 
								$("#dashboard").hover();
							
						}
					});
				

				});
				
			//});
}

function loadForecastWidgets(forecastId) {
	
	/*$.post(applicationRoot + "application/forecast/" + forecastId, 
	{ },
	function(result) {*/
		var $content = newPage("forecast");
		//var library = $.parseJSON(result);
		//var widgetIdArray = library.widgetIds;
		//var widgetTypeIdArray = library.widgetTypeIds;
		//var widgets = library.widgets;
		
		trendsWidgetLibraryId = 2;
		$.post(applicationRoot + "widgetLibraries/" + trendsWidgetLibraryId + "/widgetTypes", function(data) {
			
			widgetLibraryTypes = $.parseJSON(data);
			widgets = new Array();
			for (index in widgetLibraryTypes) {
				widgets.push({"widgetTypeId": widgetLibraryTypes[index].id, "id": 0})
			}
			/*
			var widgets = [
			               {"widgetTypeId": 1, "id": 234},
			               {"widgetTypeId": 6, "id": 236}, 
			              ];
			*/
			loadWidgets($content, widgets, function() { });
			
			$content.sortable({
				helper: "clone",
				revert: true, 
				tolerance: "pointer", 
				zIndex: 100,
				cancel: "div.widget-content"
			});
		});

	//});
	
}


