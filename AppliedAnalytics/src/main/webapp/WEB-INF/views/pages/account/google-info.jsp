<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="google-info">
	<h1>Google sign in and authentication; return some kind of object with google data! </h1>
	<form action="<c:url value="/accounts/processgoogleinfo" />" method="POST">
		<input type="submit" />
	</form>
	
</div>