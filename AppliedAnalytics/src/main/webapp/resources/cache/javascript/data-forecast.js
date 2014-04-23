/**
 * data-forecast.js
 */

/* Global Variables */
var lineTypes = ['', 'cardinal', ''];
var lineclasses = ['raw', 'smooth', 'normal'];
var dataForecastDataKeys = ['jsonHitCount', 'smooth', 'normal'];
var filterStartIndex = 0; //TODO: get filter date start
//var filterEndIndex = window.newData.length-1; // TODO: get filter date end

//flag  indicating that the widget is loading so not to call the update function which creates a race condition
var loadingDataForecast = false; 

/**
 * Initally loads the entire widget including model and view.
 */
function loadDataForecast(id, callback) {
	loadingDataForecast = true;
	var $element = $('#' + id);
	//TODO: Possibly send id number out to append to ids in the model
	$.post(applicationRoot + "DataForecast", null, function(response) {
		
		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
					$element.html(response); 

					getDataForecastData(id, function() {
						
						setupButtons(id);
						
						// legend
						createLegend(id, lineclasses);

					});
					
					if(callback)			
						callback();
					
					
			});
		}
		else
			console.error('Could not append Data Forecast Widget to id: ' + id);

	});
	
	loadingDataForecast = false;
}


function getDataForecastData(id, callback) {
	$.post(applicationRoot + "DataForecast", {"serialize": 1}, function(response) {
		var d = $.parseJSON(response);
		
		if (d && d.data)
			$("#" + id).data("hasData", true); // flag the widget as having data.
		
		$("#" + id + " .spinner-content").hide();

		$("#" + id + " .help").tooltip({ content: d.description });

		$('#' + id + ' #dataForecastData').data({
													"data": d.data,
													"endDate": d.endDate
												});
		
		$('#' + id + ' #dataForecastData').empty().graph({
															data: d.data,
															id: id,
															yKey: dataForecastDataKeys,
															lineClass: lineclasses,
															lineType: lineTypes,
															startIndex: 0, 
															endIndex: d.data.length - 1, 
															pointSize: 4,
															dateLine: d.endDate
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
function updateDataForecast(id, callback) {
	
	if (loadingDataForecast)
		return;
	
	var $buttons = $('#' + id + ' div.left button.active').removeClass('active');
	var $timeBtn = $('#' + id + ' div.right button.active');

	getDataForecastData(id, function() {
		
		(!$buttons.length) ? setupButtons(id)
						   : $.each($buttons, function() { $(this).click(); });
		
		$timeBtn.click();
		callback();
	});

}


function setupButtons(id) {
	
	var $element = $("#" + id);
	
	$element.off('click.df', "button");
	
	// Setup forecast buttons
	$element.on('click.df', "#rawBtn", function() { toggleLine(id, 'raw', this); });
	$element.on('click.df', "#normBtn", function() { toggleLine(id, 'normal', this); });
	$element.on('click.df', "#smoothBtn", function() { toggleLine(id, 'smooth', this); });

	$element.on('click.df', "#dayBtn", function() { toggleAggregation(id, TIMESPAN_DAY, this); });
	$element.on('click.df', "#weekBtn", function() { toggleAggregation(id, TIMESPAN_WEEK, this); });
	$element.on('click.df', "#monBtn", function() { toggleAggregation(id, TIMESPAN_MONTH, this); });
	
	// initially turn on RAW
	$("#" + id + " #rawBtn").click(); // initially turn on raw data.
}

/**
 * Toggles the plot lines on and off
 * 
 * @param className - name of the class aka line.
 * @param btn - id of the button clicked.
 */
function toggleLine(id, className, btn) {
	var line = d3.selectAll('#' + id + ' .' + className + '.plot');
	var altLine = d3.select('#' + id + ' .' + className + '.alternate');
	var legend = d3.select('#' + id + ' .' + className + '.graph-legend');
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

function reactivateLines(id) {

	var $buttons = $('#' + id + ' button.active').removeClass('active');		
	(!$buttons.length) ? $("#" + id + " #rawBtn").click()
					   : $.each($buttons, function() { $(this).click(); });	

}

TIMESPAN_DAY = 0;
TIMESPAN_WEEK = 1;
TIMESPAN_MONTH = 2;
/**
 * Toggles the different time aggregation views (day/week/month).
 * 
 * @author Dave Wong
 * @param id
 * @param timeSpanType
 */
function toggleAggregation(id, timeSpanType, btn) {
	numDays = 1;
	$("#" + id + " div.right button").removeClass("active"); // remove active status of old button
	switch(timeSpanType) {
		case TIMESPAN_DAY:
			numDays = 1;
			break;
		case TIMESPAN_WEEK:
			numDays = 7;
			break;
		case TIMESPAN_MONTH:
			numDays = 30;
			break;
		default:
			break;
	}
	
	data = $('#' + id + ' #dataForecastData').data("data");
	endDate = $('#' + id + ' #dataForecastData').data("endDate");
	newData = new Array();
	for (i in data) {
		if (i % numDays == 0) {
			if (i != 0)
				newData.push(newRow);
			newRow = new Array();
			newRow = { jsonDate:null, jsonHitCount: 0, normal: 0, smooth:0 };
			newRow.jsonDate = data[i].jsonDate; //Set the date for this new row as the first date available.
			
		}
		newRow.jsonHitCount += data[i].jsonHitCount;
		newRow.normal += data[i].normal;
		newRow.smooth += data[i].smooth;
	}
	if (i % numDays != 0)
		newData.push(newRow);
	
	$('#' + id + ' #dataForecastData').empty().graph({
		data: newData,
		id: id,
		yKey: dataForecastDataKeys,
		lineClass: lineclasses,
		lineType: lineTypes,
		startIndex: 0, 
		endIndex: newData.length - 1, 
		pointSize: 4,
		dateLine: endDate
	});
	
	//toggleLine(id, 'raw', null);
	reactivateLines(id);
	
	$(btn).addClass("active"); //set active class on right buttons

}

/**
 * Creates the legend and functionality pertianing
 * to the data forecast widget.
 * 
 * @param lines - array of classes aka lines
 */
function createLegend(id, lines) {

	for(var i = 0; i < lines.length; i++) {
		
		var legendLine = d3.select('#' + id + ' #graphLines').append('li')
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
