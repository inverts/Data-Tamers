/**
 * utilities.js
 * 
 * These are utility functions to help with our visualization API
 */

function checkKeys(obj, idx, keys, value) {
	
	if (keys.length == 0)
		return;

	else if (keys instanceof Array) {

		for (var i = 0; i < keys.length; i++) {
			if (obj[idx][keys[i]] == value)
				return true;
		}
		
		return false;
	}
	else
		return obj[idx][keys] == value;
}


/* Generic method for constructing tables with data */
function createTableView(id, data, wPct, urlIndex) {
	var $view = $("#" + id).empty().attr("style", "display:none;");
	
	if (!wPct)
		wPct = 32;
	
	// add sub-title if applicable
	if (data.title)
		$("<h4>").html("<b>" + data.title + "</b>").appendTo($view);
	
	var $table = $("<table>").addClass("view").appendTo($view);
	var $thead = $("<thead>").addClass("data-header").appendTo($table);
	var $tbody = $("<tbody>").addClass("data-body").appendTo($table);

	// setup column headers
	for (var i = 0; i < data.keys.length; i++) {
		var $th = $("<th>").html("<div>" + data.keys[i] + "</div>").appendTo($thead);
		if (i == 0)
			$th.addClass("long");
		else if (i == data.keys.length - 1)
			$th.addClass("last");
		else
			$th.css("width", (100 - wPct) / (data.keys.length - 1) + "%");
	}

	var $tr = $("<tr>").appendTo($tbody);
	var $td = $("<td>").attr("colspan", data.keys.length).appendTo($tr);
	
	var $overflow = $("<div>").addClass("overflow").appendTo($td);
	var $tableData = $("<table>").addClass("data-table").appendTo($overflow);
	
	// access data via column headers as key names
	for (var i = 0; i < data[data.keys[0]].length; i++) {
		var tr = $("<tr>").appendTo($tableData);
		for (var j = 0; j < data.keys.length; j++) {
			var $a = $("<a>").html(data[data.keys[j]][i]);
			// add url if available
			if (urlIndex != "undefined" && data.url && j == urlIndex)
				$a.attr("href", data.url[i]);
			
			var td = $("<td>").append($a).appendTo(tr);
			if (j == 0)
				td.addClass("long");
			else if (j == data.keys.length - 1)
				td.addClass("last");
			else
				td.css("width", (100 - wPct) / (data.keys.length - 1) + "%");
		}
	}
}


// Returns the index of the first instance of the specified object 
// in an object array or -1 if it does not exist.
function getIndex(objectarray, key, value) {
	for (var i = 0; i < objectarray.length; i++) {
		if (checkKeys(objectarray, i, key, value))
			return i;
	}
	
	return -1;	
}

// Converts the jsonDate to a date object
function getDate(d) {
	return new Date(d.jsonDate);
}

function keyValues(obj, keys) {
	
	if (keys.length == 0)
		return;

	else if (keys instanceof Array) {
		
		var values = [];
		
		for (var i = 0; i < keys.length; i++) {
			if (obj[keys[i]] != "undefined")
				values.push(obj[keys[i]]);
		}
		
		return values;
	}
	else
		return obj[keys];
}


// gets the index of the object in an object 
// array based on a given Math metric
function getIndexBy(objectarray, key, math) {
	var values = $.map(objectarray, function(obj){
					return keyValues(obj, key);
				  });
	
	if (values.length)
		return getIndex(objectarray, key, math.apply( Math, values));
	
	return -1;
}





// gets the value based on the specified metric!
// array could be an object array or array of object arrays!
function getValueBy(array, key, math) {
	
	if (array.length == 0)
		return;
	
	var values = [];
	
	if (array[0] instanceof Array) {
		values = $.map(array, function(val) {
			return getValueBy(val, key, math);
		});
	}
	else {
		values = $.map(array, function(obj){
			return keyValues(obj, key);
		});
	}
	
	if (values.length)
		return math.apply(Math, values);
}


function getArrayBy(array, math) {
	
	if (array.length == 0)
		return;
	
	if (array[0] instanceof Array) {
		var dataArrays = $.map(array, function(val) {
			return [getArrayBy(val, math)];
		});
		
		var size = dataArrays.length;
		if (size) {
			dataArrays.sort(function(a, b){
				return a.length - b.length;
			});
			return dataArrays[math.apply(Math, [0, size - 1])];
		}
	}
	else
		return array;
	
}


