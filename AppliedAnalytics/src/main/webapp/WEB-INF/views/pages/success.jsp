<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Success</title>
	<!-- This page is temporary, for debugging purposes, and should be removed at a later point. -->
</head>
<body>
<h1>
	Success! 
</h1>

<P>You have successfully granted the application access to view your Google Analytics data.</P>
<p>Your ACCESS TOKEN is: <strong>${accessToken}</strong></p>
<p>Your REFRESH TOKEN is: <strong>${refreshToken}</strong></p>

</body>
</html>
