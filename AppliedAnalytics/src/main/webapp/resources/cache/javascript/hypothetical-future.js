/**
 * hypothetical-future.js
 */

$(document).ready(function() {
	
	updateHypotheticalWidget('hypotheticalWidget');

});


function updateHypotheticalWidget(id, dimension, change) {
	
	var $element = $('#' + id);
	$.post("HypotheticalFuture", { dimension: dimension, change: change }, 
		function(response) {
			$element.fadeOut("fast", function() {
				$element.html(response);
				var canvas = document.getElementById('hypotheticalFutureData');
				
				
				//points = HypotheticalFutureData.points;		
				var p = new Processing(canvas, hypotheticalSketch);
				
				// Setup change event
				$('#hypotheticalWidget select').on('change', function() {
					updateHypotheticalWidget(id, $('#traffic_source').val(), $('#change_pct').val());
				});
				$element.fadeIn("fast");

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
	    var leftMargin = 20;
	    var topMargin = 100;
	    var plotHeight = 250;
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
	        $p.size(640, 480);

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
	        x1 = 300;
	        x2 = 300;

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
	            if (i == historicalLength) {
		        	   x1 = x;
		        	   x2 = x;
		           }
	            $p.vertex(x, y);
	        }
	        $p.endShape();

	        $p.noFill();
	        $p.stroke(255, 0, 0);
	        $p.strokeWeight(3);
	        
	        //This is the hypothetical data line.
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

	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(4);
	        
	        //This is the past-to-future separation line.
	        $p.line(x1, y1, x2, y2);
	        
	        //This outlines the graph.
	        $p.rect(rx, ry, w, h);
	        drawLineLabels();
	        var hoverSet = 0;
	        for (var i = 0; i < points.values.length; i++) {
	            x = $p.map(i, 0, points.values.length - 1, plotX1, plotX2);
	            y = $p.map(points.values[i], Y_MIN, Y_MAX, $p.height - topMargin, $p.height - topMargin - plotHeight);

	            var delta = $p.abs($p.mouseX - x);
	            if (hoverSet == 0 && (delta < 10) && (y > plotY1) && (y < plotY2)) {
	                $p.stroke(255);
	                $p.fill(0);
	                $p.ellipse(x, y, 8, 8);

	                var labelVal = $p.round(points.values[i]);
	                var label = new Label("" + labelVal, x, y);
	                
	                hoverSet = 1;
	            }
	        }
	        highlightHypo(amount, xhval, yhval);
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
	        $p.textFont(helvetica, 10);
	        $p.fill(64, 64, 64);
	        $p.text("UNCHANGED", 60, 85);
	        $p.text("HYPOTHETICAL", 220, 85);
	        $p.stroke(255, 128, 0);
	        $p.strokeWeight(2);
	        $p.line(20, 80, 55, 80);
	        $p.stroke(255, 0, 127);
	        $p.strokeWeight(2);
	        $p.line(180, 80, 215, 80);
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
	        var yaxis = y1 + 20;
	        var xaxis = x1 - 270;
	        var y_1 = y1 - 5;
	        var y_2 = y1 + 5;

	        $p.textFont(helvetica, 10);
	        $p.fill(0);
	        $p.text("", x1 - 15, y1 + 20);
	        $p.text("SUNDAY", x1 - 270, yaxis);
	        $p.text("SUNDAY", xaxis + 90, yaxis);
	        $p.text("SUNDAY", xaxis + 180, yaxis);
	        $p.text("SUNDAY", xaxis + 330, yaxis);
	        $p.text("SUNDAY", xaxis + 420, yaxis);
	        $p.text("SUNDAY", xaxis + 510, yaxis);

	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(3);
	        $p.line(xaxis + 17, y_1, xaxis + 17, y_2);
	        $p.line(xaxis + 107, y_1, xaxis + 107, y_2);
	        $p.line(xaxis + 197, y_1, xaxis + 197, y_2);
	        $p.line(xaxis + 347, y_1, xaxis + 347, y_2);
	        $p.line(xaxis + 437, y_1, xaxis + 437, y_2);
	        $p.line(xaxis + 527, y_1, xaxis + 527, y_2);

	        $p.text("9/29", x1 - 270 + 10, yaxis + 15);
	        $p.text("10/6", xaxis + 90 + 10, yaxis + 15);
	        $p.text("10/13", xaxis + 180 + 7, yaxis + 15);
	        $p.text("10/20", xaxis + 330 + 5, yaxis + 15);
	        $p.text("10/27", xaxis + 420 + 5, yaxis + 15);
	        $p.text("11/3", xaxis + 510 + 8, yaxis + 15);
	    }
	    $p.drawTickMarks = drawTickMarks;

	})

