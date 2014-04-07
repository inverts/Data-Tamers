/**
 * traffic-source-trends.js
 * 
 * TODO: Widgets should be loaded by their actual id in the database in the event
 * that we have widget-specific saved properties that are displayed in the future. 
 */

/**
 * @param id the html DOM element id to place this data into.
 */
function loadTrafficSourceTrendsWidget(id, callback) {
	var $element = $('#' + id);
	$.get(applicationRoot + "widgets/traffic-source-trends", null, 
		function(response) {
			if ($element.length > 0) {
				$element.fadeIn("fast", function() { 
						$element.html(response);
						
						getTrafficSourceTrendsData($element, function() {
							$element.data("hasData", true); // flag the widget as having data.
						});
						
						if(callback)			
							callback();
				});
			}
			else
				console.error('Could not append Traffic Source Trends to id: ' + id);

	});	
}


function getTrafficSourceTrendsData($element, callback) {

	$.get(applicationRoot + "widgets/traffic-source-trends/data", null, 
			function(response) {
				dataModel = $.parseJSON(response);
				dataRows = dataModel.trafficSourceDataList;
				metric = dataModel.metric.substr(3);
				
				var id = $element.attr("id");
				//Convert dataRows to raw data
				rawData = new Array();
				
				for(i in dataRows) {
					changePerMonth = Math.round(dataRows[i]['slope'] * 30.436875 * 10) / 10;
					variancePerMonth = Math.round(dataRows[i]['confidenceHalfWidth'] * 30.436875 * 10) / 10;
					if (Math.abs(changePerMonth) > 1 && Math.abs(changePerMonth) - variancePerMonth > 0) {
						row = new Array();
						row.push(dataRows[i]['sourceName']);
						row.push(changePerMonth + " ±" + variancePerMonth + " " + metric);
						rawData.push(row);
					}
				}
				rawData = rawData.sort(function(row1,row2) {
					return parseFloat(row1[1]) - parseFloat(row2[1]);
				});
				
				$("#" + id + " .spinner-content").hide();
				
				$("#" + id + " #trafficSourceTrendsBest").table({
					"data"		: dataRows,
					"rawData"	: rawData,
					"columnHeaders" : [
					                   {"name" : "Source"}, 
					                   {"name" : "Change in " + metric + " per month"}
					                  ],
					"m"				: {"length": 3}, // columns
					"n"				: {"length": dataRows.length, "keys": null}, // rows
					"title"			: dataModel.name,
					"sorting"		: [[1, 'desc']]
				}).show();

				//$("#" + $element.attr("id")).show();
		});	
	
}


function updateTrafficSourceTrends(id, callback) {
	
	getTrafficSourceTrendsData($("#" + id));
}
