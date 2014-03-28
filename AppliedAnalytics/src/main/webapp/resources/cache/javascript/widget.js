/**
 *  widget.js
 */


/**
 * calls each widgets model and view and appends
 * the widget to the associated page.
 * 
 * @param $content - the jQuery element the widget will be appended too.
 * @param widgetIdArray - array of widgetIds
 */
//TODO: Accommodate widget specifics (like filter).
function loadWidgets($content, widgets, callback) {
	//TODO: Allow custom options for how widgets should be displayed. (Should the have a remove option, for example)
	for(var i = 0; i < widgets.length; i++) {

		var widgetTypeId = widgets[i].widgetTypeId;
		var widgetId = widgets[i].id;
		
		// load in each widget and fire the callback
		loadWidget($content, widgetTypeId, widgetId, i, callback);
		
	}
	
	// store number of widgets loaded
	// widget will not be dragged while user clicks on content
	$content.data("n", widgets.length);
}


/**
 * Loads in the specified widget based on widgetTypeId.
 * 
 * @author - Andrew Riley
 * @param $content - the jQuery page element the widget will be appended too.
 * @param widgetTypeId - Database Id of the widget type.
 * @param widgetId - Database Id of the widget instance.
 * @param i - an arbitrary number, usually the index 
 * priority or number of widgets + 1.
 */
function loadWidget($content, widgetTypeId, widgetId, i, callback)
{
	//Create an empty widget div.
	var $div = $("<div>").addClass("w_container")
						 .data({
							 	"widgetTypeId": widgetTypeId, 
							 	"widgetId": widgetId 
							   })
						 .appendTo($content);
	
	$div.data("pos", $div.index());
	
	var elementId; 
	
	// which widget's load function do we need to call?
	switch(widgetTypeId)
	{
		case 1: 
			elementId = "dataForecastWidget" + i;
			$div.attr("id", elementId);
			$.when(loadDataForecast(elementId)).then(function() { widgetEvents($content, $div, elementId); });
			break;
			
		case 2:
			elementId = "websitePerformanceWidget" + i;
			$div.attr("id", elementId);
			$.when(loadWebsitePerformanceWidget("websitePerformanceWidget" + i)).then(function() { widgetEvents($content, $div, elementId); });
			break;
			
		case 7:
			elementId = "keyContributingFactorsWidget" + i;
			$div.attr("id", elementId);
			$.when(loadKeyContributingFactorsWidget(elementId)).then(function() { widgetEvents($content, $div, elementId); });
			break;
		
		case 4:
			elementId = "keywordInsightWidget" + i;
			$div.attr("id", elementId);
			$.when(loadKeywordInsight(elementId)).then(function() { widgetEvents($content, $div, elementId); });
			break;
			
		case 5:
			elementId = "trafficSourceTrendsWidget" + i;
			$div.attr("id", elementId);
			$.when(loadTrafficSourceTrendsWidget(elementId)).then(function() { widgetEvents($content, $div, elementId); });
			break;
			
		case 6: 
			elementId = "boostPerformanceWidget" + i;
			$div.attr("id", elementId);
			$.when(loadBoostPerformanceWidget(elementId)).then(function() { widgetEvents($content, $div, elementId); });
			break;
	}
	
	// execute callback function if provided.
	if (callback)
		callback(elementId);

}

/**
 * Sets up all global widget events.
 * @author - Andrew Riley
 * @param $content
 * @param $div
 * @param elementId
 */
function widgetEvents($content, $div, elementId) {
	
	// collapse event on title double click
	$div.on("dblclick", ".widget_title", function(e) {
		$(this).parent().siblings(".widget-content").slideToggle("fast");
	});

	// display trash bin for removal on click and hold
	if ($content.hasClass("dashboard-content") || $content.hasClass("widget-select")) {
		var timeoutId = 0;
		$div.on("mousedown.widget", function(e) {
			// we use a timeout to capture the hold so we don't get flashes
			// of the trash bin on every single click.
			timeoutId = setTimeout(function() { $trash.show(); }, 500); 
		}).bind("mouseup.widget", function(e){
			// if just a normal click and not hold, we don't want to show the trash bin at all.
			clearTimeout(timeoutId);
			$trash.hide();
		});
	}
	
}


/**
 * Updates each widget"s visualization based on new data 
 * without reloading the entire widget.
 * @author - Andrew Riley
 */
function updateWidgets(){
	
	// get all the active widgets on the current page.
	var widgets = $.map($(".w_container"), function(widget) {
						return { "elementId": widget.id, "widgetTypeId" : $(widget).data("widgetTypeId") };
					});
	
	for(var i = 0; i < widgets.length; i++) {
		
		// call appropriate widget's update function.
		switch(widgets[i].widgetTypeId)
		{
			case 1:
				updateDataForecast(widgets[i].elementId);
				break;
			case 4:
				updateKeywordInsight(widgets[i].elementId);
				break;
		}
	}
		
}

/**
 * Since adding widgets is a little different when doing it from
 * the drag n drop, I created a new function that handles it.
 * @author - Andrew Riley
 * @param widgetTypeId - the widget type id
 * @param $widget - the widget helper object. Needed to inject the new widget data to make it 'real'.
 */
function addWidgetByList(widgetTypeId, $widget) {
	
	var $dash = $(".dashboard-content");
	var nWidgets = $dash.data("n");
	
	$.post(applicationRoot + "addWidget", {widgetTypeId: widgetTypeId, dashboardId: $dash.data("id"), priority: $widget.index()},
			function(response) {
				var result = $.parseJSON(response);
				
				$widget.data({
					"widgetId": result.widgetId,
					"widgetTypeId": widgetTypeId
				});
				
				if (result)
					$dash.data("n", ++nWidgets);
				
				console.log("added widget: " + $widget.attr("id"));
				
			});
	
	
}

/**
 * Adds a widget from dragging the widget onto a dashboard link.
 * @author - Andrew Riley
 * @param widget - the widget to be added into another dashboard
 * @param li - dashboard li element. Used to extract the dashboard Id
 */
function addWidget(widget, li) 
{
	if (li.length) {
		var id = widget.attr("id");
		var widgetTypeId = widget.data("widgetTypeId");
		var widgetName = $("#" + id + " .widget_title").html();
		//var nWidgets = $(".dashboard-content").data("n");

		$.post(applicationRoot + "addWidget", {widgetTypeId: widgetTypeId, dashboardId: li.attr("id")},
				function(response) {
					var result = $.parseJSON(response);
					Modal.alert({
						"title" : "Add Widget",
						"content": widgetName + " has been added to dashboard \"" + li.children("a").html()  + "\"!"
					});
				});

	}

}


/**
 * Removes a widget from the dashboard and deletes it from the database.
 * @author - Andrew Riley
 * @param id - the element id of the widget as on the current page
 */
function removeWidget(id) {

	if (id) {
		var widget = $("#" + id);
		var widgetId = widget.data("widgetId");
		var nWidgets = $(".dashboard-content").data("n");
		var widgetName = $("#" + id + " .widget_title").html();

		$.post(applicationRoot + "removeWidget", {widgetId: widgetId}, 
				function(response) {
					if (response)
						widget.remove();
					// decrement the number of widgets on the page.
					$(".dashboard-content").data("n", --nWidgets);
						console.warn("removed widget: " + id);
						
						//TODO: Make the JSON response the title and content so we can use string properties.
						Modal.alert({
							"title" : "Remove Widget",
							"content": widgetName + " has been removed!"
						});
				});

	}

}


/**
 * Updates the database of the new position of the widgets.
 * Checks all widgets to see if its index() is different than
 * its original position.
 */
function updateWidgetPosition() {
	
	var result = [];
	var widgets = $(".w_container");
	
	// look for the widgets that have changed positions
	$.each(widgets, function() {
		var $widget = $(this);
		var idx = $widget.index();
		if (idx == parseInt($widget.data("pos")))
			return;
		
		var obj = {"widgetId": $widget.data("widgetId"), "pos": idx};
		result.push(obj);
		$widget.data("pos", idx);
	});
	
	if(result.length > 0) {
		$.post(applicationRoot + "updateWidgetPosition", {"widgets": JSON.stringify(result) },
				function() {
		});
	}
}


/**
 * Next and Previous slide controls used on widgets with multiple views.
 * @author - Andrew Riley
 * @param id - widget element Id (the id used to distinguish widgets on the pages)
 * @param viewClass - uniformed class name given to each view of the widget.
 */
function nextPreviousControls(id, viewClass) {
	
	var $parent = $("#" + id + " ." + viewClass).parent();
	
	// previous
	$("#" + id + " .w-prev").click(function(e) {
		var $prev = $parent.children(".active").prev();
		$parent.children(".active").removeClass("active").hide("slide", {direction: "right"}, "fast", function() {
			($prev.length) ? $prev.addClass("active").show("slide", {direction: "left" }, "fast")
					       : $parent.children("." + viewClass + ":last").show("slide", {direction: "left"}, "fast")
					       										     .addClass("active");
		});
		
	});
	
	// next
	$("#" + id + " .w-next").click(function(e) {
		var $next = $parent.children(".active").next();
		$parent.children(".active").removeClass("active").hide("slide", {direction: "left"}, "fast", function() {
			($next.length) ? $next.addClass("active").show("slide", {direction: "right"}, "fast")
					       : $parent.children("." + viewClass + ":first").show("slide", {direction: "right"}, "fast")
					       										     .addClass("active");
		});
	});	
	
}

/**
 * Direct view button event.
 * @author - Andrew Riley
 * @param id - widget element Id
 * @param view - Id of the view on the widget
 */
function changeViewBtn(id, view) {
	
	var $parent = $("#" + id + " #" + view).parent();
	
	$parent.children(".active").removeClass("active").hide();
	$("#" + id + " #" + view).show().addClass("active");
}


