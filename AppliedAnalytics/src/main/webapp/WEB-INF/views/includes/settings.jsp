<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="settings-background"></div>
<aside class="settings">
	<div class="settings-outer">
		<div class="settings-content">
			<h2>Settings</h2>
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
						<c:choose>
							<c:when test="${not empty settings.getActiveProfile() }" >
								<tr><td class="rowlabel">Active Profile</td><td>${ settings.getActiveProfile().getName() }</td></tr>
							</c:when>
						</c:choose>
						<tr><td class="rowlabel">Account</td><td>
						<c:choose>
							<c:when test="${not empty settings.getCurrentAccounts() && not empty settings.getCurrentAccounts().getItems()}" >
								<select id="select-account">
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
										<select id="select-property">
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
										<select id="select-profile">
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
								</td><td><input id="update-button" type="button" value="Update" /></td></tr>
							</c:when>
						</c:choose>
						
					</tbody>
			</table>
		</div>
	</div>
</aside>