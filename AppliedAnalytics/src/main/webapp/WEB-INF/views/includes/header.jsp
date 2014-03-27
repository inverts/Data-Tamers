<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<header id="header">

	<c:choose>
		<c:when test="${HEADER.type == 'APPLICATION'}">
			<div class="header-application">
				<div class="filter">
					<input type="text" id="start-date" readonly value="${filter.getActiveStartDateString() }" />
					<input type="text" id="end-date" readonly value="${filter.getActiveEndDateString() }" /><br />
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
				<div class="trash"><span class="glyphicon glyphicon-trash"></span></div>	
			</div>
		</c:when> 
		
		<c:otherwise>
			<div class="header-home">
				<div class="entry-wrapper">
					<div class="logo">
						<img src="<c:url value="/cache/images/logo-280.png" />" />
					</div>
					<div class="right">
						<c:choose>
							<c:when test="${HEADER.type == 'HOME'}">
								<span class="nav-item"><a href="#"><fmt:message key="header.about" /></a></span>
								<span class="nav-item"><a href="<c:url value="/login" />"><fmt:message key="header.login" /></a></span>
							</c:when>
						</c:choose>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</header>
 