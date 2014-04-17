
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
			console.log("No response from Keyword Insight");
			return;
		}

		var d = $.parseJSON(response);
		
		if (d.data.noData) {
			
			console.log("No data for keywordInsight");
			nullDataView(id, $("#" + id + " #keywordInsightScatter"));
			createNullTables(id, $("#" + id + " #keywordInsightImprove"), d.data.improve, 2);
			createNullTables(id, $("#" + id + " #keywordInsightBest"), d.data.best, 2);	
			createNullTables(id, $("#" + id + " #keywordInsightWorst"), d.data.worst, 2);		
			createNullTables(id, $("#" + id + " #keywordInsightAll"), d.data.all, 2);
			createNullTables(id, $("#" + id + " #keywordInsightBestSub"), d.data.bestsubstr, 2);
			createNullTables(id, $("#" + id + " #keywordInsightWorstSub"), d.data.worstsubstr, 2);
			
			
			// Even though there is no data, we still need to fire off 
			// the callback if its in place otherwise when we change to an
			// account with data. We don't have our controls.
			if (callback)
				callback();

			return;
		}

		var sdata = [];
		
			for (var i = 0; i < d.data.scatter.allKeywords.length; i++) {
				sdata.push({
					key: d.data.scatter.allKeywords[i],
					values: []
				});
	
				sdata[i].values.push({
					x: d.data.scatter.allBounceRate[i],
					y: d.data.scatter.allVisitsPercent[i],
					group: d.data.scatter.allGroups[i]
					//size: .4,   //Configure the size of each scatter point
					//shape: "circle"  //Configure the shape of each scatter point.
				});
			} 
	
			$("#" + id + " .spinner-content").hide();
			
			$("#" + id + " .help").tooltip({ content: d.description });
			
	
			// scatter view
			$("#" + id + " #keywordInsightScatter").empty().scatter({
				"id"	: id,
				"xLabel": d.data.scatter.label[0],
				"yLabel": d.data.scatter.label[1],
				"xKey" 	: d.data.scatter.keys[0],
				"yKey"	: d.data.scatter.keys[1],
				"data"	: sdata
			});

		
		createKITable(id, $("#" + id + " #keywordInsightImprove"), d.data.improve, null, d.data.paidimprove, 2);
		createKITable(id, $("#" + id + " #keywordInsightBest"), d.data.best, null, d.data.paidbest, 2);	
		createKITable(id, $("#" + id + " #keywordInsightWorst"), d.data.worst, null, d.data.paidworst, 2);		
		createKITable(id, $("#" + id + " #keywordInsightAll"), d.data.all, null, d.data.paidall, 2, true);
		createKITable(id, $("#" + id + " #keywordInsightBestSub"), d.data.bestsubstr, null, d.data.paidbestsubstr, 2);
		createKITable(id, $("#" + id + " #keywordInsightWorstSub"), d.data.worstsubstr, null, d.data.paidworstsubstr, 2);


		if(callback)
			callback();

	});

}


function createKITable(id, $tableDiv, data, urls, paidData, colLines, search) {
	
	var headers = function() {
		var result = [];
		for(var i = 0; i < data.keys.length; i++)
			result.push({name: data.keys[i]});
		
		return result;
	};
	
	$tableDiv.children(".organic").empty().table({
		"data"			: data,
		"id"		 	: id,
		"elementId"		: $tableDiv.attr("id"),
		"columnHeaders" : headers(),
        "m"			    : {"length": data.keys.length, "keys": data.keys, "url": urls}, // columns
        "n"				: {"length": data[data.keys[0]].length, "keys": null}, // rows
        "title"			: data.title,
        "search"		: search,
        "subClass"		: "organic",
        "columnLines"	: colLines || 1,
        "dom"			: paidData ? { "cName": "tableXtra", "content" : $("<input>").attr({"type": "button", "class": "xelement paidbtn", "value": "View Paid"}), } 
        						   : null
	});
	
	if (paidData) {
		$tableDiv.children(".paid").empty().table({
			"data"			: paidData,
			"id"		 	: id,
			"elementId"		: $tableDiv.attr("id"),
			"columnHeaders" : headers(),
	        "m"			    : {"length": paidData.keys.length, "keys": paidData.keys, "url": urls}, // columns
	        "n"				: {"length": paidData[paidData.keys[0]].length, "keys": null}, // rows
	        "title"			: paidData.title,
	        "search"		: search,
	        "subClass"		: "paid",
	        "columnLines"	: colLines || 1,
	        "dom"			: { "cName": "tableXtra", "content" : $("<input>").attr({"type": "button", "class": "xelement paidbtn", "value": "View Organic"}) }
		});
		
		// setup paid/organic switch event.
		$tableDiv.find("input.paidbtn").on("click", function() {
			var active = $tableDiv.children(".active").removeClass("active").attr("class");
			
			(active == "organic") ? $tableDiv.children(".paid").addClass("active")
								  : $tableDiv.children(".organic").addClass("active");
		});
		
	}
	
}

function createNullTables(id, $tableDiv, data, colLines) {
	
	var headers = function() {
		var result = [];
		for(var i = 0; i < data.keys.length; i++)
			result.push({name: data.keys[i]});
		
		return result;
	};
	
	$tableDiv.children(".organic").empty().table({
		"data"			: [],
		"id"		 	: id,
		"elementId"		: $tableDiv.attr("id"),
		"columnHeaders" : headers(),
        "title"			: data.title,
        "subClass"		: "organic",
        "columnLines"	: colLines || 1,
        "oLanguage": { "sEmptyTable": "Nothing to report here for now!" }
	});
}



//Updates the widget.
function updateKeywordInsight(id) {
	getKeywordInsightData(id, function() {
		$("#" + id + " .keywordVisual.active").show();
	}); 
}

