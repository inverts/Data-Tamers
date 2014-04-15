/**
 *  scatter.js
 */

(function ($) {
	registerKeyboardHandler = function(callback) {
		  d3.select(window).on("keydown", callback);  
		};

		/* global variables */
		var defaults = {
				'xLabel': '',
				'yLabel': '',
				'xKey' 	: '',
				'yKey'	: ''
		};

	/* function declaration */
	$.fn.scatter = function (params) {
		return this.each(function() {
			var $this = $(this);
			var settings = $.extend({}, defaults, params);
			if(settings.data == null){
				return;
			}			
			var sdata = settings.data;		

			var margin = {
					"top" : 20,
					"right" : 15,
					"bottom" : 50,
					"left" : 60
				};
			var width = $this.width();
			var height = $this.height();			


			var svg = d3.select(this).append("svg").attr("height", height).attr("width", width);


			// get size of object sdata
			Object.size = function(obj) {
			    var size = 0, key;
			    for (key in obj) {
			        if (obj.hasOwnProperty(key)) size++;
			    }
			    return size;
			};
			var len = Object.size(sdata);	
			var getColors = d3.scale.linear()
				.domain([0,1,2,3])
				.range(["green", "yellow", "red"]);
						
			
			var chart;
			nv.addGraph(function() {
			  chart = nv.models.scatterChart()
			                //.showDistX(true)
			                //.showDistY(true)
			  			    //.color(colors)
			  				.color(["rgb(0,102,0)","rgb(128,255,0)","rgb(255,255,0)","rgb(204,0,0)"])
			                .useVoronoi(false)
			                .transitionDuration(300)
			                .size(50)
			                ;

			  chart.xAxis.axisLabel("% Bounce Rates");
			 // chart.yAxis.axisLabel("Multipage Visit Rates (%)");
			  chart.yAxis.axisLabel("% Visits");
			  chart.xAxis.tickFormat(d3.format('.02f'));
			  chart.yAxis.tickFormat(d3.format('.02f'));

			  //increase size of points

			  //tooltip content
			  chart.tooltipContent(function(key, y, e, graph) {
			        var x = String(graph.point.x);
			        var y = String(graph.point.y);
			        var key = String(graph.point.kw);
			        tooltip_str = '<center><b>'+key+'</b></center>';
			        return tooltip_str;
			    });


			  //append data to chart
			  svg.datum(formatData(sdata))
			      .call(chart);

			  nv.utils.windowResize(chart.update);

			  return chart;
			});


			function getKeywords(sdata){
				var words = [];
				for(var i=0; i < len; i++){						  
					  words[i] = sdata[i].key;
				  }
				return words;
			}

			function formatData(sdata) { 
			  var data = [];
			  var xvals = [];
			  var yvals = [];
			  var words = [];		
			  var groups = [];		    
			  var keys = [];

			  /* accessing data : */
				// keywords -- sdata[index].key
				// values BOUNCE RATE -- sdata[index].values[0].x
				// values VISITS -- sdata[index].values[0].y
			  	// values GROUP -- sdata[index].values[0].group
			  for(var i=0; i < len; i++){						  
				  xvals[i] = sdata[i].values[0].x;
				  yvals[i] = sdata[i].values[0].y;
				  words[i] = sdata[i].key;
				  groups[i] = sdata[i].values[0].group;
			  }
  
			  keys[0] = ("best");
			  keys[3] = ("worst");
			  keys[1] = ("better");
			  keys[2] = ("okay");
			  
			  var color0 = "rgb(0,102,0)";
			  var color1 = "rgb(128,255,0)";
			  var color2 = "rgb(255,255,0)";
			  var color3 =  "rgb(204,0,0)";
			 
			  
			  for (var i = 0; i < 4; i++) {
				    data.push({
				      key: keys[i],
				      values: []
				    });

				    for (var j = 0; j < len; j++) {
				      var col = "black";
				      if(groups[j] == 0){
				    	  col = color0;
				      }
				      else if(groups[j] == 1){
				    	  col = color1;
				      }
				      else if(groups[j] == 2){
				    	  col = color2;
				      }
				      else {
				    	  col = color3;
				      }
				      data[i].values.push({x: xvals[j], y: yvals[j], kw: words[j], color:col});
				    }
				  }

				  return data;
				}			

		});
	}; 

}(jQuery));
