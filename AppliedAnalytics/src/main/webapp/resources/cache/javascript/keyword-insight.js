/**
 * keyword-insight.js
 */

/* Global Variables */
var sdata = [];  // 2-D array x, y datapoints for scatterplot

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

		getKeywordInsightData(id,false, function() {
			// Collapse Event
			/*$('.keywordInsight .widget_title').dblclick(function() {
				$('.keywordInsight .widget-content').slideToggle('fast');
			});*/



		});

	});		
}


function viewScatter(id) {
	var data = dataset;
	console.log(data);
	// if no data display error message
	if (data == null) {
		console.log("keyword insight data is null.")
		//("#keywordInsightSettings")
		//		.append('<h2><fmt:message key="keywordinsight.gaOverQuotaError"/></h2>');
	} else { // else parse and display data

		for(var r = 0; r < data.allCpcKeywords.length; r++){
			sdata.push([data.allCpcBounceRate[r], data.allCpcVisitsPercent[r], data.allCpcKeywords[r]]);
		}
	}


	console.log(sdata);

	$('#' + id + ' #keywordInsightData').empty().scatter({
		'id'	: 'keywordInsightData',
		'xLabel': 'Percentage of Bounce Rates',
		'yLabel': 'Percentage of Webpage Visits',
		'xKey' 	: 'bounceRates',
		'yKey'	: 'visits',
		'data'	: sdata
	});

	$('#' + id + ' .viewOption').addClass('table')
	.html('option name')
	.click(function() {
		viewTable(id);
	});

}

function viewTable(id) {
	var data = dataset;
	var n;
	console.log(data);
	// if no data display error message
	if (data == null) {
		console.log("keyword insight data is null.")
		//("#keywordInsightSettings")
		//		.append('<h2><fmt:message key="keywordinsight.gaOverQuotaError"/></h2>');
	} else { // else parse and display data

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
		var message = $('<br><h4><b>Improve website performance for the following keywords:</h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);

		if (data.helpKeywords.length < 5)
			n = data.helpKeywords.length;
		else
			n = 5;

		tr.appendTo(table);
		for (var r = 0; r < n; r++) {
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
		var message = $('<br><h4><b>The best performing keywords:</b></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);

		if (data.bestKeywords.length < 5)
			n = data.bestKeywords.length;
		else
			n = 5;

		tr.appendTo(table);
		for (var r = 0; r < n; r++) {
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
		var message = $('<br><h4><b>The best performing keyword substrings:</b></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');

		if (data.bestWords.length < 5)
			n = data.bestWords.length;
		else
			n = 5;

		$('<th>Word Substring</th>').appendTo(tr);
		$('<th>Keyword Count</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < n; r++) {
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
		var message = $('<br><h4><b>The worst performing keyword substrings:</b></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Word Substring</th>').appendTo(tr);
		$('<th>Keyword Count</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);

		if (data.worstWords.length < 5)
			n = data.worstWords.length;
		else
			n = 5;

		tr.appendTo(table);
		for (var r = 0; r < n; r++) {
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

		/*
		// All cpc keywords table
		var message = $('<br><h4><b>All paid keywords:</b></h4><br>');
		var table = $('<table><tbody>');
		var tr = $('<tr>');
		$('<th>Keywords</th>').appendTo(tr);
		$('<th>Visits (%)</th>').appendTo(tr);
		$('<th>Bounce Rate (%)</th>').appendTo(tr);
		$('<th>Multipage Visits (%)</th>').appendTo(tr);

		if (data.allCpcKeywords.length < 5)
				n = allCpcKeywords.length;
		else
				n = 5;

		tr.appendTo(table);
		for (var r = 0; r < n; r++) {
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
		 */
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
