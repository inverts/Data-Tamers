/**
 * utilities.js
 * 
 * These are utility functions to help with our visualization API
 */

// Returns the index of the first instance of the specified object 
// in an object array or -1 if it does not exist.
function getIndex(objectarray, key, value) {
	for (var i = 0; i < objectarray.length; i++) {
		if (objectarray[i][key] == value)
			return i;
	}
	
	return -1;	
}

// Converts the jsonDate to a date object
function getDate(d) {
	return new Date(d.jsonDate);
}

// gets the index of the object in an object 
// array based on a given Math metric
function getIndexBy(objectarray, key, math) {
	var values = $.map(objectarray, function(obj){
					return obj[key];
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
			return (key == "jsonDate") ? new Date(obj[key]) : obj[key];
		});

	}
	
	if (values.length)
		return math.apply(Math, values);
}


