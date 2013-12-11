<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>


<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${model.serverTime}. </P>

<input type="button" value="Log in" onclick="document.location.href = '/appliedanalytics/login';" />


<script src="<c:url value="/cache/javascript/libs/processing-1.4.1.min.js"/>"></script>
<canvas data-processing-sources="<c:url value="/cache/pde/hello_sketch.pde"/>"></canvas>


