/**
 * bar.js
 */

(function ($) {
	/*registerKeyboardHandler = function(callback) {
		  d3.select(window).on("keydown", callback);  
		};*/
		
	/* global variables */
	var defaults = {
			'id'	: ''
	};
	
	/* function declaration */
	$.fn.bar = function (params) {
		return this.each(function() {
			var $this = $(this);
			var settings = $.extend({}, defaults, params);
			var sdata = settings.data;		
			
			var padding = 40; //padding for scale values
			var margin = {top: -10, right: 0, bottom: 20, left: 0};
			var width = $this.width();
			var height = $this.height()-margin.bottom;

			var svg = d3.select("#" + this.id).append("svg").attr("height", height).attr("width", width);
			var color = d3.scale.ordinal().range(["#a6cee3", "#1f78b4", "#b2df8a"]);
			//var c = ["#a6cee3", "#1f78b4", "#b2df8a"];

			//var numGroups = 5;
			//var numSeries = 3;
			
			// tooltip
			var tooltip = d3.select("#" + this.id).append("div").attr("class", "tooltip").style("opacity", 0);

			  var x0 = d3.scale.ordinal()
			      .rangeBands([0, width], 0.2);

			  var x1 = d3.scale.ordinal();

			  var y = d3.scale.linear()      
			      .range([0, height]);



			svg.append("g")
			  .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
			

			var pageNames = d3.keys(settings.data[0]).filter(function(key) { return key !== "page"; });
			settings.data.forEach(function(d) {
			  d.vals = pageNames.map(function(name) { return {name: name, value: +d[name]}; });
			});

			x0.domain(settings.data.map(function(d) { return d.page; }));
			x1.domain(pageNames).rangeRoundBands([0, x0.rangeBand()]);
			y.domain([0, 120]);


			var pageP = svg.selectAll(".page")
			      .data(settings.data)
			    .enter().append("g")
			      .attr("class", "g")
			      .attr("transform", function(d) { return "translate(" + x0(d.page) + ",0)"; });

			pageP.selectAll("rect").data(function(d) {
				return d.vals;
			}).enter().append("rect").attr("width", x1.rangeBand()).attr("x",
					function(d) {
						return x1(d.name);
					}).attr("y", function(d) {
				return y(d.value);
			}).attr("height", function(d) {
				return height - y(d.value);
			}).style("fill", function(d) {
				return color(d.name);
			})
					.on(
							"mouseover",
							function(d) {
								tooltip.transition().duration(200).style(
										"opacity", .9);
								tooltip.html("(" + d.name + "%" + ")").style(
										"font-size", "15px").style("left",
										(d3.event.pageX + 5) + "px").style(
										"top", (d3.event.pageY - 28) + "px");
							}).on("mouseout", function(d) {
						tooltip.transition().duration(500).style("opacity", 0);
					});

			// Add y axis
			var yMax = 100;
			var yScale = d3.scale.linear()
			  .domain([0, yMax])
			  .range([height-5, padding]);

			var yAxis = d3.svg.axis().scale(yScale).orient("left");
			svg.append("g").attr("class", "axis")
			  .attr("transform", "translate(" + 25 + ",0)")
			  .text(function(pages) {return pages;})
			  .call(yAxis);

			// Add x axis
			var xAxis = d3.svg.axis()
				.scale(x0)
				.tickSize(5,1)
				.tickValues(settings.data.map(function(d) { return d.page; }))
				.orient("bottom");
			
			svg.append("g").attr("class", "axis")
			  .attr("transform", "translate(25," + (height-margin.bottom) + ")")
			  .call(xAxis);
			
			// Legend
			var legend = svg.selectAll(".legend")
			      .data(pageNames)
			    .enter().append("g")
			      .attr("class", "legend")
			      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });

			  legend.append("rect")
			      .attr("x", width - 18)
			      .attr("width", 18)
			      .attr("height", 18)
			      .style("fill", color);

			  legend.append("text")
			      .attr("x", width - 24)
			      .attr("y", 9)
			      .attr("dy", ".35em")
			      .style("font-size","15px")
			      .style("text-anchor", "end")
			      .text(function(d) { return d; });

			// add legend label
			svg.append("g")
			    .append("text")
			      .attr("transform", "rotate(-90)")
			      .attr("y",30)
			      .attr("x", -50)
			      .attr("dy", ".71em")
			      .style("font-size","15px")
			      .style("text-anchor", "end")
			      .text("Percentage");

		
		});
	};
}(jQuery));
