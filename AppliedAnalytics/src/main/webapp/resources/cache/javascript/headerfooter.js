/**
 * headerfooter.js
 */

var $header = $('#header');
var $footer = $('#footer');

$(function() {
	  $( "#start-date" ).datepicker();
	  $( "#end-date" ).datepicker();
	  $("#start-date").change(updateDates);
	  $("#end-date").change(updateDates);
});


function updateDates() {
	$.post( "filter", { startDate: $("#start-date").val(), endDate: $("#end-date").val() }, function( data, status, xhr ) {
		if (typeof dashboardId != 'undefined') {
			loadDashboard(dashboardId);
		}
	});
}