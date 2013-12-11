<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script src="<c:url value="cache/javascript/hypothetical-future.js" />"></script>

<div class="dashboard-content">
	<h1>
		Dashboard! 
	</h1>
	
	<P>  The time on the server is ${serverTime}. </P>
	
	
	
	<div id="testWidget" class="w_container"></div>
	<br /><br />
	<div id="testWidget2" class="w_container"></div>
	<br /><br />
	<div id="testWidget3" class="w_container"></div>
	<br /><br />
	<div id="testWidget4" class="w_container"></div>
</div>