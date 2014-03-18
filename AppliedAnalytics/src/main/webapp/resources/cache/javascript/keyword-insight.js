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

			console.error("could not append Keyword Insight Widget to id: " + id);
		}

		$("#" + id + " .dropdown-menu").attr("id", id);
		$a = $("<a>").attr({"role": "menu-item", "class": "viewOption"});
		$li = $("<li>").append($a);
		$("#" + id + " .dropdown-menu").append($li);
		
		// Get the data and setup widget functions after data has been retrieved.
		getKeywordInsightData(id, function() {

			nextPreviousControls(id, "keywordVisual");

			// direct view buttons
			$("#" + id + " a.scatter").click(function(e) { changeViewBtn(id, "keywordInsightScatter"); });
			$("#" + id + " a.improve").click(function(e) { changeViewBtn(id, "keywordInsightImprove"); });
			$("#" + id + " a.best").click(function(e) { changeViewBtn(id, "keywordInsightBest"); });
			$("#" + id + " a.worst").click(function(e) { changeViewBtn(id, "keywordInsightWorst"); });
			$("#" + id + " a.all").click(function(e) { changeViewBtn(id, "keywordInsightAll"); });
			$("#" + id + " a.bestsub").click(function(e) { changeViewBtn(id, "keywordInsightBestSub"); });
			$("#" + id + " a.worstsub").click(function(e) { changeViewBtn(id, "keywordInsightWorstSub"); });
		});

	});		
}


function getKeywordInsightData(id, callback) {
	$.post(applicationRoot + "KeywordInsight", {"serialize": 1}, function(response) {
		
		// if no data display error message
		if (response == null) {
			console.log("Data from Keyword Insight model is null");
			return;
		}
			
		var d = $.parseJSON(response);
		
		if (!d.data) {
			// TODO: Handle null data scenario.
			console.log("No data for keywordInsight");
			
			// Even though there is no data, we still need to fire off 
			// the callback if its in place otherwise when we change to an
			// account with data. We don't have our controls.
			if (callback)
				callback();
			
			return;
		}
		
		var sdata = [];
		
		for (var r = 0; r < d.data.all[d.data.all.keys[0]].length; r++)
			sdata.push([d.data.all[d.data.all.keys[2]][r], d.data.all[d.data.all.keys[1]][r], d.data.all[d.data.all.keys[0]][r]]);
		
		
		// scatter view
		$("#" + id + " #keywordInsightScatter").empty().scatter({
			"id"	: "keywordInsightScatter",
			"xLabel": d.data.scatter.label[0],
			"yLabel": d.data.scatter.label[1],
			"xKey" 	: d.data.scatter.keys[0],
			"yKey"	: d.data.scatter.keys[1],
			"data"	: sdata
		});
		
		// Create each table view
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

// Updates the widget.
function updateKeywordInsight(id) {
	getKeywordInsightData(id); 
}


