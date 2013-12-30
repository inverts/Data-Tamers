/**
 * headerfooter.js
 */

var $header = $('#header');
var $footer = $('#footer');

/* Determines what action to take with the header depending on what state its in */
$(function() {
	switch(headerAttributes.state) {
	
	case 'Application':
		$header = $('#header-application').show();
		$('.footer-nav').show();
		break;
	case 'Entry' : 
		$header = $('#header-entry').show();
		break;
	}
});


$(function() {
	  $( "#start-date" ).datepicker();
	  $( "#end-date" ).datepicker();
	  $("#start-date").change(updateDates);
	  $("#end-date").change(updateDates);
});


function updateDates() {
	$.post( "<c:url value="/filter/" />", { startDate: $("#start-date").val(), endDate: $("#end-date").val() }, function( data ) {
			//Maybe we will want to do something with the resulting data later. For now, just update the model.
		});
	updateHypotheticalWidget('hypotheticalWidget');
}