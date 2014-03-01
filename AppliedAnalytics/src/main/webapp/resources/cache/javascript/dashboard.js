/**
 *  dashboard.js
 */


$(function() {
	
	// Dragging Feature
	$(".dashboard-content").sortable({ 
		revert: true, 
		tolerance: "pointer", 
		containment: $(".content")
	});
	
	
});

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
	return w2.priority - w1.priority;
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
		//Create an empty widget div.
		var $div = $('<div>').addClass('w_container')
							 .prop('draggable', true)
							 .appendTo($('.dashboard-content'));
		
		var widgetId = widgets[i].widgetTypeId;
		
		//Label and fill the div accordingly depending on the widget type.
		//TODO: Create widget id to function mappings elsewhere and use them here.
		switch(widgetId)
		{
			case 1: 
				$div.attr('id', 'dataForecastWidget' + i).data('widgetId', widgetId);
				loadDataForecast('dataForecastWidget' + i);
				break;
				
			case 2:
				$div.attr('id', 'websitePerformanceWidget' + i).data('widgetId', widgetId);
				loadWebsitePerformanceWidget('websitePerformanceWidget' + i);
				break;
				
			case 3:
				$div.attr('id', 'keyContributingFactorsWidget' + i).data('widgetId', widgetId);
				loadKeyContributingFactors('keyContributingFactorsWidget' + i);
				break;
			
			case 4:
				$div.attr('id', 'keywordInsightWidget' + i).data('widgetId', widgetId);
				loadKeywordInsight('keywordInsightWidget' + i);
				break;
				
			case 5: 
				$div.attr('id', 'growingProblemsWidget' + i).data('widgetId', widgetId);
				loadGrowingProblemsWidget('growingProblemsWidget' + i);
				break;
				
			case 6: 
				$div.attr('id', 'boostPerformanceWidget' + i).data('widgetId', widgetId);
				loadBoostPerformanceWidget('boostPerformanceWidget' + i);
				break;
				
			case 7: 
				$div.attr('id', 'revenueSourcesWidget' + i).data('widgetId', widgetId);
				loadRevenueSourcesWidget('revenueSourcesWidget' + i);
				break;
				
		}
	}
	
	// widget will not be dragged while user clicks on content
	$('.dashboard-content').sortable({ cancel: '.widget-content'});
}

/**
 * Updates each widget's visualization based on new data 
 * without reloading the entire widget.
 */
function updateWidgets(){
	
	var widgets = $.map($('.w_container'), function(widget) {
						return { 'id': widget.id, 'widgetId' : $(widget).data('widgetId') };
					});
	
	for(var i = 0; i < widgets.length; i++) {
		
		switch(widgets[i].widgetId)
		{
			case 1:
				updateDataForecast(widgets[i].id);
				break;
			
		}
	}
		
	
	
	
}


