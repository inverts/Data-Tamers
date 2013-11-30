/**
 * Hypothetical Future Widget JS
 */

$(document).ready(function() {
	
	$.get("widget", null, function(response) {
		$('#testWidget').append(response);
	});
	
});