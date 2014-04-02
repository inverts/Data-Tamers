<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<header id="header">

	<c:choose>
		<c:when test="${HEADER.type == 'APPLICATION'}">
			<div class="header-application">
				<div class="filter">
					<label>Data Range:</label>
					<input type="text" id="start-date" readonly value="${filter.getActiveStartDateString() }" />
					<input type="text" id="end-date" readonly value="${filter.getActiveEndDateString() }" />
					<label>What are you interested in?</label>
					<select>
						<option value="ga:visits">Visits</option>
						<option value="ga:newVisits">New Visits</option>
					</select>
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
				<div class="trash"><span class="glyphicon glyphicon-trash"></span></div>	
			</div>
		</c:when> 
		
		<c:otherwise>
			<div class="header-home">
				<div class="entry-wrapper">
					<div class="logo">
						<a href="/appliedanalytics/"><img src="<c:url value="/cache/images/logo-280.png" />" /></a>
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
 