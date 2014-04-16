/**
 * line.js
 */

/**
 * A function that draws a line representing a provided slope.
 * Places this line inside a provided container.
 * The background of the line is colored based on the value of the slope.
 * 
 * Returns the canvas element that the line was drawn on.
 * 
 * @author Dave Wong
 * @param slope The slope to use
 * @param container The DOM element container or qualified selector.
 * @param width The width the container and canvas will be sized to.
 * @param height The height the container and canvas will be sized to.
 */
function drawSlope(slope, container, width, height, lineWidth) {
	//Set up the container and canvas.
	$(container).css("width", width);
	$(container).css("height", height);
	var c = document.createElement("canvas");
	$(c).prop("width", width)
	$(c).prop("height", height)
	$(c).css("width", width);
	$(c).css("height", height);
	var ctx=c.getContext("2d");

	//Set background color based on slope with some fun color math :)
	maxSlope = 100.0;
	maxLog = Math.log(maxSlope);
	colorSlope = Math.log(Math.abs(slope)); //We use the log to accelerate the transition from green to red.
	intensity = 225 - Math.round((Math.min(colorSlope, maxSlope) * 110.0) / maxLog); 
	ctx.fillStyle = (slope < 0) ? 'rgb(225,' + intensity + ',0)' : 'rgb(' + intensity + ',225,0)';
	ctx.fillRect(0, 0, width, height);
	
	//Scale slope for better viewing - this means a slope of 5 will run the diagonal.
	slope = slope / 5
	
	//Calculate start and end points depending on the slope.
	if (Math.abs(slope) > 1) {
		var y1 = height;
		var y2 = 0;
		var x1 = (width/2)-((1/slope) * (width/2))
		var x2 = (width/2)+((1/slope) * (width/2))
	} else {
		var y1 = (height/2) + (slope * (height/2));
		var y2 = (height/2) - (slope * (height/2));
		var x1 = 0;
		var x2 = width;
	}
	
	//Draw the line.
	ctx.beginPath();
	ctx.moveTo(x1, y1);
	ctx.lineTo(x2, y2);
	ctx.lineWidth = lineWidth;
	ctx.stroke();
	$(container).append(c);
	return c;
}




	/* function declaration */
		function drawLine(slope, object) {
			var svg = d3.select(object).append("svg");			
				
			// append background rectangle
			var rect = svg.append("rect")
			// TODO: Center the box around the plot x,y attr
				.attr("x", 50)
				.attr("y", 20)
				.attr("rx", 10)
				.attr("ry", 10)
				.attr("width", 100)
				.attr("height", 100)
				.style("fill", '#F8F8F8')
				.style("stroke", "black");
						
			
			
			// create line
			nv.addGraph(function() {
				  var chart = nv.models.lineChart()
				                .transitionDuration(350)  
				                .showYAxis(false)        
				                .showXAxis(false)        
				                .showLegend(false)
				                .width(100)
				                .height(100)
				                .forceY([-1,1])
				                .useInteractiveGuideline(false) 
				                .tooltips(false) 
				  ;		

				  svg.datum(formatData())         //Populate the <svg> element with chart data...
				      .call(chart);          //Finally, render the chart!

				  //Update the chart when window resizes.
				  nv.utils.windowResize(function() { chart.update(); });
				  return chart;
				});
				
				
			function formatData(){
				var data = [];
				var x1, x2, y1, y2;
				
				if(slope <= 1){
					y1 = -1;
					x1 = -1*slope;
					y2 = 1;
					x2 = slope;
				}
				else {
					x1 = -1/slope;
					y1 = -1;
					y2 = 1/slope;
					x2 = 1;			
				}
				
				for(var i=0; i < 1; i++){
					for(var j=0; j < 1; j++) {
						data[i].values.push({
							x:x1,
							y:y1
						},
						{
							x:x2,
							y:y2
						});
					}
				} 
				return data;
			}
			// end chart 
}; 
