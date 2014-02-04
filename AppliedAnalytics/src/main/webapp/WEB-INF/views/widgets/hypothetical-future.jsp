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

<script src="http://d3js.org/d3.v3.min.js" charset="utf-8"></script>
  
<div class="widget_wrapper widgetView">
	<form id="hypotheticalFutureSettings">
		<div class="widget_header">			
			<div class="widget_title"><fmt:message key="dataforecast.title" /></div>
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
			<!-- ADDED 2/1 -->
			<div id="backrect">
            <div id="top-controls">
                <div id = "buttonsContainer">
                    <div id = "option">
                    <input name="smoothedButton" type="button" value="smoothed" style="float:left;" onclick="updateSmoothed()"/>
                    <input name="normalizedButton" type="button" value="normalized" style="float:left;" onclick="updateNormalized()"/>
                    <input name="rawButton" type="button" value="raw" style="float:left;" onclick="updateRaw()"/>
                    </div>               
                </div>
                <div id = "timeButton">
                    <input name="dayButton" type="button" value="day" style="float:right;" onclick="updateDay()"/>
                    <input name="weekButton" type="button" value="week" style="float:right;" onclick="updateWeek()"/>
                    <input name="monthButton" type="button" value="month" style="float:right;" onclick="updateMonth()"/>
                </div>
            </div>
            <div id="checkboxID" style="position:absolute; left:220px; top:10px; width:200px;" onclick="addNoise()">
                <input type="checkbox" name="noise_checkbox" value="noise" />add noise to future
            </div>
            <div id="smooth_button" original-title="view smoothed graph"></div>
            <div id="chart"></div>
        </div>
			<canvas id="hypotheticalFutureData"></canvas>
								
		</div>
	</form>
</div>
