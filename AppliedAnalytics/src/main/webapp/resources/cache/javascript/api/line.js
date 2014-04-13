/**
 * line.js
 */

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
				.attr("width", $this.width() / 2-50)
				.attr("height", $this.width() / 2-50)
				.style("fill", '#F8F8F8')
				.style("stroke", "black");
						
			
			
			// create line
			nv.addGraph(function() {
				  var chart = nv.models.lineChart()
				                .transitionDuration(350)  
				                .showYAxis(false)        
				                .showXAxis(false)        
				                .showLegend(false)
				                .width($this.width() / 2 - 10)
				                .height($this.width() / 2 - 10)
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
				data.push({
					key: sdata.sourceName,
					values:[]
				});
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
