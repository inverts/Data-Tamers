<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="<c:url value="/cache/javascript/sidepanel.js" />"></script>

<script>
var dashboards = '${SIDEPANEL.model.getDashboards().toString()}';
$(function(){ getDashboardList($.parseJSON(dashboards)); });
</script>

<nav id="sidepanel" class="sidepanel" data-animate="${SIDEPANEL.animate}">
	<div class="sidepanel-content" >
		<div class="sidepanel-header">
			<img src="<c:url value="/cache/images/logo-280.png" />" width=100%/>
		</div>
		<div class="sidepanel-nav">
			<!-- DASHBOARD -->
			<div id="dashboard" class="nav-cell">
				<div class="nav-icon" style="background-position:0 0;"></div>
				<a class="nav-txt"><fmt:message key="dashboard" /></a>
			</div>
			<div class="dashlist">
				<ul id="dashlist" class="dashlist"></ul>
				<div class="addNewDash" id="addDashboard">
					<span class="glyphicon glyphicon-plus"></span>
					<fmt:message key="dashboard.add.link" />
				</div>
			</div>
			
			<!-- TRENDS -->
			<div id="trends"class="nav-cell">
				<div class="nav-icon" style="background-position:0 -30px;"></div>
				<a class="nav-txt"><fmt:message key="trends" /></a>
			</div>
			<div class="trends-list">
				<ul id="trends-list">
					<li class="widgetLink growingProblemsWidget"><fmt:message key="growingproblems.title" /><span class="glyphicon glyphicon-credit-card"></span></li>
				</ul>
				<div id="trends-thumb"></div>
			</div>
			
			<!-- FORECAST -->
			<div id="forecast" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -60px;"></div>
				<a class="nav-txt"><fmt:message key="forecast" /></a>
			</div>
			<div class="forecast-list">
				<ul id="forecast">
					<li id="1" class="widgetLink dataForecastWidget"><fmt:message key="dataforecast.title" /><span class="glyphicon glyphicon-credit-card"></span></li>
				</ul>
				<div id="forecast-thumb"></div>
			</div>
			
			<!-- ALERTS -->
			<div id="suggestions" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -90px;"></div>
				<a class="nav-txt"><fmt:message key="alerts" /></a>
			</div>
		</div>	
	</div>
</nav>