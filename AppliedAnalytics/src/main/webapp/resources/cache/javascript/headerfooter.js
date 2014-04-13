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
	
	$("#start-date").tooltip({
		open: "load",
		close: {element: $(".hasDatepicker"), event: "click"},
		gravity: "nw",
		domReady: true,
		content: "Choose a date range in which to view data."
	});
	
	
	$("#select-profile").tooltip({
		open: "load",
		close: {element: $("#select-profile"), event: "click"},
		gravity: "n",
		domReady: true,
		content: "Choose the account you want to view data for."
	});
	
	
});


function updateDates() {
	$.post( "filter", { startDate: $("#start-date").val(), endDate: $("#end-date").val() }, function( data, status, xhr ) {
		if (typeof dashboardId != 'undefined') {
			loadDashboard(dashboardId);
		}
	});
}