<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- CSS -->
<link href="<c:url value="/cache/css/site.css" />" rel="stylesheet">
<link href="<c:url value="/cache/css/widget.css" />" rel="stylesheet">
<link href="<c:url value="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />" rel="stylesheet">

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
		
			<!-- Application Header Panel -->
			<div id="header-application">
				<div class="filter">
					<input type="text" id="start-date" readonly value="${ filter.getActiveStartDateString() }" />
					<input type="text" id="end-date" readonly value="${ filter.getActiveEndDateString() }" /><br />
					<fmt:message key="filter.activemetric" /> ${filter.getActiveMetric().toUpperCase().substring(0,4).concat(filter.getActiveMetric().substring(4)).substring(3) }
				</div> 
				<div class="avatar">
					<c:choose>
						<c:when test="${not empty settings.getGoogleUserData().getPicture()}" >
							<img class="profile-image" title="" src="${settings.getGoogleUserData().getPicture()}?sz=50" /><br />
						</c:when>
						<c:otherwise>
							<img class="profile-image" src="<c:url value="/cache/images/default_user_50.png" />" /><br />
						</c:otherwise>
					</c:choose>
				</div>
				<div class="messages"></div>		
			</div>
	
			<!-- Left side navigation panel -->
			<jsp:include page="${SIDEPANEL.path}"/>
			
			<!--  Right side settings pane -->
			
            <div class="settings">
			<jsp:include page="${SETTINGS}"/>
			</div>
			
			<!-- Site Content -->
			<div class="content">
				<jsp:include page="${BODY}"/>
			</div>
		</div>
		
		<!--  Site Footer -->
		<jsp:include page="${FOOTER.path}"/>
		
	</div>
	
</body>
</html>
