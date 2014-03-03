<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="widget widgetView dataForecast">
	<div class="widget_header">
		<div class="widget_title"><fmt:message key="dataforecast.title" /></div>
		<c:import url="../includes/widget-menu.jsp" />
	</div>
		<div class="widget-content">
            <div class="controls">
            	<div class="btn-group btn-group-sm left">
            		<button type="button" id="rawBtn" class="btn btn-default"><fmt:message key="dataforecast.button.raw" /></button>
            		<button type="button" id="smoothBtn" class="btn btn-default"><fmt:message key="dataforecast.button.smoothed" /></button>
            		<button type="button" id="normBtn" class="btn btn-default"><fmt:message key="dataforecast.button.normal" /></button>
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
            <div class="graph-legend">
            	<ul id="graphLines"></ul>
            </div>
        	<div id="dataForecastData"></div>
      </div>						
</div>
