/**
 * test.js
 */




function createDataSet(size, yrange) {
	
	var result = [];
	var date = new Date();
	
	for(var i = 0; i < size; i++) {
		var obj = {};

		obj["jsonDate"] = date.toDateString();
		obj["jsonHitCount"] = Math.floor((Math.random()*yrange[1]) + yrange[0]);
		result.push(obj);
		date = new Date(date.getTime() + 86400000);
	}

	return result;
	
}




$(function() {
	
	var test = createDataSet(3, [1, 20]);
	
	if (test.length != 3)
		console.error("createDataSet failed");
	
	
});