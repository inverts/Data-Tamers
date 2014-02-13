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
	
});