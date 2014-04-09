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
		
		//Create an empty widget div.
		var $div = $("<div>").addClass("w_container")
							 .data({
								 	"widgetTypeId": widgetTypeId, 
								 	"widgetId": widgetId 
								   })
							 .appendTo($content);
		
		// load in each widget and fire the callback
		loadWidget($div, widgetTypeId, i, callback);
		
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
function loadWidget($div, widgetTypeId, i, callback)
{

	$div.data("pos", $div.index());
	
	
	var elementId, n;
	
	// which widget's load function do we need to call?
	switch(widgetTypeId)
	{
		case 1:
			n = i || $(".dataForecast").length;
			elementId = "dataForecastWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);			
			loadDataForecast(elementId, function() { widgetEvents($div, elementId); });
			break;
			
		case 2:
			n = i || $(".pagePerformance").length;
			elementId = "websitePerformanceWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);
			loadWebsitePerformanceWidget(elementId, function() { widgetEvents($div, elementId, "pagePerformanceVisual"); });
			break;
			
		case 7:
			n = i || $(".keyContributingFactors").length;
			elementId = "keyContributingFactorsWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);
			loadKeyContributingFactorsWidget(elementId, function() { widgetEvents($div, elementId); });
			break;
		
		case 4:
			n = i || $(".keywordInsight").length;
			elementId = "keywordInsightWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);
			loadKeywordInsight(elementId, function() { widgetEvents($div, elementId, "keywordVisual"); });
			break;
			
		case 5:
			n = i || $(".growingProblems").length;
			elementId = "trafficSourceTrendsWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);
			loadTrafficSourceTrendsWidget(elementId, function() { widgetEvents($div, elementId, "trafficSourceVisual"); });
			break;
			
		case 6:
			n = i || $(".boostPerformance").length;
			elementId = "boostPerformanceWidget";
			while($("#" + elementId + n).length) { n++; } // need to ensure that the number is not already used for this widget
			elementId += n;
			$div.attr("id", elementId);
			loadBoostPerformanceWidget(elementId, function() { widgetEvents($div, elementId); });
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
function widgetEvents($div, elementId, viewClass) {
	
	var $content = $div.parent();
	
	if ($content.hasClass("widget-select"))
		$content.children("img").remove();
	
	// collapse event on title double click
	$div.on("dblclick.widget", ".widget_title", function(e) {
		var selector = e && e.data || this;
		$(selector).parent().siblings(".widget-content").slideToggle("fast");
	});

	// display trash bin for removal on click and hold
	/*if ($content.hasClass("dashboard-content") || $content.hasClass("widget-select")) {
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
	}*/
	
	// Next and Previous view controls
	if (viewClass) {

		// previous
		$div.on("click",  ".w-prev", function(e) {
			var $parent = $("#" + elementId + " ." + viewClass).parent();
			var $prev = $parent.children(".active").prev();
			$parent.children(".active").removeClass("active").hide("slide", {direction: "right"}, "fast", function() {
				($prev.length) ? $prev.addClass("active").show("slide", {direction: "left" }, "fast")
						       : $parent.children("." + viewClass + ":last").show("slide", {direction: "left"}, "fast")
						       										     .addClass("active");
			});
			
		});
		
		// next
		$div.on("click", ".w-next", function(e) {
			var $parent = $("#" + elementId + " ." + viewClass).parent();
			var $next = $parent.children(".active").next();
			$parent.children(".active").removeClass("active").hide("slide", {direction: "left"}, "fast", function() {
				($next.length) ? $next.addClass("active").show("slide", {direction: "right"}, "fast")
						       : $parent.children("." + viewClass + ":first").show("slide", {direction: "right"}, "fast")
						       										     .addClass("active");
			});
		});	
	}
	
}


/**
 * Updates each widget"s visualization based on new data 
 * without reloading the entire widget.
 * @author - Andrew Riley
 */
function updateWidgets() {
	
	// get all the active widgets on the current page.
	var widgets = $.map($(".w_container"), function(widget) {
						return { "elementId": widget.id, "widgetTypeId" : $(widget).data("widgetTypeId") };
					});
	
	for(var i = 0; i < widgets.length; i++) {
		updateWidget(widgets[i].widgetTypeId, widgets[i].elementId);
	}
}

/**
 * Updates the specified widget.
 * @author - Andrew Riley
 * @param widgetTypeId - TypeId of the widget
 * @param elementId - Id of the widget container
 */
function updateWidget(widgetTypeId, elementId) {
	
	// call appropriate widget's update function.
	switch(widgetTypeId)
	{
		case 1:
			updateDataForecast(elementId);
			break;
		case 2:
			updatePagePerformance(elementId);
			break;
		case 4:
			updateKeywordInsight(elementId);
			break;
		case 5:
			updateTrafficSourceTrends(elementId);
			break;
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

	$.ajax({
		type: 'POST',
		url: applicationRoot + "addWidget",
		data: {widgetTypeId: widgetTypeId, dashboardId: $dash.data("id"), priority: $widget.index()},
		success: function(response) {
			var result = $.parseJSON(response);
			//TODO: Needs localized strings from response.
			
			// add widget data to the widget element
			if (result) {
				$widget.data({
					"widgetId": result.widgetId,
					"widgetTypeId": widgetTypeId
				});
				
				$dash.data("n", ++nWidgets); //increment number of widgets on page.
				
				console.log("added widget: " + $widget.attr("id"));
			}
			else {
				Modal.alert({
					"title": "Add Widget",
					"content": "There was a problem trying to add " + $widget.children(".widget_title").html() + 
							   ". This widget will now be removed."
				});
				
				$widget.remove(); // if we cannot save the widget to the database, remove it off the page.
			}
			
		},
		error: handleAjaxError
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

		$.ajax({
			type: 'POST',
			url: applicationRoot + "addWidget",
			data: {widgetTypeId: widgetTypeId, dashboardId: li.attr("id")},
			success: function(response) {
				var result = $.parseJSON(response);
				Modal.alert({
					"title" : "Add Widget",
					"content": widgetName + " has been added to dashboard \"" + li.children("a").html()  + "\"!"
				});
			},
			error: handleAjaxError
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

		$.ajax({
			type: 'POST',
			url: applicationRoot + "removeWidget",
			data: {widgetId: widgetId},
			success: function(response, status, xhr) {
				if (status != "success")
					console.warn("There was a problem during the AJAX request.");
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
			},
			error: handleAjaxError
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
	
	// Look for the widgets that have changed positions
	$.each(widgets, function() {
		var $widget = $(this);
		var idx = $widget.index();
		if (idx == parseInt($widget.data("pos")))
			return;
		
		var obj = {"widgetId": $widget.data("widgetId"), "pos": idx};
		result.push(obj);
		$widget.data("pos", idx);
	});
	
	// If widgets have changed, save the changes.
	if(result.length > 0) {
		$.ajax({
			type: 'POST',
			url: applicationRoot + "updateWidgetPosition",
			data: {"widgets": JSON.stringify(result) },
			success: function() {
				console.log("Widget position updated successfully.")
			},
			error: handleAjaxError
		});
	}
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


