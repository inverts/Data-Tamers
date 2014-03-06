/**
 * data-forecast.js
 */

/* Global Variables */
//var dataForecastData = window.newData;
var lineTypes = ['', 'cardinal', ''];
var lineclasses = ['raw', 'smooth', 'normal'];
var dataForecastDataKeys = ['jsonHitCount', 'smooth', 'normal'];
var filterStartIndex = 0; //TODO: get filter date start
//var filterEndIndex = window.newData.length-1; // TODO: get filter date end

/**
 * Initally loads the entire widget including model and view.
 */
function loadDataForecast(id) {
	
	var $element = $('#' + id);
	//TODO: Possibly send id number out to append to ids in the model
	$.post(applicationRoot + "DataForecast", null, function(response) {
		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
					$element.append(response); 
			});
		}
		else
			console.error('Could not append Data Forecast Widget to id: ' + id);
		
		
		
		getDataForecastData(id, function() {
			
			// Collapse Event
			$('.dataForecast .widget_title').dblclick(function() {
				$('.dataForecast .widget-content').slideToggle('fast');
			});
			
			$('#rawBtn').on('click', function() { toggleLine('raw', this); });
			$('#normBtn').on('click', function() { toggleLine('normal', this); });
			$('#smoothBtn').on('click', function() { toggleLine('smooth', this); });
			
			createLegend(lineclasses);
			
			$('#rawBtn').click(); // initially turn on raw data.
			
			$('#' + id + ' .dropdown-menu').attr('id', id);
			
		});

	});
}


function getDataForecastData(id, callback) {
	$.post(applicationRoot + "DataForecast", {"serialize": 1}, function(response) {
		var d = $.parseJSON(response);
		
		$('#' + id + ' #dataForecastData').empty().graph({
															data: d.data,
															yKey: dataForecastDataKeys,
															lineClass: lineclasses,
															lineType: lineTypes,
															startIndex: 0, 
															endIndex: d.data.length - 1, 
															pointSize: 4,
															dateLine: new Date().setHours(0,0,0,0) // todays date without time
														});
		
		if(callback)
			callback();
		
	});
}


/**
 * Updates the graph of the forecast widget without having to 
 * call the model and view again.
 * 
 * @param id - dashboard container id
 */
function updateDataForecast(id) {
	
	var $buttons = $('#' + id + ' button.active').removeClass('active');
	
	getDataForecastData(id, function() {
		$.each($buttons, function() {
			$(this).click();
		});	
	});
}


/**
 * Toggles the plot lines on and off
 * 
 * @param className - name of the class aka line.
 * @param btn - id of the button clicked.
 */
function toggleLine(className, btn) {
	var line = d3.selectAll('.' + className + '.plot');
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


/**
 * Creates the legend and functionality pertianing
 * to the data forecast widget.
 * 
 * @param lines - array of classes aka lines
 */
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
