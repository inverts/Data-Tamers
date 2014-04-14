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
		for (var i = 0; i < d.data.scatter.allKeywords.length; i++) {
			sdata.push({
				key: d.data.scatter.allKeywords[i],
				values: []
			});

			sdata[i].values.push({
				x: d.data.scatter.allBounceRate[i],
				// y: d.data.scatter.allMultipageVisitsPercent[i],
				y: d.data.scatter.allVisitsPercent[i],
				size: .4,   //Configure the size of each scatter point
				shape: "circle"  //Configure the shape of each scatter point.
			});
		} 

		$("#" + id + " .spinner-content").hide();
		
		$("#" + id + " .help").tooltip({ content: d.description });
		

		// scatter view
		$("#" + id + " #keywordInsightScatter").empty().scatter({
			"id"	: "keywordInsightScatter",
			"xLabel": d.data.scatter.label[0],
			"yLabel": d.data.scatter.label[1],
			"xKey" 	: d.data.scatter.keys[0],
			"yKey"	: d.data.scatter.keys[1],
			"data"	: sdata
		});

		
		createKITable($("#" + id + " #keywordInsightImprove"), d.data.improve, null, d.data.paidimprove);

		/*$("#" + id + "#keywordInsightImprove div.tableCheckBox").change(function() {
			if ($(this).is(":checked"))
				createKITable($("#" + id + " #keywordInsightImprove"), d.data.paidimprove, null, d.data.paidimprove);
			else
				createKITable($("#" + id + " #keywordInsightImprove"), d.data.improve, null, d.data.paidimprove);
		});*/
		

		createKITable($("#" + id + " #keywordInsightBest"), d.data.best, null, d.data.paidbest);	
		createKITable($("#" + id + " #keywordInsightWorst"), d.data.worst, null, d.data.paidworst);		
		createKITable($("#" + id + " #keywordInsightAll"), d.data.all, null, d.data.paidall, true);
		createKITable($("#" + id + " #keywordInsightBestSub"), d.data.bestsubstr, null, d.data.paidsubstr);
		createKITable($("#" + id + " #keywordInsightWorstSub"), d.data.worstsubstr, null, d.data.paidworstsubstr);


		if(callback)
			callback();

	});

}


function createKITable($tableDiv, data, urls, paidData, search) {
	
	var headers = function() {
		var result = [];
		for(var i = 0; i < data.keys.length; i++)
			result.push({name: data.keys[i]});
		
		return result;
	};
	
	$tableDiv.children(".organic").table({
		"data"			: data,
		"id"			: $tableDiv.attr("id"),
		"columnHeaders" : headers(),
        "m"			    : {"length": data.keys.length, "keys": data.keys, "url": urls}, // columns
        "n"				: {"length": data[data.keys[0]].length, "keys": null}, // rows
        "title"			: data.title,
        "search"		: search,
        "subClass"		: "organic",
        "dom"			: paidData ? { "cName": "tableCheckBox", "content" : $("<input>").attr({"type": "button", "class": "xelement paidbtn", "value": "View Paid"}), } 
        						   : null
	});
	
	if (paidData) {
		$tableDiv.children(".paid").table({
			"data"			: paidData,
			"id"			: $tableDiv.attr("id"),
			"columnHeaders" : headers(),
	        "m"			    : {"length": paidData.keys.length, "keys": paidData.keys, "url": urls}, // columns
	        "n"				: {"length": paidData[paidData.keys[0]].length, "keys": null}, // rows
	        "title"			: paidData.title,
	        "search"		: search,
	        "subClass"		: "paid",
	        "dom"			: { "cName": "tableCheckBox", "content" : $("<input>").attr({"type": "button", "class": "xelement paidbtn", "value": "View Organic"}) }
		});
		
		// setup paid/organic switch event.
		$tableDiv.find("input.paidbtn").on("click", function() {
			var active = $tableDiv.children(".active").removeClass("active").attr("class");
			
			(active == "organic") ? $tableDiv.children(".paid").addClass("active")
								  : $tableDiv.children(".organic").addClass("active");
		});
		
	}
	
	

	
}



//Updates the widget.
function updateKeywordInsight(id) {
	getKeywordInsightData(id, function() {
		$("#" + id + " .keywordVisual.active").show();
	}); 
}


