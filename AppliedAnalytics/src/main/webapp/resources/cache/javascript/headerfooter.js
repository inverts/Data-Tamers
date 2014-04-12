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
	
	
	$(".filter").on( "change.filter", "#select-profile", function() {
		updateSettings({ profile: $('#select-profile option:selected').val() });
		updateSettings({ update: 1 }, updateWidgets);
	});
	
	var $tutorial = $(".tutorialToggle");
	
	$tutorial.tooltip({
		gravity: "ne",
		open: "load",
		close: { 
			element: $tutorial,
			event: "click", 
			callback: function() { $("video").remove(); $tutorial.hide(); } 
		},
		content: "Click here to close the tutorial or drag a widget onto the dashboard."
	});
	
});


function updateDates() {
	$.post( "filter", { startDate: $("#start-date").val(), endDate: $("#end-date").val() }, function( data, status, xhr ) {
		if (typeof dashboardId != 'undefined') {
			loadDashboard(dashboardId);
		}
	});
}