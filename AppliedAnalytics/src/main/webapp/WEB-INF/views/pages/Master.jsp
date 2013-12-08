<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- CSS -->
<link href="<c:url value="/cache/css/site.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/sidepanel.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/headerfooter.css" />" rel="stylesheet">

<!-- JavaScript -->
<script src="<c:url value="/cache/javascript/libs/jquery-1.10.2.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/jquery-ui-1.10.3.custom.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/processing-1.4.1.min.js" />"></script>
<script src="<c:url value="/cache/javascript/site.js" />"></script>

<title>${TITLE}</title>
</head>
<body>
	<div class="wrapper">
		<jsp:include page="${SIDEPANEL}"/>
		<jsp:include page="${HEADER}"/>
		<div class="content">
			<jsp:include page="${BODY}"/>
		</div>
		<jsp:include page="${FOOTER}"/>
	</div>
</body>
</html>