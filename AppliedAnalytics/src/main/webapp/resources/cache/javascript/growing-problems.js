/**
 * growing-problems.js
 */


function loadGrowingProblemsWidget(id) {

	var $element = $('#' + id);
	$.post(applicationRoot + "/GrowingProblems", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'growingProblems', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .append(response);
			}
			
			$('.growingProblems .widget_title').click(function() {
				$('.growingProblems .widget-header').slideToggle('fast');
			});
	});	
}
