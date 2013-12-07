/**
 * site.js
 */

var transitionOptions = {
		ease: 'swing',
		fadeOut: 100,
		floatIn: 500,
		offsetLeft: "20px",
		offsetRight: "20px"
};

$(document).ready(function() {
	
	var $header = $('.header');
	var $footer = $('.footer');
	var $sidePanel = $('#sidepanel');
	
	displayContent();
	
	/* Do appropriate animations */
	if ($sidePanel.length)
		displaySidePanel($sidePanel, displayContent);
	else
		displayContent();
	
	
});


/* Animates the side panel (if toggled) */
function displaySidePanel($sidePanel, callback) {
	
	if ($sidePanel.length) {
		// set panel height to content + header
		$sidePanel.css('height', $('.wrapper').height());
		if ($sidePanel.data('animate')) {
			$sidePanel.animate({width: "400px"}, 500, 'swing', function() {
				$('.sidepanel-content').show();
				callback();
				displayWidgets();
			});
		}
		else {
			$sidePanel.css('width', 400);
			$('.sidepanel-content').show();
			callback();
		}
	}
}

/* Displays the widgets */
function displayWidgets() {
	var $views = $('.widgetView');
	
	$views.css({
		marginLeft: transitionOptions.offsetLeft,
		marginRight: transitionOptions.offsetRight,
		opacity: 0,
		display: 'block',
		visibility: 'visible'
	}).animate({
		marginRight: 0,
		marginLeft: 0,
		opacity: 1
	}, transitionOptions.floatIn, transitionOptions.ease);
	
}



function displayContent() {
	
	$('.content').css('visibility', 'visible');
	
}