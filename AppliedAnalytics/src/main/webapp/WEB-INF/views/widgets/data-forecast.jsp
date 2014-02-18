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
</script>  
 
<style>
#top-controls { height:50px; }
circle { fill:blue; }
</style>

<div class="widget widgetView dataForecast">
	<div class="widget_header">
		<div class="widget_title"><fmt:message key="dataforecast.title" /></div>
	</div>
		<div class="widget-content">
            <div id="top-controls">
                <div id = "buttonsContainer">
                    <div id = "option">
                    <input id="smoothedButton" type="button" value="smoothed" style="float:left;"/>
                    <input id="normalizedButton" type="button" value="normalized" style="float:left;"/>
                    <input id="rawButton" type="button" value="raw" style="float:left;"/>
                    </div>               
                </div>
                <div id = "timeButton">
                    <input name="dayButton" type="button" value="day" style="float:right;"/>
                    <input name="weekButton" type="button" value="week" style="float:right;"/>
                    <input name="monthButton" type="button" value="month" style="float:right;"/>
                </div>
            </div>
        <div id="smooth_button"></div>
        <div id="chart"></div>
        <div id="dataForecastData"></div>
     </div>						
</div>