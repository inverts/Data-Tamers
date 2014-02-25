/**
 * application.js
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

	displaySidePanel();
	/* Settings Event Handlers */
		$('.profile-image').click(function() {
			$('.right-pane')
				.addClass('settings')
				.on( "change", "#select-account", function() {
					$.post(applicationRoot + "settings", { account: $('#select-account option:selected').val() }, function( data ) {
						  $(".settings").html( data );
					});
				})
				.on( "change", "#select-property", function() {
					$.post(applicationRoot + "settings", { property: $('#select-property option:selected').val() }, function( data ) {
						  $(".settings").html( data );
					});
				})
				.on( "change", "#select-profile", function() {
					$.post(applicationRoot + "settings", { profile: $('#select-profile option:selected').val() }, function( data ) {
						  $(".settings").html( data );
					});
				})
				.on( "click", "#update-button", function() {
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

		/*$( ".settings" ).on( "change", "#select-account", function() {
			$.post(applicationRoot + "settings", { account: $('#select-account option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
			});
		$( ".settings" ).on( "change", "#select-property", function() {
			$.post(applicationRoot + "settings", { account: $('#select-property option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
			});
		$( ".settings" ).on( "change", "#select-profile", function() {
			$.post(applicationRoot + "settings", { account: $('#select-profile option:selected').val() }, function( data ) {
				  $(".settings").html( data );
				});
			});
		
		//TODO: we need to find a better way of updating widgets rather than
		// calling the update function of each and every widget.
		
		// The solution is to have event listeners on the widgets.
		$( "#update-button" ).on( "click", "#update-button", function() {
			$.post(applicationRoot + "settings", { update: 1 }, function( data ) {
				  $(".settings").html( data );
					updateHypotheticalWidget('hypotheticalWidget');
				});
			});*/
		
	
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
					.hide();
                $(window).off('click.settings');
                $('.settings-content').hide();
        });
}