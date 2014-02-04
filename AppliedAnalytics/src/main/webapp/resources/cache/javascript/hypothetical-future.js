/**
 * hypothetical-future.js
 */

$(function() {
	updateHypotheticalWidget('hypotheticalWidget');
});

function updateHypotheticalWidget(id) {
	var $element = $('#' + id);
	$.post("HypotheticalFuture", null, function(response) {
		$element.fadeOut("fast", function() {
			$element.html(response);
			var canvas = document.getElementById('hypotheticalFutureData');

			// points = HypotheticalFutureData.points;
			var p = new Processing(canvas, hypotheticalSketch);

			$element.fadeIn("fast");
			window.onresize = function(event) {
				var p = new Processing(canvas, hypotheticalSketch);
			}
		});
	});
}

//assume everything in points is a string!
var hypotheticalSketch = (function($p) {
	var margin = {
			top : 30,
			right : 20,
			bottom : 30,
			left : 50
	}, width = 600 - margin.left - margin.right, height = 300 - margin.top
	- margin.bottom;
	var top = 30;
	var right = 20;
	var bottom = 30;
	var left = 20;

	/***************************************************************************
	 * GET (TODO:REAL) DATA
	 **************************************************************************/

	// TODO test data : need to replace w/ real
	var hypoData = [];
	var hypoDates = []
	var trendData = [];
	var trendDates = [];

	for (var i = 0; i < 11; i++) {
		var fakeData = Math.random() * 30;
		hypoData.push(fakeData);
		var fakeTrend = Math.random() * 30;
		trendData.push(fakeTrend);
	}
	for (var i = 0; i < 11; i++) {
		var fakeDate = i;
		hypoDates.push(i);
	}
	for (var i = 11; i < 22; i++) {
		var fakeDate = i;
		trendDates.push(fakeDate);
	}

	var stopX = hypoData[hypoData.length];
	var stopY = hypoDates[hypoDates.length];

	/***************************************************************************
	 * DRAW INNER BACKGROUND
	 **************************************************************************/
	// draw background shape
	var w = 600;
	var h = 300;
	var svg = d3.select("#backrect").append("svg").attr("width", w).attr(
			"height", h);

	var buttons = document.getElementById("buttonsContainer");
	svg.select("#buttons");

	var borderpath = svg.append("rect").attr("x", 30).attr("y", 50).attr(
			"width", w - 60).attr("height", h - 80).style("stroke-width", 5)
			.style("stroke", "grey");

	var focus = svg.append("g").attr("class", "focus").style("display", "none");

	focus.append("circle").attr("r", 4.5);

	focus.append("text").attr("x", 9).attr("dy", ".35em");

	var innerRect = svg.append("rect").attr("x", 30).attr("y", 50).attr(
			"width", w - 60).attr("height", h - 80).attr("fill", "#F0F0F0");

	var smallW = w - 35;
	var smallH = h - 80;

	/***************************************************************************
	 * GET (TODO: REAL) DATE DATA PLOT X AXIS
	 **************************************************************************/

	var data = [ {
		"jsonDate" : "09\/22\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "09\/26\/11",
		"jsonHitCount" : 9,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "09\/27\/11",
		"jsonHitCount" : 9,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "09\/29\/11",
		"jsonHitCount" : 26,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "09\/30\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/03\/11",
		"jsonHitCount" : 3,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/06\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/11\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/12\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/13\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/14\/11",
		"jsonHitCount" : 5,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/17\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/18\/11",
		"jsonHitCount" : 6,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/19\/11",
		"jsonHitCount" : 8,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/20\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/21\/11",
		"jsonHitCount" : 4,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/24\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/25\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "10\/27\/11",
		"jsonHitCount" : 3,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/01\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/02\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/03\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/04\/11",
		"jsonHitCount" : 37,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/08\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/10\/11",
		"jsonHitCount" : 39,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/11\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/14\/11",
		"jsonHitCount" : 15,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/15\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/16\/11",
		"jsonHitCount" : 5,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/17\/11",
		"jsonHitCount" : 4,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/21\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/22\/11",
		"jsonHitCount" : 3,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/23\/11",
		"jsonHitCount" : 11,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/24\/11",
		"jsonHitCount" : 2,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/25\/11",
		"jsonHitCount" : 1,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/28\/11",
		"jsonHitCount" : 10,
		"seriesKey" : "Website Usage"
	}, {
		"jsonDate" : "11\/29\/11",
		"jsonHitCount" : 3,
		"seriesKey" : "Website Usage"
	} ];

	/* DEFAULT: WEEKLY VIEW */
	drawWeekDates();

	function drawWeekDates() {

		// helper function
		function getDate(d) {
			return new Date(d.jsonDate);
		}

		// get max and min dates - this assumes data is sorted
		var minDate = getDate(data[0]), maxDate = getDate(data[data.length - 1]);

		var w = smallW - 15, h = smallH, p = 30, y = d3.scale.linear().domain(
				[ 0, 50 ]).range([ h, 0 ]), x = d3.time.scale().domain(
						[ minDate, maxDate ]).range([ 0, w ]);

		var vis = svg.data([ data ]).append("svg:svg").attr("width", w).attr(
				"height", h + p * 2).append("svg:g").attr("transform",
						"translate(" + p + "," + p + ")");

		var rules = vis.selectAll("g.rule").data(x.ticks(5)).enter().append(
		"svg:g").attr("class", "rule");

		rules.append("svg:text").attr("x", x).attr("y", h + 3).attr("dy",
		".71em").attr("text-anchor", "middle").style("font-size",
		"12px").text(x.tickFormat(10));

	}
	// TODO
	function drawDayDates() {

	}
	// TODO
	function drawMonthDates() {

	}

	/***************************************************************************
	 * PLOT LEGENDS
	 **************************************************************************/
	var historicalLegend = svg.append("line").attr("x1", 30).attr("y1", 40)
	.attr("x2", 60).attr("y2", 40).attr("stroke-width", 4).style(
			"stroke-dasharray", ("3,3")).attr("stroke", "black");

	var trendLegend = svg.append("line").attr("x1", 140).attr("y1", 40).attr(
			"x2", 170).attr("y2", 40).attr("stroke-width", 4).attr("stroke",
			"black");

	var historicalLabel = svg.append("text").attr("x", 65).attr("y", 45).attr(
			"fill", "black").style("font-size", "12px").style("font-family",
			"Helvetica").text("historical");

	var trendLabel = svg.append("text").attr("x", 175).attr("y", 45).attr(
			"fill", "black").style("font-size", "12px").style("font-family",
			"Helvetica").text("future");

	/***************************************************************************
	 * PLOT LINE GRAPHS
	 **************************************************************************/

	var stopX = hypoData[hypoData.length - 1];
	var stopY = hypoDates[hypoDates.length - 1];

	var hypoScaleX = d3.extent(hypoData, function(d) {
		return hypoData.x;
	});
	var hypoScaleY = d3.extent(hypoDates, function(d) {
		return hypoData.y;
	});

	// TODO: need to figure out what x ultimately becomes.
	/* hypo data */

	var maxData = d3.max(hypoData);
	var maxDate = d3.max(hypoDates);
	var x = d3.scale.linear().domain([ 0, maxData ]).range([ 30, smallW ]);
	var y = d3.scale.linear().domain([ 0, d3.max(hypoData, function(d) {
		return d;
	}) ]).range([ smallH - 20, smallH + 20 ]);
	var hypoLine = d3.svg.line().x(function(d, i) {
		return x(i);
	}).y(function(d) {
		return y(d);
	});

	svg.append("path").attr("id", "hypolineID").attr("class", "line").attr("d",
			hypoLine(hypoData)).style("stroke-dasharray", ("3,3")).style({
				'fill' : 'none'
			}).style({
				'stroke' : '#2ca02c'
			}).style({
				'stroke-weight' : '3px'
			});

	/* trend data */
	var maxTrendData = d3.max(trendData);
	var maxTrendDate = d3.max(trendDates);
	var minTrendDate = d3.min(trendDates);
	var xt = d3.scale.linear().domain([ 0, maxTrendData ])
	.range([ 30, smallW ]);
	var yt = d3.scale.linear().domain([ 0, d3.max(trendData, function(d) {
		return d;
	}) ]).range([ smallH - 20, smallH + 20 ]);
	var trendLine = d3.svg.line().x(function(d, i) {
		return xt(i);
	}).y(function(d) {
		return yt(d);
	});

	svg.append("path").attr("id", "trendlineID").attr("class", "line").attr(
			"d", trendLine(trendData)).style({
				'fill' : 'none'
			}).style({
				'stroke' : '#2ca02c'
			}).style({
				'stroke-weight' : '3px'
			});

	// added for hover
	var trend_g = svg.append("g").attr("id", "line_o")
	trend_g.append("path").attr("d", trendLine(trendData)).style({
		'fill' : 'none'
	}).style({
		'stroke' : '#2ca02c'
	}).style({
		'stroke-weight' : '3px'
	});

	var hypo_g = svg.append("g").attr("id", "line_o")
	hypo_g.append("path").attr("d", hypoLine(hypoData)).style(
			"stroke-dasharray", ("3,3")).style({
				'fill' : 'none'
			}).style({
				'stroke' : '#2ca02c'
			}).style({
				'stroke-weight' : '3px'
			});

	/* for hover showing value along line */
    appendCircle(trend_g, hypo_g);

	/***************************************************************************
	 * HOVER TOOLTIP
	**************************************************************************/
    
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
 // FUNCTION to append circle to lines
    function appendCircle(tg, hg) {   

    // make a transparant overlay to capture mousemovements
    trend_g = tg;
    hypo_g = hg;

    /* TREND LINE */
    trend_g.append('path')
        .classed('overlay', true)
        .attr('d', trendLine(trendData))
        .selectAll('#line_o')
        .on('mouseover', function() {d3.select(this).showTooltip();})
        .on('mouseout', removeTooltip());

    /* HYPOTHETICAL LINE */
    hypo_g.append('path')
        .classed('overlay', true)
        .attr('d', hypoLine(hypoData))
        .selectAll('#line_o')
        .on('mouseover', function() {d3.select(this).showTooltip2();})
        .on('mouseout', removeTooltip2());  
      

    // displays hover tooltip for TREND LINE
    function showTooltip() {
            var line_path = d3.select('#line_o path.overlay')[0][0];
            var length_at_point = 0,
            total_length = line_path.getTotalLength(),
            mouse = d3.mouse(line_path);

            // find point on the line to draw tangent on, 
            var INTERVAL = 100;
            while (line_path.getPointAtLength(length_at_point).x < mouse[0] && length_at_point < total_length)
                length_at_point += INTERVAL

            length_at_point -= INTERVAL


            while (line_path.getPointAtLength(length_at_point).x < mouse[0] && length_at_point < total_length) {
                length_at_point++;
            };

            var point = line_path.getPointAtLength(length_at_point),
                prev = {},
                next = {},
                delta = {};

            if (length_at_point > 1 && length_at_point < (total_length - 1)) {
                prev = line_path.getPointAtLength(length_at_point - 1);
                next = line_path.getPointAtLength(length_at_point + 1);
                delta = {
                    x: next.x - prev.x,
                    y: next.y - prev.y
                }
            } else {

                return;
            };


         trend_g.append("svg:text")
        .attr('id', 'text')
        .style("font-size", "12px")
        .style("font-family", "Helvetica")
        .attr('x', point.x)
        .attr('y', point.y)
        .attr('dy', '.31em')
        .attr("transform", "translate(10,-5)")
            //round to 2 decimal places
        .text(Math.round(point.y * 100) / 100);

            var tangent = svg.append('circle')
        .attr('id', 'tangent')
        .attr('r', 3)
        .attr('cx', point.x)
        .attr('cy', point.y);
        };

        // displays hover tooltip for HYPOTHETICAL LINE
    function showTooltip() {
            var line_path2 = d3.select('#line_o path.overlay')[0][0];
            var length_at_point2 = 0,
            total_length2 = line_path2.getTotalLength(),
            mouse2 = d3.mouse(line_path2);

            // find point on the line to draw tangent on, 
            var INTERVAL2 = 100;
            while (line_path2.getPointAtLength(length_at_point2).x < mouse2[0] && length_at_point2 < total_length2)
                length_at_point2 += INTERVAL2

            length_at_point2 -= INTERVAL2


            while (line_path2.getPointAtLength(length_at_point2).x < mouse2[0] && length_at_point2 < total_length2) {
                length_at_point2++;
            };

            var point2 = line_path2.getPointAtLength(length_at_point2),
                prev2 = {},
                next2 = {},
                delta2 = {};

            if (length_at_point2 > 1 && length_at_point2 < (total_length2 - 1)) {
                prev2 = line_path2.getPointAtLength(length_at_point2 - 1);
                next2 = line_path2.getPointAtLength(length_at_point2 + 1);
                delta2 = {
                    x: next2.x - prev2.x,
                    y: next2.y - prev2.y
                }
            } else {

                return;
            };

         hypo_g.append("svg:text")
            .attr('id', 'text')
            .style("font-size", "12px")
            .style("font-family", "Helvetica")
            .attr('x', point2.x)
            .attr('y', point2.y)
            .attr('dy', '.31em')
            .attr("transform", "translate(10,-5)")
                //round to 2 decimal places
            .text(Math.round(point2.y * 100) / 100);

        var tangent2 = svg.append('circle')
            .attr('id', 'tangent2')
            .attr('r', 3)
            .attr('cx', point2.x)
            .attr('cy', point2.y);
        };
      //remove all text and circles
        function removeTooltip() {
            d3.select('#tangent').remove();       
            svg.select("#text").remove();
        };
      //remove all text and circles
        function removeTooltip2() {
            d3.select('#tangent2').remove();       
            svg.select("#text2").remove();
        };


    //end of function
};     
    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    
	
	/***************************************************************************
	 * PLOT DRAGGABLE LINE
	 **************************************************************************/
	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
	drawDraggableLine();
	
	function drawDraggableLine() {
		// TODO: start at last x coord value
		var xStart = smallW / 2;		
		
		var drag = d3.behavior.drag()
		.origin(function() {
		var t = d3.select(this);
		return {x: t.attr('x'), y: t.attr('y')};
		})
		//.origin(function(d) {return d;})
		.on("drag",d3.select('#drag_line').dragEvent);	
		
		var newg = svg.append("g")
			.data([{x : xStart, y : 50}]);		
	
		var dragRect = newg.append("rect")
			.attr("id", "drag_line")
			.attr("x", function(d) {return d.x;})
			.attr("y", function(d) {return d.y;})
			.attr("height", smallH)
			.attr("width", 2)
			.attr("fill", "grey")
			.attr("cursor", "move") // can also do cursor
		.call(drag);
				
		
		function dragEvent(d,i) {	
			var current = d3.select('#drag_line');
			var t = d3.select(this);
			var newX = function() {var t = d3.select(this); return {x:t.attr('x')};};
			var newY = function() {var t = d3.select(this); return {y:t.attr('y')};};
			if (newX <= 30) {
                newX = 35;
            }
            if (newX >= w - 35) {
                newX = w - 35;
            }
            dragRect.attr("x", d.x = newX);
        }
	// end of function
	};	

	/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

	/*
	 * TODO if draggable axis is dragged... go to zoom behavoir
	 */
	/***************************************************************************
	 * PAN + ZOOM BEHAVIOR
	 **************************************************************************/
	var yscale = d3.scale.linear().domain([ 0, 50 ]).range([ smallH, 20 ]);
	var xscale = d3.time.scale().domain([ 0, 200 ]).range([ 30, smallW ]); // TODO:
	// change
	// domain
	// to
	// min/max
	// values
	var zoom = d3.behavior.zoom().x(x).y(x).on("zoom", zoomed);

	svg.call(zoom);

	function zoomed() {
		// console.log(d3.event.translate);
		// console.log(d3.event.scale);
	}

	/***************************************************************************
	 * TODO:add noise to future CHECKBOX OPTION
	 **************************************************************************/
	function addNoise() {
	}

	/***************************************************************************
	 * BUTTON - LINE SELECTION: smoothed, normalized, raw
	 **************************************************************************/
	function removePrevious() {
		d3.selectAll('#line_o').remove();
		d3.select("#normID").remove();
		d3.select("#normID2").remove();
		d3.select("#rawID").remove();
		d3.select("#rawID2").remove();
		d3.select("#hypolineID").remove();
		d3.select("#trendlineID").remove();
		d3.select('#smoothID').remove();
		d3.select('#smoothID2').remove();
	}

	function updateSmoothed() {
		removePrevious();

		var smoothedLine = d3.svg.line().interpolate("cardinal").x(
				function(d, i) {
					return x(i);
				}).y(function(d) {
					return y(d);
				});

		var hg = svg.append("path").attr("id", "smoothID")
		.attr("class", "line").attr("d", smoothedLine(hypoData)).style(
				"stroke-dasharray", ("3,3")).style({
					'fill' : 'none'
				}).style({
					'stroke' : '#1f77b4'
				}).style({
					'stroke-weight' : '3px'
				});

		var smoothedTrend = d3.svg.line().interpolate("cardinal").x(
				function(d, i) {
					return x(i);
				}).y(function(d) {
					return y(d);
				});

		var tg = svg.append("path")
		.attr("id", "smoothID2")
		.attr("class","line")
		.attr("d", smoothedTrend(trendData))
		.style({'fill' : 'none'})
		.style({'stroke' : '#1f77b4'})
		.style({'stroke-weight' : '3px'});

		appendCircle(tg, hg);

	}

	function updateNormalized() {
		removePrevious();

		var normalizedLine = d3.svg.line().interpolate("basis").x(
				function(d, i) {
					return x(i);
				}).y(function(d) {
					return y(d);
				});

		svg.append("path").attr("id", "normID").attr("class", "line").attr("d",
				normalizedLine(hypoData)).style("stroke-dasharray", ("3,3"))
				.style({
					'fill' : 'none'
				}).style({
					'stroke' : '#ff7f0e'
				}).style({
					'stroke-weight' : '3px'
				});

		var normalizedTrend = d3.svg.line().interpolate("basis").x(
				function(d, i) {
					return x(i);
				}).y(function(d) {
					return y(d);
				});

		svg.append("path").attr("id", "normID2").attr("class", "line").attr(
				"d", normalizedTrend(trendData)).style({
					'fill' : 'none'
				}).style({
					'stroke' : 'ff7f0e'
				}).style({
					'stroke-weight' : '3px'
				});

	}

	function updateRaw() {
		removePrevious();

		var rawLine = d3.svg.line().x(function(d, i) {
			return x(i);
		}).y(function(d) {
			return y(d);
		});

		svg.append("path").attr("id", "rawID").attr("class", "line").attr("d",
				rawLine(hypoData)).style("stroke-dasharray", ("3,3")).style({
					'fill' : 'none'
				}).style({
					'stroke' : '#2ca02c'
				}).style({
					'stroke-weight' : '3px'
				});

		var rawTrend = d3.svg.line().x(function(d, i) {
			return x(i);
		}).y(function(d) {
			return y(d);
		});

		svg.append("path").attr("id", "rawID2").attr("class", "line").attr("d",
				rawTrend(trendData)).style({
					'fill' : 'none'
				}).style({
					'stroke' : '#2ca02c'
				}).style({
					'stroke-weight' : '3px'
				});

	}

	/***************************************************************************
	 * BUTTONS: month, week, day
	 **************************************************************************/
	function updateWeek() {
		drawWeekDates();
	}
	function updateDay() {
	}
	function updateMonth() {
	}

});

/*
 * External vars that this file relies on:
 * 
 * historicalData futureData historicalDataSize Y_MIN Y_MAX startDate endDate
 * 
 */

/*
 * function updateHypotheticalWidget(id) {
 * 
 * var $element = $('#' + id); $.post("HypotheticalFuture", null,
 * function(response) { $element.fadeOut("fast", function() {
 * $element.html(response); var canvas =
 * document.getElementById('hypotheticalFutureData');
 * 
 * 
 * //points = HypotheticalFutureData.points; 
 * var p = new Processing(canvas,
 * hypotheticalSketch);
 * 
 * $element.fadeIn("fast"); window.onresize = function(event) { var p = new
 * Processing(canvas, hypotheticalSketch); } }); }); }
 *  // assume everything in points is a string! 
 *  var hypotheticalSketch =
 * (function($p) 
 * { var points = { values:
 * JSON.parse(historicalData.points.values) }; var Label = (function() {
 * function Label() { var $this_1 = this;
 * 
 * function $superCstr() { $p.extendClassChain($this_1) }
 * 
 * function $constr_3(txt, x, y) { $superCstr();
 * 
 * var labelW = $p.textWidth(txt);
 * 
 * if (x + labelW + 20 > $p.width) { x -= labelW + 20; }
 * 
 * $p.fill(255); $p.noStroke(); $p.rectMode($p.CORNER); $p.rect(x + 10, y - 30,
 * labelW + 10, 22);
 * 
 * $p.fill(0); $p.text(txt, x + 15, y - 15); }
 * 
 * function $constr() { if (arguments.length === 3) { $constr_3.apply($this_1,
 * arguments); } else $superCstr(); } $constr.apply(null, arguments); } return
 * Label; })(); $p.Label = Label;
 * 
 * //var values = $p.createJavaArray('float', [20]); var plotX1 = 0, plotX2 = 0,
 * plotY1 = 0, plotY2 = 0; var canvasHeight = 415; // TODO: Change 420 so it's
 * not a constant! var canvasWidth = window.innerWidth - 420; var leftMargin =
 * 10; var topMargin = 40; var plotHeight = canvasHeight-(2*topMargin); var
 * plotWidth = canvasWidth-(2*leftMargin); var plotBottom = plotHeight +
 * topMargin; var tickRadius = 5; var timer = 0.0; var helvetica = null; var x1 =
 * 0, y1 = 0, x2 = 0, y2 = 0; var rx = 0, ry = 0; var w = 0, h = 0; var
 * hypoCount = 320; var amount = 9; var xhval = $p.createJavaArray('float',
 * [amount]); var yhval = $p.createJavaArray('float', [amount]); var count =
 * 301; function setup() { $p.size(canvasWidth, canvasHeight);
 * 
 * $p.smooth(); helvetica = $p.createFont("Helvetica-Bold", 14);
 * $p.textFont(helvetica);
 * 
 * generateValues();
 * 
 * plotX1 = leftMargin; plotX2 = $p.width - leftMargin; plotY1 = topMargin;
 * plotY2 = $p.height - topMargin;
 * 
 * y1 = 378; y2 = 100; todayLineLoc = 0;
 * 
 * //This affects the width of the box, but not the data. w = 620; h = 380;
 * 
 * rx = 20; ry = 100; } $p.setup = setup;
 * 
 * function draw() { $p.background(192, 192, 192); $p.fill(238, 238, 224);
 * $p.noStroke(); $p.rectMode($p.CORNERS); $p.rect(plotX1, plotY1, plotX2,
 * plotY2);
 * 
 * drawTickMarks();
 * 
 * $p.noFill(); $p.stroke(255, 128, 0); $p.strokeWeight(3);
 * 
 * //This is the general data line. $p.beginShape(); var x = 0, y = 0;
 *  // notice parseFloat method around extraction of data for (var i = 0; i <
 * points.values.length; i++) { x = $p.map(i, 0, points.values.length - 1,
 * plotX1, plotX2); y = $p.map(points.values[i], Y_MIN, Y_MAX, $p.height -
 * topMargin, $p.height - topMargin - plotHeight); if (i == historicalDataSize) {
 * todayLineLoc = x } $p.vertex(x, y); } $p.endShape();
 * 
 * $p.noFill(); $p.stroke(0, 143, 255); $p.strokeWeight(3);
 * 
 * $p.beginShape(); var x = 0, y = 0; for (i in futureData) { x = $p.map(i, 0,
 * futureData.length - 1, todayLineLoc, plotX2); y = $p.map(futureData[i],
 * Y_MIN, Y_MAX, $p.height - topMargin, $p.height - topMargin - plotHeight);
 * $p.vertex(x, y); } $p.endShape();
 * 
 * 
 * $p.noFill(); $p.stroke(0, 255, 0); $p.strokeWeight(3);
 * 
 * //This is the hypothetical data line.
 *  // $p.beginShape(); // var xv = 301; // var xamt = 9; // var dist =
 * $p.__int_cast(301) / 8 + 2; // var xh = $p.createJavaArray('float', [xamt]); //
 * var yh = $p.createJavaArray('float', [xamt]); // // yh =
 * generateYValues(xamt, 205); // for (var i = 0; i < xamt; i++) { // yhval[i] =
 * yh[i]; // } // for (var i = 0; i < xamt; i++) { // xh[i] = xv; // xhval[i] =
 * xh[i]; // xv = xv + dist; // } // for (var i = 0; i < xamt; i++) { //
 * $p.stroke(255, 0, 127); // $p.vertex(xh[i], yh[i]); // } // $p.endShape();
 * 
 * $p.stroke(104, 104, 104); $p.strokeWeight(4);
 * 
 * //This is the past-to-future seperation line. $p.line(todayLineLoc,
 * topMargin, todayLineLoc, plotHeight+topMargin);
 * 
 * //This outlines the graph. $p.rect(leftMargin, topMargin,
 * plotWidth+leftMargin, plotHeight+topMargin);
 * 
 * drawLineLabels(); var hoverSet = 0; for (var i = 0; i < points.values.length;
 * i++) { x = $p.map(i, 0, points.values.length - 1, plotX1, plotX2); y =
 * $p.map(points.values[i], Y_MIN, Y_MAX, $p.height - topMargin, $p.height -
 * topMargin - plotHeight);
 * 
 * var delta = $p.abs($p.mouseX - x); if (i < historicalDataSize) $p.stroke(255,
 * 128, 0); else $p.stroke(0, 143, 255);
 * 
 * $p.fill(255); $p.ellipse(x, y, 8, 8); if (hoverSet == 0 && (delta < 10) && (y >
 * plotY1) && (y < plotY2)) { $p.stroke(60); $p.fill(255, 200, 255);
 * $p.ellipse(x, y, 8, 8);
 * 
 * var labelVal = $p.round(points.values[i]); var label = new Label(labelVal + "
 * Visits", x, y);
 * 
 * hoverSet = 1; } } // don't need yet //highlightHypo(amount, xhval, yhval); }
 * $p.draw = draw;
 * 
 * function keyPressed() { generateValues(); } $p.keyPressed = keyPressed;
 * 
 * function generateValues() { for (var i = 0; i < points.values.length; i++) {
 * points.values[i] = points.values[i]; }
 * 
 * plotX1 = leftMargin; plotX2 = $p.width - plotX1; } $p.generateValues =
 * generateValues;
 * 
 * 
 * function drawLineLabels() { var labelX = leftMargin; var labelY = topMargin -
 * 15; var labelPadding = 30; var label1 = "HISTORICAL"; var label2 = "FUTURE";
 * var lineLength = 35; var lineLabelPadding = 5; var fontSize = 10; var lineY =
 * labelY - (fontSize / 2);
 * 
 * label1Start = labelX label2Start = label1Start + lineLength +
 * lineLabelPadding + $p.textWidth(label1) + labelPadding;
 * 
 * $p.textFont(helvetica, fontSize); $p.fill(64, 64, 64); $p.strokeWeight(3);
 * $p.stroke(255, 128, 0); $p.line(label1Start, lineY, label1Start + lineLength,
 * lineY); $p.stroke(0, 143, 255); $p.line(label2Start, lineY, label2Start +
 * lineLength, lineY); $p.text("HISTORICAL", label1Start + lineLength +
 * lineLabelPadding, labelY); $p.text("FUTURE", label2Start + lineLength +
 * lineLabelPadding, labelY); } $p.drawLineLabels = drawLineLabels;
 * 
 * function generateYValues(amt, start) { var ret = $p.createJavaArray('float',
 * [amt]); var increment = 14; ret[0] = start; for (var i = 1; i < amt; i++) {
 * if (i % 2 == 0) { ret[i] = start + increment; increment = increment + 7; }
 * else { ret[i] = start - increment; increment = increment - 10; } } return
 * ret; } $p.generateYValues = generateYValues;
 * 
 * function highlightHypo(amt, xh, yh) { var x = 0, y = 0; for (var i = 0; i <
 * amt; i++) { x = xh[i]; y = yh[i];
 * 
 * var delta = $p.abs($p.mouseX - x); if ((delta < 15) && (y > plotY1) && (y <
 * plotY2)) { $p.stroke(255); $p.fill(0); $p.ellipse(x, y, 8, 8);
 * 
 * var labelVal = $p.round(yh[i]); var label = new Label("" + labelVal, x, y); } } }
 * $p.highlightHypo = highlightHypo;
 * 
 * 
 * function drawTickMarks() { $p.textFont(helvetica, fontSize); $p.fill(0); var
 * startLoc = plotX1; var endLoc = todayLineLoc - ($p.textWidth(endDate)/2); var
 * futureEndLoc = leftMargin + plotWidth - $p.textWidth(futureEndDate); var
 * fontSize = 15; var labelYLoc = plotBottom + (fontSize + tickRadius + 5);
 * //Draw date labels $p.text(startDate, startLoc, labelYLoc); $p.text(endDate,
 * endLoc, labelYLoc); $p.text(futureEndDate, futureEndLoc, labelYLoc);
 * 
 * var tickUpper = y1 + (tickRadius); var tickLower = y1 - (tickRadius);
 * 
 * //Draw date tick marks $p.stroke(104, 104, 104); $p.strokeWeight(3);
 * $p.line(plotX1, tickUpper, plotX1, tickLower); $p.line(todayLineLoc,
 * tickUpper, todayLineLoc, tickLower); $p.line(plotX2, tickUpper, plotX2,
 * tickLower);
 *  } $p.drawTickMarks = drawTickMarks;
 *  })
 */
