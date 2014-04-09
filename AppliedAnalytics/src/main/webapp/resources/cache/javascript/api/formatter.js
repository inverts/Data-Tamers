/**
 * formatter.js
 * Formats data for nvd3 charts
 */

(function ($) {
	registerKeyboardHandler = function(callback) {
		  d3.select(window).on("keydown", callback);  
		};
		
	/* global variables */
	var defaults = {
			'chart'	: '',
			'title'	: ''
	};
	
	/* function declaration */
	$.fn.bar = function (params) {
		return this.each(function() {
			
			var $this = $(this);
			var settings = $.extend({}, defaults, params);
			var sdata = settings.data;	
			
			
			/*	
			 var original =   [{
				    "values" : [{
				        "y" : 3,
				        "x" : "04/05/2013"
				    }, {
				        "y" : 1,
				        "x" : "04/11/2013"
				    }, {
				        "y" : 3,
				        "x" : "04/12/2013"
				    }],
				    "key" : "Apples"
				}, {
				    "values" : [{
				        "y" : 3,
				        "x" : "04/05/2013"
				    }, {
				        "y" : 0,
				        "x" : "04/11/2013"
				    }, {
				        "y" : 1,
				        "x" : "04/12/2013"
				    }],
				    "key" : "Oranges"
				}];*/
			
			// for multi bar chart
			/*
			function formatData(sdata) {
				var p = sdata["Webpath Path"];
				var paths = [];
				var keys= [];					
				var numKeys = sdata.keys.length;
				var numVals = sdata["Visits (%)"].length;					
								
				for(i=1; i < numKeys; i++){
					keys[i-1]=(sdata.keys[i]);
				}
									
				for(i=0; i < paths.length; i++){
					paths[i] = ('"'+p[i]+'"');
				}
				
			var data = [];						
				for(i=0; i < 3; i++){
					data.push({
						key: keys[i],
						values:[]
					});
					
					for(j=0; j < sdata["Webpath Path"].length; j++){
						data[i].values.push({
							x:j,
							y:sdata[sdata.keys[i+1]][j]
						});
					}
					
				}		
			 return data;
				
				
			} */
			
			
		});
			
	};
}(jQuery));