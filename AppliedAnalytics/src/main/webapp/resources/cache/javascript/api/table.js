/**
 * table.js
 */

(function ($) {

	/* These are the default values for the params object in table(params). */
	var defaults = {
			"columnHeaders" : [{"name" : null}],
			"rowHeaders"	: [{"name" : null}],
			"m"				: {"length": 0, "keys": null}, // columns
			"n"				: {"length": 0, "keys": null}, // rows
			"url"			: {"links": null, "cols": []},
			"search"		: false,
			"show"			: 5,
			"title"			: "",
			"sorting"		: [],
			"noSort"		: null, // specify columns to not have sorting by [ column Number ]
			"oLanguage"		: { },
			"columnLines"	: 1,
			"subClass"		: null
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
			var heightOffSet = (settings.search || (settings.dom && settings.dom.cName)) ? 71 : 60;
			var id = settings.elementId || this.id;
			var multiLineHeader = 20 * (settings.columnLines - 1);
			
			// Base Cases << What does that mean in this context...??
			var table = {
							"id"		: id + "DataTable",
							"data"		: settings.data,
							"rawData"	: settings.rawData,
							"title"		: settings.title,
							"size"		: { "width": $this.width(), "height": $this.height() - heightOffSet - multiLineHeader },
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
							"element"	: null,
							"sorting"	: settings.sorting,
							"oLanguage"	: settings.oLanguage
						};

			// This is the container that the table will be placed in.
			var $view = $("#" + settings.id + " #" + id).attr("style", "display:none;");
			
			
			// Now we place the table in the container (empty), and keep the reference to this table.
			table.element = $("<table>").css("width", table.size.width).appendTo((settings.subClass) ? $view.find("." + settings.subClass).empty()
																									 : $view.empty());
			
			var addedDOM = 'l<"subtitle">';
			
			if (settings.dom)
				addedDOM += '<"' + settings.dom.cName + '">';

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
										"sDom": addedDOM + 'frtip',
										aaData: getData(), 		//Here is where the data actually gets passed in
										aoColumns: getHeaders(),
										aoColumnDefs: getDefinitions(),
										iDisplayLength: settings.show,
										aaSorting: settings.sorting,
										oLanguage: settings.oLanguage
									});

			// If there is a title to the table element, set the contents of all divs on the page
			// with class "tableTitle" to this title ..... -_-
			if (table.title) {
				(settings.subClass) ? $("#" + settings.id + " #" + id + " ." + settings.subClass + " div.subtitle").html("<b>" + table.title + "</b>")
									: $("#" + settings.id + " #" + id + " div.subtitle").html("<b>" + table.title + "</b>");
			}
			
			if (settings.dom && settings.dom.content) {
				var container = (settings.subClass) ? $("#" + settings.id + " #" + id + " ." + settings.subClass + " div." + settings.dom.cName)
													: $("#" + settings.id + " #" + id + " div." + settings.dom.cName);
				
				var c = settings.dom.content;
				if (typeof(c) == "object")
					for(var i = 0; i < c.length; i++)
						container.append(c[i]);
				else
					container.append(c);
				
			}

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
			  				  		  		  return "<a href=\"" + table.url.links[data] + "\" target=\"_blank\">" + data + "</a>";
					  				  		  // return "<div id='website-performance-wrapper'>" +
					  				  		  // 		"<iframe id='website-performance-iframe' src='" + table.url.links[data] + "'></div>";
					  				  	  	}
				  	  			};

				  	  definitions.push(urls);
			  	}
				
				if (settings.noSort) {
					var noSort = { 
						"bSortable": false, 
						"aTargets": settings.noSort 
					};
				
					definitions.push(noSort);
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
