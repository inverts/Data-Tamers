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
			// tooltip
			//var tip = d3.select("#" + settings.id).append("div")
			//.attr("class", "tip").style("opacity", 0);


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

			var chart;
			nv.addGraph(function() {
			  chart = nv.models.scatterChart()
			                //.showDistX(true)
			                //.showDistY(true)
			                .useVoronoi(false)
			                .color(d3.scale.category10().range())
			                .transitionDuration(300);

			  chart.xAxis.axisLabel("Bounce Rates (%)");
			 // chart.yAxis.axisLabel("Multipage Visit Rates (%)");
			  chart.yAxis.axisLabel("Visit Rates (%)");
			  chart.xAxis.tickFormat(d3.format('.02f'));
			  chart.yAxis.tickFormat(d3.format('.02f'));


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

			  for(var i=0; i < len; i++){						  
				  xvals[i] = sdata[i].values[0].x;
				  yvals[i] = sdata[i].values[0].y;
				  words[i] = sdata[i].key;
			  }
			  /* accessing data : */
				// keywords -- sdata[index].key
				// values BOUNCE RATE -- sdata[index].values[0].x
				// values VISITS -- sdata[index].values[0].y

			  for (var i = 0; i < 1; i++) {
				    data.push({
				      key: 'keywords',
				      values: []
				    });

				    for (var j = 0; j < len; j++) {
				      data[i].values.push({x: xvals[j], y: yvals[j], kw: words[j]});
				    }
				  }

				  return data;
				}


		});
	}; 

}(jQuery));
