/**
 * keyword-insight.js
 */

/* Global Variables */
var visits = [];
var bounceRate = [];
var dataset = [
               [5, 20], [480, 90], [250, 50], [100, 33], [330, 95],
               [410, 12], [475, 44], [25, 67], [85, 21], [220, 88]
             ];

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
				
				console.error("could not append Keyword Insight Widget to id: " + id);
			}
		
		$('#' + id + ' .dropdown-menu').attr('id', id);
		$a = $('<a>').attr({'role': 'menu-item', 'class': 'viewOption'});
		$li = $('<li>').append($a);
		$('#' + id + ' .dropdown-menu').append($li);
		
		getKeywordInsightData(id, true, function() {
			// Collapse Event
			$('.keywordInsight .widget_title').dblclick(function() {
				$('.keywordInsight .widget-content').slideToggle('fast');
			});
			
			
			
		});

	});		
}


function viewScatter(id) {
	
	/*var data =JSON.parse('${ kiModel.getScatterPlotDataPoints() }');
	for(var r = 0; r < data.allKeywords.length; r++)
		{
		  data.allKeywords[r];   // keywords
		  data.allVisitsPercent[r];  // visits in percent
		  data.allBounceRate[r];    // bounce rate in percent
		  data.allMultipageVisitsPercent[r]; // multipage visits in percent
	    }*/
	
	$('#' + id + ' #keywordInsightData').empty().scatter({
		'id'	: 'keywordInsightData',
		'xLabel': 'Percentage of Bounce Rates',
		'yLabel': 'Percentage of Webpage Visits',
		'xKey' 	: 'bounceRates',
		'yKey'	: 'visits',
		'data'	: dataset
	});
	
	$('#' + id + ' .viewOption').addClass('table')
								.html('option name')
								.click(function() {
									viewTable(id);
								});
	
}

function viewTable(id) {
	var dataPoints = dataset;
	console.log(dataset);
	// if no data display error message
	if (dataPoints == null) {
	     console.log("keyword insight data is null.")
		//("#keywordInsightSettings")
		//		.append('<h2><fmt:message key="keywordinsight.gaOverQuotaError"/></h2>');
	} else { // else parse and display data
		var data = dataset;
		console.log(data);

	/*	// Remove keywords table
		var message = $('<h4><fmt:message key="keywordinsight.removeSuggestion"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.length; r++) {
			var tr = $('<tr>');
			var key = '<td>' + data.removeKeywords[r] + '</td>';
			var visits = '<td>' + data.removeVisitsPercent[r] + '</td>';
			var bounceRate = '<td>' + data.removeBounceRate[r] + '</td>';
			var rank = '<td>'
					+ Math.round(100 * data.removeMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");
*/
		// Help keywords table
		var message = $('<br><h4><fmt:message key="keywordinsight.changeSuggestion"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.helpKeywords.length; r++) {
			var tr = $('<tr>');
			var key = '<td>' + data.helpKeywords[r] + '</td>';
			var visits = '<td>' + data.helpVisitsPercent[r] + '</td>';
			var bounceRate = '<td>' + data.helpBounceRate[r] + '</td>';
			var rank = '<td>'
					+ Math.round(100 * data.helpMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");

		// Best keywords table
		var message = $('<br><h4><fmt:message key="keywordinsight.bestMessage"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.bestKeywords.length; r++) {
			var tr = $('<tr>');
			var key = '<td>' + data.bestKeywords[r] + '</td>';
			var visits = '<td>' + data.bestVisitsPercent[r] + '</td>';
			var bounceRate = '<td>' + data.bestBounceRate[r] + '</td>';
			var rank = '<td>'
					+ Math.round(100 * data.bestMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");

		// Best keyword substring performance table
		var message = $('<br><h4><fmt:message key="keywordinsight.bestWordsMessage"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Word Substring</th>').appendTo(tr);
		$('<th>Keyword Count</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.bestWords.length; r++) {
			var tr = $('<tr>');
			var word = '<td>' + data.bestWords[r] + '</td>';
			var keywordCount = '<td>' + data.bestWordsCount[r] + '</td>';
			var rank = '<td>'
					+ Math.round(100 * data.bestWordsMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(word).appendTo(tr);
			$(keywordCount).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");

		// Worst keyword substring performance table
		var message = $('<br><h4><fmt:message key="keywordinsight.worstWordsMessage"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Word Substring</th>').appendTo(tr);
		$('<th>Keyword Count</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.worstWords.length; r++) {
			var tr = $('<tr>');
			var word = '<td>' + data.worstWords[r] + '</td>';
			var keywordCount = '<td>' + data.worstWordsCount[r] + '</td>';
			var rank = '<td>'
					+ Math
							.round(100 * data.worstWordsMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(word).appendTo(tr);
			$(keywordCount).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");

		// All cpc keywords table
		var message = $('<br><h4><fmt:message key="keywordinsight.searchMessage"/></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.allCpcKeywords.length; r++) {
			var tr = $('<tr>');
			var key = '<td>' + data.allCpcKeywords[r] + '</td>';
			var visits = '<td>' + data.allCpcVisitsPercent[r] + '</td>';
			var bounceRate = '<td>' + data.allCpcBounceRate[r] + '</td>';
			var rank = '<td>'
					+ Math.round(100 * data.allCpcMultipageVisitsPercent[r])
					/ 100.0 + '</td>';

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");

		$('#' + id + ' .viewOption').removeClass('table').html('option name2')
				.click(function() {
					viewScatter(id);
				});
	}
}

function getKeywordInsightData(id, tableView, callback) {
	$.post(applicationRoot + "KeywordInsight", {"serialize": 1}, function(response) {
		// if no data display error message
		if (response == null) {
			console.log("Data from Keyword Insight model is null");
			return;
			//("#keywordInsightSettings").append('<h2><fmt:message key="keywordinsight.gaOverQuotaError"/></h2>');
		}
		var d = $.parseJSON(response);
		dataset = d.data;
		if(!tableView) {
			viewScatter(id);
		}
		else {
			 viewTable(id);
		}
		
		if(callback)
			callback();
		
	});
	
}

function updateKeywordInsight(id) {
	//TODO: determine what state we are in
	// getKeywordInsightData()    //with appropriate flag
	
}
