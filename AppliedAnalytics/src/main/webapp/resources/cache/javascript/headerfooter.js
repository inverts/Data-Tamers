/**
 * 
 */

var $header = $('#header');
var $footer = $('#footer');


$(document).ready(function() {
	
	switch(headerAttributes.state) {
	
		case 'Application': $('#header-application').show(); break;
		case 'Entry' : $('#header-entry').show(); break;
	
	}


	  /*
	  //Add event handles for updating the dates
	  $("#start-date").change(updateDates);
	  $("#end-date").change(updateDates);
	  */
});
$(function () {
	  $( "#start-date" ).datepicker();
	  $( "#end-date" ).datepicker();
	  $("#start-date").change(updateDates);
	  $("#end-date").change(updateDates);
});
