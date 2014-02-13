/**
 * graph.js
 * 
 * jQuery function that attaches a grpah to a specified element.
 * 
 */

(function ($) {
	
	/* global variables */
	var defaults = { // default parameter values
			'startIndex': 0,
			'endIndex': 0,
			'heightProportion': 1.0,
			'widthProportion': 1.0,
			'xScale': 10,
			'yScale': 10,
			'showLines': false,
			'hideYaxis': false,
			'pointSize': 0
	}; 
	
	/* function declaration */
	$.fn.graph = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this); // get a jQuery object of the element graph is attached too
			var settings = $.extend({}, defaults, params); // we are integrating our default values into our params.
			var elementId = this.id;
			var w = $this.width() * settings.widthProportion;
			var h = $this.height() * settings.heightProportion;
			
			// the graph SVG with all properties
			var vis = { 
					"d3": d3.select(this).append("svg:svg").attr({ // svg as a d3 object
							"width": w, 
							"height": h,
							"id": elementId + "Graph"
						}),
						
					"jQuery": $("#" + elementId + "Graph").css('background', '#fff'), //svg as a jQuery object
					
					"id": elementId + "Graph"  //id of svg
			};
			
			if (!settings.endIndex)
				settings.endIndex = settings.data.length;
			
			// initial graph upon load
			drawGraph(settings.data, settings.startIndex, settings.endIndex);
			
			
			function drawGraph(data, startIndex, endIndex) {

				// Base Cases
				if (endIndex >= data.length || endIndex < startIndex || endIndex - startIndex < 4)
					return;
				
				vis.d3.selectAll('g').remove();
				
				// parse out data of date range.
				var showData = data.slice(startIndex, endIndex);
				
				// get date range
				var minDate = getDate(showData[0]),
					maxDate = getDate(showData[showData.length - 1]);
				
				// get Y range
				var yMax = d3.max(showData, function(d){return d.jsonHitCount; });
				
				// Data Points
				var x = d3.time.scale().domain([minDate, maxDate]).range([0, w]),
			    	y = d3.scale.linear().domain([0, yMax]).range([h - 20, 20]);
				
				var graph = vis.d3.data([showData]).append("g").attr("transform", "translate(0, 0)");
				
				//$.extend(settings, graph);
				
				// create X-Axis
				var xAxis = d3.svg.axis().scale(x).ticks(d3.time.days, 2).tickFormat(d3.time.format('%b %d')).orient("bottom");
				
				// create Y-Axis
				if (!settings.hideYaxis) {
					
					var yAxis = d3.svg.axis().scale(y).tickSize(w)
									  .tickFormat(function(d) { return d; }).orient("right");
									  
					var gy = graph.append("g").attr("class", "y axis").call(yAxis);
					
					gy.selectAll("g").filter(function(d) { return d; }).classed("minor", true);
					gy.selectAll("g").selectAll("text").attr({ "x": 4, "dy": -4 });
					
				}
				
				graph.append("g").attr({
					"class": "x axis",
					"transform": "translate(0," + (h - 20) + ")"
				}).call(xAxis);
				
				// Draw Graph
				graph.append("svg:path").attr({
					"class": "line",
					"d": d3.svg.line().x(function(d) { return x(getDate(d)); })
									  .y(function(d) { return y(d.jsonHitCount); })
				});
				
				if (settings.pointSize) {
					graph.selectAll("circle.line").data(showData)
						 .enter().append("svg:circle")
						 .attr({
							 "class": "line",
							 "cx": function(d) { return x(getDate(d)); },
							 "cy": function(d) { return y(d.jsonHitCount); },
							 "r": settings.pointSize
						 });
						 
					graph.selectAll("circle.line").on("mouseover", function(d) {
								var point = {'x': $(this).attr("cx"), 'y': $(this).attr("cy")};
								showTooltip(point, d.jsonHitCount);
						}).on("mouseout", removeTooltip);
				}
				
			} // end drawGraph
			
			function showTooltip(point, value)
			{
				vis.d3.append("svg:text")
		        .attr({
		        	"class": "tooltip",
		        	"x": point.x,
		        	"y": point.y,
		        	"dy": ".31em",
		        	"transform": "translate(0 -10)" // adjust position around circle.
		        })
		        .style("font-size", "12px")
		        .style("font-family", "Helvetica")
		            //round to 2 decimal places
		        .text(value);
			}
			
			function removeTooltip()
			{
			     vis.d3.select(".tooltip").remove();
			}
			
			
			/* Graph adjusting events */
			
			// temp zoom function
			$this.on('click', function(){
				drawGraph(settings.data, --settings.startIndex, ++settings.endIndex);
			});
			
			
			

		});

	};
	
} (jQuery));