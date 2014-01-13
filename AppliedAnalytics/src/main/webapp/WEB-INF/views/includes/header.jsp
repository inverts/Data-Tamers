<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script>
var headerAttributes = { 
	state: '${HEADER.state}'
};

</script>


<header id="header">

	<!-- Header for Application -->
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
	 
	<!-- Header for Entry Page -->
	<div id="header-entry">
		<div class="entry-wrapper">
			<img src="<c:url value="/cache/images/logo-280.png" />" />
			<div class="right">
				<span class="nav-item"><a href="#"><fmt:message key="header.about" /></a></span>
				<span class="nav-item"><a href="<c:url value="/login" />"><fmt:message key="header.login" /></a></span>
			</div>
		</div>
	</div>
	
</header>
 