/**
 * boost-performance.js
 */

(function($) {
	$(document).ready(function() {
		var divs = $('.wrapper-dropdown').hide();
		var source = $('.content-dropdown').click(function() {
			
		})
	});
});




var clicked = false;
$('.wrapper-dropdown').click(function(e){
	e.preventDefault();
$(this).parent().next().find('.wrapper-dropdown').slideToggle();
	return false;
});

$(document).ready(function() {
	if(!clicked) {
		$('.wrapper-dropdown').hide();
		clicked = true;
	}
});

$('.dropID').click(function(event) {
	$(this).toggleClass('active');
	event.stopPropagation();
});


$(function() {
	// Temporary
	GetBoostPerformanceWidget();
	
});



function GetBoostPerformanceWidget() {
	
	var $element = $('#boostPerformance');
	$.post(applicationRoot + "/BoostPerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'boostPerformance', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .append(response);
			}
			
			$('.boostPerformance .widget_title').click(function() {
				$('.boostPerformance .widget-content').slideToggle('fast');
			});
	});	
	
	
	
}



