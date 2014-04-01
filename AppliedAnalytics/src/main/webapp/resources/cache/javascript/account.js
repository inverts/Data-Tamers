/**
 * account.js
 */

$(function() {
	/* Enables continue button when terms are checked. */
	$('#terms-check').change(function(e) {
		e.stopPropagation();
		$(this).prop('checked') ? $('#terms-submit').prop('disabled', false)
								: $('#terms-submit').prop('disabled', true);
	});
	
	$('#terms-cancel').on('click', function(){ window.location.href = applicationRoot; });
	$('#account-submit.disabled').on('click', function(){ alert("A google account must be attached!");});

	if ($("div.start").length) {
		$(".right-pane").addClass("newAccount");
		showNewAccountForm(800);
	}
});

/**
 * @author - Andrew Riley
 * @param formId
 */
function invokeGoogleAuthentication(formId) {
	var invokeFlag = $('<input>')
					.attr('type', 'hidden')
					.attr('name', 'googleAuth').val('1');
	
	$('#' + formId).append(invokeFlag).submit();
	
}


function showNewAccountForm(width) {        
    if ($(".newAccount").length) {
    	$(".newAccount")
    		.show()
    		.animate({width: width}, 500, 'swing', function() {
    			
    			$.post(applicationRoot + "accounts/accountform", null, function(response) {
    				 $('.newAccount').html(response);
    			});
                                                        
            }).click(function(e){ e.stopPropagation(); });
    }
}

/*function hideSettingsPanel() {
	$(".account").animate({width: 0}, 500, 'swing', function() {
			$('.right-pane')
				.removeClass('account')
				.hide();
            $(window).off('click.account');
            $('.account-content').hide();
    });
}*/