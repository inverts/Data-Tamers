/**
 * table.js
 */


(function ($) {

	/* This looks like a helper function but I don't see it being used anywhere. */
	registerKeyboardHandler = function(callback) {
	  d3.select(window).on("keydown", callback);  
	};

	/* These are the default values for the params object in table(params). */
	var defaults = {
			"columnHeaders" : [{"name" : null}],
			"rowHeaders"	: [{"name" : null}],
			"m"				: {"length": 0, "keys": null}, // columns
			"n"				: {"length": 0, "keys": null}, // rows
			"url"			: {"links": null, "cols": []},
			"search"		: false,
			"show"			: 5,
			"title"			: ""
	}; 

	/**
	 * This adds the table() function to jQuery objects.
	 * 
	 * 
	 * @param params The Javascript object of all of the parameters
	 */
	$.fn.table = function (params) { 
		return this.each(function() {
			var $this = $(this);
			var settings = $.extend({}, defaults, params); // Combine params with the defaults.
			var heightOffSet = function() {
				if(settings.title && settings.search)
					return 110;
				else if (settings.search)
					return 90;
				else if (settings.title)
					return 80;
				else
					return 50;
			}
			// Base Cases << What does that mean in this context...??
			var table = {
							"id"		: this.id + "DataTable",
							"data"		: settings.data,
							"rawData"	: settings.rawData,
							"title"		: settings.title,
							"size"		: { "width": $this.width(), "height": $this.height() - heightOffSet() },
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

			// This is the container that the table will be placed in.
			var $view = $("#" + $this.attr("id")).empty().attr("style", "display:none;");

			// Now we place the table in the container (empty), and keep the reference to this table.
			table.element = $("<table>").css("width", table.size.width).appendTo($view);
			

			//This invokes the jQuery DataTables plugin on the <table> element.
			//Isn't the correct syntax ".dataTable(..."?
			table.element.DataTable({
										bProcessing: true,
										bAutoWidth: false,
										bPaginate: false,
										bFilter: settings.search,
										bInfo: false,
										sScrollY: table.size.height + "px",
										sScrollXInner: "100%",
										sScrollYInner: "100%",
										"sDom": 'l<"subtitle'+$this.attr("id")+'">frtip',
										aaData: getData(), 		//Here is where the data actually gets passed in
										aoColumns: getHeaders(),
										aoColumnDefs: getDefinitions(),
										iDisplayLength: settings.show,
									});

			// If there is a title to the table element, set the contents of all divs on the page
			// with class "tableTitle" to this title ..... -_

			if (table.title)
				$("div.subtitle"+$this.attr("id")).html("<b>" + table.title + "</b>");

			// If search is enabled (a search box element is provided), then bind the filter function
			// to changes in the search box. But if the search box is empty.. then forget the binding? -_-
			if (settings.search && settings.search.length) {
				settings.search.keyup(function() {
					//It turns out that this is never called, as expected.
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
				if (typeof(table.rawData) != 'undefined')
					return table.rawData;

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