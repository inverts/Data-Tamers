/**
 * boost-performance.js
 */


$(function() {
	// Temporary
	GetBoostPerformanceWidget();
});



function GetBoostPerformanceWidget() {
	
	var $element = $('#boostPerformance');
	$.post(applicationRoot + "/BoostPerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'boostPerformance', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .append(response);
			}
			
			$('.boostPerformance .widget_title').click(function() {
				$('.boostPerformance .widget-content').slideToggle('fast');
			});
	});	
	
	
	
}



