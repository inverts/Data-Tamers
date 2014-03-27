/**
 * table.js
 */


(function ($) {
	
	registerKeyboardHandler = function(callback) {
	  d3.select(window).on("keydown", callback);  
	};
	
	/* global variables */
	var defaults = { // default parameter values
			"columnHeaders" : [{"name" : null}],
			"rowHeaders"	: [{"name" : null}],
			"m"				: {"length": 0, "keys": null}, // columns
			"n"				: {"length": 0, "keys": null}, // rows
			"url"			: {"links": null, "cols": []},
			"search"		: false,
			"show"			: 5,
			"title"			: ""
	}; 
	
	/* function declaration */
	$.fn.table = function (params) { // params is the javascript object of all of the parameters
		return this.each(function() {
			var $this = $(this);
			var settings = $.extend({}, defaults, params); 
			
			// Base Cases
			
			
			var table = {
							"id"		: this.id + "DataTable",
							"data"		: settings.data,
							"title"		: settings.title,
							"size"		: { "width": $this.width(), "height": $this.height() },
							"cols"		: {
											"length": settings.m.length,
											"headers": {
														"length": settings.columnHeaders.length,
														"name": $.map(settings.columnHeaders, function(val){ return val.name; }),
														"elements": []
													   },
											"keys" : settings.m.keys,
											"elements": []
										  },
							"rows"		: {
											"length": settings.n.length,
											"show" : settings.show,
											"headers": {
														"length": settings.rowHeaders.length,
														"name": $.map(settings.rowHeaders, function(val){ return val.name; }),
														"elements": []
													   },
											"keys" : settings.n.keys,
											"elements": []
										  },
							"url"		: settings.url,
							"element"	: null
						};

			var $view = $("#" + $this.attr("id")).empty().attr("style", "display:none;");
			
			table.element = $("<table>").css("width", table.size.width).appendTo($view);
			
			table.element.DataTable({
										bProcessing: true,
										bAutoWidth: false,
										bPaginate: false,
										bFilter: settings.search,
										bInfo: false,
										sScrollXInner: "100%",
										sScrollYInner: "100%",
										"sDom": '<"tableTitle">frtip',
										aaData: getData(),
										aoColumns: getHeaders(),
										aoColumnDefs: getDefinitions(),
										iDisplayLength: settings.show,
									});
			if (table.title)
				$("div.tableTitle").html(table.title);
				
				
			if (settings.search && settings.search.length) {
				settings.search.keyup(function() {
					table.element.fnFilter( $(this).val() );
				});
			}
				
			/**
			 * Gets the column headers in correct dataTable format.
			 * @Return: an array of column header objects.
			 */	
			function getHeaders() {
				var colHeaders = [];
				for (var i = 0; i < table.cols.headers.length; i++)
					colHeaders.push({
						"sTitle": table.cols.headers.name[i],
						"aTargets": [i]
					});

				return colHeaders;
			}
			
			
			
			function getDefinitions() {
				var definitions = [];
				
				// Hyperlinks
				if (table.url.links) {
				  	  var urls = {
			  			  		  "aTargets": table.url.cols,
			  				  	  "mRender": function (data, type, full) {
					  				  		   return "<a href=\"" + "#" + "\">" + data + "</a>"
					  				  	  	}
				  	  			};
				  	  
				  	  definitions.push(urls);
			  	}
				
				return definitions;
			}
			
			
			
			/**
			 * Puts the data into the correct dataTable format.
			 * @Return: an array of row arrays containing column data.
			 */
			function getData() {
				var result = [];
				
				for (var i = 0; i < table.rows.length; i++) {
					var nkey = table.rows.keys && table.rows.keys[i] || i;
					var row = [];
					
					for (var j = 0; j < table.cols.length; j++) {
						var mkey = table.cols.keys && table.cols.keys[j] || j;
						row.push(table.data[mkey][nkey]);
					}
					
					result.push(row);
					
				}
				
				return result;	
			}


		});

	};
	
} (jQuery));