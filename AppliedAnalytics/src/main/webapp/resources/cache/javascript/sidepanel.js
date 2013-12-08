/**
 * Sidepanel.js
 */



$(document).ready(function() {
	
	/* Navigation hover effect */
	$('.nav-cell').hover(function(){
		$(this).animate({ backgroundColor: 'gray' }, 100, 'swing');
	}, function() {
		$(this).animate({ backgroundColor: 'transparent' }, 0, 'swing');
	});
	
	
	$('#dashboard').click(function() {
		$('#dashlist').slideToggle();
	});
	
	
});