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
			
			getPagePerformanceData(id, function() {
				
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
		createTableView("pagePerformanceTable", d.data);
		
		
	});
}

function updatePagePerformance(id){
	
}

	    