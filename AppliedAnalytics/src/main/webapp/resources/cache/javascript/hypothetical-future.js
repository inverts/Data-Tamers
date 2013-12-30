/**
 * hypothetical-future.js
 */

$(function() {
	updateHypotheticalWidget('hypotheticalWidget');
});

/*
 * External vars that this file relies on:
 * 
 * historicalData
 * futureData
 * historicalDataSize
 * Y_MIN
 * Y_MAX
 * startDate
 * endDate
 * 
 */
function updateHypotheticalWidget(id) {
	
	var $element = $('#' + id);
	$.post("HypotheticalFuture", null, 
		function(response) {
			$element.fadeOut("fast", function() {
				$element.html(response);
				var canvas = document.getElementById('hypotheticalFutureData');
				
				
				//points = HypotheticalFutureData.points;		
				var p = new Processing(canvas, hypotheticalSketch);
				
				$element.fadeIn("fast");
				window.onresize = function(event) {
					var p = new Processing(canvas, hypotheticalSketch);
				}
			});
	});		
}

// assume everything in points is a string!
var hypotheticalSketch =
	(function($p) {
		var points = { values: JSON.parse(historicalData.points.values) };
	    var Label = (function() {
	        function Label() {
	            var $this_1 = this;

	            function $superCstr() {
	                $p.extendClassChain($this_1)
	            }

	            function $constr_3(txt, x, y) {
	                $superCstr();

	                var labelW = $p.textWidth(txt);

	                if (x + labelW + 20 > $p.width) {
	                    x -= labelW + 20;
	                }

	                $p.fill(255);
	                $p.noStroke();
	                $p.rectMode($p.CORNER);
	                $p.rect(x + 10, y - 30, labelW + 10, 22);

	                $p.fill(0);
	                $p.text(txt, x + 15, y - 15);
	            }

	            function $constr() {
	                if (arguments.length === 3) {
	                    $constr_3.apply($this_1, arguments);
	                } else $superCstr();
	            }
	            $constr.apply(null, arguments);
	        }
	        return Label;
	    })();
	    $p.Label = Label;

	    //var values = $p.createJavaArray('float', [20]);
	    var plotX1 = 0,
	        plotX2 = 0,
	        plotY1 = 0,
	        plotY2 = 0;
	    var canvasHeight = 415;
	    // TODO: Change 420 so it's not a constant!
	    var canvasWidth = window.innerWidth - 420;
	    var leftMargin = 10;
	    var topMargin = 40;
	    var plotHeight = canvasHeight-(2*topMargin);
	    var plotWidth = canvasWidth-(2*leftMargin);
	    var plotBottom = plotHeight + topMargin;
	    var tickRadius = 5;
	    var timer = 0.0;
	    var helvetica = null;
	    var x1 = 0,
	        y1 = 0,
	        x2 = 0,
	        y2 = 0;
	    var rx = 0,
	        ry = 0;
	    var w = 0,
	        h = 0;
	    var hypoCount = 320;
	    var amount = 9;
	    var xhval = $p.createJavaArray('float', [amount]);
	    var yhval = $p.createJavaArray('float', [amount]);
	    var count = 301;
	    function setup() {
	        $p.size(canvasWidth, canvasHeight);

	        $p.smooth();
	        helvetica = $p.createFont("Helvetica-Bold", 14);
	        $p.textFont(helvetica);

	        generateValues();

	        plotX1 = leftMargin;
	        plotX2 = $p.width - leftMargin;
	        plotY1 = topMargin;
	        plotY2 = $p.height - topMargin;

	        y1 = 378;
	        y2 = 100;
	        todayLineLoc = 0;

	        //This affects the width of the box, but not the data.
	        w = 620;
	        h = 380;

	        rx = 20;
	        ry = 100;
	    }
	    $p.setup = setup;

	    function draw() {
	        $p.background(192, 192, 192);
	        $p.fill(238, 238, 224);
	        $p.noStroke();
	        $p.rectMode($p.CORNERS);
	        $p.rect(plotX1, plotY1, plotX2, plotY2);

	        drawTickMarks();

	        $p.noFill();
	        $p.stroke(255, 128, 0);
	        $p.strokeWeight(3);
	        
	        //This is the general data line.
	        $p.beginShape();
	        var x = 0,
	            y = 0;
	        
	        // notice parseFloat method around extraction of data
	        for (var i = 0; i < points.values.length; i++) {
	            x = $p.map(i, 0, points.values.length - 1, plotX1, plotX2);
	            y = $p.map(points.values[i], Y_MIN, Y_MAX, $p.height - topMargin, $p.height - topMargin - plotHeight);
	            if (i == historicalDataSize) {
	            	todayLineLoc = x
	            }
	            $p.vertex(x, y);
	        }
	        $p.endShape();

	        $p.noFill();
	        $p.stroke(0, 143, 255);
	        $p.strokeWeight(3);

	        $p.beginShape();
	        var x = 0,
	            y = 0;
	        for (i in futureData) {
	            x = $p.map(i, 0, futureData.length - 1, todayLineLoc, plotX2);
	            y = $p.map(futureData[i], Y_MIN, Y_MAX, $p.height - topMargin, $p.height - topMargin - plotHeight);
	            $p.vertex(x, y);
	        }
	        $p.endShape();


	        $p.noFill();
	        $p.stroke(0, 255, 0);
	        $p.strokeWeight(3);
	        
	        //This is the hypothetical data line.
	        /*
	        $p.beginShape();
	        var xv = 301;
	        var xamt = 9;
	        var dist = $p.__int_cast(301) / 8 + 2;
	        var xh = $p.createJavaArray('float', [xamt]);
	        var yh = $p.createJavaArray('float', [xamt]);

	        yh = generateYValues(xamt, 205);
	        for (var i = 0; i < xamt; i++) {
	            yhval[i] = yh[i];
	        }
	        for (var i = 0; i < xamt; i++) {
	            xh[i] = xv;
	            xhval[i] = xh[i];
	            xv = xv + dist;
	        }
	        for (var i = 0; i < xamt; i++) {
	            $p.stroke(255, 0, 127);
	            $p.vertex(xh[i], yh[i]);
	        }
	        $p.endShape();
	         */
	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(4);

	        //This is the past-to-future seperation line.
	        $p.line(todayLineLoc, topMargin, todayLineLoc, plotHeight+topMargin);
	        
	        //This outlines the graph.
	        $p.rect(leftMargin, topMargin, plotWidth+leftMargin, plotHeight+topMargin);
	        
	        drawLineLabels();
	        var hoverSet = 0;
	        for (var i = 0; i < points.values.length; i++) {
	            x = $p.map(i, 0, points.values.length - 1, plotX1, plotX2);
	            y = $p.map(points.values[i], Y_MIN, Y_MAX, $p.height - topMargin, $p.height - topMargin - plotHeight);

	            var delta = $p.abs($p.mouseX - x);
	            if (i < historicalDataSize)
	            	$p.stroke(255, 128, 0);
	            else
	    	        $p.stroke(0, 143, 255);
	            	
                $p.fill(255);
                $p.ellipse(x, y, 8, 8);
	            if (hoverSet == 0 && (delta < 10) && (y > plotY1) && (y < plotY2)) {
	                $p.stroke(60);
	                $p.fill(255, 200, 255);
	                $p.ellipse(x, y, 8, 8);

	                var labelVal = $p.round(points.values[i]);
	                var label = new Label(labelVal + " Visits", x, y);
	                
	                hoverSet = 1;
	            }
	        }
	        /* don't need yet */
	        //highlightHypo(amount, xhval, yhval);
	    }
	    $p.draw = draw;

	    function keyPressed() {
	        generateValues();
	    }
	    $p.keyPressed = keyPressed;

	    function generateValues() {
	        for (var i = 0; i < points.values.length; i++) {
	            points.values[i] = points.values[i];
	        }

	        plotX1 = leftMargin;
	        plotX2 = $p.width - plotX1;
	    }
	    $p.generateValues = generateValues;


	    function drawLineLabels() {
			var labelX = leftMargin;
			var labelY = topMargin - 15;
			var labelPadding = 30;
			var label1 = "HISTORICAL";
			var label2 = "FUTURE";
			var lineLength = 35;
			var lineLabelPadding = 5;
			var fontSize = 10;
			var lineY = labelY - (fontSize / 2);
			
			label1Start = labelX
			label2Start = label1Start + lineLength + lineLabelPadding + $p.textWidth(label1) + labelPadding;
			
	        $p.textFont(helvetica, fontSize);
	        $p.fill(64, 64, 64);
	        $p.strokeWeight(3);
	        $p.stroke(255, 128, 0);
	        $p.line(label1Start, lineY, label1Start + lineLength, lineY);
	        $p.stroke(0, 143, 255);
	        $p.line(label2Start, lineY, label2Start + lineLength, lineY);
	        $p.text("HISTORICAL", label1Start + lineLength + lineLabelPadding, labelY);
	        $p.text("FUTURE", label2Start + lineLength + lineLabelPadding, labelY);
	    }
	    $p.drawLineLabels = drawLineLabels;

	    function generateYValues(amt, start) {
	        var ret = $p.createJavaArray('float', [amt]);
	        var increment = 14;
	        ret[0] = start;
	        for (var i = 1; i < amt; i++) {
	            if (i % 2 == 0) {
	                ret[i] = start + increment;
	                increment = increment + 7;
	            } else {
	                ret[i] = start - increment;
	                increment = increment - 10;
	            }
	        }
	        return ret;
	    }
	    $p.generateYValues = generateYValues;

	    function highlightHypo(amt, xh, yh) {
	        var x = 0,
	            y = 0;
	        for (var i = 0; i < amt; i++) {
	            x = xh[i];
	            y = yh[i];

	            var delta = $p.abs($p.mouseX - x);
	            if ((delta < 15) && (y > plotY1) && (y < plotY2)) {
	                $p.stroke(255);
	                $p.fill(0);
	                $p.ellipse(x, y, 8, 8);

	                var labelVal = $p.round(yh[i]);
	                var label = new Label("" + labelVal, x, y);
	            }
	        }
	    }
	    $p.highlightHypo = highlightHypo;


	    function drawTickMarks() {
	        $p.textFont(helvetica, fontSize);
	        $p.fill(0);
	        var startLoc = plotX1;
	        var endLoc = todayLineLoc - ($p.textWidth(endDate)/2);
	        var futureEndLoc = leftMargin + plotWidth - $p.textWidth(futureEndDate);
	        var fontSize = 15;
	        var labelYLoc = plotBottom + (fontSize + tickRadius + 5);
	        //Draw date labels
	        $p.text(startDate, startLoc, labelYLoc);
	        $p.text(endDate, endLoc, labelYLoc);
	        $p.text(futureEndDate, futureEndLoc, labelYLoc);
	        
	        var tickUpper = y1 + (tickRadius);
	        var tickLower = y1 - (tickRadius);
	        
	        //Draw date tick marks
	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(3);
	        $p.line(plotX1, tickUpper, plotX1, tickLower);
	        $p.line(todayLineLoc, tickUpper, todayLineLoc, tickLower);
	        $p.line(plotX2, tickUpper, plotX2, tickLower);
	        
	    }
	    $p.drawTickMarks = drawTickMarks;

	})

