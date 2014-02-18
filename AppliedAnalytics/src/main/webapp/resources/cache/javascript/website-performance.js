/**
 * website-performance.js
 */

$(function() {
	// Temporary
	GetWebsitePerformanceWidget();

});


function GetWebsitePerformanceWidget() {
	
	var $element = $('#websitePerformance');
	
	$.post("WebsitePerformance", null, 
		function(response) {
			if ($element.length > 0)
				$element.empty().append(response);
			else {
				$element = $('<div>').attr({ 'id': 'websitePerformance', 'class': 'w_container'})
									 .prop('draggable', true)
									 .appendTo('.dashboard-content')
									 .append(response);
			}
			
		//	var canvas = document.getElementById('websitePerformanceData');
		//	var p = new Processing(canvas, performanceSketch);

	});		
}

// assume everything in points is a string!

// * GET DATA *
//var points = { values: JSON.parse(RevenueSourcesData.points.values) };


var performanceSketch = 
	(function($p) {

	    var values = $p.createJavaArray('float', [20]);
	    var plotX1 = 0,
	        plotX2 = 0,
	        plotY1 = 0,
	        plotY2 = 0;
		var canvasHeight = 580;
		var canvasWidth = 640;
	    var leftMargin = 20;
	    var topMargin = 100;
	    var plotHeight = 250;
	    var plotWidth = canvasWidth - (leftMargin * 2);
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
	    var font = null;
	    var points = null;

	    var pageNames = null;
	    var exitRates = null;
	    var visits = null;
	    var bounceRates = null;

	    function setup() {
	        $p.size(640, 580);

	        $p.smooth();
	        font = $p.createFont("Helvetica-Bold", 14);
	        $p.textFont(font);

	        plotX1 = leftMargin;
	        plotX2 = $p.width - leftMargin;
	        plotY1 = topMargin;
	        plotY2 = 480 - topMargin;

	        x1 = 500;
	        y1 = 378;
	        x2 = 500;
	        y2 = 100;

	        w = 620;
	        h = 380;

	        rx = 20;
	        ry = 100;

	        initData();
	    }
	    $p.setup = setup;

	    function draw() {
	        $p.background(192, 192, 192);
	        $p.fill(238, 238, 224);
	        $p.noStroke();
	        $p.rectMode($p.CORNERS);
	        $p.rect(plotX1, plotY1, plotX2, plotY2);

	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(4);
	        $p.rect(rx, ry, w, h);

	        drawData();
	        drawTitles();
	        drawLabels();
	        drawTable();
	    }
	    $p.draw = draw;

	    function drawTable() {
	        $p.textFont(font, 14);
	        var x = plotX1;
	        var y = plotY1 + 325;
	        $p.fill(0);
	        $p.text("WEBSITE PERFORMANCE", x, y);
	        $p.textFont(font, 12);
	        $p.text("Page URL", x, y + 12);
	        $p.text("% of visits", x + 245, y + 12);
	        $p.text("Exit Rate", x + 325, y + 12);
	        $p.fill(204, 229, 255);
	        $p.rect(x, y + 15, 300, 20);
	        $p.rect(x, y + 15 + 25, 300, 20);
	        $p.rect(x, y + 15 + 25 + 25, 300, 20);
	        $p.rect(x, y + 15 + 25 + 25 + 25, 300, 20);
	        $p.rect(x, y + 15 + 25 + 25 + 25 + 25, 300, 20);
	        $p.fill(255, 153, 204);
	        $p.rect(x + 325, y + 15, 50, 20);
	        $p.rect(x + 325, y + 15 + 25, 50, 20);
	        $p.rect(x + 325, y + 15 + 25 + 25, 50, 20);
	        $p.rect(x + 325, y + 15 + 25 + 25 + 25, 50, 20);
	        $p.rect(x + 325, y + 15 + 25 + 25 + 25 + 25, 50, 20);

	        $p.fill(0);
	        $p.text(pageNames[0], x + 5, y + 30);
	        $p.text(pageNames[1], x + 5, y + 30 + 25);
	        $p.text(pageNames[2], x + 5, y + 30 + 25 + 25);
	        $p.text(pageNames[3], x + 5, y + 30 + 25 + 25 + 25);
	        $p.text(pageNames[4], x + 5, y + 30 + 25 + 25 + 25 + 25);
	        $p.text(visits[0], x + 250, y + 30);
	        $p.text(visits[1], x + 250, y + 30 + 25);
	        $p.text(visits[2], x + 250, y + 30 + 25 + 25);
	        $p.text(visits[3], x + 250, y + 30 + 25 + 25 + 25);
	        $p.text(visits[4], x + 250, y + 30 + 25 + 25 + 25 + 25);
	        $p.text(exitRates[0], x + 325, y + 30);
	        $p.text(exitRates[1], x + 325, y + 30 + 25);
	        $p.text(exitRates[2], x + 325, y + 30 + 25 + 25);
	        $p.text(exitRates[3], x + 325, y + 30 + 25 + 25 + 25);
	        $p.text(exitRates[4], x + 325, y + 30 + 25 + 25 + 25 + 25);
	    }
	    $p.drawTable = drawTable;

	    function drawTitles() {
	        $p.textFont(font, 16);
	        $p.fill(0);
	        $p.text("Website Performance", plotX1, 85);
	    }
	    $p.drawTitles = drawTitles;

	    
	    function drawData() {
	        var x = $p.mouseX;
	        var y = $p.mouseY;
	        var data = "";
	        var labelWidth = $p.textWidth(data);
	        if (x + labelWidth + 20 > $p.width) {
	            x -= labelWidth + 20;
	        }
	        $p.noFill();
	        $p.noStroke();
	        $p.rectMode($p.CORNER);
	        $p.rect(x + 10, y - 30, labelWidth + 10, 22);

	        $p.fill(0);
	        $p.text(data, x + 15, y - 15);
	    }
	    $p.drawData = drawData;

	    function drawLabels() {
	        var size = 0;
	        var dist = plotWidth / 6;
	        $p.textFont(font, 10);

	        $p.stroke(104, 104, 104);
	        $p.strokeWeight(4);
	        $p.line(plotX1 + dist, plotY1 + 275, plotX1 + dist, plotY1 + 285);
	        $p.line(plotX1 + 2 * dist, plotY1 + 275, plotX1 + 2 * dist, plotY1 + 285);
	        $p.line(plotX1 + 3 * dist, plotY1 + 275, plotX1 + 3 * dist, plotY1 + 285);
	        $p.line(plotX1 + 4 * dist, plotY1 + 275, plotX1 + 4 * dist, plotY1 + 285);
	        $p.line(plotX1 + 5 * dist, plotY1 + 275, plotX1 + 5 * dist, plotY1 + 285);

	        $p.text(pageNames[0], plotX1 + 65, plotY1 + 300);
	        $p.text(pageNames[1], plotX1 + 65 + 115, plotY1 + 300);
	        $p.text(pageNames[2], plotX1 + 65 + 110 + 90, plotY1 + 300);
	        $p.text(pageNames[3], plotX1 + 65 + 110 + 90 + 130, plotY1 + 300);
	        $p.text(pageNames[4], plotX1 + 65 + 110 + 90 + 130 + 90, plotY1 + 300);

	        drawBars();
	    }
	    $p.drawLabels = drawLabels;

	    function initData() {
	        pageNames = $p.createJavaArray('String', [5]);
	        pageNames[0] = "/Search/Results/";
	        pageNames[1] = "/Pos-Systems/";
	        pageNames[2] = "/2013/Category/Product/";
	        pageNames[3] = "Supplies/Books/";
	        pageNames[4] = "Supplies/Audio-Books/";

	        exitRates = $p.createJavaArray('float', [5]);
	        exitRates[0] = 70;
	        exitRates[1] = 21;
	        exitRates[2] = 5;
	        exitRates[3] = 45;
	        exitRates[4] = 12;

	        visits = $p.createJavaArray('float', [5]);
	        visits[0] = 13;
	        visits[1] = 46;
	        visits[2] = 5;
	        visits[3] = 25;
	        visits[4] = 36;

	        bounceRates = $p.createJavaArray('float', [5]);
	        bounceRates[0] = 12;
	        bounceRates[1] = 38;
	        bounceRates[2] = 13;
	        bounceRates[3] = 25;
	        bounceRates[4] = 90;
	    }
	    $p.initData = initData;

	    function drawBars() {
	        var H = 480;
	        var w = 15;
	        var h1 = 0,
	            h2 = 0,
	            h3 = 0;
	        var h11 = 0,
	            h21 = 0,
	            h31 = 0;
	        var x1 = 0,
	            x2 = 0,
	            y1 = 0,
	            y2 = 0;
	        var c1 = $p.color(120, 45, 45);
	        var c2 = $p.color(102, 102, 255);
	        var c3 = $p.color(51, 153, 255);
			var bottom = topMargin+plotHeight+30;
	        var dist = plotWidth / 6;
			x1 = plotX1 + dist - (1.5*w);
			
			for (i = 0; i < 5; i++) {
				h1 = $p.map(exitRates[i], 0, 200, plotY2 - topMargin, plotY2 - topMargin - plotHeight);
				$p.fill(c1);
				$p.strokeWeight(0.5);
				$p.stroke(c1);
				$p.rect(x1,  bottom, w, -h1);
				x1 = x1 + w;
				h2 = $p.map(visits[i], 0, 200, plotY2 - topMargin, plotY2 - topMargin - plotHeight);
				$p.fill(c2);
				$p.stroke(c2);
				$p.rect(x1, bottom, w, -h2);
				x1 = x1 + w;
				h3 = $p.map(bounceRates[i], 0, 200, plotY2 - topMargin, plotY2 - topMargin - plotHeight);
				$p.fill(c3);
				$p.stroke(c3);
				$p.rect(x1, bottom, w, -h3);
				x1 = x1 + w;
				x1 += dist - (3*w);
			}
	    }
	    $p.drawBars = drawBars;

	})

	    