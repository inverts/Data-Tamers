<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  
<script>
//var RevenueSourceData = JSON.parse('${DATA}');
</script>  
  
<div class="widget_wrapper widgetView">
	<form id="revenueSourcesSettings">
		<div class="widget_header">			
			<div class="widget_title">REVENUE SOURCES</div>
			
			
		</div>
		<div id="revenueSourcesGraph" class="rs_visual">
			<!-- add Visualization elements -->
			<canvas id="revenueSourcesData"></canvas>
								
		</div>
	</form>
</div>
