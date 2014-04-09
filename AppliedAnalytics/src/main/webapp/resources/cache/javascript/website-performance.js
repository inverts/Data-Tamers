/**
 * website-performance.js
 */


function loadWebsitePerformanceWidget(id, callback) {
	
	var $element = $('#' + id);
	
	$.post(applicationRoot + "WebsitePerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.fadeIn("fast", function() {
					$element.html(response);
					
					getPagePerformanceData(id, function(e) {
						
						$("#" + id + " a.bar").click(function(e) { changeViewBtn(id, "pagePerformanceBar"); });
						$("#" + id + " a.pp-table").click(function(e) { changeViewBtn(id, "pagePerformanceTable"); });
					
						$element.data("hasData", true); // flag the widget as having data.
						
					});
					
					if(callback)				
						callback();
				});
			else {
				console.log("Could not append PagePerformance");
			}

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
		
		//
		//console.log(d.data[ d.data.keys[ 2 ] ][0]);
		//console.log(d.data["Visits (%)"]);
		
		
		if (!d.data) {
			console.log("No data for pagePerformance");		
			
			if (callback)
				callback();
			
			return;
		}

		$("#" + id + " .spinner-content").hide();
		
		// bar chart view
		$("#" + id + " #pagePerformanceBar").empty().bar({
			"id"	: "pagePerformanceBar",
			"data": d.data
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
			"title"         : d.data.title
		});
		
		//createTableView("pagePerformanceTable", d.data, 38, 0);
		
		if (callback)
			callback();

	});
}

function updatePagePerformance(id){
	getPagePerformanceData(id, function() {
		$("#" + id + " .pagePerformanceVisual.active").show();
	});
}

	    