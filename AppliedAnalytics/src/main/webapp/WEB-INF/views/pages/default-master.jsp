<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="shortcut icon" href="<c:url value="/cache/images/icon.ico" />" />
<!--  DEFAULT MASTER -->
<!-- CSS -->
<link href="<c:url value="/cache/css/libs/bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/site.css" />" rel="stylesheet">

<script type="text/javascript">
	/* Set the application root directory for the javascript files. */
	applicationRoot = "<c:url value="/" />";
</script>
<!-- JavaScript -->
<script src="<c:url value="/cache/javascript/libs/jquery-1.10.2.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/jquery-ui-1.10.3.custom.min.js" />"></script>
<script src="<c:url value="/cache/javascript/libs/processing-1.4.1.min.js" />"></script>
<script src="<c:url value="/cache/javascript/headerfooter.js" />"></script>
<script src="<c:url value="/cache/javascript/site.js" />"></script>


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
