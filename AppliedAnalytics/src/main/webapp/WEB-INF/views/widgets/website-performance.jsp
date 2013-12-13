<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<link href="<c:url value="/cache/css/widget.css" />" rel="stylesheet">  

<script>
//var WebsitePerformanceData = JSON.parse('${DATA}');
</script>  
  
<div class="widget_wrapper widgetView">
	<form id="websitePerformanceSettings">
		<div class="widget_header">			
			<div class="widget_title">WEBSITE PERFORMANCE</div>
			
			
		</div>
		<div id="websitePerformanceGraph" class="rs_visual">
			<!-- add Visualization elements -->
			<canvas id="websitePerformanceData"></canvas>
								
		</div>
	</form>
</div>
