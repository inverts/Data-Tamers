/**
 * test.js
 */




function createDataSet(size, yrange) {
	
	var result = [];
	var date = new Date("01\/20\/14");
	for(var i = 0; i < size; i++) {
		var obj = {};

		obj["jsonDate"] = date.toDateString();
		
		obj["jsonHitCount"] = Math.floor((Math.random()*yrange[1]) + yrange[0]);
		obj["smooth"] = Math.floor((Math.random()*yrange[1]) + yrange[0]);
		obj["normal"] = Math.floor((Math.random()*yrange[1]) + yrange[0]);
		result.push(obj);
		date = new Date(date.getTime() + 86400000);
	}

	return result;
	
}





$(function() {
	
	var test = createDataSet(3, [1, 20]);
	
	if (test.length != 3)
		console.error("createDataSet failed");
	
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
	
	
	// Test of getIndexBy

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
	
	// Test of getValueBy
	
	testkey = "andrew";
	var val = getValueBy(objArrMax, testkey, Math.max);
	if (val != 20)
		console.error("getValueBy - obj - max: expected 20, got " + val);
	
	testkey = "andrew";
	val = getValueBy(objArrMax, testkey, Math.min);
	if (val != 7)
		console.error("getValueBy - obj - min: expected 7, got " + val);
	
	
	var testSet = [objArrMax, 
	               [{"andrew": 6, "dave": 4}, {"andrew": 9, "dave": 14}, {"andrew": 27, "dave": 12}], 
	               [{"andrew": 13}, {"andrew": 7}, {"andrew": 25}, {"andrew": 21} ] 
				  ];
	
	
	testkey = "andrew";
	val = getValueBy(testSet, testkey, Math.max);
	if (val != 27)
		console.error("getValueBy - array - max: expected 27, got " + val);
	
	testkey = ["andrew", "dave"];
	val = getValueBy(testSet, testkey, Math.min);
	if (val != 4)
		console.error("getValueBy - array - min: expected 4, got " + val);
	
	// Dates
	/*var dateArray = [{"jsonDate": "09\/22\/11"}, {"jsonDate": "12\/12\/11"}, {"jsonDate": "01\/22\/12"}, {"jsonDate": "07\/31\/11"}];
	
	testkey = "jsonDate";
	val = getValueBy(dateArray, testkey, Math.max);
	if (val != new Date("01\/22\/12").valueOf())
		console.error("getValueBy - date - max: expected " + new Date("01\/22\/12").toDateString() + ", got " + new Date(val).toDateString());
	
	testkey = "jsonDate";
	val = getValueBy(dateArray, testkey, Math.min);
	if (val != new Date("07\/31\/11").valueOf())
		console.error("getValueBy - date - min: expected " + new Date("07\/31\/11").toDateString() + ", got " + new Date(val).toDateString());
	*/
	
	// Get Array By
	var arr = getArrayBy(objArrMax, Math.max);
	if (arr.length != objArrMax.length)
		console.error("getArrayBy - single - max: expected 5, got " + arr.length);
	
	arr = getArrayBy(objArrMax, Math.min);
	if (arr.length != objArrMax.length)
		console.error("getArrayBy - single - min: expected 5, got " + arr.length);
	
	arr = getArrayBy(testSet, Math.max);
	if (arr.length != objArrMax.length)
		console.error("getArrayBy - multi - max: expected 5, got " + arr.length);
	
	arr = getArrayBy(testSet, Math.min);
	if (arr.length != testSet[1].length)
		console.error("getArrayBy - multi - min: expected 3, got " + arr.length);
	
	
	
	
});