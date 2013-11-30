<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>

<script src="<c:url value="cache/javascript/hypothetical-future.js" />"></script>

<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${model.serverTime}. </P>

<div id="testWidget"></div>

<input type="button" value="Log in" onclick="document.location.href = '/appliedanalytics/login';" />
