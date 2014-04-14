<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="<c:url value="/cache/javascript/data-forecast.js" />"></script>
<script src="<c:url value="/cache/javascript/website-performance.js" />"></script>
<script src="<c:url value="/cache/javascript/key-contributing-factors.js" />"></script>
<script src="<c:url value="/cache/javascript/keyword-insight.js" />"></script>
<script src="<c:url value="/cache/javascript/dashboard.js" />"></script>
<script src="<c:url value="/cache/javascript/traffic-source-trends.js" />"></script>
<script src="<c:url value="/cache/javascript/overview.js" />"></script>
<script src="<c:url value="/cache/javascript/boost-performance.js" />"></script>

<script>
var dashboardId = ${model.dashboardId};

/* Check if the URL indicate's we're on a different dashboard. */
var before = "#dashboards/";
var after = "/";
var begin = window.location.href.indexOf(before);
if (begin > 0) {
	begin += before.length;
	end = window.location.href.indexOf(after, begin);
	if (end < 0)
		newId = window.location.href.substr(begin);
	else
		newId = window.location.href.substr(begin, end - begin);
	newId = parseInt(newId);
	console.log(newId);
	if (!isNaN(newId)){
		dashboardId = newId;
	}
}
loadDashboard(dashboardId);

</script>

<div id="application-page" class="dashboard">
	<div class="dashboard-content"></div>
</div>
