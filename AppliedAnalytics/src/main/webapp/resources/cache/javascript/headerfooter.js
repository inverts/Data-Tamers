/**
 * headerfooter.js
 */

var $header = $('#header');
var $footer = $('#footer');

$(function() {
	if ($("loglabel").length)
		$('#loglabel').dropdown();
	
	if ($( "#start-date" ).length)
		$( "#start-date" ).datepicker();
	if ($( "#end-date" ).length)
		$( "#end-date" ).datepicker();
	if ($("#start-date").length)
		$("#start-date").change(updateDates);
	if ($("#end-date").length)
		$("#end-date").change(updateDates);
});


function updateDates() {
	$.post( "filter", { startDate: $("#start-date").val(), endDate: $("#end-date").val() }, function( data, status, xhr ) {
		if (typeof dashboardId != 'undefined') {
			loadDashboard(dashboardId);
		}
	});
}