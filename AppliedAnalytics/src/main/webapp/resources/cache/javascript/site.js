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

var panelHeight = 0;

$(document).ready(function() {
	
	//What is this, "PHPScript"? :)
	var $header = $('.header');
	var $footer = $('.footer');
	var $sidePanel = $('#sidepanel');
	var $settings = $('.settings');
	panelHeight = $('.wrapper').height();
	/* Do appropriate animations */
	if ($sidePanel.length)
		displaySidePanel($sidePanel, panelHeight, displayContent);
	else
		displayContent();

	$('.profile-image').click(function() {
		showSettingsPanel($settings, panelHeight);
	});
	
});


/* Animates the side panel (if toggled) */
function displaySidePanel($sidePanel, panelHeight, callback) {
	
	if ($sidePanel.length) {
		// set panel height to content + header
		$sidePanel.css('height', panelHeight);
		if ($sidePanel.data('animate')) {
			$sidePanel.animate({width: "310px"}, 500, 'swing', function() {
				$('.sidepanel-content').show();
				if (typeof(callback != 'undefined'))
					callback();
				displayWidgets();
			});
		}
		else {
			$sidePanel.css('width', 310);
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

function showSettingsPanel($settings, panelHeight, callback) {
	
	if ($settings.length) {
		// set panel height to wrapper height
		$settings.css('height', panelHeight).show();
		
		$settings.animate({width: "250px"}, 500, 'swing', function() {
			$('.settings-content').show();
			
			$(window).on('click.settings', function() {
				hideSettingsPanel($settings);
			});
					
			if (callback)
				callback();
			
		}).click(function(e){ e.stopPropagation(); });

	}
}

function hideSettingsPanel($settings) {
	$settings.animate({width: 0}, 500, 'swing', function() {
		$(this).hide();
		$(window).off('click.settings');
	});
	
}

