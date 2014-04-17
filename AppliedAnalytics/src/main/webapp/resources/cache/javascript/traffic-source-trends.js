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
						
						getTrafficSourceTrendsData($element, function(e) {
							
						
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
				var slopeGraphs = new Array();
				var slopeGraphSlopes = new Array();
				//Convert dataRows to raw data
				rawData = new Array();
				var avgDaysInMonth = 30.436875;
				for(i in dataRows) {
					changePerMonth = Math.round(dataRows[i]['slope'] * avgDaysInMonth * 10) / 10;
					variancePerMonth = Math.round(dataRows[i]['confidenceHalfWidth'] * avgDaysInMonth * 10) / 10;
					lowerBound = Math.round((changePerMonth - variancePerMonth)*10) / 10;
					upperBound = Math.round((changePerMonth + variancePerMonth)*10) / 10;
					
					if (Math.abs(changePerMonth) > 1 && Math.abs(changePerMonth) - variancePerMonth > 0) {
						row = new Array();
						row.push(dataRows[i]['sourceName']);
						/*
						var element = document.createElement("div");
						$(element).prop("id", "tst-row-" + i);
						*/
						slopeGraphs.push(i);
						slopeGraphSlopes.push(changePerMonth);
						row.push("<div id='tst-row-" + i + "'></div>");
						if (changePerMonth > 0)
							row.push(lowerBound + " to " + upperBound + " more visits");
						else
							row.push(upperBound + " to " + lowerBound + " less visits");
						//row.push(changePerMonth + " ±" + variancePerMonth + " " + metric);
						rawData.push(row);
					}
				}
				rawData = rawData.sort(function(row1,row2) {
					return parseFloat(row1[1]) - parseFloat(row2[1]);
				});
				
				$("#" + id + " .spinner-content").hide();
				$("#" + id + " .help").tooltip({ content: dataModel.description });
				
				$("#" + id + " #trafficSourceTrendsBest").table({
					"data"		: dataRows,
					"id"		: id,
					"rawData"	: rawData,
					"columnHeaders" : [
					                   {"name" : "Source"}, 
					                   {"name" : "Visual Trend"}, 
					                   {"name" : "Change in " + metric + " per month"}
					                  ],
					"m"				: {"length": 3}, // columns
					"n"				: {"length": dataRows.length, "keys": null}, // rows
					"title"			: dataModel.name,
					"sorting"		: [[2, 'desc']],
					"oLanguage": { "sEmptyTable": "Nothing to report here for now!" }
				}).show();
				
				for(index in slopeGraphs) {
					canvas = drawSlope(slopeGraphSlopes[index], $("#" + id + " #tst-row-" + slopeGraphs[index]), 50, 50, 2);
					$(canvas).css("border", "2px solid #666");
					$(canvas).css("border-radius", "10px");
					$("#tst-row-" + slopeGraphs[index]).css("margin", "auto");
				}
				
				$.each($("table.dataTable thead tr[role='row'] th"), function(index, element) {
					if (index != 0)
						$(element).css("text-align", "center");
				})

		});	
	
}


function updateTrafficSourceTrends(id, callback) {
	getTrafficSourceTrendsData(id, function() {
		$("#" + id + " .trafficSourceVisual.active").show();
	});
	//getTrafficSourceTrendsData($("#" + id));
}
