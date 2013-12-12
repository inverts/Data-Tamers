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
	
});

$(function() {
	  $( "#start-date" ).datepicker();
	  $( "#end-date" ).datepicker();
});