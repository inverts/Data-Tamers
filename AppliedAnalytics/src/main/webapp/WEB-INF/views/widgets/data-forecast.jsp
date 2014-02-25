<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
 
<script>
var historicalData = JSON.parse('${ hfModel.getDataPoints() }');
var futureData = JSON.parse('${ hfModel.getYValuesForecast() }');
var historicalDataSize = ${ hfModel.getYValues().size() };
var Y_MIN = ${ hfModel.getYRange().getKey() - hfModel.getYRangePadding() };
var Y_MAX = ${ hfModel.getYRange().getValue() + hfModel.getYRangePadding() };
var startDate = '${ filterModel.getActiveStartDateString() }';
var endDate = '${ filterModel.getActiveEndDateString() }';
var futureEndDate = '${ hfModel.getFutureEndDate() }';

window.newData = JSON.parse('${ hfModel.getJSONPointsFormatted() }');
//window.onload(updateHypotheticalWidget());
</script>  
 
<style>
#top-controls { padding-bottom:5px; }
circle { fill:blue; }
</style>

<div class="widget widgetView dataForecast">
	<div class="widget_header">
		<div class="widget_title"><fmt:message key="dataforecast.title" /></div>
	</div>
		<div class="widget-content">
            <div class="controls">
            	<div class="btn-group btn-group-sm left">
            		<button type="button" id="smoothBtn" class="btn btn-default"><fmt:message key="dataforecast.button.smoothed" /></button>
            		<button type="button" id="normBtn" class="btn btn-default"><fmt:message key="dataforecast.button.normal" /></button>
            		<button type="button" id="rawBtn" class="btn btn-default"><fmt:message key="dataforecast.button.raw" /></button>
            	</div>
            	<div class="noise left">
            		<input type="checkbox" class="checkbox" name="noise" />
            		<label name="noise"><fmt:message key="dataforecast.checkbox.noise" /></label>
            	</div>
            	<div class="btn-group btn-group-sm right">
            		<button type="button" id="dayBtn" class="btn btn-default"><fmt:message key="dataforecast.button.day" /></button>
            		<button type="button" id="weekBtn" class="btn btn-default"><fmt:message key="dataforecast.button.week" /></button>
            		<button type="button" id="monBtn" class="btn btn-default"><fmt:message key="dataforecast.button.month" /></button>
            	</div>
            </div>
        <div id="smooth_button"></div>
        <div id="chart"></div>
        <div id="dataForecastData"></div>
     </div>						
</div>

<script tpye="text/javascript">

registerData();

</script>