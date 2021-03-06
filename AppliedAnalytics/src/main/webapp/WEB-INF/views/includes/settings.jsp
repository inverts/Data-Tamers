<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="settings-content">
	<div id="your-account">
	<h3>Your Account</h3>
	<table>
		<tbody>
			<tr>
				<td class="rowlabel">Name</td>
				<td>${user.getFirstName()} ${user.getLastName()}</td>
			</tr>
			<tr>
				<td class="rowlabel">Email</td>
				<td>${user.getEmail()}</td>
			</tr>
			<tr>
				<td class="rowlabel">Join Date</td>
				<td>${user.getJoinDateAsString()}</td>
			</tr>
			<tr>
				<td class="rowlabel"></td>
				<td></td>
			</tr>
		</tbody>
	</table>
	<input type="button" class="form-control" value="Log Out" style="width:50%" onclick="document.location.href=applicationRoot + 'signout';" />
	</div>
	<div id="google-account-info">
	<h3>Google Account Settings</h3>
	        <div class="left-pane">
	                <c:choose>
	                        <c:when test="${not empty settings.getGoogleUserData().getPicture()}" >
	                                <img class="profile-image-large" title="" src="${settings.getGoogleUserData().getPicture()}?sz=150" /><br />
	                        </c:when>
	                        <c:otherwise>
	                                <img class="profile-image-large" src="<c:url value="/cache/images/default_user_90.png" />" /><br />
	                        </c:otherwise>
	                </c:choose>
	        </div>
	        
	        <table>
	                <tbody>
	                        <tr><td class="rowlabel">Name</td><td>${ settings.getGoogleUserData().getName() }</td></tr>
	                        <tr><td class="rowlabel">Email</td><td>${ settings.getGoogleUserData().getEmail() }</td></tr>
	                        <!-- <c:choose>
	                                <c:when test="${not empty settings.getActiveProfile() }" >
	                                        <tr><td class="rowlabel">Active Profile</td><td>${ settings.getActiveProfile().getName() }</td></tr>
	                                </c:when>
	                        </c:choose> -->
	                        <tr><td class="rowlabel">Account</td><td>
	                        <c:choose>
	                                <c:when test="${not empty settings.getCurrentAccounts() && not empty settings.getCurrentAccounts().getItems()}" >
	                                        <select id="select-account" class="form-control">
	                                        <c:forEach var="account" items="${ settings.getCurrentAccounts().getItems() }">
	                                                <option value="${ account.getId() }" ${settings.getAccountSelection() == account ? "selected" : "" }>
	                                                ${ account.getName() }
	                                                </option>
	                                        </c:forEach>
	                                        </select>
	                                </c:when>
	                                <c:otherwise>
	                                        (Account List Unavailable)
	                                </c:otherwise>
	                        </c:choose>
	                        </td></tr>
	                        
	                        <c:choose>
	                                <c:when test="${not empty settings.getCurrentWebproperties()}" >
	                                        <tr><td class="rowlabel">Property</td><td>
	                                        <c:choose>
	                                                <c:when test="${not empty settings.getCurrentWebproperties().getItems()}" >
	                                                        <select id="select-property" class="form-control">
	                                                        <c:forEach var="property" items="${ settings.getCurrentWebproperties().getItems() }">
	                                                                <option value="${ property.getId() }" ${settings.getPropertySelection() == property ? "selected" : "" }>
	                                                                ${ property.getName() }
	                                                                </option>
	                                                        </c:forEach>
	                                                        </select>
	                                                </c:when>
	                                                <c:otherwise>
	                                                        (Property List Unavailable)
	                                                </c:otherwise>
	                                        </c:choose>
	                                        </td></tr>
	                                </c:when>
	                        </c:choose>
	                        
	                        <c:choose>
	                                <c:when test="${not empty settings.getCurrentProfiles()}" >
	                                        <tr><td class="rowlabel">Profile</td><td>
	                                        <c:choose>
	                                                <c:when test="${not empty settings.getCurrentProfiles().getItems()}" >
	                                                        <select id="select-profile" class="form-control">
	                                                        <c:forEach var="profile" items="${ settings.getCurrentProfiles().getItems() }">
	                                                                <option value="${ profile.getId() }" ${settings.getProfileSelection() == profile ? "selected" : "" }>
	                                                                ${ profile.getName() }
	                                                                </option>
	                                                        </c:forEach>
	                                                        </select>
	                                                </c:when>
	                                                <c:otherwise>
	                                                        (Profile List Unavailable)
	                                                </c:otherwise>
	                                        </c:choose>
	                                        </td></tr>
	                                </c:when>
	                        </c:choose>
	                        
	                        <c:choose>
	                                <c:when test="${not empty settings.getCurrentProfiles()}" >
	                                        <tr><td class="rowlabel" style="color: #FFDD00;">
	                                        <c:choose>
	                                                <c:when test="${not empty update }" >
	                                                        ${ update }
	                                                </c:when>
	                                        </c:choose>
	                                        </td><td><input id="update-button" class="form-control" type="button" value="Update" /></td></tr>
	                                </c:when>
	                        </c:choose>
	                                
	                        </tbody>
	        </table>
	        <br>
			<a href="https://www.google.com/settings/" target="_blank">Edit Google Account Settings</a> <span class="link glyphicon glyphicon-share-alt"></span><br />
			<a href="https://www.google.com/analytics/settings/" target="_blank">Edit Google Analytics Account Settings</a> <span class="link glyphicon glyphicon-share-alt"></span><br />
	        
	   </div>
</div>