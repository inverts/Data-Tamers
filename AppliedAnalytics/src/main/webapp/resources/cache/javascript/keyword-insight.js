/**
 * keyword-insight.js
 */

$(function() {
	// Temporary
	GetKeywordInsightWidget();

});


function GetKeywordInsightWidget() {
	
	var $element = $('#keywordInsight');
	
	$.post("KeywordInsight", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'keywordInsight', 'class': 'w_container'})
									 .prop('draggable', false)
									 .appendTo('.dashboard-content')
									 .append(response);
			}
			// Collapse Event
			$('.keywordInsight .widget_title').click(function() {
				$('.keywordInsight .widget-content').slideToggle('fast');
			});

	});		
}


	    