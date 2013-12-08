<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<h1>You are not logged in.</h1>
<p>Please log in below.</p>

<input type="button" value="Log in" onclick="document.location.href = '/appliedanalytics/login';" />

	<script src="<c:url value="/cache/javascript/libs/processing-1.4.1.min.js"/>"></script>
	<canvas data-processing-sources="<c:url value="/cache/pdes/hello_sketch.pde"/>"></canvas>
	