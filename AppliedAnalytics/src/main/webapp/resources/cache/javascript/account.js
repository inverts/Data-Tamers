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
});