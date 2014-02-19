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


function getIndexBy(objectarray, key, math) {
	var values = $.map(objectarray, function(obj){
					return obj[key];
				  });
	
	if (values.length)
		return getIndex(objectarray, key, math.apply( Math, values));
	
	return -1;
}


/* UTILITY TESTS */
$(function(){
	
	// Test of getIndex
		
		var objArr = [{"lynn": 12 }, {"dave": 10 }, {"gwen": 20 }, {"andrew": 15 }, {"andrew": 7}];
		
		var testkey = "andrew";
		var idx = getIndex(objArr, testkey, 15);
		if (idx != 3)
			console.error("getIndex - singleObj: expected 3, got " + idx + " for value = 15");
		
		idx = getIndex(objArr, testkey, 7);
		if (idx != 4)
			console.error("getIndex - singleObj: expected 4, got " + idx + " for value = 7");

		
		var testData = [ {
			"jsonDate" : "09\/22\/11",
			"jsonHitCount" : 2,
			"seriesKey" : "Website Usage"
		}, {
			"jsonDate" : "09\/26\/11",
			"jsonHitCount" : 9,
			"seriesKey" : "Website Usage"
		}];
		
		testkey = "jsonDate";
		idx = getIndex(testData, testkey, "09\/26\/11");
		if (idx != 1)
			console.error("getIndex - multiObj: expected 1, got " + idx + " for value = 09\/26\/11");
		
		idx = getIndex(testData, testkey, "09\/28\/11");
		if (idx != -1)
			console.error("getIndex - multiObj: expected -1, got " + idx + " for value = 09\/26\/11");
		
		
	// Test of getIndexOfMax
	
		var objArrMax = [{"andrew": 12 }, {"andrew": 10 }, {"andrew": 20 }, {"andrew": 15 }, {"andrew": 7}];
		
		testkey = "andrew";
		idx = getIndexBy(objArrMax, testkey, Math.max);
		if (idx != 2)
			console.error("getIndexBy - Max -base: expected 2, got " + idx);
		
		idx = getIndexBy(objArrMax, testkey, Math.min);
		if (idx != 4)
			console.error("getIndexBy - Min -base: expected 4, got " + idx);
		
		testkey = "dave"
		idx = getIndexBy(objArrMax, testkey, Math.max);
		if (idx != -1)
			console.error("getIndexBy - none: expected -1, got " + idx + " for value");
		
		objArrMax.push({"andrew": 20 });
		
		testkey = "andrew";
		idx = getIndexBy(objArrMax, testkey, Math.max);
		if (idx != 2)
			console.error("getIndexBy - duplicate: expected 2, got " + idx);
		
});