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
			
			//$("#" + id + " .keywordVisual").hide();
			
			var $parent = $("#" + id + " #keywordInsightData");
			
			// previous button
			$("#" + id + " .prev").click(function(e) {
				var $prev = $parent.children(".active").prev();
				$parent.children(".active").removeClass("active").hide("slide", {direction: "right"}, "fast", function() {
					($prev.length) ? $prev.addClass("active").show("slide", {direction: "left" }, "fast")
							       : $parent.children(".keywordVisual:last").show("slide", {direction: "left"}, "fast")
							       										     .addClass("active");
				});
				
			});
			
			// next button
			$("#" + id + " .next").click(function(e) {
				var $next = $parent.children(".active").next();
				$parent.children(".active").removeClass("active").hide("slide", {direction: "left"}, "fast", function() {
					($next.length) ? $next.addClass("active").show("slide", {direction: "right"}, "fast")
							       : $parent.children(".keywordVisual:first").show("slide", {direction: "right"}, "fast")
							       										     .addClass("active");
				});
			});	

			// direct view buttons
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

/* Directly switches a view within this widget */
function switchView(id, $parent, view) {
	$parent.children(".active").removeClass("active").hide();
	$("#" + id + " #" + view).show().addClass("active");
	
}

/* Generic method for constructing keyword tables with data */
function createTableView(id, data) {
	var $view = $("#" + id).empty().attr("style", "display:none;");
	
	// add sub-title
	$("<h4>").html("<b>" + data.title + "</b>").appendTo($view);
	
	var $table = $("<table>").addClass("view").appendTo($view);
	var $thead = $("<thead>").addClass("data-header").appendTo($table);
	var $tbody = $("<tbody>").addClass("data-body").appendTo($table);

	// setup column headers
	for (var i = 0; i < data.keys.length; i++) {
		var $th = $("<th>").html("<div>" + data.keys[i] + "</div>").appendTo($thead);
		if (i == 0)
			$th.addClass("words");
		else if (i == data.keys.length - 1)
			$th.addClass("last");
		else
			$th.css("width", (100 - 32) / (data.keys.length - 1) + "%");
	}

	var $tr = $("<tr>").appendTo($tbody);
	var $td = $("<td>").attr("colspan", data.keys.length).appendTo($tr);
	
	var $overflow = $("<div>").addClass("keywordTable").appendTo($td);
	var $tableData = $("<table>").addClass("data-table").appendTo($overflow);
	
	// access data via column headers as key names
	for (var i = 0; i < data[data.keys[0]].length; i++) {
		var tr = $("<tr>").appendTo($tableData);
		for (var j = 0; j < data.keys.length; j++) {
			var td = $("<td>").html("<div>" + data[data.keys[j]][i] + "</div>").appendTo(tr);
			if (j == 0)
				td.addClass("words");
			else if (j == data.keys.length - 1)
				td.addClass("last");
			else
				td.css("width", (100 - 32) / (data.keys.length - 1) + "%");
		}
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
		
		if (!d.data) {
			// TODO: Handle null data scenario.
			console.log("No data for keywordInsight");
			
			// Even though there is not data, we still need to fire off 
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


