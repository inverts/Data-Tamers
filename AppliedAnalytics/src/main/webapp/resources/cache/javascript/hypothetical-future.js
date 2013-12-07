/**
 * Hypothetical Future Widget JS
 */

$(document).ready(function() {
	
	GetWidget('testWidget');

});


function GetWidget(id, source, change) {
	
	var $element = $('#' + id);
	
	$.post("HypotheticalFuture", { source: source, change: change }, 
		function(response) {
			$element.empty().append(response);
			
			// Setup change event
			$('select').on('change', function() {
				GetWidget(id, $('#traffic_source').val(), $('#change_pct').val());
			});
	});
	
}