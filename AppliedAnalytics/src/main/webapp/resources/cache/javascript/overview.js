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
					$("#" + id + " a.totals1").click(function(e) { changeViewBtn(id, "overviewTotals1"); });
					$("#" + id + " a.totals2").click(function(e) { changeViewBtn(id, "overviewTotals2"); });

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
		
		$("#" + id + " .spinner-content").hide();
		$("#" + id + " .help").tooltip({ content: d.description });

		if (d.data.noData) {
			// TODO: Handle null data scenario.
			console.log("No data for overview");
			
			// show empty table
			$("#" + id + " #overviewTotals1").table({
				"data": [],
				"id" : id,
				"columnHeaders" : [
				                   {"name" : d.data.total.keys[0]}, // change to channel property in totals property.
				                   {"name" : d.data.total.keys[3]},
				                   {"name" : d.data.total.keys[2]},
				                   {"name" : d.data.total.keys[1]},
				                   {"name" : d.data.total.keys[4]}
				                  ],
				"title"			: d.data.total.title1,
				"columnLines"	: 2,
				"oLanguage": { "sEmptyTable": "Nothing to report here for now!" }
			}).show();
			
			$("#" + id + " #overviewTotals2").table({
				"data": [],
				"id": id,
				"columnHeaders" : [
				                   {"name" : d.data.total.keys[0]},
				                   {"name" : d.data.total.keys[3]},
				                   {"name" : d.data.total.keys[4]},
				                   {"name" : d.data.total.keys[5]},
				                   {"name" : d.data.total.keys[6]}
				                  ],
	           "title"			: d.data.total.title2,
	           "columnLines"	: 3,
	           "oLanguage": { "sEmptyTable": "Nothing to report here for now!" }
			});
			
			
			
			// Even though there is no data, we still need to fire off 
			// the callback if its in place otherwise when we change to an
			// account with data. We don't have our controls.
			if (callback)
				callback();

			return;
		}



		

		//console.log(d.data);
		// Create each table view

		// add the key for channels into this array.
		var totalKeys1 = [d.data.total.keys[0], d.data.total.keys[3], d.data.total.keys[2], d.data.total.keys[1], d.data.total.keys[4]];
		
		$("#" + id + " #overviewTotals1").table({
			"data": d.data.total,
			"id": id,
			"columnHeaders" : [
			                   {"name" : d.data.total.keys[0]}, // change to channel property in totals property.
			                   {"name" : d.data.total.keys[3]},
			                   {"name" : d.data.total.keys[2]},
			                   {"name" : d.data.total.keys[1]},
			                   {"name" : d.data.total.keys[4]}
			                  ],
           "m"			    : {"length": totalKeys1.length, "keys": totalKeys1 },
           "n"				: {"length": d.data.total[d.data.total.keys[0]].length, "keys": null}, // rows
           "title"			: d.data.total.title1,
           "columnLines"	: 2
		}).show();

		var totalKeys2 = [ d.data.total.keys[0], d.data.total.keys[3], d.data.total.keys[4], d.data.total.keys[5], d.data.total.keys[6]];
		$("#" + id + " #overviewTotals2").table({
			"data": d.data.total,
			"id": id,
			"columnHeaders" : [
			                   {"name" : d.data.total.keys[0]},
			                   {"name" : d.data.total.keys[3]},
			                   {"name" : d.data.total.keys[4]},
			                   {"name" : d.data.total.keys[5]},
			                   {"name" : d.data.total.keys[6]}
			                  ],
           "m"				: {"length": totalKeys2.length, "keys": totalKeys2}, // columns
           "n"				: {"length": d.data.total[d.data.total.keys[0]].length, "keys": null}, // rows
           "title"			: d.data.total.title2,
           "columnLines"	: 3
		});

		if(callback)
			callback();

	});



}

//Updates the widget.
function updateOverview(id) {
	getOverviewData(id, function() {
		$("#" + id + " .overviewVisual").hide();
		$("#" + id + " .overviewVisual.active").show();
	}); 
}


