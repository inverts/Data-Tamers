<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<script> 
var data =JSON.parse('${ wpModel.getDataPoints() }');
//var obj = eval ("(" + data + ")");
console.log(data);
//var arr = JSON.parse(JSONObject);
document.getElementById("visits0").innerHTML=data.visitsPercent[0][0];
document.getElementById("url0").innerHTML=data.pageTitle[0][0]; 
document.getElementById("url0").setAttribute("href",data.pageLink[0][0]);
document.getElementById("bounce0").innerHTML=data.bounceRate[0][0];
document.getElementById("exit0").innerHTML=data.exitRate[0][0];
document.getElementById("visits1").innerHTML=data.visitsPercent[0][1];
document.getElementById("url1").innerHTML=data.pageTitle[0][1];
document.getElementById("url1").setAttribute("href",data.pageLink[0][1]);
document.getElementById("bounce1").innerHTML=data.bounceRate[0][1];
document.getElementById("exit1").innerHTML=data.exitRate[0][1];
document.getElementById("visits2").innerHTML=data.visitsPercent[0][2];
document.getElementById("url2").innerHTML=data.pageTitle[0][2]; 
document.getElementById("url2").setAttribute("href",data.pageLink[0][2]);
document.getElementById("bounce2").innerHTML=data.bounceRate[0][2];
document.getElementById("exit2").innerHTML=data.exitRate[0][2];
document.getElementById("visits3").innerHTML=data.visitsPercent[0][3];
document.getElementById("url3").innerHTML=data.pageTitle[0][3]; 
document.getElementById("url3").setAttribute("href",data.pageLink[0][3]);
document.getElementById("bounce3").innerHTML=data.bounceRate[0][3];
document.getElementById("exit3").innerHTML=data.exitRate[0][3];
document.getElementById("visits4").innerHTML=data.visitsPercent[0][4];
document.getElementById("url4").innerHTML=data.pageTitle[0][4]; 
document.getElementById("url4").setAttribute("href",data.pageLink[0][4]);
document.getElementById("bounce4").innerHTML=data.bounceRate[0][4];
document.getElementById("exit4").innerHTML=data.exitRate[0][4];
</script> 

<form id="websitePerformanceSettings">
<h4><b><center>Improve these high impact webpages:</center></h4>
	<table class = "websitePerformanceClass" id = "websitePerformance-table">		
	<thead>
	<tr>
		<th align="left">Webpage Title (link)</th>
		<th align="left"> Visits (%)</th>
		<th align="left"> Bounce Rate (%)</th>
		<th align="left"> Exit Rate (%)</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td id = page0 bgcolor = "#99FFFF" headers="page"><a id = url0 target="_blank"></a></td>
		<td id = visits0 bgcolor = "#B6D8E4" headers="visits"> </td>
		<td id = bounce0 bgcolor = "#DD7280" headers="bounce"> </td>
		<td id = exit0 bgcolor="#EAAFB6" headers="exit"> </td>
	</tr>
	<tr>
		<td id = page1 bgcolor = "#99FFFF" headers="page"><a id = url1 target="_blank"></a></td>
		<td id = visits1 bgcolor = "#B6D8E4" headers="visits"> </td>
		<td id = bounce1 bgcolor = "#DD7280" headers="bounce"> </td>
		<td id = exit1 bgcolor="#EAAFB6" headers="exit"> </td>
	</tr>
	<tr>
		<td id = page2 bgcolor = "#99FFFF" headers="page"><a id = url2 target="_blank"></a></td>
		<td id = visits2 bgcolor = "#B6D8E4" headers="visits"> </td>
		<td id = bounce2 bgcolor = "#DD7280" headers="bounce"> </td>
		<td id = exit2 bgcolor="#EAAFB6" headers="exit"> </td>
	</tr>
	<tr>
		<td id = page3 bgcolor = "#99FFFF" headers="page"><a id = url3 target="_blank"></a></td>
		<td id = visits3 bgcolor = "#B6D8E4" headers="visits"> </td>
		<td id = bounce3 bgcolor = "#DD7280" headers="bounce"> </td>
		<td id = exit3 bgcolor="#EAAFB6" headers="exit"> </td>
	</tr>
	<tr>
		<td id = page4 bgcolor = "#99FFFF" headers="page"><a id = url4 target="_blank"></a></td>
		<td id = visits4 bgcolor = "#B6D8E4" headers="visits"> </td>
		<td id = bounce4 bgcolor = "#DD7280" headers="bounce" > </td>
		<td id = exit4 bgcolor="#EAAFB6" headers="exit"> </td>
	</tr>   
	</tbody>
	</table>
    <!--<div id="websitePerformanceGraph" class="rs_visual">  -->
		<!-- add Visualization elements -->
	<!--<canvas id="websitePerformanceData"></canvas>  -->
	<!--</div> -->
	
</form>


