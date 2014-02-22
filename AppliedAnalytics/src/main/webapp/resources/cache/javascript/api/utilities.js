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
			if (obj[keys[i]])
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


