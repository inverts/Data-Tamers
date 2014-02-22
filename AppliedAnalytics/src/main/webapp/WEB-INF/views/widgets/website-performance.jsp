<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<head>
<style type = "text/css">
</style>
   
<script> 
var data =JSON.parse('${ wpModel.getDataPoints() }');
//var obj = eval ("(" + data + ")");
console.log(data);
//var arr = JSON.parse(JSONObject);
document.getElementById("visits0").innerHTML=data.visitsPercent[0][0];
document.getElementById("page0").innerHTML=data.pagePath[0][0]; 
document.getElementById("bounce0").innerHTML=data.bounceRate[0][0];
document.getElementById("exit0").innerHTML=data.exitRate[0][0];
document.getElementById("visits1").innerHTML=data.visitsPercent[0][1];
document.getElementById("page1").innerHTML=data.pagePath[0][1]; 
document.getElementById("bounce1").innerHTML=data.bounceRate[0][1];
document.getElementById("exit1").innerHTML=data.exitRate[0][1];
document.getElementById("visits2").innerHTML=data.visitsPercent[0][2];
document.getElementById("page2").innerHTML=data.pagePath[0][2]; 
document.getElementById("bounce2").innerHTML=data.bounceRate[0][2];
document.getElementById("exit2").innerHTML=data.exitRate[0][2];
document.getElementById("visits3").innerHTML=data.visitsPercent[0][3];
document.getElementById("page3").innerHTML=data.pagePath[0][3]; 
document.getElementById("bounce3").innerHTML=data.bounceRate[0][3];
document.getElementById("exit3").innerHTML=data.exitRate[0][3];
document.getElementById("visits4").innerHTML=data.visitsPercent[0][4];
document.getElementById("page4").innerHTML=data.pagePath[0][4]; 
document.getElementById("bounce4").innerHTML=data.bounceRate[0][4];
document.getElementById("exit4").innerHTML=data.exitRate[0][4];
</script> 
</head>
<body>
<div id = "websitePerformance-wrapper">
<div class="widget widgetView pagePerformance">
	<form id="websitePerformanceSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="websiteperformance.title" /></div>			
		</div>
	<div class="widget-content">
		<table class = "websitePerformanceClass" id = "websitePerformance-table">		
		<thead>
		<tr>
			<th align="left">Webpage Path </th>
			<th align="left"> Visits (%)</th>
			<th align="left"> Bounce Rate (%)</th>
			<th align="left"> Exit Rate (%)</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td id = page0 bgcolor = "#99FFFF" headers="page"> </td>
			<td id = visits0 bgcolor = "#B6D8E4" headers="visits"> </td>
			<td id = bounce0 bgcolor = "#DD7280" headers="bounce"> </td>
			<td id = exit0 bgcolor="#EAAFB6" headers="exit"> </td>
		</tr>
		<tr>
			<td id = page1 bgcolor = "#99FFFF" headers="page"> </td>
			<td id = visits1 bgcolor = "#B6D8E4" headers="visits"> </td>
			<td id = bounce1 bgcolor = "#DD7280" headers="bounce"> </td>
			<td id = exit1 bgcolor="#EAAFB6" headers="exit"> </td>
		</tr>
				<tr>
			<td id = page2 bgcolor = "#99FFFF" headers="page"> </td>
			<td id = visits2 bgcolor = "#B6D8E4" headers="visits"> </td>
			<td id = bounce2 bgcolor = "#DD7280" headers="bounce"> </td>
			<td id = exit2 bgcolor="#EAAFB6" headers="exit"> </td>
		</tr>
		<tr>
			<td id = page3 bgcolor = "#99FFFF" headers="page"> </td>
			<td id = visits3 bgcolor = "#B6D8E4" headers="visits"> </td>
			<td id = bounce3 bgcolor = "#DD7280" headers="bounce"> </td>
			<td id = exit3 bgcolor="#EAAFB6" headers="exit"> </td>
		</tr>
		<tr>
			<td id = page4 bgcolor = "#99FFFF" headers="page"> </td>
			<td id = visits4 bgcolor = "#B6D8E4" headers="visits"> </td>
			<td id = bounce4 bgcolor = "#DD7280" headers="bounce" > </td>
			<td id = exit4 bgcolor="#EAAFB6" headers="exit"> </td>
		</tr>   
		</tbody>
		</table>
	</div>
	    <!--<div id="websitePerformanceGraph" class="rs_visual">  -->
			<!-- add Visualization elements -->
		<!--<canvas id="websitePerformanceData"></canvas>  -->
		<!--</div> -->
		
	</form>
</div>
</div>
</body>
