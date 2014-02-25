/**
 * keyword-insight.js
 */


function loadKeywordInsight(id) {
	
	var $element = $("#" + id);
	
	$.post(applicationRoot + "KeywordInsight", null, 
		function(response) {
		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
					$element.append(response); 
			});
		}
			else {
				
				console.error("could not append Keyword Insight Widget to id: " + id)
			}
		
		//TODO: Visualization
		
		//TODO: Other widget functions
		
		// Collapse Event
		$('.keywordInsight .widget_title').click(function() {
			$('.keywordInsight .widget-content').slideToggle('fast');
		});

	});		
}


function updateKeywordInsight(id) {
	
}