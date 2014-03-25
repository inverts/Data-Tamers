/**
 * traffic-source-trends.js
 * 
 * TODO: Widgets should be loaded by their actual id in the database in the event
 * that we have widget-specific saved properties that are displayed in the future. 
 */

/**
 * @param id the html DOM element id to place this data into.
 */
function loadTrafficSourceTrendsWidget(id) {

	var $element = $('#' + id);
	$.get(applicationRoot + "/widgets/traffic-source-trends", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'traffic-source-trends', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .html(response);
			}
			
	});	
}


function getTrafficSourceTrendsData(id) {
	
	$.get(applicationRoot + "/widgets/traffic-source-trends/data", null, 
			function(response) {
				//TODO: Place data in widget.
		});	
	
}
