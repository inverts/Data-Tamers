/**
 * hypothetical-future.js
 */



$(function() {
	updateHypotheticalWidget();
	
	
	
});

function updateHypotheticalWidget() {
	
	var $element = $('#hypotheticalFuture');
	$.post(applicationRoot + "DataForecast", null, function(response) {
		if ($element.length > 0) {
			$element.fadeOut("fast", function() { 
					$element.empty().append(response).show(); 
			});
		}
		else {
			$element = $('<div>').attr({ 'id': 'hypotheticalFuture', 'class': 'w_container'})
								 .prop('draggable', true)
								 .appendTo('.dashboard-content')
								 .append(response);
		}
		
		//$('#hypotheticalFutureData').append(hypotheticalSketch);
		
		var lineclasses = ['raw', 'smooth', 'normal'];
		
		$('#dataForecastData').graph({
			data: window.newData,
			yKey: ['jsonHitCount', 'smooth', 'normal'],
			lineClass: lineclasses,
			lineType: ['', 'cardinal', ''],
			startIndex: 0,
			endIndex: window.newData.length-1,
			pointSize: 0,
			databuffer: 10,
			dateLine: new Date().setHours(0,0,0,0)
				
		});
		
		// Collapse Event
		$('.dataForecast .widget_title').click(function() {
			$('.dataForecast .widget-content').slideToggle('fast');
		});
		
		$('#rawBtn').on('click', function() { toggleLine('raw', this); });
		
		$('#normBtn').on('click', function() { toggleLine('normal', this); });
		
		$('#smoothBtn').on('click', function() { toggleLine('smooth', this); });
		
		createLegend(lineclasses);
		
		$('#rawBtn').click();
		
		
		//var canvas = document.getElementById('hypotheticalFutureData');
		
		// points = HypotheticalFutureData.points;
		//var p = new Processing(canvas, hypotheticalSketch);
		

		//$element.fadeIn("fast");
		/*window.onresize = function(event) {
			var p = new Processing(canvas, hypotheticalSketch);
		}*/
		
		// widget will not be dragged while user clicks on content
		$('.dashboard-content').sortable({ cancel: '.widget-content'});
	});
}


function toggleLine(className, btn) {
	var line = d3.select('.' + className + '.plot');
	var altLine = d3.select('.' + className + '.alternate');
	var legend = d3.select('.' + className + '.graph-legend');
	var current = line.attr('class');
	
	if (current == className + ' plot') {
		line.attr('class', className + ' plot active');
		legend.attr('class', className + ' graph-legend active');
		if (altLine.length)
			altLine.attr('class', className + ' alternate active');
	}
	else{
		line.attr('class', className + ' plot');
		legend.attr('class', className + ' graph-legend');
		if (altLine.length)
			altLine.attr('class', className + ' alternate');
	}
	
	$(btn).toggleClass('active');
	
}

function createLegend(lines) {
	

	for(var i = 0; i < lines.length; i++) {
		
		var legendLine = d3.select('#graphLines').append('li')
								.attr('class', lines[i] + ' graph-legend')
								.append('svg')
								.append('g')
								.attr("transform", "translate(0, 10)");
		legendLine.append('rect')
				  .attr({
			   	    "height": 20,
				  })
			   	  .style({
				   "fill": "transparent"
			   	});
		
		legendLine.append('svg')
				  .append('line')
				  .attr({
					    	'class': lines[i],
					    	'y1': 5, 'y2': 5,
					    	'x1': 5, 'x2': 35, 
				  		});
		
		legendLine.append("text")
				  .text(lines[i])
				  .attr({
					  "x": 40,
					  "y": 8
				  });
	}
	
	
}
