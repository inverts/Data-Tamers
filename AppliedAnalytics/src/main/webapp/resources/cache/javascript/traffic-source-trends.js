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
	$.get(applicationRoot + "/widgets/traffic-source-trends", null, 
		function(response) {
			if ($element.length > 0) {
				$element.fadeIn("fast", function() { 
						$element.html(response); 
				});
			}
			else
				console.error('Could not append Traffic Source Trends to id: ' + id);
			
			getTrafficSourceTrendsData($element, function() {
				
				$element.data("hasData", true); // flag the widget as having data.
			});
			
			if(callback)			
				callback();
			
	});	
}


function getTrafficSourceTrendsData($element, callback) {

	$.get(applicationRoot + "widgets/traffic-source-trends/data", null, 
			function(response) {
				dataModel = $.parseJSON(response);
				dataRows = dataModel.trafficSourceDataList;
				var id = $element.attr("id");
				//Convert dataRows to raw data
				rawData = new Array();
				
				for(i in dataRows) {
					row = new Array();
					row.push(dataRows[i]['sourceName']);
					row.push(dataRows[i]['slope']);
					row.push(dataRows[i]['confidenceHalfWidth']);
					rawData.push(row);
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
					                   {"name" : "Slope"},
					                   {"name" : "Confidence"}
					                  ],
					"m"				: {"length": 3}, // columns
					"n"				: {"length": dataRows.length, "keys": null}, // rows
					"title"			: dataModel.name
				}).show();

				//$("#" + $element.attr("id")).show();
		});	
	
}


function updateTrafficSourceTrends(id, callback) {
	
	getTrafficSourceTrendsData($("#" + id));
}
