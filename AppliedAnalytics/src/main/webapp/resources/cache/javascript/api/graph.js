/**
 * graph.js
 * 
 * jQuery function that attaches a grpah to a specified element.
 * 
 */

(function ($) {
	
	registerKeyboardHandler = function(callback) {
	  d3.select(window).on("keydown", callback);  
	};
	
	/* global variables */
	var defaults = { // default parameter values
			'startIndex': 0,
			'endIndex'	: 0,
			'dataSet'	: [],		// dataSet takes precedence over data
			'title'		: '',
			'xLabel'	: 'Test X',
			'yLabel'	: 'Test Y',
			'showLines'	: false,
			'hideYaxis'	: false,
			'pointSize'	: 0,
			'databuffer': 0,
			'rangeMin'	: 4
	}; 
	
	/* function declaration */
	$.fn.graph = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this).sortable({ cancel: '.widget-content'}); 	// disable widget drag on graph
			var settings = $.extend({}, defaults, params); 					// we are integrating our default values into our params.
			
			var padding = {
				     		 "top":    settings.title  ? 40 :20,			// padding required for graph labels
				    	     "right":  30,
				    	     "bottom": settings.xLabel ? 60 : 10,
				    	     "left":   settings.yLabel ? 70 : 45
				    	  };

			var width = $this.width(),										// svg takes on width and height of parent element.
				height = $this.height();
			
			//TODO: Find which data set has the largest date range; make graph based off its dimensions
			
			//if (settings.dataSet.length > 0)
			

			// graph object
			var graph = {
							"title"		: null,
							"data"		: settings.data,
							"view"		: {
											"width": width - padding.left - padding.right, 
											"height": height - padding.top - padding.bottom,
											"rect": null,
											"svg": null
										  },
							"x"			: { "domain": [], "point": function(){}, "axis": {"label": null, "svg": null}, "down": Math.NaN },
							"y"			: { "domain": [], "point": function(){}, "axis": {"label": null, "svg": null}, "down": Math.NaN },
							"size"		: { "width": 0, "height": 0 },
							"zoom"		: function() {}
						};
			

			graph.x.domain = [getValueBy(graph.data, "jsonDate", Math.min), getValueBy(graph.data, "jsonDate", Math.max)];
			graph.y.domain = [0, getValueBy(graph.data, "jsonHitCount", Math.max)];
			
			if (!settings.endIndex)											
				settings.endIndex = settings.data.length - 1;
			
			var from = getDate(graph.data[settings.startIndex]),		// static date range chosen.
			  	  to = getDate(graph.data[settings.endIndex ]);

			// Base Cases
			if (settings.endIndex >= settings.data.length || 
					settings.endIndex < settings.startIndex || 
					settings.endIndex - settings.startIndex < settings.rangeMin)
				return;

			
			graph.x.point = d3.time.scale().domain([from, to]).range([0, graph.view.width]); 		// function to get data points at x
			graph.y.point = d3.scale.linear().domain(graph.y.domain.reverse()).nice().range([0, graph.view.height]).nice();	// function to get data points at y

			// create a buffer region for the dates
			graph.x.domain = d3.time.scale().domain(graph.x.domain).nice().domain();

			// Compute width of entire graph
			graph.size.width  = graph.x.point(getDate(graph.data[graph.data.length - 1])),
			graph.size.height =  graph.y.point(graph.data[getIndexBy(graph.data, "jsonHitCount", Math.max)].jsonHitCount);

			// the graph SVG with all properties
			var vis = d3.select(this).append("svg:svg")
									 .attr({ 
											"width": width, 
											"height": height,
											"id": this.id + "Graph"
									 	   })
									 .append("g")
									 .attr("transform", "translate(" + padding.left + "," + padding.right + ")");
			
			// panning and zoom boundaries
			$.extend(settings, {constraint: { x: {
												  	min: graph.x.point(getDate(graph.data[0])) - settings.databuffer, 
												  	max: graph.view.width - graph.size.width + 2
												 }, 
											  y: {	min: 0,
												  	max: graph.size.height - settings.databuffer
												 }}});
			// Zoom limits
			var zoomOut = Math.round(graph.x.point(getDate(graph.data[graph.data.length - 1])) / graph.view.width),
				zoomIn = 0 - (graph.view.height / graph.size.height);
			
			graph.zoom = d3.behavior.zoom()
									.x(graph.x.point)
									.y(graph.y.point)
									.scaleExtent([zoomIn, zoomOut])
									.on("zoom", function() {
															var t = graph.zoom.translate(),
															tx = t[0],
															ty = t[1];
															
															// x - boundaries
															tx = Math.min(tx, 0 - settings.constraint.x.min);
															tx = Math.max(tx, settings.constraint.x.max);
															// y - boundaries
															ty = Math.min(ty, 0 - settings.constraint.y.max);
															ty = Math.max(ty, settings.constraint.y.min);
															
															// move graph
															graph.zoom.translate([tx, ty]);
															
															// redraw the graph that moved
															drawGraph()();
														});
			
			graph.view.rect = vis.append("rect")
									   .attr({
												"width": graph.view.width, 
												"height": graph.view.height
											})
									   .style({
										 		"stroke": "#CCC",
										 		"stroke-width": 1,
										 		"fill": "#FFF"
											 })
									   .attr("pointer-events", "all").on("mousedown.drag", pan())
									   .call(graph.zoom);
			
			
			graph.view.svg = vis.append("svg")
									  .attr({
												"top": 0,
												"left": 0,
												"width": graph.view.width,
												"height": graph.view.height,
												"viewBox": "0 0 " + graph.view.width + " " + graph.view.height,
												"class": "line"
											});
			
			
			// x axis label
			vis.append("g")
			   .attr({
				  		"class": "xaxis",
				  		"transform": "translate(0, " + graph.view.height + ")"
				  	 });
			
			 if (settings.xLabel) {
				 graph.x.axis.label = vis.append("text")
								         .attr("class", "axis")
								         .text(settings.xLabel)
								         .attr({"x": graph.view.width/2, "y": graph.view.height, "dy": "3.5em" })
								         .style("text-anchor","middle");
			 }
			

			
			//  y-axis label
			vis.append("g")
			   .attr({
				  		"class": "yaxis",
				  		"transform": "translate(0, 0)"
				  	 });
			
			if (settings.yLabel) {
			    graph.y.axis.label = vis.append("g").append("text")
								        .attr("class", "axis")
								        .text(settings.yLabel)
								        .style("text-anchor","middle")
								        .attr("transform","translate(" + -40 + " " + graph.view.height/2+") rotate(-90)");
			}
			

			// apply graph title
			if (settings.title) {
				    graph.title = vis.append("text")
							         .attr("class", "axis")
							         .text(settings.title)
							         .attr({ "x": graph.view.width/2, "dy": "-0.8em" })
							         .style("text-anchor","middle");
			}
			
			  
			  
			 d3.select(this)
		       .on("mousemove.drag", mousemove())
		       .on("mouseup.drag",   mouseup());
			  
			 drawGraph()(); // draw graph on DOM load
			  
			 function pan() {
				 return function() {
					 d3.select("body").style("cursor", "move");
				 };
			 };
			  
			 /**
			  * Redraws the points on the graph
			  */
			 function update() {
				  
				  d3.selectAll("path").remove(); // remove old graph points/lines
				  
				  graph.view.svg.data([graph.data])
				  					  .append("path")
				  					  .attr({
												"d": d3.svg.line().x(function(d) { return graph.x.point(getDate(d)); })
								  				  				  .y(function(d) { return graph.y.point(d.jsonHitCount); }),
								  				"pointer-events": "all"
				  					  		})
				  					  .on("mousedown.drag", pan())
				  					  .call(graph.zoom);
				  
				  // apply the circles
				  var circle = vis.select("svg").selectAll("circle")
				      							.data(function(d) { return d; });
				 
				  circle.enter().append("circle")
						        .attr("class", function(d) { return d === this.selected ? "selected" : null; })
						        .attr("cx",    function(d) { return graph.x.point(new Date(d.jsonDate)); })
						        .attr("cy",    function(d) { return graph.y.point(d.jsonHitCount); })
						        .attr("r", settings.pointSize)
						        .attr("pointer-events", "all").on("mousedown.drag", pan())
						        .call(graph.zoom);
				 
				  circle
				      .attr("class", function(d) { return d === this.selected ? "selected" : null; })
				      .attr("cx", function(d) { return graph.x.point(new Date(d.jsonDate)); })
				      .attr("cy", function(d) { return graph.y.point(d.jsonHitCount); });
				 
				  circle.exit().remove();
				 
				  if (d3.event && d3.event.keyCode) {
				    d3.event.preventDefault();
				    d3.event.stopPropagation();
				  }
				};

				function mousemove() {
					  return function() {
					    var p = d3.mouse(vis[0][0]);
					    
					    if (this.dragged) {
					    	this.dragged.y = y.invert(Math.max(0, Math.min(self.size.height, p[1])));
					    	update();
					    }
					    
					    if (!isNaN(graph.x.down)) {
					      d3.select("body").style("cursor", "ew-resize");
					      var rupx = graph.x.point.invert(p[0]),
					          xaxis1 = graph.x.point.domain()[0],
					          xaxis2 = graph.x.point.domain()[1],
					          xextent = xaxis2 - xaxis1;
					      if (rupx != 0) {
					        var changex, new_domain;
					        changex = downx / rupx;
					        new_domain = [xaxis1, xaxis1 + (xextent * changex)];
					        graph.x.point.domain(new_domain);
					        drawGraph()();
					      }
					      d3.event.preventDefault();
					      d3.event.stopPropagation();
					    };
					    
					    if (!isNaN(graph.y.down)) {
					      d3.select("body").style("cursor", "ns-resize");
					      var rupy = graph.y.point.invert(p[1]),
					          yaxis1 = graph.y.point.domain()[1],
					          yaxis2 = graph.y.point.domain()[0],
					          yextent = yaxis2 - yaxis1;
					      if (rupy != 0) {
					        var changey, new_domain;
					        changey = downy / rupy;
					        new_domain = [yaxis1 + (yextent * changey), yaxis1];
					        graph.y.point.domain(new_domain);
					        drawGraph()();
					      }
					      d3.event.preventDefault();
					      d3.event.stopPropagation();
					    }
					  }
					};
					
					function mouseup() {
						  return function() {
						    document.onselectstart = function() { return true; };
						    d3.select("body").style("cursor", "auto");
						    if (!isNaN(graph.x.down)) {
						      drawGraph()();
						      graph.x.down = Math.NaN;
						      d3.event.preventDefault();
						      d3.event.stopPropagation();
						    };
						    if (!isNaN(graph.y.down)) {
						      drawGraph()();
						      graph.y.down = Math.NaN;
						      d3.event.preventDefault();
						      d3.event.stopPropagation();
						    }
						    if (this.dragged) { 
						      this.dragged = null; 
						    }
						  }
						}
					
					function drawGraph() {
						return function() {
	
							// d is the tick point.
						    /*var tx = function(d) { return "translate(" + x(d) + ",0)"; },
						    	ty = function(d) { return "translate(0," + y(d) + ")"; },*/
						    
						    stroke = function(d) { return d ? "#ccc" : "#666"; },
						    
						    //fx = x.tickFormat(d3),
						    //fy = y.tickFormat(10);
						 
						  graph.x.axis.svg = d3.svg.axis()
						  						   .scale(graph.x.point)
						  						   .orient("bottom")
						  						   .tickFormat(d3.time.format("%b %d"));
						  
						  graph.y.axis.svg = d3.svg.axis()
					  							   .scale(graph.y.point)
					  							   .orient("left");
						    
						  vis.select("g.xaxis").call(graph.x.axis.svg);
						  
						  vis.select("g.yaxis").call(graph.y.axis.svg);
						  
						// Regenerate x-axis lines…
						    /*var gx = vis.selectAll("g.x")
						        .data(x.ticks(10), String)
						        .attr("transform", tx)
						        .call(xAxis);
						        
						  
						 
						    //gx.select("text").text(fx);
						 
						    var gxe = gx.enter().insert("g", "a")
						        .attr("class", "x")
						        .attr("transform", tx);
						 
						    gxe.append("line")
						        .attr("stroke", stroke)
						        .attr("y1", 0)
						        .attr("y2", h);
						 
						    gxe.append("text")
						        .attr("class", "axis")
						        .attr("y", h)
						        .attr("dy", "1em")
						        .attr("text-anchor", "middle")
						        .text(fx)
						        .style("cursor", "ew-resize")
						        .on("mouseover", function(d) { d3.select(this).style("font-weight", "bold");})
						        .on("mouseout",  function(d) { d3.select(this).style("font-weight", "normal");})
						        .on("mousedown.drag",  dragX());
						    
						   
						 
						    gx.exit().remove();*/
						 
						    // Regenerate y-axis lines…
						    /*var gy = vis.selectAll("g.y")
								        .data(y.ticks(10), String)
								        .attr("transform", ty);
						 
						    gy.select("text")
						        .text(fy);
						 
						    var gye = gy.enter().insert("g", "a")
						        .attr("class", "y")
						        .attr("transform", ty)
						        .attr("background-fill", "#FFEEB6");
						 
						    gye.append("line")
						        .attr("stroke", stroke)
						        .attr("x1", 0)
						        .attr("x2", w);
						    
						    // x-axis 
						    gye.append("text")
						    	.attr({
						    		"class": "axis",
						    		"x": -3,
						    		"dy": ".35em",
						    		"text-anchor": "end",
						    	})
						        .text(fy)
						        .style("cursor", "ns-resize")
						        .on("mouseover", function(d) { d3.select(this).style("font-weight", "bold");})
						        .on("mouseout",  function(d) { d3.select(this).style("font-weight", "normal");})
						        .on("mousedown.drag",  dragY());
						    
						    
						 
						    gy.exit().remove();*/
						  
						    //vis.selectAll("path").attr("transform", tx);
 
						  	graph.view.rect.call(graph.zoom);
						    update();    
						  }; 
						}
					
					function dragX() {
						  return function(d) {
						    document.onselectstart = function() { return false; };
						    var p = d3.mouse(vis[0][0]);
						    graph.x.down = graph.x.point.invert(p[0]);
						  };
						};
						 
					function dragY(d) {
						  return function(d) {
						    document.onselectstart = function() { return false; };
						    var p = d3.mouse(vis[0][0]);
						    graph.y.down = graph.y.point.invert(p[1]);
						  };
					};
			

		});

	};
	
} (jQuery));