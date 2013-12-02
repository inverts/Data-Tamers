/**
 * Hypothetical Future Widget JS
 */

$(document).ready(function() {
	
	$.get("HypotheticalFuture", null, function(response) {
		$('#testWidget').append(response);
	});
	
});