/**
 * graph.js
 * 
 * jQuery function that attaches a graph to a specified element.
 * @author - Andrew Riley
 * 
 */
var nGraphs = 0; // number of graphs currently. Needed for the clipping mask id.
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
			'dateLine'	: null,
			'showLegend': true
	}; 
	
	/* function declaration */
	$.fn.graph = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this);	// disable widget drag on graph
			var settings = $.extend({}, defaults, params); 					// we are integrating our default values into our params.
			var id = settings.id;
			var padding = {
				     		 "top":    settings.title  ? 22 : 5,			// padding required for graph labels
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
			

			/********************************
			 *		GRAPH PROPERTIES		*
			 ********************************/
			var graph = {
							"id"		: this.id + "Graph",
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
							"line"		: { "_class": settings.lineClass, "type": settings.lineType },
							"n"			: nGraphs++
						};
			
			/********************************
			 *		  GRAPH DOMAIN			*
			 ********************************/
			graph.x.domain = [d3.min(graph.data, function(d) { return getDate(d[graph.x.key]); }), 
			                  d3.max(graph.data, function(d) { return getDate(d[graph.x.key]); })];
			graph.y.domain = [getValueBy(graph.data, graph.y.keys, Math.min) - 15, getValueBy(graph.data, graph.y.keys, Math.max) + 15];
			
			if (!settings.endIndex)											
				settings.endIndex = settings.data.length - 1;
			
			var from = new Date(graph.data[settings.startIndex][graph.x.key]),		// static date range chosen.
			  	  to = new Date(graph.data[settings.endIndex ][graph.x.key]);
			
			$.extend(settings, {from: from, to: to});

			/********************************
			 *			BASE CASES			*
			 ********************************/
			if (settings.endIndex >= settings.data.length || 
					settings.endIndex < settings.startIndex || 
					settings.endIndex - settings.startIndex < settings.rangeMin)
				return;
			

			/********************************
			 *			GRAPH SCALE			*
			 ********************************/
			graph.x.point = d3.time.scale().domain([from, to]).range([0, graph.view.width]); 		// function to get data points at x
			graph.y.point = d3.scale.linear().domain(graph.y.domain.reverse()).nice().range([0, graph.view.height]).nice();	// function to get data points at y

			// create a buffer region for the dates
			//graph.x.domain = d3.time.scale().domain(graph.x.domain).nice().domain();
			
			// Compute width of entire graph
			graph.size.width  = graph.x.point(getDate(graph.data[graph.data.length - 1])),
			graph.size.height =  graph.y.point(0) - graph.y.point(graph.data[getIndexBy(graph.data, graph.y.keys, Math.max)].jsonHitCount);

			// the graph SVG with all properties
			var vis = d3.select(this).append("svg:svg")
									 .attr({ 
											"width": width, 
											"height": height,
											"id": graph.id
									 	   })
									 .append("g")
									 .attr("transform", "translate(" + padding.left + "," + padding.top + ")");
			
			/********************************
			 *		ZOOM BOUNDARIES			*
			 ********************************/
			// Zoom limits
			/*
			var zoomOut = Math.round(graph.x.point(getDate(graph.data[graph.data.length - 10])) / graph.view.width),
				//zoomIn = 0 - (graph.view.height / graph.size.height);
			zoomIn = 0.3;
			*/
			
			var maximumZ = 16, //Zoom in; you can make something look this many times bigger
				minimumZ = 1; //Zoom out; same as above, you're scaling the image by this number.
			
			
			graph.zoom = d3.behavior.zoom()
									.x(graph.x.point)
									.scaleExtent([minimumZ, maximumZ])
									.on("zoom", function() {
										
															d3.selectAll("#" + id + " #" + graph.id + " Circle").remove(); // remove plots
															var t = graph.zoom.translate(),
															tx = t[0], 
															ty = t[1];
															
															var diff = { "left": 0, "right": 0 };
															
															var yMax = graph.y.point(graph.data[getIndexBy(graph.data, graph.y.keys, Math.max)].jsonHitCount);

															//tx is the amount of pixels to the right the beginning of the graph is from the starting point.
															viewMinusGraphWidth = graph.view.width - graph.size.width;
															if (tx > 0)
																tx = 0;
															if (tx < viewMinusGraphWidth)
																tx = viewMinusGraphWidth;
															
															// y - boundaries
															ty = Math.min(ty, graph.size.height);
															ty = Math.max(ty, graph.size.height - graph.y.point(0));
															
															// move graph
															graph.zoom.translate([tx, ty]);
															
															// redraw the graph that moved
															drawGraph()();
															
															// get new graph size
															graph.size.width  = graph.x.point(new Date(graph.data[graph.data.length - 1][graph.x.key])) - graph.x.point(new Date(graph.data[0][graph.x.key])),
															graph.size.height =  graph.y.point(0) - graph.y.point(graph.data[getIndexBy(graph.data, graph.y.keys, Math.max)].jsonHitCount);
														});
			/********************************
			 *	  CREATE GRAPH VIEW AREA	*
			 ********************************/
			graph.view.rect = vis.append("rect")
									   .attr({
												"width": graph.view.width, 
												"height": graph.view.height
											})
									   .style({
										 		"stroke": "#666",
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
												"viewBox": "0 0 " + graph.view.width + " " + graph.view.height
											});
			
			
			/********************************
			 *		APPLY X-AXIS LABEL		*
			 ********************************/
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
			

			/********************************
			 *		APPLY Y-AXIS LABEL		*
			 ********************************/
			vis.append("g")
			   .attr({
				  		"class": "yaxis",
				  		"transform": "translate(0, 0)"
				  	 });
			
			if (settings.yLabel) {
			    graph.y.axis.label = vis.append("g").append("text")
								        .attr("class", "xaxis label")
								        .text(settings.yLabel)
								        .style("text-anchor","middle")
								        .attr("transform","translate(" + -40 + " " + graph.view.height/2+") rotate(-90)");
			}
			
			
			/********************************
			 *		 APPLY GRAPH TITLE		*
			 ********************************/
			if (settings.title) {
				    graph.title = vis.append("text")
							         .attr("class", "yaxis label")
							         .text(settings.title)
							         .attr({ "x": graph.view.width/2, "dy": "-0.8em" })
							         .style("text-anchor","middle");
			}

			 drawGraph()(); // draw graph on DOM load
			 
			 d3.select(this).on("mouseup.drag",   function() { d3.select("body").style("cursor", "auto"); });
			 
			 function pan() {
				 return function() {
					 d3.select("body").style("cursor", "move");
				 };
			 };
			  
			/********************************
			 *	 UPDATES THE GRAPH LINES	*
			 ********************************/
			 function update(dateLine) {
				 
				/********************************
				 *	 	CLIPPING PROPERTIES		*
				 ********************************/
				var clip = { "x": null, "width": null, "height": graph.view.height, "show": false };
				 
				 if (dateLine) {
					 clip.x = graph.x.point(dateLine);
					 clip.width = graph.view.width - graph.x.point(dateLine);
					 clip.show = graph.x.point(dateLine) < graph.view.width;
				 }
				 

				/********************************
				 *	 	  DRAW ALL LINES		*
				 ********************************/
				 for (var i = 0; i < graph.y.keys.length; i++) {
					  
					  var $line = $("#" + id + " ." + graph.line._class[i] + ".plot");
					  var classes = null;
					  var active = false;
					  
					  if($line.length) {
						  classes = $line.attr("class");
						  active = classes.indexOf("active") != -1;
						  d3.select("#" + id + " path." + graph.line._class[i] + ".plot").remove();
						  d3.select("#" + id + " path." + graph.line._class[i] + ".alternate" ).remove();
					  	  d3.select("#" + id + " #" + graph.line._class[i] + "clip" + graph.n).remove();
					  }
					  
					  // Setup clipping if applicable
					  if (clip.show) {
						  vis.select("svg").append("clipPath")
						  				   .attr("id", graph.line._class[i] + "clip" + graph.n)
										   .append("rect")
										   .attr({
													"x":  clip.x,
													"width": clip.width,
													"height": clip.height
												});
					  }
	  
					  /********************************
					   *		PLOT THE LINE		  *
					   ********************************/
					  graph.view.svg.data([graph.data]).append("path")
					  					  .attr({
													"d": d3.svg.line().interpolate(graph.line.type[i])
																	  .x(function(d) { return graph.x.point(getDate(d)); })
									  				  				  .y(function(d) { return graph.y.point(d[graph.y.keys[i]]); }),
									  				"pointer-events": "all",
									  				"class": classes || graph.line._class[i] + " plot",
									  				"id": graph.y.keys[i]
					  					  		})
					  					  .on("mousedown.drag", pan())
					  					  .on("mouseover", function() {
					  						  
					  						 var key = d3.select("#" + id + " #" + this.id).attr("id");
					  						 var current = d3.select("#" + id + " #" + this.id).attr("class");

					  						 plotPoints(current, key);
					  						  
					  					  })
					  					  .call(graph.zoom);
					  
					  /********************************
					   *	 APPLY CLIPPING MASK	  *
					   ********************************/
					  if (clip.show) {
						  graph.view.svg.append("path")
						  				.attr({
						  						"class": (active) ? graph.line._class[i] + " alternate active" 
						  										  : graph.line._class[i] + " alternate",
						  						"clip-path": "url(#" + graph.line._class[i] + "clip" + graph.n + ")"
						  					  })
						  			    .datum(graph.data)
						  			    .attr("d", d3.svg.line().interpolate(graph.line.type[i])
													  .x(function(d) { return graph.x.point(getDate(d)); })
					  				  				  .y(function(d) { return graph.y.point(d[graph.y.keys[i]]); }));
					  }
					  
					  
					  
					  // apply the circles
					
				  } // end for
				  
				 /********************************
				  *	  APPLY VERTICAL DATE LINE   *
				  ********************************/
				  if (dateLine) {
					  d3.selectAll("#" + id + " line.dateLine").remove();
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
				
				
				function plotPoints(current, key) {
					
					
					d3.selectAll("#" + id + " #" + graph.id + " Circle").remove(); // remove old plots
					
					var circle = vis.select("svg").selectAll("circle").data(function(d) { return d; });
					
					circle.enter()
						  .append("circle")
						  .attr({
							  "class": current,
							  "cx": function(d) { return graph.x.point(new Date(d[graph.x.key])); },
							  "cy": function(d) { return graph.y.point(d[key]); },
							  "r": settings.pointSize,
							  "title": function(d) { return Math.round(d[key]) + " visits"; } // for tooltip
						  });
					
					circle.exit();
					
					$("#" + id + " #" + graph.id + " circle").tipsy({
						 gravity: "s",
						 html: false,
						 title: function() { return $(this).attr("original-title"); }
					 });
					
				}
						
					
					function drawGraph() {
						return function() {

						  /********************************
						   *	APPLY X-AXIS TICKS		  *
						   ********************************/
						  graph.x.axis.svg = d3.svg.axis()
						  						   .scale(graph.x.point)
						  						   .orient("bottom")
						  						   .tickFormat(d3.time.format("%b %d"));
						  
						  vis.select("g.xaxis").call(graph.x.axis.svg);
						  
						  /********************************
						   *	APPLY Y-AXIS TICKS		  *
						   ********************************/
						  graph.y.axis.svg = d3.svg.axis()
					  							   .scale(graph.y.point)
					  							   .orient("left");

						  vis.select("g.yaxis").call(graph.y.axis.svg);

						  /********************************
						   *		REDRAW LINES		  *
						   ********************************/
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