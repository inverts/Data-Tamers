/**
 * overview.js
 */

function loadOverview(id, callback) {

	var $element = $("#" + id);
	$.post(applicationRoot + "Overview", null, 
			function(response) {

		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
				$element.html(response); 

				// Get the data and setup widget functions after data has been retrieved.
				getOverviewData(id, function() {

					// direct view buttons
					$("#" + id + " a.totals").click(function(e) { changeViewBtn(id, "overviewTotals"); });
//					$("#" + id + " a.channels").click(function(e) { changeViewBtn(id, "overviewChannels"); });

					$element.data("hasData", true); // flag the widget as having data.

				});

				if(callback)			
					callback();
			});
		}
		else {
			console.error("could not append Overview Widget to id: " + id);
		}

		$("#" + id + " .dropdown-menu").attr("id", id);
		$a = $("<a>").attr({"role": "menu-item", "class": "viewOption"});
		$li = $("<li>").append($a);
		$("#" + id + " .dropdown-menu").append($li);

	});		
}


function getOverviewData(id, callback) {
	$.post(applicationRoot + "Overview", {"serialize": 1}, function(response) {

		// if no data display error message
		if (response == null) {
			console.log("Data from Overview model is null");
			return;
		}

		var d = $.parseJSON(response);

		if (d.data === null || d.data === undefined) {
			// TODO: Handle null data scenario.
			console.log("No data for overview");
			
			// Even though there is no data, we still need to fire off 
			// the callback if its in place otherwise when we change to an
			// account with data. We don't have our controls.
			if (callback)
				callback();

			return;
		}



		$("#" + id + " .spinner-content").hide();
		
		$("#" + id + " .help").tooltip({ content: d.description });

		console.log(d.data);
		// Create each table view

		$("#" + id + " #overviewTotals").table({
			"data": d.data.totals.data,
			"columnHeaders" : [
			                   {"name" : d.data.totals.keys[0], "width": "45%"}, 
			                   {"name" : d.data.totals.keys[1]},
			                   {"name" : d.data.totals.keys[2]},
			                   {"name" : d.data.totals.keys[3]},
			                   {"name" : d.data.totals.keys[4]},
			                   {"name" : d.data.totals.keys[5]}
			                   ],
			                   "m"			    : {"length": d.data.totals.keys.length, "keys": d.data.totals.keys, "url": {"links": null, "key": 0}}, // columns
			                   "n"				: {"length": d.data.totals.data[0].length, "keys": null}, // rows
			                   "title"			: d.data.totals.title
		});

/*		
		$("#" + id + " #overviewChannels").table({
			"data": d.data.best,
			"columnHeaders" : [
			                   {"name" : "", "width": "45%"}, 
			                   {"name" : ""},
			                   {"name" : ""},
			                   {"name" : ""}
			                   ],
			                   "m"				: {"length": d.data.best.keys.length, "keys": d.data.best.keys, "url": {"links": null, "key": 0}}, // columns
			                   "n"				: {"length": d.data.best[d.data.best.keys[0]].length, "keys": null}, // rows
			                   "title"			: d.data.best.title
		});
*/

		if(callback)
			callback();

	});



}

//Updates the widget.
function updateOverview(id) {
	getOverview(id, function() {
		$("#" + id + " .overviewVisual.active").show();
	}); 
}


