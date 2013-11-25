<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

<P>  The time on the server is ${serverTime}. </P>


<!-- Our functions for API authorization. -->
<script type="text/javascript" src="/resources/authorization.js"></script>"

<!-- Google JavaScript API library. When loaded, it will call our authorization functions above. -->
<script type="text/javascript" src="https://apis.google.com/js/client.js?onload=handleClientLoad"></script>


</body>
</html>
