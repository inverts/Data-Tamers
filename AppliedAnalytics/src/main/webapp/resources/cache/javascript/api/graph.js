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
			'endIndex': 0,
			'title': '',
			'xLabel': 'Test X',
			'yLabel': 'Test Y',
			'showLines': false,
			'hideYaxis': false,
			'pointSize': 0
	}; 
	
	/* function declaration */
	$.fn.graph = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this).sortable({ cancel: '.widget-content'}); // get a jQuery object of the element graph is attached too
			var settings = $.extend({}, defaults, params); // we are integrating our default values into our params.
			var elementId = this.id;
			
			var padding = {
				     		 "top":    settings.title  ? 40 :20,
				    	     "right":  30,
				    	     "bottom": settings.xLabel ? 60 : 10,
				    	     "left":   settings.yLabel ? 70 : 45
				    	  };
			
			
			var width = $this.width(),
				height = $this.height();
			
			var w = width - padding.left - padding.right,
				h = height - padding.top - padding.bottom;
			
			if (!settings.endIndex)
				settings.endIndex = settings.data.length;
			

			// Base Cases
			if (settings.endIndex >= settings.data.length || 
					settings.endIndex < settings.startIndex || 
					settings.endIndex - settings.startIndex < 4)
				return;
			
			
			// parse out data of date range.
			var showData = settings.data.slice(settings.startIndex, settings.endIndex);
			
			var dateRange = $.map(showData, function(val){
								return new Date(val.jsonDate);
							});
			
			// get date range
			
			var xMin = getDate(settings.data[0]),
				xMax = getDate(settings.data[settings.data.length - 1]);
			
			var xlimit = d3.time.scale().domain([xMin, xMax]).nice();
			//xMax = xlimit.domain()[1];
			xMin = xlimit.domain()[0];
			
			var xFrom = getDate(settings.data[settings.startIndex]),
				xTo = getDate(settings.data[settings.endIndex]);
			
			// get max value
			var yShowMax = d3.max(showData, function(d){ return d.jsonHitCount;});
			
			// get Y range
			var yMin = 0,
				yMax = d3.max(settings.data, function(d){return d.jsonHitCount;});
			
			// Data Points
			var x = d3.time.scale().domain([xFrom, xTo]).range([0, w]),
		    	y = d3.scale.linear().domain([yShowMax, yMin]).nice().range([0, h]).nice();

			
			var downx = Math.NaN,
				downy = Math.NaN;
			

			// Compute width of entire graph
			var graphWidth  = x(getDate(settings.data[settings.data.length - 1])),
			    graphHeight =  y(settings.data[getIndexBy(settings.data, "jsonHitCount", Math.max)].jsonHitCount);

			// the graph SVG with all properties
			var vis = d3.select(this).append("svg:svg").attr({ // svg as a d3 object
							"width": width, 
							"height": height,
							"id": this.id + "Graph"
						}).append("g").attr("transform", "translate(" + padding.left + "," + padding.right + ")");
			
			$.extend(settings, {constraint: { x: {
												  	min: x(getDate(settings.data[0])) - 2, 
												  	max: w - graphWidth + 2
												 }, 
											  y: {	min: 0,
												  	max: graphHeight - 2
												 }}});
			
			var zoomOut = Math.round(x(getDate(settings.data[settings.data.length - 1])) / w),
				zoomIn = 0 - (h / graphHeight);
			
			var zoom = d3.behavior.zoom().x(x).y(y).scaleExtent([zoomIn, zoomOut]).on("zoom", function() {
															var t = zoom.translate(),
															tx = t[0],
															ty = t[1];

															tx = Math.min(tx, 0 - settings.constraint.x.min);
															tx = Math.max(tx, settings.constraint.x.max);
															
															ty = Math.min(ty, 0 - settings.constraint.y.max);
															ty = Math.max(ty, settings.constraint.y.min);
															
															zoom.translate([tx, ty]);
															
															drawGraph()();
														});
			
			var plot = vis.append("rect").attr({"width": w, "height": h})
										 .style({
											 		"stroke": "#CCC",
											 		"stroke-width": 1,
											 		"fill": "#FFF"
											 	})
										 .attr("pointer-events", "all").on("mousedown.drag", pan())
										 .call(zoom);
			
			var graph = vis.append("svg").attr({
				"top": 0,
				"left": 0,
				"width": w,
				"height": h,
				"viewBox": "0 0 " + w + " " + h,
				"class": "line"
			});
			
			/*var tick = d3.svg.line()
				    .interpolate("monotone")
				    .x(function(d) { return x(new Date(d.jsonDate)); })
				    .y(function(d) { return y(d.jsonHitCount); });*/
			
			// append x-axis
			vis.append("g")
				 .attr("class", "xaxis")
				 .attr("transform", "translate(0, " + h + ")");
			
			// append y-axis
			vis.append("g")
				.attr("class", "yaxis")
				.attr("transform", "translate(0, 0)");
			
			//var graph = vis.data([showData]).append("g").attr("transform", "translate(0, 0)");
			
			
			
			// old way of applying graph
			/*vis.append("svg:path").attr({
				"class": "line",
				"d": d3.svg.line().x(function(d) { return x(getDate(d)); })
								  .y(function(d) { return y(d.jsonHitCount); })
			});*/
			
			
			 // apply graph title
			 if (settings.title) {
				    vis.append("text")
				        .attr("class", "axis")
				        .text(settings.title)
				        .attr({ "x": w/2, "dy": "-0.8em" })
				        .style("text-anchor","middle");
				  }
			
			 
			 // apply x-axis label
			  if (settings.xLabel) {
			    vis.append("text")
			        .attr("class", "axis")
			        .text(settings.xLabel)
			        .attr({"x": w/2, "y": h, "dy": "3.5em" })
			        .style("text-anchor","middle");
			  }
			  
			  // apply y-axis label
			  if (settings.yLabel) {
			    vis.append("g").append("text")
			        .attr("class", "axis")
			        .text(settings.yLabel)
			        .style("text-anchor","middle")
			        .attr("transform","translate(" + -40 + " " + h/2+") rotate(-90)");
			  }
			  
			  d3.select(this)
		      	.on("mousemove.drag", mousemove())
		      	.on("mouseup.drag",   mouseup());
			  
			  drawGraph()();
			  
			  function pan() {
				  return function() {
				    //registerKeyboardHandler(keydown());
				    d3.select("body").style("cursor", "move");
				    dragged = true;
				    /*if (d3.event.altKey) {
				      var p = d3.svg.mouse(vis.node());
				      var newpoint = {};
				      newpoint.x = x.invert(Math.max(0, Math.min(w,  p[0])));
				      newpoint.y = y.invert(Math.max(0, Math.min(h, p[1])));
				      points.push(newpoint);
				      points.sort(function(a, b) {
				        if (a.x < b.x) { return -1; };
				        if (a.x > b.x) { return  1; };
				        return 0
				      });
				      selected = newpoint;
				      update();
				      d3.event.preventDefault();
				      d3.event.stopPropagation();
				    }    */
				  };
			  };
			  

			  function update() {
				  // update the graph line on redraw
				  /*var lines = vis.select("path").attr("d", d3.svg.line().x(function(d) { return x(getDate(d)); })
		  			  												.y(function(d) { return y(d.jsonHitCount); }));*/
				  
				  d3.selectAll("path").remove();
				  
				  graph.data([settings.data]).append("path").attr({
						"d": d3.svg.line().x(function(d) { return x(getDate(d)); })
		  				  				  .y(function(d) { return y(d.jsonHitCount); })
				  });
				  
				  // apply the circles
				  var circle = vis.select("svg").selectAll("circle")
				      .data(function(d) { return d; });
				 
				  circle.enter().append("circle")
				      .attr("class", function(d) { return d === this.selected ? "selected" : null; })
				      .attr("cx",    function(d) { return x(new Date(d.jsonDate)); })
				      .attr("cy",    function(d) { return y(d.jsonHitCount); })
				      .attr("r", settings.pointSize)
				      .style("cursor", "ns-resize");
				 
				  circle
				      .attr("class", function(d) { return d === this.selected ? "selected" : null; })
				      .attr("cx", function(d) { return x(new Date(d.jsonDate)); })
				      .attr("cy", function(d) { return y(d.jsonHitCount); });
				 
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
					    
					    if (!isNaN(downx)) {
					      d3.select("body").style("cursor", "ew-resize");
					      var rupx = x.invert(p[0]),
					          xaxis1 = x.domain()[0],
					          xaxis2 = x.domain()[1],
					          xextent = xaxis2 - xaxis1;
					      if (rupx != 0) {
					        var changex, new_domain;
					        changex = downx / rupx;
					        new_domain = [xaxis1, xaxis1 + (xextent * changex)];
					        x.domain(new_domain);
					        drawGraph()();
					      }
					      d3.event.preventDefault();
					      d3.event.stopPropagation();
					    };
					    
					    if (!isNaN(downy)) {
					      d3.select("body").style("cursor", "ns-resize");
					      var rupy = y.invert(p[1]),
					          yaxis1 = y.domain()[1],
					          yaxis2 = y.domain()[0],
					          yextent = yaxis2 - yaxis1;
					      if (rupy != 0) {
					        var changey, new_domain;
					        changey = downy / rupy;
					        new_domain = [yaxis1 + (yextent * changey), yaxis1];
					        y.domain(new_domain);
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
						    d3.select("body").style("cursor", "auto");
						    if (!isNaN(downx)) {
						      drawGraph()();
						      downx = Math.NaN;
						      d3.event.preventDefault();
						      d3.event.stopPropagation();
						    };
						    if (!isNaN(self.downy)) {
						      drawGraph()();
						      downy = Math.NaN;
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
							
							//zoom.translate([tx, ty]);
							
							
							/* Constrain the pan domain */
							/*var xCurrent = { min: x.domain()[0], max: x.domain()[1] },
								yCurrent = { max: y.domain()[0], min: y.domain()[1] };

							if (xCurrent.max > xMax) {
								x.domain([xCurrent.min, xMax]);
								return;
							}
							else if (xCurrent.min < xMin) {
								x.domain([xCurrent.xMax, xMin]);
								return;
							}
							
							if (yCurrent.max > yMax) {
								y.domain([yMax, yCurrent.min]);
								return;
							}
							else if (yCurrent.min < yMin) {
								y.domain([yCurrent.max, yMin]);
								return;
							}*/
							
							
								
							// d is the tick point.
						    /*var tx = function(d) { return "translate(" + x(d) + ",0)"; },
						    	ty = function(d) { return "translate(0," + y(d) + ")"; },*/
						    
						    stroke = function(d) { return d ? "#ccc" : "#666"; },
						    
						    fx = x.tickFormat(d3),
						    fy = y.tickFormat(10);
						 
						  var xAxis = d3.svg.axis()
						  					.scale(x)
						  					.orient("bottom")
						  					.tickFormat(d3.time.format("%b %d"));
						  
						  var yAxis = d3.svg.axis()
					  					.scale(y)
					  					.orient("left");
						    
						  vis.select("g.xaxis").call(xAxis);
						  
						  vis.select("g.yaxis").call(yAxis);
						  
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
 
						    plot.call(zoom);
						    update();    
						  }; 
						}
					
					function dragX() {
						  return function(d) {
						    document.onselectstart = function() { return false; };
						    var p = d3.mouse(vis[0][0]);
						    downx = x.invert(p[0]);
						  };
						};
						 
					function dragY(d) {
						  return function(d) {
						    document.onselectstart = function() { return false; };
						    var p = d3.mouse(vis[0][0]);
						    downy = y.invert(p[1]);
						  };
					};
			

		});

	};
	
} (jQuery));