/**
 * keyword-insight.js
 */


function loadKeywordInsight(id, callback) {

	var $element = $("#" + id);
	$.post(applicationRoot + "KeywordInsight", null, 
			function(response) {
		
		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
				$element.html(response); 
				
				// Get the data and setup widget functions after data has been retrieved.
				getKeywordInsightData(id, function() {

					// direct view buttons
					$("#" + id + " a.scatter").click(function(e) { changeViewBtn(id, "keywordInsightScatter"); });
					$("#" + id + " a.improve").click(function(e) { changeViewBtn(id, "keywordInsightImprove"); });
					$("#" + id + " a.best").click(function(e) { changeViewBtn(id, "keywordInsightBest"); });
					$("#" + id + " a.worst").click(function(e) { changeViewBtn(id, "keywordInsightWorst"); });
					$("#" + id + " a.all").click(function(e) { changeViewBtn(id, "keywordInsightAll"); });
					$("#" + id + " a.bestsub").click(function(e) { changeViewBtn(id, "keywordInsightBestSub"); });
					$("#" + id + " a.worstsub").click(function(e) { changeViewBtn(id, "keywordInsightWorstSub"); });
					
					$element.data("hasData", true); // flag the widget as having data.

				});
				
				if(callback)			
					callback();
			});
		}
		else {
			console.error("could not append Keyword Insight Widget to id: " + id);
		}

		$("#" + id + " .dropdown-menu").attr("id", id);
		$a = $("<a>").attr({"role": "menu-item", "class": "viewOption"});
		$li = $("<li>").append($a);
		$("#" + id + " .dropdown-menu").append($li);

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
		
		if (d.data === null || d.data === undefined) {
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
		
		$("#" + id + " .spinner-content").hide();
		
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
		//createTableView("keywordInsightImprove", d.data.improve);
		
		$("#" + id + " #keywordInsightImprove").table({
			"data": d.data.improve,
			"columnHeaders" : [
			                   {"name" : d.data.improve.keys[0], "width": "45%"}, 
			                   {"name" : d.data.improve.keys[1]},
			                   {"name" : d.data.improve.keys[2]},
			                   {"name" : d.data.improve.keys[3]}
			                  ],
			"m"			    : {"length": d.data.improve.keys.length, "keys": d.data.improve.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.improve[d.data.improve.keys[0]].length, "keys": null}, // rows
			"title"			: "Improve the website to better meet the needs of the following keywords:"
		});
		
		//createTableView("keywordInsightBest", d.data.best);
		
		$("#" + id + " #keywordInsightBest").table({
			"data": d.data.best,
			"columnHeaders" : [
			                   {"name" : d.data.best.keys[0], "width": "45%"}, 
			                   {"name" : d.data.best.keys[1]},
			                   {"name" : d.data.best.keys[2]},
			                   {"name" : d.data.best.keys[3]}
			                  ],
			"m"				: {"length": d.data.best.keys.length, "keys": d.data.best.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.best[d.data.best.keys[0]].length, "keys": null}, // rows
			"title"			: d.data.best.title
		});
		
		//createTableView("keywordInsightWorst", d.data.worst);
		
		$("#" + id + " #keywordInsightWorst").table({
			"data": d.data.worst,
			"columnHeaders" : [
			                   {"name" : d.data.worst.keys[0], "width": "45%"}, 
			                   {"name" : d.data.worst.keys[1]},
			                   {"name" : d.data.worst.keys[2]},
			                   {"name" : d.data.worst.keys[3]}
			                  ],
			"m"				: {"length": d.data.worst.keys.length, "keys": d.data.worst.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.worst[d.data.worst.keys[0]].length, "keys": null}, // rows
			"title"			: d.data.worst.title
		});
		
		//createTableView("keywordInsightAll", d.data.all);
		
		$("#" + id + " #keywordInsightAll").table({
			"data": d.data.all,
			"columnHeaders" : [
			                   {"name" : d.data.all.keys[0]}, 
			                   {"name" : d.data.all.keys[1]},
			                   {"name" : d.data.all.keys[2]},
			                   {"name" : d.data.all.keys[3]}
			                  ],
			"m"				: {"length": d.data.all.keys.length, "keys": d.data.all.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.all[d.data.all.keys[0]].length, "keys": null}, // rows
			"title"			: d.data.all.title,
			"search"		: true,
			"show"			: 0
		});
		
		//createTableView("keywordInsightBestSub", d.data.bestsubstr);
		
		$("#" + id + " #keywordInsightBestSub").table({
			"data": d.data.bestsubstr,
			"columnHeaders" : [
			                   {"name" : d.data.bestsubstr.keys[0], "width": "45%"}, 
			                   {"name" : d.data.bestsubstr.keys[1]},
			                   {"name" : d.data.bestsubstr.keys[2]}
			                  ],
			"m"				: {"length": d.data.bestsubstr.keys.length, "keys": d.data.bestsubstr.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.bestsubstr[d.data.bestsubstr.keys[0]].length, "keys": null}, // rows
			"title"			: d.data.bestsubstr.title
		});
		
		//createTableView("keywordInsightWorstSub", d.data.worstsubstr);
		
		$("#" + id + " #keywordInsightWorstSub").table({
			"data": d.data.worstsubstr,
			"columnHeaders" : [
			                   {"name" : d.data.worstsubstr.keys[0], "width": "45%"}, 
			                   {"name" : d.data.worstsubstr.keys[1]},
			                   {"name" : d.data.worstsubstr.keys[2]}
			                  ],
			"m"				: {"length": d.data.worstsubstr.keys.length, "keys": d.data.worstsubstr.keys, "url": {"links": null, "key": 0}}, // columns
			"n"				: {"length": d.data.worstsubstr[d.data.bestsubstr.keys[0]].length, "keys": null}, // rows
			"title"			: d.data.worstsubstr.title
		});

		if(callback)
			callback();
		
	});

}

// Updates the widget.
function updateKeywordInsight(id) {
	getKeywordInsightData(id); 
}


