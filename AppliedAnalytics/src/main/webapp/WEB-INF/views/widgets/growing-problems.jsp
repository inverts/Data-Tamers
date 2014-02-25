<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<head>
<style type = "text/css">
</style>  
<script>
//var GrowingProblemsData = JSON.parse('${DATA}');

</script> 
</head> 
  
 <body>
<div id = "growingProblems-wrapper">
<div class="widget widgetView growingProblems">
	<form id="growingProblemsSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="growingproblems.title" /></div>
		</div>
		
	<div class = "widget-content">
	<div class = "gp-chart-background">	
		<table class = "growingProblemsChart" id = growingProblems-table">
		<!-- Sources -->
		<thead>
		<tr>
			<th align = "left" height = 45>Traffic Source </th>
			<th align = "left" height = 45>Conversion Rate </th>
			<th align = "left" height = 45>Visits </th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td id = "trafficSource" >Traffic Source1 </td>
			<td id = "conversionRate" >Conversion Rate1 </td>
			<td id = "visits" >Visits1 </td>
		</tr>
		<tr>
			<td id = "trafficSource" >Traffic Source2 </td>
			<td id = "conversionRate">Conversion Rate2 </td>
			<td id = "visits">Visits2 </td>
		</tr>

		
		</tbody>		
		</table>
	</div>
	</div>
	</form>
</div>
</div>
</body>
