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
			'title'		: '',
			'xKey'		: 'jsonDate',
			'yKey'		: ['jsonHitCount'],
			'lineClass' : ['line'],
			'lineType'  : [],
			'xLabel'	: '',
			'yLabel'	: '',
			'hideYaxis'	: false,
			'pointSize'	: 0,
			'databuffer': 0,
			'rangeMin'	: 4,
			'dateLine'	: null
	}; 
	
	/* function declaration */
	$.fn.graph = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this).sortable({ cancel: '.widget-content'}); 	// disable widget drag on graph
			var settings = $.extend({}, defaults, params); 					// we are integrating our default values into our params.
			
			var padding = {
				     		 "top":    settings.title  ? 40 :20,			// padding required for graph labels
				    	     "right":  30,
				    	     "bottom": settings.xLabel ? 60 : 30,
				    	     "left":   settings.yLabel ? 70 : 45
				    	  };

			var width = $this.width(),										// svg takes on width and height of parent element.
				height = $this.height();
			
			// put single strings into an array
			if (!settings.yKey instanceof Array)
				settings.yKey = [settings.yKey];
			
			if (!settings.lineClass instanceof Array)
				settings.lineClass = [settings.lineClass];
			
			if (!settings.lineType instanceof Array)
				settings.lineType = [settings.lineType];
			
			if (settings.lineClass < settings.yKey) {					// if the classes are less than the keys, make copies of last class.
				var l = settings.lineClass.length;
				for(var i = 0; i < settings.yKey.length; i++)
					settings.lineClass.push(settings.lineClass[l - 1]);
				
			}
			

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
							"x"			: { "domain": [], 
											"point": function(){}, 
											"axis": {"label": null, "svg": null}, 
											"down": Math.NaN,
											"key" : settings.xKey },
							"y"			: { "domain": [], 
											"point": function(){}, 
											"axis": {"label": null, "svg": null}, 
											"down": Math.NaN, 
											"keys" : settings.yKey },
											
							"size"		: { "width": 0, "height": 0 },
							"zoom"		: function() {},
							"line"		: { "_class": settings.lineClass, "type": settings.lineType }
						};
			
			
			graph.x.domain = [d3.min(graph.data, function(d) { return getDate(d[graph.x.key]); }), 
			                  d3.max(graph.data, function(d) { return getDate(d[graph.x.key]); })];
			graph.y.domain = [0, getValueBy(graph.data, graph.y.keys, Math.max)];
			
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
			graph.size.height =  graph.y.point(graph.data[getIndexBy(graph.data, graph.y.keys, Math.max)].jsonHitCount);

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
												"class": graph.line.class
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
			 function update(dateLine) {
				 
				 // Clipping object
				var clip = { "x": null, "width": null, "height": graph.view.height, "show": false };
				 
				 if (dateLine) {
					 clip.x = graph.x.point(dateLine);
					 clip.width = graph.view.width - graph.x.point(dateLine);
					 clip.show = graph.x.point(dateLine) < graph.view.width;
				 }

				  // draw all graphs
				  for (var i = 0; i < graph.y.keys.length; i++) {
					  
					  var $line = $("." + graph.line._class[i]);
					  var classes = null;
					  var active = false;
					  
					  if($line.length) {
						  classes = $line.attr("class");
						  active = classes.indexOf("active") != -1;
						  d3.select("." + graph.line._class[i]).remove();
						  d3.select("." + graph.line._class[i] + ".alternate" ).remove();
					  	  d3.select("#" + graph.line._class[i] + "clip").remove();
					  }
					  
					  // Setup clipping if applicable
					  if (clip.show) {
						  vis.select("svg").append("clipPath")
						  				   .attr("id", graph.line._class[i] + "clip")
										   .append("rect")
										   .attr({
													"x":  clip.x,
													"width": clip.width,
													"height": clip.height
												});
					  }
	  
					  // Draw the graph line
					  graph.view.svg.data([graph.data])
					  					  .append("path")
					  					  .attr({
													"d": d3.svg.line().interpolate(graph.line.type[i])
																	  .x(function(d) { return graph.x.point(getDate(d)); })
									  				  				  .y(function(d) { return graph.y.point(d[graph.y.keys[i]]); }),
									  				"pointer-events": "all",
									  				"class": classes || graph.line._class[i]
					  					  		})
					  					  .on("mousedown.drag", pan())
					  					  .call(graph.zoom);
					  
					  // apply clipping mask if applicable
					  if (clip.show) {
						  graph.view.svg.append("path")
						  				.attr({
						  						"class": (active) ? graph.line._class[i] + " alternate active" 
						  										  : graph.line._class[i] + " alternate",
						  						"clip-path": "url(#" + graph.line._class[i] + "clip)"
						  					  })
						  			    .datum(graph.data)
						  			    .attr("d", d3.svg.line().interpolate(graph.line.type[i])
													  .x(function(d) { return graph.x.point(getDate(d)); })
					  				  				  .y(function(d) { return graph.y.point(d[graph.y.keys[i]]); }));
					  }
					  
					  // apply the circles
					 /*var circle = vis.select("svg").selectAll("circle")
					      							.data(function(d) { return d; });
					  
					  
					 
					  circle.enter().append("circle")
							        .attr("class", function(d) { return d === this.selected ? "selected" : null; })
							        .attr("cx",    function(d) { return graph.x.point(new Date(d[graph.x.key])); })
							        .attr("cy",    function(d) { return graph.y.point(d[graph.y.keys[i]]); })
							        .attr("r", settings.pointSize)
							        .attr("pointer-events", "all").on("mousedown.drag", pan())
							        .call(graph.zoom);
					 
					  circle
					      .attr("class", "circle")
					      .attr("cx", function(d) { return graph.x.point(new Date(d.jsonDate)); })
					      .attr("cy", function(d) { return graph.y.point(d[graph.y.keys[i]]); });
					 
					  circle.exit().remove();*/
				  } // end for
				  
				  if (dateLine) {
					  d3.selectAll("line.dateLine").remove();
					  vis.select("svg").append("line")
					  				   .attr({
					  							"class": "dateLine",
					  							"y1": 0,
					  							"y2": graph.view.height,
					  							"x1": graph.x.point(dateLine),
					  							"x2": graph.x.point(dateLine),
					  						 });

				  }			
				 
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
					    	update(settings.callback);
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
						    var tx = function(d) { 
						    	var today = new Date();
						    	if (d == today.getDate())
						    		settings.onToday;
						    	else
						    		return null;
						    };
						    
						    	//ty = function(d) { return "translate(0," + y(d) + ")"; },
						    
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

 
						  graph.view.rect.call(graph.zoom);
						  update(settings.dateLine);    
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