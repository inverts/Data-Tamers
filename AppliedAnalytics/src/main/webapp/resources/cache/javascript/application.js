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
				$('.settings-content #update-button').prop('disabled', true);
				$('.settings #select-property').html('').append('<option value="">Loading...</option>');
				$('.settings #select-profile').html('').append('<option value="">Loading...</option>');
				
				updateSettings({ account: $('.settings #select-account option:selected').val() });
			})
			
			.on( "change.settings", "#select-property", function() {
				$('.settings-content #update-button').prop('disabled', true);
				$('#select-profile').html('').append('<option value="">Loading...</option>');
				
				updateSettings({ property: $('.settings #select-property option:selected').val() });
			})
			
			.on( "change.settings", "#select-profile", function() {
				$('.settings-content #update-button').prop('disabled', true);
				
				updateSettings({ profile: $('.settings #select-profile option:selected').val() });
			})
			
			.on( "click.settings", "#update-button", function() {
				updateSettings({ update: 1 }, function() {
					updateWidgets();
					//TODO: This is pretty kludge, I need to reorganize a bunch of things to make this better.
					$(".filter #activeProfileInfo #select-profile option:selected").removeAttr("selected");
					$(".filter #activeProfileInfo #select-profile option[value=" + $('.settings #select-profile option:selected').val() + "]").attr("selected", true);
				});
			});

		
		updateSettings();
		
		showSettingsPanel(550);
		
	});
		
});

function updateSettings(data, callback) {
	successFn = handleAjaxSuccess;
	if (typeof(callback) != 'undefined') {
		successFn = function(data, status, xhr) {
			handleAjaxSuccess(data, status, xhr);
			callback();
		}
	}

	if (typeof(data) == 'undefined') 
		data = null;
	
	$.ajax({
		type: 'GET',
		url: applicationRoot + "settings",
		data: data,
		success: successFn,
		error: handleAjaxError
	});
	
}

function handleAjaxSuccess(data, status, xhr) {
	if (status != "success")
		console.warn("There was a problem during the AJAX request.");
	
	$(".settings").html( data );
}

function handleAjaxError(xhr, status, errorName) {
	console.log("AJAX Error - " + xhr.status + ": " + errorName);

	if (xhr.status == 401) {
		Modal.call({
			"title": "Looks like you've logged out!",
			"content": "Sorry, but it appears you have logged out or your session has expired. Please log in again.",
			"action": function(e) {  e.preventDefault(); document.location.href = applicationRoot + "login"; }
		});
	} else {
		Modal.alert({
			"title": "An error occurred!",
			"content": "Sorry, we could not complete that action at this time.",
		});
	}
}



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
