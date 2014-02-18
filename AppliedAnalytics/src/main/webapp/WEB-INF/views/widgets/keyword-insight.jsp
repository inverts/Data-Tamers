<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<head>
<style>
table, th, td
{
background-color:#DCDCDC;
border:1px solid black;
}
th, td
{
padding:3px;
}
</style>
</head>
<body>
<div class="widget_wrapper widgetView">
	<form id="keywordInsightSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="keywordinsight.title" /></div>			
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
				
	</form>
</div>
</body>