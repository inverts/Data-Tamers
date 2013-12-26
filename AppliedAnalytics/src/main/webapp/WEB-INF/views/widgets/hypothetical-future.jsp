<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
<script>
var historicalData = JSON.parse('${ hfModel.getDataPoints() }');
var futureData = JSON.parse('${ hfModel.getYValuesForecast() }');
var historicalDataSize = ${ hfModel.getYValues().size() };
var Y_MIN = ${ hfModel.getYRange().getKey() - hfModel.getYRangePadding() };
var Y_MAX = ${ hfModel.getYRange().getValue() + hfModel.getYRangePadding() };
var startDate = '${ filterModel.getActiveStartDateString() }';
var endDate = '${ filterModel.getActiveEndDateString() }';
var futureEndDate = '${ hfModel.getFutureEndDate() }';
</script>  
  
<div class="widget_wrapper widgetView">
	<form id="hypotheticalFutureSettings">
		<div class="widget_header">			
			<div class="widget_title">DATA FORECAST</div>
			<!-- Temporarily hidden until implemented. -->
			<div style="display:none;">
				<div class="widget_input">
					<label>Traffic Source:</label> <select id="traffic_source"></select>
				</div>
				<div class="widget_input">				
					<label>Increase:</label> <select id="change_pct">
						<c:forEach var="option" items="${changeOptions}">
							<option value="${option.getKey()}" ${option.getValue()}>
								${option.getKey()}%</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
		<div id="hypotheticalFutureGraph" class="hf_visual">
			<!-- add Visualization elements -->
			<canvas id="hypotheticalFutureData"></canvas>
								
		</div>
	</form>
</div>
