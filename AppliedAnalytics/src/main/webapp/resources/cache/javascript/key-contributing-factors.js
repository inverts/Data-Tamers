/**
 * key-contributing-factors.js
 */


function loadKeyContributingFactorsWidget(id, callback) {
	var $element = $('#' + id);
	//TODO: Possibly send id number out to append to ids in the model
	$.post(applicationRoot + "KeyContributingFactors", null, function(response) {
		if ($element.length > 0) {
			$element.fadeIn("fast", function() { 
					$element.append(response); 
			});
		}
		else
			console.error('Could not append KeyContributingFactors Widget to id: ' + id);
	});

}


function updateKeyContributingFactors(id) {
	
	
	getKeyContributingFactorsData(id, function() {
		if(callback){				
			callback();
		}
		
	});
}

function getKeyContributingFactorsData(id, callback) {
	$.post(applicationRoot + "KeyContributingFactors", {"serialize": 1}, function(response) {
		var d = $.parseJSON(response);
		
		$("#" + id + " .spinner-content").hide();
		
		$('#' + id + ' .keyContributingFactorsChart').empty().scatter({
			id	: 'keyContributingFactorsChart',
			xLabel: 'Percentage of bounce rates',
			yLabel: 'Percentage of visits',
			xKey 	: 'bounceRate',
			yKey	: 'visits'
		});
		
		if(callback)
			callback();
		
	});
}