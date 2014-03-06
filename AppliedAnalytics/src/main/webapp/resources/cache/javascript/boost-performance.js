/**
 * boost-performance.js
 */


function loadBoostPerformanceWidget(id) {
	var $element = $('#' + id);
	$.post(applicationRoot + "/BoostPerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'boostPerformance', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .append(response);
			}
			
			$('.boostPerformance .widget_title').click(function() {
				$('.boostPerformance .widget-content').slideToggle('fast');
			});
			
			$('.bp-box').click(function() {
				$(this).children(".bp-content").toggle();
			});
			
			// add option to menu for table view toggle
			$tableMode = $('<li>').html('Show as table').on();
			$('#' + id + ' .dropdown-menu').attr('id', id).append($tableMode);
	});	
	
	
	
}



