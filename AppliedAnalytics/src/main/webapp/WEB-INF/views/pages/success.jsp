<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Success</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/cache/css/main.css" />
</head>
<body>
<h1>
	Success! 
</h1>

<P>You have successfully granted the application access to view your Google Analytics data.</P>
<p>Your ACCESS TOKEN is: <strong>${model.accessToken}</strong></p>
<p>Your REFRESH TOKEN is: <strong>${model.refreshToken}</strong></p>
User Info:
<div class="profile-info">
	<c:choose>
	<c:when test="${not empty model.picture}" >
	<img class="profile-image" src="${model.picture}?sz=90" /><br />
	</c:when>
	<c:otherwise>
	<img class="profile-image" src="<%=request.getContextPath()%>/cache/images/default_user.png" /><br />
	</c:otherwise>
	</c:choose>
	<p><strong>${model.name}</strong></p>
	<p><strong>${model.email}</strong></p>
</div>

</body>
</html>
