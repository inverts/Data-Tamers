<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script src="<c:url value="cache/javascript/hypothetical-future.js" />"></script>
<script src="<c:url value="cache/javascript/website-performance.js" />"></script>
<script src="<c:url value="cache/javascript/revenue-sources.js" />"></script>
<script src="<c:url value="cache/javascript/dashboard.js" />"></script>


<style>
.dashboard-content {
} 

.dashboard-content, .w_container {
}

.w_container {
	float:left;
}

</style>

<div class="dashboard-content">
	<div id="hypotheticalWidget" class="w_container" draggable="true"></div>
	<div id="websitePerformance" class="w_container" draggable="true"></div>
	<div id="revenueSources" class="w_container" draggable="true"></div>
	<div id="testWidget4" class="w_container" draggable="true"></div>		
</div>