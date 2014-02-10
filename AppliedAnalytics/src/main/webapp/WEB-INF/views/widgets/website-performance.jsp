<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<head>
<style>
#websitePerformanceSettings table, #websitePerformanceSettings th, #websitePerformanceSettings td
{
background-color:#DCDCDC;
border:1px solid black;
}
#websitePerformanceSettings th, #websitePerformanceSettings td
{
padding:3px;
}
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
<div class="widget widgetView">
	<form id="websitePerformanceSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="websiteperformance.title" /></div>			
		</div>
	
		<table>
			<th> Webpage Path </th>
			<th> Visits (%)</th>
			<th> Bounce Rate (%)</th>
			<th> Exit Rate (%)</th>
		<tr>
			<td id = page0> </td>
			<td id = visits0> </td>
			<td id = bounce0> </td>
			<td id = exit0> </td>
		</tr>
		<tr>
			<td id = page1> </td>
			<td id = visits1> </td>
			<td id = bounce1> </td>
			<td id = exit1> </td>
		</tr>
				<tr>
			<td id = page2> </td>
			<td id = visits2> </td>
			<td id = bounce2> </td>
			<td id = exit2> </td>
		</tr>
		<tr>
			<td id = page3> </td>
			<td id = visits3> </td>
			<td id = bounce3> </td>
			<td id = exit3> </td>
		</tr>
		<tr>
			<td id = page4> </td>
			<td id = visits4> </td>
			<td id = bounce4> </td>
			<td id = exit4> </td>
		</tr>   
		</table>
	    <!--<div id="websitePerformanceGraph" class="rs_visual">  -->
			<!-- add Visualization elements -->
		<!--<canvas id="websitePerformanceData"></canvas>  -->
		<!--</div> -->
		
	</form>
</div>
</body>
