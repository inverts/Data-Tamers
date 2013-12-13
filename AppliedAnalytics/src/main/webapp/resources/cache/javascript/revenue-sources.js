/**
 * revenue-sources.js
 */

$(document).ready(function() {
	
	GetWidget('revenueSources');

});


function GetWidget(id, source, change) {
	
	var $element = $('#' + id);
	$.post("RevenueSources", null, 
		function(response) {
			$element.empty().append(response);

			var canvas = document.getElementById('revenueSourcesData');
				
			var p = new Processing(canvas, revenueSketch);
			
			
	});		
}

// assume everything in points is a string!

// * GET DATA *
//var points = { values: JSON.parse(RevenueSourcesData.points.values) };


var revenueSketch = 
	(function($p) {

	    var sources = null;
	    var values = null;
	    var font = null;

	    function setup() {
	        $p.size(320, 300);
	        $p.smooth();
	        font = $p.createFont("Helvetica-Bold", 14);
	        $p.textFont(font);
	    }
	    $p.setup = setup;

	    function draw() {
	        $p.background(192, 192, 192);
	        drawTitle();
	        drawCircleData();
	        drawLegend();
	        drawAmounts();
	    }
	    $p.draw = draw;

	    function drawTitle() {
	        $p.textFont(font, 16);
	        $p.fill(0);
	        $p.text("Revenue Sources", 10, 20);
	    }
	    $p.drawTitle = drawTitle;

	    function drawCircleData() {
	        $p.fill(0);
	        $p.ellipse($p.width / 2 - 40, $p.height / 2, 200, 200);
	        $p.stroke(192, 192, 192);
	        $p.fill(51, 51, 255);
	        $p.arc($p.width / 2 - 40, $p.height / 2, 200, 200, 0, $p.HALF_PI + $p.QUARTER_PI);
	        $p.fill(255, 0, 0);
	        $p.arc($p.width / 2 - 40, $p.height / 2, 200, 200, $p.HALF_PI + $p.QUARTER_PI, $p.PI);
	        $p.fill(255, 128, 0);
	        $p.arc($p.width / 2 - 40, $p.height / 2, 200, 200, $p.PI, (3 * $p.PI) / 2);
	        $p.fill(150, 0, 153);
	        $p.arc($p.width / 2 - 40, $p.height / 2, 200, 200, (3 * $p.PI) / 2, (7 * $p.PI) / 4);
	        $p.fill(0, 153, 0);
	        $p.arc($p.width / 2 - 40, $p.height / 2, 200, 200, (7 * $p.PI) / 4, $p.TWO_PI);

	        $p.fill(192, 192, 192);
	        $p.ellipse($p.width / 2 - 40, $p.height / 2, 90, 90);
	    }
	    $p.drawCircleData = drawCircleData;

	    function drawAmounts() {
	        var w = $p.width / 2 - 40;
	        var h = $p.height / 2;
	        var count = 0;
	        $p.fill(255);
	        $p.text(values[0], w + 50, h - 20);
	        count++;
	        $p.text(values[1], w - 75, h + 25);
	        count++;
	        $p.text(values[2], w + 10, h + 65);
	        count++;
	        $p.text(values[3], w + 15, h - 55);
	        count++;
	        $p.text(values[4], w - 60, h - 40);
	        count++;
	    }
	    $p.drawAmounts = drawAmounts;

	    function drawLegend() {
	        var count = 0;
	        $p.textFont(font, 10);
	        $p.fill(51, 51, 255);
	        $p.rect(235, 50, 10, 10);
	        $p.fill(0);
	        $p.text(sources[count], 250, 60);
	        count++;
	        $p.fill(255, 0, 0);
	        $p.rect(235, 65, 10, 10);
	        $p.fill(0);
	        $p.text(sources[count], 250, 75);
	        count++;
	        $p.fill(255, 128, 0);
	        $p.rect(235, 80, 10, 10);
	        $p.fill(0);
	        $p.text(sources[count], 250, 90);
	        count++;
	        $p.fill(0, 153, 0);
	        $p.rect(235, 95, 10, 10);
	        $p.fill(0);
	        $p.text(sources[count], 250, 105);
	        count++;
	        $p.fill(153, 0, 153);
	        $p.rect(235, 110, 10, 10);
	        $p.fill(0);
	        $p.text(sources[count], 250, 120);
	    }
	    $p.drawLegend = drawLegend;
	})