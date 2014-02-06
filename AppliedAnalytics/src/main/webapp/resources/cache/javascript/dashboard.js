/**
 *  dashboard.js
 */


$(function() {
	
	$('.dashboard-content').sortable({ 
		revert: true, 
		tolerance: 'pointer', 
		containment: $('.content')
	});

});


function getWidget(widgetId, data) {
	
	switch(widgetId)
	{
		case 1: return getHypotheticalFuture();
		case 2: return GetWebsitePerformanceWidget();
		default: return null;
	}
	
}
