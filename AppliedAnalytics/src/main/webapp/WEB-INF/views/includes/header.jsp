<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<header id="header">

	<c:choose>
		<c:when test="${HEADER.type == 'APPLICATION'}">
			<div class="header-application">
				<div class="filter">
					<label>Date Range:</label>
					<input class="form-control" type="text" id="start-date" readonly value="${filter.getActiveStartDateString() }" />
					<input class="form-control" type="text" id="end-date" readonly value="${filter.getActiveEndDateString() }" />
					<div id="interestMetricSelection">
						<label>What are you interested in?</label>
						<select class="form-control">
							<option value="ga:visits" ${filter.getActiveMetric()=="ga:visits" ? "selected" : ""}>Visits</option>
							<option value="ga:newVisits" ${filter.getActiveMetric()=="ga:newVisits" ? "selected" : ""}>New Visits</option>
						</select>
					</div>
					<div id="activeProfileInfo">
						<label>Google Profile: </label>
						<!-- Always shows the current active profile we're looking at. -->
						<c:choose>
                                <c:when test="${not empty settings.getCurrentProfiles()}" >
                                        <c:choose>
                                                <c:when test="${not empty settings.getCurrentProfiles().getItems()}" >
                                                        <select id="select-profile" class="form-control">
                                                        <c:forEach var="profile" items="${ settings.getCurrentProfiles().getItems() }">
                                                                <option value="${ profile.getId() }" ${settings.getActiveProfile() == profile ? "selected" : "" }>
                                                                ${ profile.getName() }
                                                                </option>
                                                        </c:forEach>
                                                        </select>
                                                </c:when>
                                                <c:otherwise>
                                                        (Profile List Unavailable)
                                                </c:otherwise>
                                        </c:choose>
                                </c:when>
                        </c:choose>
					</div>
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
								
								<div class="nav-item dropdown">
								  <a id="loglabel" role="button" data-toggle="dropdown">
								  <c:choose>
								  	<c:when test="${not empty model.loggedin }">
								  		${model.name}
								  	</c:when>
								  	<c:otherwise>
								    	<fmt:message key="header.guest" />
								    </c:otherwise>
								  </c:choose>
								  	<span class="caret"></span>
								  </a>
								  <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
								  	<!-- both -->
								  	<li><a href="/appliedanalytics/accounts/getstarted">New Account</a></li>
								  	<li><a href="http://www.google.com/analytics/" target="_blank">Google Analytics</a></li>
								  	<c:choose>
								  		<c:when test="${not empty model.loggedin}">
								  			<li><a href="application">Dashboard</a></li>
								  			<li class="divider"></li>
											<li><a href="/appliedanalytics/signout"><fmt:message key="header.logout" /></a></li>
								    	</c:when>
								    	<c:otherwise>
										    <li class="divider"></li>
											<li><a href="/appliedanalytics/login"><fmt:message key="header.login" /></a></li>
								    	</c:otherwise>
								    </c:choose>
								  </ul>
								</div>
							</c:when>
						</c:choose>
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
</header>
 