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
			var sdata = settings.data;			

			var margin = {
				"top" : 20,
				"right" : 15,
				"bottom" : 50,
				"left" : 60
			};
			var width = $this.width();
			var height = $this.height();			
			// tooltip
			//var tip = d3.select("#" + settings.id).append("div")
			//.attr("class", "tip").style("opacity", 0);


			var svg = d3.select("#"+settings.id).append("svg").attr("height", height).attr("width", width);


			// get size of object sdata
			Object.size = function(obj) {
			    var size = 0, key;
			    for (key in obj) {
			        if (obj.hasOwnProperty(key)) size++;
			    }
			    return size;
			};
			var len = Object.size(sdata);	

			var chart;
			nv.addGraph(function() {
			  chart = nv.models.scatterChart()
			                //.showDistX(true)
			                //.showDistY(true)
			                .useVoronoi(false)
			                .color(d3.scale.category10().range())
			                .transitionDuration(300)
			                ;

			  chart.xAxis.axisLabel("Bounce Rates (%)");
			  chart.yAxis.axisLabel("Multipage Visit Rates (%)");
			  chart.xAxis.tickFormat(d3.format('.02f'));
			  chart.yAxis.tickFormat(d3.format('.02f'));


			  //tooltip content
			  chart.tooltipContent(function(key, y, e, graph) {
			        var x = String(graph.point.x);
			        var y = String(graph.point.y);
			        var key = String(graph.point.kw);
			        tooltip_str = '<center><b>'+key+'</b></center>';
			        return tooltip_str;
			    });


			  //append data to chart
			  svg.datum(formatData(sdata))
			      .call(chart);

			  nv.utils.windowResize(chart.update);

			  return chart;
			});


			function getKeywords(sdata){
				var words = [];
				for(var i=0; i < len; i++){						  
					  words[i] = sdata[i].key;
				  }
				return words;
			}

			function formatData(sdata) { 
			  console.log(sdata)
			  var data = [];
			  var xvals = [];
			  var yvals = [];
			  var words = [];		

			  for(var i=0; i < len; i++){						  
				  xvals[i] = sdata[i].values[0].x;
				  yvals[i] = sdata[i].values[0].y;
				  words[i] = sdata[i].key;
			  }
			  /* accessing data : */
				// keywords -- sdata[index].key
				// values BOUNCE RATE -- sdata[index].values[0].x
				// values VISITS -- sdata[index].values[0].y

			  for (var i = 0; i < 1; i++) {
				    data.push({
				      key: 'keywords',
				      values: []
				    });

				    for (var j = 0; j < len; j++) {
				      data[i].values.push({x: xvals[j], y: yvals[j], kw: words[j]});
				    }
				  }

			  	  console.log(data)
				  return data;
				}


			/*******************************************************************
			 * GRAPH PROPERTIES *
			 ******************************************************************/

			/*
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
			
			// first color scale
			var color = d3.scale.category20();
						
			
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
					.style("fill", function(d) { return color(d[2]); })
					.on("mouseover", function(d) {	
						d3.select(this).transition().attr("r", 10);
						tip.transition()
			               .duration(200)
			               .style("opacity", .9)
			               .attr("font-size", "10px");
						tip.html(d[2])
						   	.style("left", (this.pageX) + "px")
						   	.style("bottom", (d3.pageY) + "px");
			      })
			      .on("mouseout", function(d) {		
			    	  d3.select(this).transition().attr("r", 5);
			          tip.transition()
		               .duration(500)
		               .style("opacity", 0);
			      }); 
			
			// remove duplicates
		    var legend_data = sdata.filter(function(elem, pos) {
		        return sdata.indexOf(elem) == pos;
		    });
			// Legend
			var legend = svg.selectAll(".legend")
			      .data(legend_data)
			    .enter().append("g")
			      .attr("class", "legend")
			      .attr("transform", function(d, i) { return "translate(0," + i * 10 + ")"; });

			  legend.append("rect")
			      .attr("x", width - 18)
			      .attr("width", 18)
			      .attr("height", 18)
			      .style("fill", function(d) { return color(d[2]); })
			      .on("mouseenter", function(d) {	
						tip.transition()
			               .duration(200)
			               .style("opacity", .9)
			               .attr("font-size", "10px");
						tip.html(d[2])
						//.style("left", (d3.event.pageX+300) + "px")
			            //.style("top", (d3.event.pageY-150) + "px");
			      })
			      .on("mouseleave", function(d) {
			          tip.transition()
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
			    .attr("x", (width-padding)-10)
			    .attr("y", height-10)  
			    .text("Bounce Rate (%)");    //("Percentage of Bounce Rates");

			// y axis
			svg.append("text")
			    .attr("class", "ylabel")
			    .attr("text-anchor", "middle")
			    .attr("x", -1 * (height/2))
			    .attr("y", 15)
			    .attr("transform", "rotate(-90)")
			    .text("Webpage Visits (%)"); //("Percentage of Webpage Visits"); */

		});
	}; 

}(jQuery));
