<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- Commit 22:44 -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- HOME MASTER -->
<!-- CSS -->
<link href="<c:url value="/cache/css/libs/bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/entry.css" />" rel="stylesheet">

<link rel="shortcut icon" href="<c:url value="/cache/images/icon.ico" />" />

<script src="<c:url value="/cache/javascript/libs/jquery-1.10.2.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/jquery-ui-1.10.3.custom.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/bootstrap.min.js" />"></script>
<script src="<c:url value="/cache/javascript/headerfooter.js" />"></script>
<script type="text/javascript">
	/* Set the application root directory for the javascript files. */
	applicationRoot = "<c:url value="/" />";
	window.onload = function() {
		$(".entry-content").css('visibility','visible').hide().fadeIn(600, function() {
			$(".entry-content #center-box").fadeIn(1000);
		});
	};
</script>


<title>${TITLE}</title>
</head>

<body>

	<div class="page">
	
		<!-- Site Header -->
		<jsp:include page="${HEADER.path}"/>
		
		<div class="wrapper">		
			<jsp:include page="${BODY}"/>
		</div>
		
		<!--  Site Footer -->
		<jsp:include page="${FOOTER.path}"/>
		
	</div>
	
</body>
</html>
