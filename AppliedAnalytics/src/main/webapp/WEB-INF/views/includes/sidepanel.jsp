<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script>
var dashboards = '${SIDEPANEL.model.getDashboards().toString()}';
$(function(){ getDashboardList($.parseJSON(dashboards)); });
</script>

<nav id="sidepanel" class="sidepanel" data-animate="${SIDEPANEL.animate}">
	<div class="sidepanel-content" >
		<div class="sidepanel-header">
			<a href="/appliedanalytics/"><img src="<c:url value="/cache/images/logo-280.png" />" width=95%/></a>
		</div>
		<div class="sidepanel-nav">
			<!-- DASHBOARD -->
			<div id="dashboard" class="nav-cell">
				<div class="nav-icon" style="background-position:0 0;"></div>
				<a class="nav-txt"><fmt:message key="dashboard" /></a>
			</div>
			<div class="dashlist">
				<ul id="dashlist"></ul>
				<div class="addNewDash" id="addDashboard">
					<span class="glyphicon glyphicon-plus"></span>
					<fmt:message key="dashboard.add.link" />
				</div>
			</div>
			
			<c:set var="widgetLibraries" value="${SIDEPANEL.model.getWidgetLibraryData()}" />
			
			<c:if test="${not empty widgetLibraries}">
				<c:forEach var="wData" items="${widgetLibraries}">
					<div id="<c:out value='${wData.getLibraryTitle().toLowerCase()}'/>" class="nav-cell">
						<span class="lib-icon glyphicon glyphicon-book"></span>
						<a class="nav-txt"><c:out value="${wData.getLibraryTitle()}"/></a>
					</div>
				    <div id="<c:out value='${wData.getLibraryTitle().toLowerCase()}'/>-list" class="widget-list">
				    	<div class="<c:out value='${wData.getLibraryTitle().toLowerCase()}'/>-list">
				    		<c:forEach var="wInfo" items="${wData.getWidgetData()}">
				    			<div id="${wInfo.getValue()}" class="widgetLink">
				    				${wInfo.getKey()}
				    				<span class="glyphicon glyphicon-credit-card"></span>
				    			</div>
				    		</c:forEach>
				    	</div>
				    </div>
				</c:forEach>
			</c:if>

			
			<!-- ALERTS -->
			<%--<div id="suggestions" class="nav-cell">
				<div class="nav-icon" style="background-position:0 -90px;"></div>
				<a class="nav-txt"><fmt:message key="alerts" /></a>
			</div>--%>
		</div>
	</div>
</nav>

<script src="<c:url value="/cache/javascript/sidepanel.js" />"></script>