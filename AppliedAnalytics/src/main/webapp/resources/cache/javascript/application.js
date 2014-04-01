/**
 * application.js
 */

var transitionOptions = { ease: 'swing', fadeOut: 100, floatIn: 500, offsetLeft: "20px", offsetRight: "20px" };

/**
 * This sets the application root directory to be used in any other javascript files.
 */
if (typeof applicationRoot == 'undefined' || !applicationRoot) {
	applicationRoot = "/appliedanalytics/";
	if (console)
		console.warn("Application root was not defined. Currently set as " + applicationRoot);
}

$(function() {
	
	// $header is obtained through header.js
	var $footer = $('#footer');
	var $sidepanel = { bg: $('.sidepanel-background'), content: $('.sidepanel-content') };

	displaySidePanel();
	/* Settings Event Handlers */
	$('.profile-image').click(function() {
		$('.right-pane')
			.addClass('settings')
			.on( "change.settings", "#select-account", function() {
				accountId = $('#select-account option:selected').val();
				$('.settings-content #update-button').prop('disabled', true);
				$('#select-property').html('').append('<option value="">Loading...</option>');
				$('#select-profile').html('').append('<option value="">Loading...</option>');
				$.post(applicationRoot + "settings", { account: accountId }, function( data ) {
					  $(".settings").html( data );
				});
			})
			.on( "change.settings", "#select-property", function() {
				propertyId = $('#select-property option:selected').val();
				$('.settings-content #update-button').prop('disabled', true);
				$('#select-profile').html('').append('<option value="">Loading...</option>');
				$.post(applicationRoot + "settings", { property: propertyId }, function( data ) {
					  $(".settings").html( data );
				});
			})
			.on( "change.settings", "#select-profile", function() {
				$('.settings-content #update-button').prop('disabled', true);
				$.post(applicationRoot + "settings", { profile: $('#select-profile option:selected').val() }, function( data ) {
					  $(".settings").html( data );
				});
			})
			.on( "click.settings", "#update-button", function() {
				$.post(applicationRoot + "settings", { update: 1 }, function( data ) {
					  $(".settings").html( data );
					  updateWidgets();
				});
			});

		$.post(applicationRoot + "settings", {}, function( data ) {
			  $(".settings").html( data );
		});
		showSettingsPanel(550);
		
	});
		
});





/* Animates the side panel (if toggled) */
function displaySidePanel() {
	
	if ($('#sidepanel').data('animate')) {
		$('#sidepanel').fadeIn( { duration: 'slow', queue: false } );
		$('#sidepanel').animate( { left: 0 }, {duration: 2000, easing: 'swing', queue: false} );
	} else {
		$('#sidepanel').css('left', 0);
		$('#sidepanel').show();
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


function showSettingsPanel(width) {        
        if ($(".settings").length) {
        	$(".settings")
        		.show()
        		.animate({width: width}, 500, 'swing', function() {
                        $('.settings-content').show();                        
                        $(window).on('click.settings', function() {
                                hideSettingsPanel();
                        });                                       
                }).click(function(e){ e.stopPropagation(); });
        }
}

function hideSettingsPanel() {
		$(".settings").animate({width: 0}, 500, 'swing', function() {
				$('.right-pane')
					.removeClass('settings')
					.hide()
				    .off('click.settings')
				    .off('change.settings');
                $(window).off('click.settings');
                $('.settings-content').hide();
        });
}

// Creates a new page layout with a wrapper class and a content div.
// Used to differentiate between dashboard and other application pages.
//
// Returns the jQuery object of the content div.
function newPage(pageclass, pageId) {
	var $wrapper = $("#application-page").attr("class", pageclass)
										 .empty();
	return $('<div>').addClass(pageclass + '-content').data("id", pageId).appendTo($wrapper);
}
