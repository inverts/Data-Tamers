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
});

function invokeGoogleAuthentication(formId) {
	var invokeFlag = $('<input>')
					.attr('type', 'hidden')
					.attr('name', 'googleAuth').val('1');
	
	$('#' + formId).append(invokeFlag).submit();
	
}