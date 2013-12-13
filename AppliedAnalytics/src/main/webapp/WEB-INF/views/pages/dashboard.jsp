<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script src="<c:url value="cache/javascript/hypothetical-future.js" />"></script>
<script src="<c:url value="cache/javascript/website-performance.js" />"></script>
<script src="<c:url value="cache/javascript/revenue-sources.js" />"></script>


<style>
.dashboard-content {
	padding:10px;
} 

.dashboard-content, .w_container {
	float:left;
}

.w_container {
	float:left;
}

#websitePerformance {
	margin-left:10px;
}
</style>

<div class="dashboard-content">
	<div>
		<div id="hypotheticalWidget" class="w_container"></div>
		<div id="websitePerformance" class="w_container"></div>
	</div>
	<div>
		<div id="revenueSources" class="w_container"></div>
		<div id="testWidget4" class="w_container"></div>		
	</div>
	

</div>