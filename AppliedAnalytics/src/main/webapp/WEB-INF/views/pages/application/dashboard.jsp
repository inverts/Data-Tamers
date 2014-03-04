<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script src="<c:url value="/cache/javascript/data-forecast.js" />"></script>
<script src="<c:url value="/cache/javascript/website-performance.js" />"></script>
<script src="<c:url value="/cache/javascript/key-contributing-factors.js" />"></script>
<script src="<c:url value="/cache/javascript/keyword-insight.js" />"></script>
<script src="<c:url value="/cache/javascript/dashboard.js" />"></script>
<script src="<c:url value="/cache/javascript/growing-problems.js" />"></script>
<script src="<c:url value="/cache/javascript/boost-performance.js" />"></script>

<script>
var widgets = [1, 4];
//$(function() { loadWidgets(widgets); });
var dashboardId = ${model.dashboardId};
loadDashboard(dashboardId);

</script>

<div class="dashboard">
	<div class="dashboard-content">
		
	</div>
</div>
