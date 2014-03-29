/**
 * website-performance.js
 */

function loadWebsitePerformanceWidget(id) {
	
	var $element = $('#' + id);
	
	$.post(applicationRoot + "WebsitePerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.fadeIn("fast", function() {
					$element.append(response);
				});
			else {
				console.log("Could not append PagePerformance");
			}
			
			getPagePerformanceData(id, function(e) {
				
				$("#" + id + " a.bar").click(function(e) { changeViewBtn(id, "pagePerformanceBar"); });
				$("#" + id + " a.pp-table").click(function(e) { changeViewBtn(id, "pagePerformanceTable"); });
			});

	});		
}

function getPagePerformanceData(id, callback){
	$.post(applicationRoot + "WebsitePerformance", {"serialize": 1}, function(response) {
		// if no data display error message
		
		if (response == null) {
			console.log("Data from Page Performance model is null");
			return;
		} 
		var d = $.parseJSON(response);
		
		if (!d.data) {
			console.log("No data for pagePerformance");		
			
			if (callback)
				callback();
			
			return;
		}
		
		var sdata = [{
		    "page": "/",
		    "visits": "65.7",
		    "bounces": "38.6",
		    "exits": "36.7"
		}, {
		    "page": "/funny-spanish-commercials/",
		    "visits": "13.4",
		    "bounces": "100",
		    "exits": "100"
		}, {
		    "page": "/interpreter-jobs/",
		    "visits": "10.4",
		    "bounces": "85.7",
		    "exits": "30.4"
		}, {
		    "page": "/contact/",
		    "visits": "6",
		    "bounces": "100",
		    "exits": "75"
		}, {
		    "page": "/the-greatest-radio-competition-of-all-time/",
		    "visits": "1.5",
		    "bounces": "100",
		    "exits": "100"
		}];
		
		// bar chart view
		$("#" + id + " #pagePerformanceBar").empty().bar({
			"id"	: "pagePerformanceBar",
			"data": sdata
		});
		
		// create table
		$("#" + id + " #pagePerformanceTable").table({
			"data": d.data,
			"columnHeaders" : [
			                   {"name" : d.data.keys[0], "width": "70%"}, 
			                   {"name" : d.data.keys[1]},
			                   {"name" : d.data.keys[2]},
			                   {"name" : d.data.keys[3]}
			                  ],
			"m"				: {"length": d.data.keys.length, "keys": d.data.keys}, // columns
			"n"				: {"length": d.data[d.data.keys[0]].length, "keys": null}, // rows
			"url"			: {"links": d.data.url, "cols": [0]},
			"search"		: true
		});
		
		//createTableView("pagePerformanceTable", d.data, 38, 0);
		
		if (callback)
			callback();

	});
}

function updatePagePerformance(id){
	
}

	    