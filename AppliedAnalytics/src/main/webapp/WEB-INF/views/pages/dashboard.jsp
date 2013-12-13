<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script src="<c:url value="cache/javascript/hypothetical-future.js" />"></script>
<script src="<c:url value="cache/javascript/revenue-sources.js" />"></script>

<style>
.dashboard-content {
		padding:10px;
		float:left;
}

.w_container {
	float:left;
}

#testWidget2 {
	margin-left:10px;
}
</style>

<div class="dashboard-content">
	<div>
		<div id="hypotheticalWidget" class="w_container"></div>
		<div id="testWidget2" class="w_container"></div>
	</div>
	<br /><br />
	<div id="testWidget3" class="w_container"></div>
	<br /><br />
	<div id="testWidget4" class="w_container"></div>

</div>