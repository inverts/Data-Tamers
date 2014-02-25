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
 * calls each widgets model and view and appends
 * the widget to the dashboard.
 * 
 * @param widgetIdArray - array of widgetIds
 * @returns
 */
function loadWidgets(widgetIdArray) {
	
	for(var i = 0; i < widgetIdArray.length; i++) {
		var $div = $('<div>').addClass('w_container')
							 .prop('draggable', true)
							 .appendTo($('.dashboard-content'));
		
		var widgetId = widgetIdArray[i];
		
		switch(widgetId)
		{
			case 1: 
				$div.attr('id', 'dataForecast' + i).data('widgetId', widgetId);
				loadDataForecast('dataForecast' + i);
				break;
				
			case 2:
				$div.attr('id', 'websitePerformance' + i).data('widgetId', widgetId);
				loadWebsitePerformance('websitePerformance' + i);
				break;
				
			case 3:
				$div.attr('id', 'keyContributingFactors' + i).data('widgetId', widgetId);
				loadKeyContributingFactors('keyContributingFactors' + i);
				break;
			
			case 4:
				$div.attr('id', 'keywordInsight' + i).data('widgetId', widgetId);
				loadKeywordInsight('keywordInsight' + i);
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


