/**
 *  scatter.js
 */

(function ($) {
	registerKeyboardHandler = function(callback) {
		  d3.select(window).on("keydown", callback);  
		};
		
	/* global variables */
	var defaults = {
			'id'	: '',
			'xLabel': '',
			'yLabel': '',
			'xKey' 	: '',
			'yKey'	: ''
	};
	
	/* function declaration */
	$.fn.scatter = function (params) {
		return this.each(function() {
			var $this = $(this);
			var settings = $.extend({}, defaults, params);
			if(settings.data == null){
				return;
			}

			var margin = {
				"top" : 20,
				"right" : 15,
				"bottom" : 50,
				"left" : 60
			};
			var width = 620 - margin.left - margin.right;
			var height = 370 - margin.top - margin.bottom;
			// tooltip
			var tooltip = d3.select("#" + settings.id).append("div")
					.attr("class", "tooltip").style("opacity", 0);

			/*******************************************************************
			 * GRAPH PROPERTIES *
			 ******************************************************************/
			var scatter = {
					"id"	:this.id + "Settings"				
			};
						
			var svg = d3.select("#"+settings.id).append("svg").attr("height", height).attr("width", width);
			svg.append("rect").style("fill", "#fff").attr("width", width).attr("height", height).attr("transform", "translate(" + margin.left, + "10)")
				.style("stroke", "#666").style("stroke-width", 1);
			var padding = 40; //padding for scale values

			var maxdataX = d3.max(sdata, function(d) {
				return d[0];
			});
			var maxdataY = d3.max(sdata, function(d){
				return d[1];
			});

			var xScale = d3.scale.linear()
				.domain([0, maxdataX])
				.range([padding, width-padding]);

			var yScale = d3.scale.linear()
				.domain([0, maxdataY])
				.range([height-padding, padding]);


			// make circles for each data point
			svg.selectAll("circle").data(sdata).enter()
				.append("circle")
					.attr("cx", function(d) {
						return xScale(d[0]);
					})
					.attr("cy", function(d) {
						return yScale(d[1]);
					})
					.attr("ke", function(d) {
						return yScale(d[2]);
					})
					.attr("r", 5)
					.on("mouseover", function(d) {
			          tooltip.transition()
			               .duration(200)
			               .style("opacity", .9);
			          tooltip.html(d[2]) //+ "(" + d[0] + ", " + d[1] + ")")
			               .style("left", (d3.event.pageX + 5) + "px")
			               .style("top", (d3.event.pageY - 28) + "px");
			      })
			      .on("mouseout", function(d) {
			          tooltip.transition()
			               .duration(500)
			               .style("opacity", 0);
			      });
					


			// add x axes (functions)
			ticknum = 10;
			var xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(ticknum);
			svg.append("g").attr("class", "scatter-axis")
				// clean up axis
				.attr("transform", "translate(0," + (height-padding) + ")")
				.call(xAxis);
		


			// add y axis (functions)
			var yAxis = d3.svg.axis().scale(yScale).orient("left").ticks(ticknum);
			svg.append("g").attr("class", "scatter-axis")
				.attr("transform", "translate(" + padding + ",0)")
				.call(yAxis);
				

			// add titles for x and y axis
			// x axis
			svg.append("text")
			    .attr("class", "xlabel")
			    .attr("text-anchor", "middle")
			    .attr("x", (width-padding)/2)
			    .attr("y", height-10)  
			    .text("Bounce Rate (%)");    //("Percentage of Bounce Rates");

			// y axis
			svg.append("text")
			    .attr("class", "ylabel")
			    .attr("text-anchor", "middle")
			    .attr("x", -1 * (height/2))
			    .attr("y", 15)
			    .attr("transform", "rotate(-90)")
			    .text("Webpage Visits (%)"); //("Percentage of Webpage Visits");

		});
	};
		
}(jQuery));





