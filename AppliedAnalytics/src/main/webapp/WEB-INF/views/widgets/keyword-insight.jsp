<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<head>
<style>
table, th, td
{
background-color:#DCDCDC;
border:1px solid black;
}
h3{color:#DCDCDC;}
th, td
{
padding:3px;
}
</style>
<script>
var dataPoints = ('${ kiModel.getDataPoints() }');

// if no data display error message
if (dataPoints == null) {
	("#keywordInsightSettings").append('<h2><fmt:message key="keywordinsight.gaOverQuotaError"/></h2>');
}
else { // else parse and display data
	var data =JSON.parse('${ kiModel.getDataPoints() }');
	console.log(data);
	// Remove keywords table
	var table = $('<table><tbody>');
	var tr = $('<tr>');
	$('<th>Keywords</th>').appendTo(tr);
	$('<th>Visits (%)</th>').appendTo(tr);
	$('<th>Bounce Rate (%)</th>').appendTo(tr);
	$('<th>Multipage Visits (%)</th>').appendTo(tr);
	tr.appendTo(table);
	for(var r = 0; r < data.removeKeywords.length; r++)
	{
	    var tr = $('<tr>');
	    var key = '<td>'+data.removeKeywords[r]+'</td>';
	    var visits = '<td>'+data.removeVisitsPercent[r]+'</td>';
	    var bounceRate = '<td>'+data.removeBounceRate[r]+'</td>';
	    var rank = '<td>'+ Math.round(100*data.removeMultipageVisitsPercent[r])/100.0+'</td>';	    
	    
	    $(key).appendTo(tr);
	    $(visits).appendTo(tr);
	    $(bounceRate).appendTo(tr);
	    $(rank).appendTo(tr);
	    tr.appendTo(table);
	}
	table.appendTo("#keywordInsightSettings");
	
	// Help keywords table
	var message = $('<br><br><h3><fmt:message key="keywordinsight.changeSuggestion"/></h3><br>');
	var table = $('<table><tbody>');
	var tr = $('<tr>');
	$('<th>Keywords</th>').appendTo(tr);
	$('<th>Visits (%)</th>').appendTo(tr);
	$('<th>Bounce Rate (%)</th>').appendTo(tr);
	$('<th>Multipage Visits (%)</th>').appendTo(tr);
	tr.appendTo(table);
	for(var r = 0; r < data.helpKeywords.length; r++)
	{
	    var tr = $('<tr>');
	    var key = '<td>'+data.helpKeywords[r]+'</td>';
	    var visits = '<td>'+data.helpVisitsPercent[r]+'</td>';
	    var bounceRate = '<td>'+data.helpBounceRate[r]+'</td>';
	    var rank = '<td>'+ Math.round(100*data.helpMultipageVisitsPercent[r])/100.0+'</td>';	    
	    
	    $(key).appendTo(tr);
	    $(visits).appendTo(tr);
	    $(bounceRate).appendTo(tr);
	    $(rank).appendTo(tr);
	    tr.appendTo(table);
	}
	message.appendTo("#keywordInsightSettings");
	table.appendTo("#keywordInsightSettings");
	
	// Best keywords table
	var message = $('<br><br><h3><fmt:message key="keywordinsight.bestMessage"/></h3><br>');
	var table = $('<table><tbody>');
	var tr = $('<tr>');
	$('<th>Keywords</th>').appendTo(tr);
	$('<th>Visits (%)</th>').appendTo(tr);
	$('<th>Bounce Rate (%)</th>').appendTo(tr);
	$('<th>Multipage Visits (%)</th>').appendTo(tr);
	tr.appendTo(table);
	for(var r = 0; r < data.bestKeywords.length; r++)
	{
	    var tr = $('<tr>');
	    var key = '<td>'+data.bestKeywords[r]+'</td>';
	    var visits = '<td>'+data.bestVisitsPercent[r]+'</td>';
	    var bounceRate = '<td>'+data.bestBounceRate[r]+'</td>';
	    var rank = '<td>'+ Math.round(100*data.bestMultipageVisitsPercent[r])/100.0+'</td>';	    

	    $(key).appendTo(tr);
	    $(visits).appendTo(tr);
	    $(bounceRate).appendTo(tr);
	    $(rank).appendTo(tr);
	    tr.appendTo(table);
	}
	message.appendTo("#keywordInsightSettings");
	table.appendTo("#keywordInsightSettings");
	
	// All word substring keyword performance table
	var message = $('<br><br><h3><fmt:message key="keywordinsight.wordPerformanceMessage"/></h3><br>');
	var table = $('<table><tbody>');
	var tr = $('<tr>');
	$('<th>Word Substring</th>').appendTo(tr);
	$('<th>Keyword Count</th>').appendTo(tr);
	$('<th>Multipage Visits (%)</th>').appendTo(tr);
	tr.appendTo(table);
	for(var r = 0; r < data.words.length; r++)
	{
	    var tr = $('<tr>');
	    var word = '<td>'+data.words[r]+'</td>';
	    var keywordCount = '<td>'+data.wordCount[r]+'</td>';
	    var rank = '<td>'+ Math.round(100*data.multipageVisitsPercent[r])/100.0+'</td>';
	    
	    $(word).appendTo(tr);
	    $(keywordCount).appendTo(tr);
	    $(rank).appendTo(tr);
	    tr.appendTo(table);
	}
	message.appendTo("#keywordInsightSettings");
	table.appendTo("#keywordInsightSettings");
	
	// All cpc keywords table
	var message = $('<br><br><h3><fmt:message key="keywordinsight.searchMessage"/></h3><br>');
	var table = $('<table><tbody>');
	var tr = $('<tr>');
	$('<th>Keywords</th>').appendTo(tr);
	$('<th>Visits (%)</th>').appendTo(tr);
	$('<th>Bounce Rate (%)</th>').appendTo(tr);
	$('<th>Multipage Visits (%)</th>').appendTo(tr);
	tr.appendTo(table);
	for(var r = 0; r < data.allCpcKeywords.length; r++)
	{
	    var tr = $('<tr>');
	    var key = '<td>'+data.allCpcKeywords[r]+'</td>';
	    var visits = '<td>'+data.allCpcVisitsPercent[r]+'</td>';
	    var bounceRate = '<td>'+data.allCpcBounceRate[r]+'</td>';
	    var rank = '<td>'+ Math.round(100*data.allCpcMultipageVisitsPercent[r])/100.0+'</td>';
	    
	    $(key).appendTo(tr);
	    $(visits).appendTo(tr);
	    $(bounceRate).appendTo(tr);
	    $(rank).appendTo(tr);
	    tr.appendTo(table);
	}
	message.appendTo("#keywordInsightSettings");
	table.appendTo("#keywordInsightSettings");
}

</script>
</head>
<body>
<div class="widget_wrapper widgetView">
	<form id="keywordInsightSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="keywordinsight.title" /></div>			
		    <h3><fmt:message key="keywordinsight.removeSuggestion"/></h3>
		</div>
	</form>
</div>
</body>