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
				
				var dashboard = $.parseJSON(result);
				var widgetIdArray = dashboard.widgetIds;
				var widgetTypeIdArray = dashboard.widgetTypeIds;
				var widgets = dashboard.widgets;
				widgets = widgets.sort(compareWidgetPriority);
				$('.dashboard-content').html("");
				loadWidgets(widgets);
				
				$(".dashboard-content").sortable({ 
					revert: true, 
					tolerance: "pointer", 
					zIndex: 100,
					stop: updateWidgetPosition
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

/**
 * calls each widgets model and view and appends
 * the widget to the dashboard.
 * 
 * @param widgetIdArray - array of widgetIds
 * @returns
 */
//TODO: Accommodate widget specifics (like filter).
function loadWidgets(widgets) {
	
	for(var i = 0; i < widgets.length; i++) {

		var widgetTypeId = widgets[i].widgetTypeId;
		var widgetId = widgets[i].id;
		
		// after widgets load, set widget events
		loadWidget(widgetTypeId, widgetId, i);
		
	}
	
	// store number of widgets loaded
	// widget will not be dragged while user clicks on content
	$('.dashboard-content').data('n', widgets.length);

}


function loadWidget(widgetTypeId, widgetId, i)
{
	var $dashboard = $('.dashboard-content')
	//Create an empty widget div.
	var $div = $('<div>').addClass('w_container')
						 //.prop('draggable', true)
						 .data({
							 	'widgetTypeId': widgetTypeId, 
							 	'widgetId': widgetId 
							   })
						 .appendTo($dashboard);
	
	$div.data('pos', $div.index());
	
	var elementId;
	
	switch(widgetTypeId)
	{
		case 1: 
			elementId = 'dataForecastWidget' + i;
			$div.attr('id', elementId);
			loadDataForecast(elementId);
			break;
			
		case 2:
			elementId = 'websitePerformanceWidget' + i;
			$div.attr('id', elementId);
			loadWebsitePerformanceWidget('websitePerformanceWidget' + i);
			break;
			
		case 7:
			elementId = 'keyContributingFactorsWidget' + i;
			$div.attr('id', elementId);
			loadKeyContributingFactorsWidget(elementId);
			break;
		
		case 4:
			elementId = 'keywordInsightWidget' + i;
			$div.attr('id', elementId);
			loadKeywordInsight(elementId);
			break;
			
		case 5:
			elementId = 'growingProblemsWidget' + i;
			$div.attr('id', elementId);
			loadGrowingProblemsWidget(elementId);
			break;
			
		case 6: 
			elementId = 'boostPerformanceWidget' + i;
			$div.attr('id', elementId);
			loadBoostPerformanceWidget(elementId);
			break;
		
	}
	
	/*** GENERAL WIDGET EVENTS ***/

	$div.on('dblclick', '.widget_title', function() { // add collapse event to widget
			$(this).parent().siblings('.widget-content').slideToggle('fast');
		}) 
		.on('click', ' .dropdown-menu a.removeWidget', function() { // remove menu event
			Modal.call({
				'title' : 'Remove Widget',
				'content': 'Remove ' + $('#' + elementId + ' .widget_title').html() + ' widget?',
				'action': function() { removeWidget(elementId); }
			});
		});
	
}




/**
 * Updates each widget's visualization based on new data 
 * without reloading the entire widget.
 */
function updateWidgets(){
	
	var widgets = $.map($('.w_container'), function(widget) {
						return { 'elementId': widget.id, 'widgetTypeId' : $(widget).data('widgetTypeId') };
					});
	
	for(var i = 0; i < widgets.length; i++) {
		
		switch(widgets[i].widgetTypeId)
		{
			case 1:
				updateDataForecast(widgets[i].elementId);
				break;
			
		}
	}
		
}


function addWidget(element, dashboardId) 
{
	if ($(element).length) {
		var id = $(element).closest('ul').attr('id');
		var widget = $('#' + id);
		var widgetTypeId = widget.data('widgetTypeId');
		var nWidgets = $('.dashboard-content').data('n');

		$.post(applicationRoot + "addWidget", {widgetTypeId: widgetTypeId, dashboardId: dashboardId},
				function(response) {
					var result = $.parseJSON(response);
					loadWidget(widgetTypeId, result.id, nWidgets++);
					
					// update the number of widgets
					$('.dashbaord-content').data('n', nWidgets);
				});

	}

}



/**
 * Removes a widget from the dashboard and deletes it from the database.
 * @param element - the element of the menu item clicked.
 */
function removeWidget(id) {

	if (id) {
		var widget = $('#' + id);
		var widgetId = widget.data('widgetId');
		var nWidgets = $('.dashboard-content').data('n');
		var widgetName = $('#' + id + ' .widget_title').html();

		$.post(applicationRoot + "removeWidget", {widgetId: widgetId}, 
				function(response) {
					if (response)
						widget.remove();
					// decrement the number of widgets on the page.
					$('.dashboard-content').data('n', --nWidgets);
						console.warn('removed widget: ' + id);
						
						//TODO: Make the JSON response the title and content so we can use string properties.
						Modal.alert({
							'title' : 'Remove Widget',
							'content': widgetName + ' has been removed!'
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
	var widgets = $('.w_container');
	
	// look for the widgets that have changed positions
	$.each(widgets, function() {
		var $widget = $(this);
		var idx = $widget.index();
		if (idx == parseInt($widget.data('pos')))
			return;
		
		var obj = {'widgetId': $widget.data('widgetId'), 'pos': idx};
		result.push(obj);
		$widget.data('pos', idx);
	});
	
	if(result.length > 0) {
		$.post(applicationRoot + "updateWidgetPosition", {"widgets": JSON.stringify(result) },
				function() {
		});
	}
}


