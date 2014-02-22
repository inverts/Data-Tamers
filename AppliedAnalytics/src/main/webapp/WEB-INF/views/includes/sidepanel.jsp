<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="<c:url value="/cache/javascript/sidepanel.js" />"></script>

<script>
var dashboardLinks = '${SIDEPANEL.model.getDashboardLinks()}';
$(function(){ getDashboardList($.parseJSON(dashboardLinks)); });
</script>

<nav id="sidepanel" class="sidepanel" data-animate="${SIDEPANEL.animate}">
	<div class="sidepanel-content" >
		<div class="sidepanel-header">
			<img src="<c:url value="/cache/images/logo-280.png" />" />
		</div>
		<div class="sidepanel-nav">
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
			<div id="trends"class="nav-cell">
				<div class="nav-icon" style="background-position:0 -30px;"></div>
				<a href="/appliedanalytics/application/trends" class="nav-txt"><fmt:message key="trends" /></a>
			</div>
			<div id="forecast" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -60px;"></div>
				<a href="/appliedanalytics/application/forecast" class="nav-txt"><fmt:message key="forecast" /></a>
			</div>
			<div id="suggestions" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -90px;"></div>
				<a href="/appliedanalytics/application/alerts" class="nav-txt"><fmt:message key="alerts" /></a>
			</div>
		</div>	
	</div>
</nav>