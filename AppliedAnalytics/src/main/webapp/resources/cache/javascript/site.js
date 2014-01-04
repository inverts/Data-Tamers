/**
 * site.js
 */

var transitionOptions = { ease: 'swing', fadeOut: 100, floatIn: 500, offsetLeft: "20px", offsetRight: "20px" };

if (typeof applicationRoot == 'undefined' || !applicationRoot) {
	applicationRoot = "/appliedanalytics/";
	if (console)
		console.warn("Application root was not defined. Currently set as " + applicationRoot);
}

$(document).ready(function() {
	
	// $header is obtained through header.js
	var $footer = $('#footer');
	var $sidepanel = { bg: $('.sidepanel-background'), content: $('.sidepanel-content') };
	var $settings = { bg: $('.settings-background'), outer: $('.settings-outer') };
	
	/* Correct content margins based off dynamic headers */
	$('.wrapper').css('margin-top', -$header.height());
	$('.content').css('margin-top', $header.height()); // margin-top should match the height of the header
	
	
	/* Sidepanel */
	if ($sidepanel.bg.length) {
		$('.content').css('margin-left', 340); // if sidepanel is present we need a margin on the content!
		$sidepanel.bg.css('bottom', $footer.height()); // ensure bottom matches height of the footer
		displaySidePanel($sidepanel, displayContent);
	}
	else
		displayContent();

	/* Settings */
		$('.profile-image').click(function() {
			showSettingsPanel(550);
		});

		$('#select-account').change( function () {
			$.post(applicationRoot + "settings", { account: $('#select-account option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
		});
		$('#select-property').change( function () {
			$.post(applicationRoot + "settings", { property: $('#select-property option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
		});
		$('#select-profile').change( function () {
			$.post(applicationRoot + "settings", { profile: $('#select-profile option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
		});
		
		//TODO: we need to find a better way of updating widgets rather than
		// calling the update function of each and every widget.
		
		// The solution is to have event listeners on the widgets.
		$('#update-button').click( function () {
			$.post(applicationRoot + "settings", { update: 1 }, function( data ) {
				  $("#settings-update").html( data );
					updateHypotheticalWidget('hypotheticalWidget');
				});
	
		});
	
});


/* Animates the side panel (if toggled) */
function displaySidePanel($sidepanel, callback) {
	
	if ($sidepanel.bg.data('animate')) {
		
		$sidepanel.bg.animate({ left: 0 }, 1000, 'swing', function() {
			$('#sidepanel').fadeIn('slow');
			callback();
		});
		
	}
	else {
		$sidepanel.bg.css('left', 0);
		$('#sidepanel').show();
		callback();
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
		marginLeft: 0,
		opacity: 1
	}, transitionOptions.floatIn, transitionOptions.ease);
	
}



function displayContent() {
	$('.content').css('visibility', 'visible');
}
/*
function showSettingsPanel(width) {
	$('.settings-background').show().animate({
		right: 0
	}, 300, 'swing');

	$('.settings-outer').show().animate({
		left: width
	}, 300, 'swing', 
		function() {
			$(window).on('click.settings', function() {
				hideSettingsPanel(width);
			});
		
	}).click(function(e){ e.stopPropagation(); });
}


function hideSettingsPanel(width) {
	$('.settings-background').animate({
		right: width
	}, 300, 'swing', 
		function() { $(this).hide(); });
	
	$('.settings-outer').animate({
		left: 0
	}, 300, 'swing', 
		function() {
			$(this).hide();
			$(window).off('click.settings');
			$("#settings-update").empty();
	});
}

*/
function showSettingsPanel(width) {        
        if ($(".settings").length) {
        	$(".settings").show();                
        	$(".settings").animate({width: width}, 500, 'swing', function() {
                        $('.settings-content').show();                        
                        $(window).on('click.settings', function() {
                                hideSettingsPanel();
                        });                                       
                }).click(function(e){ e.stopPropagation(); });
        }
}

function hideSettingsPanel() {
		$(".settings").animate({width: 0}, 500, 'swing', function() {
                $(window).off('click.settings');
        });
}

