<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="main-goal">
	<h1>New Account</h1>
	<form action="<c:url value="/accounts/processgoalinfo" />" method="POST">
		<input type="submit" />
	</form>
	
</div>