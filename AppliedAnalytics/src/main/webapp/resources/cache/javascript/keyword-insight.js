/**
 * keyword-insight.js
 */

/* Global Variables */
var sdata = [];  // 3-D array x, y, and keywords for

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

		$("#" + id + " .dropdown-menu").attr("id", id);
		$a = $("<a>").attr({"role": "menu-item", "class": "viewOption"});
		$li = $("<li>").append($a);
		$("#" + id + " .dropdown-menu").append($li);

		getKeywordInsightData(id, function() {
			
			var $parent = $("#" + id + " #keywordInsightData");
			
			// previous
			$("#" + id + " .prev").click(function(e) {
				var $prev = $parent.children(".active").prev();
				$parent.children(".active").removeClass("active").hide("slide", {direction: "right"}, "fast", function() {
					($prev.length) ? $prev.addClass("active").show("slide", {direction: "left" }, "fast")
							       : $parent.children(".keywordVisual:first").show("slide", {direction: "left"}, "fast")
							       										     .addClass("active");
				});
				
			});
			
			// next
			$("#" + id + " .next").click(function(e) {
				var $next = $parent.children(".active").next();
				$parent.children(".active").removeClass("active").hide("slide", {direction: "left"}, "fast", function() {
					($next.length) ? $next.addClass("active").show("slide", {direction: "right"}, "fast")
							       : $parent.children(".keywordVisual:first").show("slide", {direction: "right"}, "fast")
							       										     .addClass("active");
				});
			});	
			
			// direct buttons
			$("#" + id + " a.scatter").click(function(e) { switchView(id, $parent, "keywordInsightScatter"); });
			$("#" + id + " a.improve").click(function(e) { switchView(id, $parent, "keywordInsightImprove"); });
			$("#" + id + " a.best").click(function(e) { switchView(id, $parent, "keywordInsightBest"); });
			$("#" + id + " a.worst").click(function(e) { switchView(id, $parent, "keywordInsightWorst"); });
			$("#" + id + " a.all").click(function(e) { switchView(id, $parent, "keywordInsightAll"); });
			$("#" + id + " a.bestsub").click(function(e) { switchView(id, $parent, "keywordInsightBestSub"); });
			$("#" + id + " a.worstsub").click(function(e) { switchView(id, $parent, "keywordInsightWorstSub"); });
		});

	});		
}

function switchView(id, $parent, $view) {
	$parent.children(".active").removeClass("active").hide();
	$("#" + id + " #" + $view).show().addClass("active");
	
}

function createTableView(id, data) {
	var $view = $("#" + id).empty();
	
	// add sub-title
	$("<h4>").html("<b>" + data.title + "</b>").appendTo($view);
	
	var $table = $("<table>").addClass("keywordTable").appendTo($view);
	
	var $trlabel = $("<tr>").addClass("columnHeader").appendTo($table);
	// setup column headers
	for (var i = 0; i < data.keys.length; i++)
		$("<th>").html(data.keys[i]).appendTo($trlabel);
	
	// access data via column headers as key names
	for (var i = 0; i < data[data.keys[0]].length; i++) {
		var $tr = $("<tr>").appendTo($table);
		for (var j = 0; j < data.keys.length; j++)
			$("<th>").html(data[data.keys[j]][i]).appendTo($tr);
	}
}

function getKeywordInsightData(id, callback) {
	$.post(applicationRoot + "KeywordInsight", {"serialize": 1}, function(response) {
		// if no data display error message
		if (response == null) {
			console.log("Data from Keyword Insight model is null");
			return;
		}
			
		var d = $.parseJSON(response);
		
		if (d.data === undefined) {
			console.log("Data from Keyword Insight model is undefined");
			console.log(d);
			return;
		}
		
		for(var r = 0; r < d.data.all[d.data.all.keys[0]].length; r++){
			sdata.push([d.data.all[d.data.all.keys[2]][r], d.data.all[d.data.all.keys[1]][r], d.data.all[d.data.all.keys[0]][r]]);
		}
		// scatter view
		$("#" + id + " #keywordInsightScatter").empty().scatter({
			"id"	: "keywordInsightScatter",
			"xLabel": "Percentage of Bounce Rates",
			"yLabel": "Percentage of Webpage Visits",
			"xKey" 	: "bounceRates",
			"yKey"	: "visits",
			"data"	: sdata
		});
		
		createTableView("keywordInsightImprove", d.data.improve);
		createTableView("keywordInsightBest", d.data.best);
		createTableView("keywordInsightWorst", d.data.worst);
		createTableView("keywordInsightAll", d.data.all);
		createTableView("keywordInsightBestSub", d.data.bestsubstr);
		createTableView("keywordInsightWorstSub", d.data.worstsubstr);

		if(callback)
			callback();

	});

}

function updateKeywordInsight(id) {
	
	//var $currentView = $("#" + id + " keywordInsightData").children(".active");
	
	getKeywordInsightData(id); 

}

//------------------------------------------------------------------
// Not currently used
function viewScatter(id, data) {
	//var data = dataset;
	console.log(data);
	// if no data display error message
	if (data == null) {
		console.log("keyword insight data is null.");
		$("<h4>Keyword Insight data is returning a null.</h4>").appendTo("#keywordInsightSettings");
		sdata = null;
		
	} else { // else parse and display data

		for(var r = 0; r < data.allCpcKeywords.length; r++){
			sdata.push([data.allCpcBounceRate[r], data.allCpcVisitsPercent[r], data.allCpcKeywords[r]]);
		}
	}

	console.log(sdata);

	$("#" + id + " #keywordInsightScatter").empty().scatter({
		"id"	: "keywordInsightData",
		"xLabel": "Percentage of Bounce Rates",
		"yLabel": "Percentage of Webpage Visits",
		"xKey" 	: "bounceRates",
		"yKey"	: "visits",
		"data"	: sdata
	});
	
}



function keywordImproveView(id) {
	var message = $("<br><h4><b>Improve website performance for the following keywords:</h4><br>");
	var table = $("<table><tbody>");
	var tr = $("<tr>");
	$("<th>Keywords</th>").appendTo(tr);
	$("<th>Visits (%)</th>").appendTo(tr);
	$("<th>Bounce Rate (%)</th>").appendTo(tr);
	$("<th>Multipage Visits (%)</th>").appendTo(tr);

	tr.appendTo(table);
	for (var r = 0; r < data.helpKeywords.length; r++) {
		var tr = $("<tr>");
		var key = "<td>" + data.helpKeywords[r] + "</td>";
		var visits = "<td>" + data.helpVisitsPercent[r] + "</td>";
		var bounceRate = "<td>" + data.helpBounceRate[r] + "</td>";
		var rank = "<td>"
			+ Math.round(100 * data.helpMultipageVisitsPercent[r])
			/ 100.0 + "</td>";

		$(key).appendTo(tr);
		$(visits).appendTo(tr);
		$(bounceRate).appendTo(tr);
		$(rank).appendTo(tr);
		tr.appendTo(table);
	}
	message.appendTo("#" + id);
	table.appendTo("#" + id);
}




function keywordBestView(id,data) {
	
	var message = $("<br><h4><b>The best performing keywords:</b></h4><br>");
	var table = $("<table><tbody>");
	var tr = $("<tr>");
	$("<th>Keywords</th>").appendTo(tr);
	$("<th>Visits (%)</th>").appendTo(tr);
	$("<th>Bounce Rate (%)</th>").appendTo(tr);
	$("<th>Multipage Visits (%)</th>").appendTo(tr);

	tr.appendTo(table);
	for (var r = 0; r < data.bestKeywords.length; r++) {
		var tr = $("<tr>");
		var key = "<td>" + data.bestKeywords[r] + "</td>";
		var visits = "<td>" + data.bestVisitsPercent[r] + "</td>";
		var bounceRate = "<td>" + data.bestBounceRate[r] + "</td>";
		var rank = "<td>"
			+ Math.round(100 * data.bestMultipageVisitsPercent[r])
			/ 100.0 + "</td>";

		$(key).appendTo(tr);
		$(visits).appendTo(tr);
		$(bounceRate).appendTo(tr);
		$(rank).appendTo(tr);
		tr.appendTo(table);
	}
	message.appendTo("#" + id);
	table.appendTo("#" + id);
}


function keywordBestSubStringView(id) {
	var message = $("<br><h4><b>The best performing keyword substrings:</b></h4><br>");
	var table = $("<table><tbody>");
	var tr = $("<tr>");

	$("<th>Word Substring</th>").appendTo(tr);
	$("<th>Keyword Count</th>").appendTo(tr);
	$("<th>Multipage Visits (%)</th>").appendTo(tr);
	tr.appendTo(table);
	for (var r = 0; r < data.bestWords.length; r++) {
		var tr = $("<tr>");
		var word = "<td>" + data.bestWords[r] + "</td>";
		var keywordCount = "<td>" + data.bestWordsCount[r] + "</td>";
		var rank = "<td>"
			+ Math.round(100 * data.bestWordsMultipageVisitsPercent[r])
			/ 100.0 + "</td>";

		$(word).appendTo(tr);
		$(keywordCount).appendTo(tr);
		$(rank).appendTo(tr);
		tr.appendTo(table);
	}
	message.appendTo("#" + id);
	table.appendTo("#" + id);
}

function keywordWorstView() {
	var message = $("<br><h4><b>The worst performing keyword substrings:</b></h4><br>");
	var table = $("<table><tbody>");
	var tr = $("<tr>");
	$("<th>Word Substring</th>").appendTo(tr);
	$("<th>Keyword Count</th>").appendTo(tr);
	$("<th>Multipage Visits (%)</th>").appendTo(tr);

	tr.appendTo(table);
	for (var r = 0; r < data.worstWords.length; r++) {
		var tr = $("<tr>");
		var word = "<td>" + data.worstWords[r] + "</td>";
		var keywordCount = "<td>" + data.worstWordsCount[r] + "</td>";
		var rank = "<td>"
			+ Math
			.round(100 * data.worstWordsMultipageVisitsPercent[r])
			/ 100.0 + "</td>";

		$(word).appendTo(tr);
		$(keywordCount).appendTo(tr);
		$(rank).appendTo(tr);
		tr.appendTo(table);
	}
	message.appendTo("#" + id);
	table.appendTo("#" + id);
}


function viewTable(id) {	
	var data = dataset;
	var n;
	console.log(data);
	// if no data display error message
	if (data == null) {
		console.log("keyword insight data is null.")
		$("<h4>Keyword Insight data is returning a null</h4>").appendTo("#keywordInsightTable");

	} else { // else parse and display data

			// Remove keywords table
		/*var message = $("<h4><fmt:message key="keywordinsight.removeSuggestion"/></h4><br>");
		var table = $("<table><tbody>");
		var tr = $("<tr>");
		$("<th>Keywords</th>").appendTo(tr);
		$("<th>Visits (%)</th>").appendTo(tr);
		$("<th>Bounce Rate (%)</th>").appendTo(tr);
		$("<th>Multipage Visits (%)</th>").appendTo(tr);
		tr.appendTo(table);
		for (var r = 0; r < data.length; r++) {
			var tr = $("<tr>");
			var key = "<td>" + data.removeKeywords[r] + "</td>";
			var visits = "<td>" + data.removeVisitsPercent[r] + "</td>";
			var bounceRate = "<td>" + data.removeBounceRate[r] + "</td>";
			var rank = "<td>"
					+ Math.round(100 * data.removeMultipageVisitsPercent[r])
					/ 100.0 + "</td>";

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");*/

		// Help keywords table
		

		// Best keywords table
		

		// Best keyword substring performance table
		

		// Worst keyword substring performance table
		
		// All cpc keywords table
		var message = $("<br><h4><b>All paid keywords:</b></h4><br>");
		var table = $("<table><tbody>");
		var tr = $("<tr>");
		$("<th>Keywords</th>").appendTo(tr);
		$("<th>Visits (%)</th>").appendTo(tr);
		$("<th>Bounce Rate (%)</th>").appendTo(tr);
		$("<th>Multipage Visits (%)</th>").appendTo(tr);

		tr.appendTo(table);
		for (var r = 0; r < data.allCpcKeywords.length; r++) {
			var tr = $("<tr>");
			var key = "<td>" + data.allCpcKeywords[r] + "</td>";
			var visits = "<td>" + data.allCpcVisitsPercent[r] + "</td>";
			var bounceRate = "<td>" + data.allCpcBounceRate[r] + "</td>";
			var rank = "<td>"
					+ Math.round(100 * data.allCpcMultipageVisitsPercent[r])
					/ 100.0 + "</td>";

			$(key).appendTo(tr);
			$(visits).appendTo(tr);
			$(bounceRate).appendTo(tr);
			$(rank).appendTo(tr);
			tr.appendTo(table);
		}
		message.appendTo("#keywordInsightSettings");
		table.appendTo("#keywordInsightSettings");
	}
}


