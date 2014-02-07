/**
 * revenue-sources.js
 */

$(function() {
	// Temporary
	GetRevenueSourcesWidget();
});


function GetRevenueSourcesWidget() {

	/*
	var $element = $('#revenueSource');
	$.post(applicationRoot + "/RevenueSources", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				
				$element = $('<div>').attr({ 'id': 'revenueSource', 'class': 'w_container'})
				 					 .prop('draggable', true)
				 					 .appendTo('.dashboard-content')
				 					 .append(response);
			}

			var canvas = document.getElementById('revenueSourcesData');
				
			var p = new Processing(canvas, revenueSketch);
			
			
	});	*/	
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
	        sources = $p.createJavaArray('String', [5]);
	        values = $p.createJavaArray('float', [5]);

	        sources[0] = "Google";
	        sources[1] = "Bing";
	        sources[2] = "Facebook";
	        sources[3] = "Yelp";
	        sources[4] = "Twitter";
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
	        $p.fill(255);
	        $p.text("12.1%", w + 50, h - 20);
	        $p.text("11.6%", w - 75, h + 25);
	        $p.text("44.5%", w + 10, h + 65);
	        $p.text("9.4%", w + 15, h - 55);
	        $p.text("22.6%", w - 60, h - 40);
	    }
	    $p.drawAmounts = drawAmounts;

	    function drawLegend() {
	        $p.textFont(font, 10);
	        $p.fill(51, 51, 255);
	        $p.rect(235, 50, 10, 10);
	        $p.fill(0);
	        $p.text("Google", 250, 60);

	        $p.fill(255, 0, 0);
	        $p.rect(235, 65, 10, 10);
	        $p.fill(0);
	        $p.text("Bing", 250, 75);

	        $p.fill(255, 128, 0);
	        $p.rect(235, 80, 10, 10);
	        $p.fill(0);
	        $p.text("Facebook", 250, 90);

	        $p.fill(0, 153, 0);
	        $p.rect(235, 95, 10, 10);
	        $p.fill(0);
	        $p.text("Yelp", 250, 105);

	        $p.fill(153, 0, 153);
	        $p.rect(235, 110, 10, 10);
	        $p.fill(0);
	        $p.text("Twitter", 250, 120);
	    }
	    $p.drawLegend = drawLegend;
	})